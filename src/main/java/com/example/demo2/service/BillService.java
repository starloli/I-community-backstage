package com.example.demo2.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo2.dto.request.BatchBillRequest;
import com.example.demo2.dto.request.BillRequset;
import com.example.demo2.dto.response.BatchResultDto;
import com.example.demo2.dto.response.MonthlyBillDto;
import com.example.demo2.entity.Bill;

import com.example.demo2.entity.User;
import com.example.demo2.enums.BillStatus;
import com.example.demo2.enums.BillType;
import com.example.demo2.enums.paymentMethodEnum;
import com.example.demo2.repository.BillDao;

import com.example.demo2.repository.UserDao;

@Service
public class BillService {

	@Autowired BillDao billDao;
	@Autowired private UserDao userDao;

	
	
	
	
	@Transactional
    public MonthlyBillDto sendMonthlyBill(BillRequset billRequset, User creator) {
        // 1. 檢查重複：該住戶該月份是否已經有「綜合帳單」
        // 一月一筆， 查 Unit + Month
        boolean exists = billDao.existsByUnitNumberAndBillingMonth(
            billRequset.getUnitNumber(), 
            billRequset.getBillingMonth()
        );

        if (exists) {
            throw new RuntimeException("住戶 " + billRequset.getUnitNumber() + 
                " 的 " + billRequset.getBillingMonth() + " 帳單已存在，請勿重複發送！");
        }

        // 2. 從 Map 取出各項金額 (對接前端的 Key)
        Map<String, BigDecimal> fees = billRequset.getFees();
        BigDecimal water = fees.getOrDefault("WATER", BigDecimal.ZERO);
        BigDecimal elec = fees.getOrDefault("ELECTRICITY", BigDecimal.ZERO);
        BigDecimal mgmt = fees.getOrDefault("MANAGEMENTFEE", BigDecimal.ZERO);
        BigDecimal car = fees.getOrDefault("CAR_PARKINGCLEANINGFEE", BigDecimal.ZERO);
        BigDecimal motor = fees.getOrDefault("LOCOMOTIVE_PARKINGCLEANINGFEE", BigDecimal.ZERO);
BigDecimal other = fees.getOrDefault("OTHERFEE", BigDecimal.ZERO);
        // 3. 建立單一 Bill 實體
        Bill bill = new Bill();
        bill.setUnitNumber(billRequset.getUnitNumber());
        bill.setTitle(billRequset.getTitle());
        bill.setBillingMonth(billRequset.getBillingMonth());
        bill.setDueDate(billRequset.getDueDate());
        bill.setRemark(billRequset.getRemark());
        bill.setCreator(creator);
        bill.setStatus(billRequset.getStatus() != null ? billRequset.getStatus() : BillStatus.UNPAID);
        bill.setBillType(BillType.MANAGEMENTFEE);

        // 填入明細欄位
        bill.setWaterFee(water);
        bill.setElectricityFee(elec);
        bill.setManagementFee(mgmt);
        bill.setCarParkingFee(car);
        bill.setLocomotiveParkingFee(motor);
        bill.setOtherFee(other);
        
        // 計算總額
        BigDecimal total = water.add(elec).add(mgmt).add(car).add(motor).add(other);
        BigDecimal roundedTotal = total.setScale(0, RoundingMode.HALF_UP);
        bill.setAmount(roundedTotal);

        // 4. 存檔並回傳你想要的 MonthlyBillDto 格式
        Bill savedBill = billDao.save(bill);
        return MonthlyBillDto.fromEntity(savedBill);
    }

    // 獲取住戶自己的帳單 (一月一筆)
    public List<MonthlyBillDto> getResidentMonthlyBills(String unitNumber) {
        List<Bill> bills = billDao.findByUnitNumber(unitNumber);
        return bills.stream()
                    .map(MonthlyBillDto::fromEntity)
                    .toList();
    }

    // 管理員更新狀態
    @Transactional
    public void putBillStatus(Integer id) {
        Bill bill = billDao.findById(id).orElseThrow(() -> new RuntimeException("找不到該帳單"));
        bill.setStatus(BillStatus.PAID);
        bill.setPaymentMethod(paymentMethodEnum.Cash);
        bill.setPaidAtDate(java.time.LocalDateTime.now()); // 建議加上繳費時間
        billDao.save(bill);
    }
    
    //用戶自己繳費
    public void userPayBillStatus(Integer id) {
    	 Bill bill = billDao.findById(id).orElseThrow(() -> new RuntimeException("找不到該帳單"));
    	  bill.setStatus(BillStatus.PAID);
          bill.setPaymentMethod(paymentMethodEnum.Online);
          bill.setPaidAtDate(java.time.LocalDateTime.now());
          billDao.save(bill);
    }
    
    //得到全部住戶的賬單情況
    public List<MonthlyBillDto> getAllBills() {
    	List<Bill> bills =billDao.findAll();
    	return bills.stream().map(s-> MonthlyBillDto.fromEntity(s)).toList();
    }

    

    //一次性發送賬單給全部住戶
    @Transactional
    public BatchResultDto sendAllBillsToUnit(BatchBillRequest request,User creator){
    	List<Map<String, Object>> unitAssets = userDao.findUnitAssetSummary();
        int totalUnits = unitAssets.size();
        
        if (totalUnits == 0) throw new RuntimeException("目前沒有任何有效住戶");

        // 2. 計算平均水電 (以「戶」為分母)
        BigDecimal totalWater = request.getCommonFees().getOrDefault("TOTAL_WATER", BigDecimal.ZERO);
        BigDecimal totalElec = request.getCommonFees().getOrDefault("TOTAL_ELECTRICITY", BigDecimal.ZERO);
        BigDecimal avgWater = totalWater.divide(BigDecimal.valueOf(totalUnits), 0, RoundingMode.HALF_UP);
        BigDecimal avgElec = totalElec.divide(BigDecimal.valueOf(totalUnits), 0, RoundingMode.HALF_UP);

        int successCount = 0;
        List<String> failedUnits = new ArrayList<>();
    	
        for (Map<String, Object> asset : unitAssets) {
            String unitNo = (String) asset.get("unitNumber");
            try {
                LocalDate billingDate = LocalDate.parse(request.getBillingMonth());

                if (billDao.existsByUnitNumberAndBillingMonth(unitNo, billingDate)) {
                    failedUnits.add(unitNo + " (帳單已存在)");
                    continue;
                }

                // --- 開始從 Map 取值並轉換型別 ---
                // 坪數 (Square Footage)
                BigDecimal userPings = new BigDecimal(asset.get("squareFootage").toString());
                
                // 汽車位 (SUM 的結果在 Java 通常是 Number 類型)
                Number carVal = (Number) asset.get("totalCarSpaces");;
                BigDecimal userCarSpaces = (carVal != null) ? BigDecimal.valueOf(carVal.longValue()) : BigDecimal.ZERO;
                
                // 機車位
                Number motoVal = (Number) asset.get("totalMotoSpaces");
                BigDecimal userMotoSpaces = (motoVal != null) ? BigDecimal.valueOf(motoVal.longValue()) : BigDecimal.ZERO;
                // ----------------------------

                Bill bill = new Bill();
                bill.setUnitNumber(unitNo);
                bill.setTitle(request.getTitle());
                bill.setBillingMonth(billingDate);
                bill.setDueDate(request.getDueDate());
                bill.setCreator(creator);
                bill.setStatus(BillStatus.UNPAID);
                bill.setRemark(request.getRemark());
                bill.setBillType(BillType.MANAGEMENTFEE);
                
                // 設定分攤費用
                bill.setWaterFee(avgWater);
                bill.setElectricityFee(avgElec);

                // 計算管理費
                BigDecimal pingPrice = request.getCommonFees().getOrDefault("PING_PRICE", BigDecimal.ZERO);
                bill.setManagementFee(pingPrice.multiply(userPings).setScale(0, RoundingMode.HALF_UP));

                // 計算汽車位費 (直接用加總後的數量)
                BigDecimal carPrice = request.getCommonFees().getOrDefault("CAR_SPACE_PRICE", BigDecimal.ZERO);
                bill.setCarParkingFee(carPrice.multiply(userCarSpaces).setScale(0, RoundingMode.HALF_UP));

                // 計算機車位費 (直接用加總後的數量)
                BigDecimal motoPrice = request.getCommonFees().getOrDefault("MOTO_SPACE_PRICE", BigDecimal.ZERO);
                bill.setLocomotiveParkingFee(motoPrice.multiply(userMotoSpaces).setScale(0, RoundingMode.HALF_UP));

                bill.setOtherFee(request.getCommonFees().getOrDefault("OTHERFEE", BigDecimal.ZERO));
                
                // 總金額加總
                BigDecimal totalAmount = bill.getWaterFee()
                        .add(bill.getElectricityFee())
                        .add(bill.getManagementFee())
                        .add(bill.getCarParkingFee())
                        .add(bill.getLocomotiveParkingFee())
                        .add(bill.getOtherFee());
                bill.setAmount(totalAmount);

                billDao.save(bill);
                successCount++;
                
            } catch (Exception e) {
                failedUnits.add(unitNo + " (錯誤: " + e.getMessage() + ")");
            }
        }
        return new BatchResultDto(successCount, failedUnits);
    }
    }
	
