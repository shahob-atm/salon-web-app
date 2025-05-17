package com.sh32bit.controller;

import com.sh32bit.mapper.NotificationMapper;
import com.sh32bit.model.Notification;
import com.sh32bit.request.NotificationRequest;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.NotificationResponse;
import com.sh32bit.service.NotificationService;
import com.sh32bit.service.client.BookingFeignClient;
import com.sh32bit.service.impl.RealTimeCommunicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;
    private final RealTimeCommunicationService realTimeCommunicationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestBody NotificationRequest req) throws Exception {
        Notification notification = notificationService.createNotification(req);
        BookingResponse booking = bookingFeignClient.getBookingById(notification.getBookingId()).getBody();
        assert booking != null;
        NotificationResponse response = NotificationMapper.toNotificationResponse(notification, booking);
        realTimeCommunicationService.sendNotification(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(
            @PathVariable Long userId) {
        List<Notification> notifications = notificationService
                .getNotificationsByUserId(userId);

        return getListResponseEntity(notifications);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        List<Notification> notifications = notificationService
                .getAllNotifications();

        return getListResponseEntity(notifications);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(
            @PathVariable Long notificationId) throws Exception {
        Notification updatedNotification = notificationService
                .markNotificationAsRead(notificationId);
        BookingResponse booking = bookingFeignClient
                .getBookingById(updatedNotification.getBookingId()).getBody();

        assert booking != null;
        NotificationResponse response = NotificationMapper.toNotificationResponse(
                updatedNotification,
                booking
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<List<NotificationResponse>> getListResponseEntity(List<Notification> notifications) {
        List<NotificationResponse> res = notifications.stream().map((notification) -> {
            BookingResponse booking = null;
            try {
                booking = bookingFeignClient
                        .getBookingById(notification.getBookingId()).getBody();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            assert booking != null;
            return NotificationMapper.toNotificationResponse(notification, booking);
        }).toList();

        return ResponseEntity.ok(res);
    }
}
