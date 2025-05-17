package com.sh32bit.mapper;

import com.sh32bit.model.Notification;
import com.sh32bit.request.NotificationRequest;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.NotificationResponse;

import java.time.LocalDateTime;

public class NotificationMapper {
    public static NotificationResponse toNotificationResponse(
            Notification notification,
            BookingResponse bookingResponse
    ) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .description(notification.getDescription())
                .userId(notification.getUserId())
                .bookingId(bookingResponse.getId())
                .salonId(notification.getSalonId())
                .createdAt(notification.getCreatedAt())
                .bookingResponse(bookingResponse)
                .build();
    }

    public static Notification toNotification(NotificationRequest req) {
        return Notification.builder()
                .type(req.getType())
                .isRead(req.getIsRead())
                .description(req.getDescription())
                .userId(req.getUserId())
                .salonId(req.getSalonId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
