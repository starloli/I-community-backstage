package com.example.demo2.dto.response;

import com.example.demo2.entity.RepairRequest;
import com.example.demo2.enums.RepairStatus;

public record RepairResponse(
    Integer repairId,
    String userName,
    String location,
    String category,
    String description,
    String imageUrl,
    RepairStatus status,
    String handlerName,
    String submittedAt,
    String resolvedAt
) {
    public static RepairResponse from(RepairRequest request) {
        String handlerName = request.getHandler() != null
                ? request.getHandler().getFullName()
                : null;
        return new RepairResponse(
            request.getRepairId(),
            request.getUser().getFullName(),
            request.getLocation(),
            request.getCategory(),
            request.getDescription(),
            request.getImageUrl(),
            request.getStatus(),
            handlerName,
            request.getSubmittedAt(),
            request.getResolvedAt()
        );
    }
}
