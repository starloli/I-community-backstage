package com.example.demo2.service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class PackegeGeminiOCRService {

	@Value("${google.gemini.api-key}")
    private String apiKey;

    public String recognizePackage(String imageInput) throws Exception{
//        try {
            String base64Data;

            // 1. 處理圖片：如果是網址就下載，如果是 Base64 就清理
            if (imageInput != null && imageInput.startsWith("http")) {
                byte[] downloaded = downloadImage(imageInput);
                base64Data = Base64.getEncoder().encodeToString(downloaded);
            } else {
                base64Data = cleanBase64(imageInput);
            }

            // 2. 準備 JSON 請求體 (使用最標準的 Gemini API 格式)
            String prompt = "你是一個專業的物業管理助理。請從這張包裹照片中擷取以下資訊，並嚴格以 JSON 格式回傳：recipientName, "
            		+ "phoneNumber, unitNumber, trackingNumber, courier, notes。"
            		+ "courier是快遞業者 比如順豐,黑貓,unitnumber是住戶的地址,請根據圖片去找到對應的物流公司,"
            		+ "黑貓宅急便', '新竹物流', '郵局', '宅配通', '順豐速運', 'DHL', 'Lalamove', 'FedEx', '中華郵局','其他',沒有上面的這幾個就回傳其他"
            		+ "如果是A棟3-01請直接回傳A棟301,只需要棟和樓號如A棟601,不需要回傳具體地址,請只回傳原始 JSON 字串，不要包含 ```json 等標籤。";
            
            String jsonPayload = """
                {
                  "contents": [{
                    "parts": [
                      {"text": "%s"},
                      {
                        "inline_data": {
                          "mime_type": "image/jpeg",
                          "data": "%s"
                        }
                      }
                    ]
                  }]
                }
                """.formatted(prompt, base64Data);

            // 3. 發送請求到 Google API
            String url ="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. 處理回應
//            if (response.statusCode() == 200) {
//                return extractTextFromResponse(response.body());
//            } else {
////                return "{\"error\":\"API 回傳錯誤: " + response.statusCode() + " " + response.body() + "\"}";
//            	throw new RuntimeException("Google API 連線失敗 (HTTP " + response.statusCode() + "): " + response.body());
//            }
            if (response.statusCode() != 200) {
                // 直接拋出異常，附帶狀態碼與錯誤訊息
                throw new RuntimeException("Google API 連線失敗 (HTTP " + response.statusCode() + "): " + response.body());
            }return extractTextFromResponse(response.body());

    }    
//        catch (Exception e) {
//            e.printStackTrace();
//            return "{\"error\":\"系統錯誤: " + e.getMessage() + "\"}";
//        }


    private byte[] downloadImage(String url) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        return client.send(request, HttpResponse.BodyHandlers.ofByteArray()).body();
    }

    private String cleanBase64(String base64Image) {
        String res = base64Image;
        if (base64Image.contains(",")) res = base64Image.split(",")[1];
        return res.replaceAll("[^A-Za-z0-9+/=]", "");
    }

    // 簡單解析 JSON 中的回傳文字 (避免引入複雜的解析庫)
//    private String extractTextFromResponse(String responseBody) {
//    	try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(responseBody);
//            
//            // 從 Google 的回應中取出 text 欄位（Jackson 會自動處理掉內部的轉義）
//            String rawText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
//
//            // 這是最關鍵的一步：只移除 Markdown 標籤，絕對不要去動裡面的引號
//            String cleanJson = rawText
//                    .replaceAll("(?s)```json\\s*", "")
//                    .replaceAll("```", "")
//                    .trim();
//
//            // 如果 AI 真的很頑皮，回傳了「沒引號」的 JSON，我們在 Controller 救
//            return cleanJson;
//        } catch (Exception e) {
//            return "{}";
//        }
//    
//    }
    
    private String extractTextFromResponse(String responseBody) throws Exception { // 宣告會拋出異常
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        
        // 檢查是否有 API 級別的錯誤 (例如 200 OK 但內容是錯誤訊息)
        if (root.has("error")) {
            throw new RuntimeException("Gemini API 內部錯誤: " + root.path("error").path("message").asText());
        }

        JsonNode candidates = root.path("candidates");
        if (candidates.isMissingNode() || candidates.isEmpty()) {
            throw new RuntimeException("AI 未能辨識圖片內容，請確保圖片清晰。");
        }

        String rawText = candidates.get(0).path("content").path("parts").get(0).path("text").asText();

        String cleanJson = rawText
                .replaceAll("(?s)```json\\s*", "")
                .replaceAll("```", "")
                .trim();

        if (cleanJson.isEmpty()) {
            throw new RuntimeException("AI 回傳了空的解析結果。");
        }

        return cleanJson;
    }
}
