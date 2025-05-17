package com.sh32bit.controller;

import com.sh32bit.mapper.NotificationMapper;
import com.sh32bit.model.Notification;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.NotificationResponse;
import com.sh32bit.service.NotificationService;
import com.sh32bit.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications/salon-owner")
@RequiredArgsConstructor
public class SalonNotificationController {
    private final NotificationService notificationService;
    private final BookingFeignClient bookingFeignClient;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsBySalonId(
            @PathVariable Long salonId) {
        List<Notification> notifications = notificationService
                .getAllNotificationsBySalonId(salonId);
        List<NotificationResponse> res = notifications
                .stream()
                .map((notification) -> {
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
