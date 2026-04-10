	package com.example.demo2.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo2.entity.Bill;
import com.example.demo2.enums.BillStatus;
import com.example.demo2.repository.BillDao;


@RestController
@RequestMapping("/payment")
public class PaymentController {

	
	@Autowired private EcpayService ecpayService;
    @Autowired private BillDao billDao;

    @PostMapping("/pay/{billId}")
    public Map<String, String> getPayForm(@PathVariable("billId") Integer billId) {
        Bill bill = billDao.findById(billId)
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
                billDao.save(bill);
            });
            
            System.out.println("帳單 " + billId + " 付款成功！");
            return "1|OK"; // 這是綠界要求的固定回傳格式
        }
        return "0|Fail";
    }
}
