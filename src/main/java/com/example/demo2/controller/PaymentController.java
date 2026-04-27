package com.example.demo2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.entity.Bill;
import com.example.demo2.entity.User;
import com.example.demo2.enums.BillStatus;
import com.example.demo2.enums.BillType;
import com.example.demo2.enums.FinancailLedgerCategory;
import com.example.demo2.enums.TransactionType;
import com.example.demo2.enums.paymentMethodEnum;
import com.example.demo2.repository.BillDao;
import com.example.demo2.repository.CodeDao;
import com.example.demo2.repository.UserDao;
import com.example.demo2.service.EcpayService;
import com.example.demo2.service.FinancialLedgerService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final CodeDao codeDao;

    @Autowired
    private EcpayService ecpayService;
    @Autowired
    private BillDao billDao;
    @Autowired
    private FinancialLedgerService financialLedgerService;

    @Autowired
    private UserDao userDao;

    PaymentController(CodeDao codeDao) {
        this.codeDao = codeDao;
    }
    @PostMapping("/pay/{billId}")
    public Map<String, String> getPayForm(@PathVariable("billId") Integer billId) {
        Bill bill = billDao.findById(billId != null ? billId : 0)
                .orElseThrow(() -> new RuntimeException("找不到該帳單"));

        String form = ecpayService.createPaymentForm(bill);

        Map<String, String> response = new HashMap<>();
        response.put("form", form);
        return response;
    }

    @PostMapping("/callback")
    public String paymentCallback(@RequestParam Map<String, String> params) {
        // 1. 綠界回傳 RtnCode 為 1 代表付款成功
        if ("1".equals(params.get("RtnCode"))) {
            String tradeNo = params.get("MerchantTradeNo"); // 例如 B30T1775705251

            // 2. 解析出 Bill ID (從 B 後面到 T 前面的數字)
            String billIdStr = tradeNo.substring(tradeNo.indexOf("B") + 1, tradeNo.indexOf("T"));
            Integer billId = Integer.parseInt(billIdStr);

            // 3. 更新資料庫狀態為「已支付」
            billDao.findById(billId).ifPresent(bill -> {
                bill.setStatus(BillStatus.PAID); // 確保你的 Bill Entity 有這個欄位
                bill.setPaidAtDate(java.time.LocalDateTime.now()); // 記錄付款時間
                bill.setPaymentMethod(paymentMethodEnum.Online); // 記錄付款方式

                billDao.save(bill);
                List<User> users = userDao.findByUnitNumber(bill.getUnitNumber());
                User payer = (users != null && !users.isEmpty()) ? users.get(0) : null;
//                System.out.print("-------------------看看我是不是null,下面 是結果"+payer);
                FinancailLedgerCategory category = FinancailLedgerCategory.MGMT_FEE; // 預設管理費
                if (bill.getBillType() == BillType.OTHEREXPENSES) {
                    category = FinancailLedgerCategory.REPAIR; // 如果是雜項則歸類為維修費
                }
               
                financialLedgerService.recordTransaction(
                    TransactionType.INCOME,               // 類型：收入
                    bill.getAmount(),                     // 金額
                    category,                             // 分類
                    "住戶線上繳費：" + bill.getTitle(),      // 備註
                    "BILL",                               // 來源模組
                    bill.getBillId().longValue(),         // 來源 ID
                    payer                              // 線上繳費通常無特定操作管理員，傳 者
                );
            });

            System.out.println("帳單 " + billId + " 付款成功！");
            return "1|OK"; // 這是綠界要求的固定回傳格式
        }
        return "0|Fail";
    }
}
