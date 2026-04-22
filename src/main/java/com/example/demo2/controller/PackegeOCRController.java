package com.example.demo2.controller;

import com.example.demo2.dto.request.PackageRequest;
import com.example.demo2.service.PackegeGeminiOCRService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin(origins = "http://localhost:4200")
public class PackegeOCRController {@Autowired
    private PackegeGeminiOCRService geminiOCRService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/scan-package")
    public PackageRequest scanPackage(@RequestBody Map<String, String> payload) {
    	String jsonResponse = ""; 
        
        try {
            String base64Image = payload.get("image");
            
            // 2. 賦值給變數
            jsonResponse = geminiOCRService.recognizePackage(base64Image);

            ObjectMapper lenientMapper = new ObjectMapper();
            lenientMapper.configure(com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            lenientMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            lenientMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            lenientMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 解析
            PackageRequest aiResult = lenientMapper.readValue(jsonResponse, PackageRequest.class);

            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return new PackageRequest(
                aiResult.recipientName(), aiResult.phoneNumber(), aiResult.unitNumber(),
                aiResult.trackingNumber(), aiResult.courier(), currentTime, aiResult.notes()
            );

        } catch (Exception e) {
            // 3. 現在這裡可以正常使用 jsonResponse 了！
            System.err.println("--- 造成報錯的 JSON 內容如下 ---");
            System.err.println(jsonResponse); 
            System.err.println("----------------------------");
            e.printStackTrace();
            return new PackageRequest("", "", "", "", "", "", "解析失敗: " + e.getMessage());
        }
    }
}
