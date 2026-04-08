package com.example.demo2.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class BatchResultDto {
	private int successCount;         // 成功發送的戶數
    private List<String> failedMessages; // 失敗的詳細原因（包含房號與錯誤訊息）
    private String processTime; // 執行時間
    public BatchResultDto(int successCount, List<String> failedMessages) {
        this.successCount = successCount;
        this.failedMessages = failedMessages;
        this.processTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
