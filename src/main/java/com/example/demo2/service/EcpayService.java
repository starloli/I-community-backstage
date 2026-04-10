package com.example.demo2.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value; // 核心修正點
import org.springframework.stereotype.Service;

import com.example.demo2.entity.Bill;
import com.example.demo2.util.EcpayUtils;




@Service
public class EcpayService {
	@Value("${ecpay.merchant_id}") private String merchantId;
    @Value("${ecpay.hash_key}") private String hashKey;
    @Value("${ecpay.hash_iv}") private String hashIv;
    @Value("${ecpay.service_url}") private String serviceUrl;
    @Value("${ecpay.return_url}") private String returnUrl;
    @Value("${ecpay.client_back_url}") private String clientBackUrl;

    public String createPaymentForm(Bill bill) {
        // 1. 準備基本參數
        Map<String, String> params = new TreeMap<>();
        params.put("MerchantID", merchantId);
        // 訂單編號加上時間戳記，確保唯一性 (綠界規定 20 字元內)
        params.put("MerchantTradeNo", "B" + bill.getBillId() + "T" + System.currentTimeMillis() / 1000);
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(bill.getAmount().intValue())); // 必須是整數
        params.put("TradeDesc", "Community_Fee_" + bill.getUnitNumber());
        params.put("ItemName", bill.getTitle());
        params.put("ReturnURL", returnUrl);
        params.put("ClientBackURL", clientBackUrl);
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1"); // 使用 SHA256

        // 2. 呼叫我們之前寫好的 EcpayUtils 產生雜湊值
        String checkMacValue = EcpayUtils.generateCheckMacValue(hashKey, hashIv, params);
        params.put("CheckMacValue", checkMacValue);

        // 3. 組合成自動 Submit 的 HTML 表單
        return buildForm(params);
    }

    private String buildForm(Map<String, String> params) {
        StringBuilder form = new StringBuilder();
        form.append("<form id='ecpayForm' action='").append(serviceUrl).append("' method='post'>");
        params.forEach((key, value) -> {
            form.append("<input type='hidden' name='").append(key).append("' value='").append(value).append("'/>");
        });
        form.append("</form>");
        form.append("<script>document.getElementById('ecpayForm').submit();</script>");
        return form.toString();
    }}
