package com.sh32bit.service;

import com.sh32bit.model.Notification;
import com.sh32bit.request.NotificationRequest;

import java.util.List;

public interface NotificationService {
    Notification createNotification(NotificationRequest req);

    List<Notification> getNotificationsByUserId(Long userId);

    List<Notification> getAllNotifications();

    Notification markNotificationAsRead(Long notificationId) throws Exception;

    void deleteNotification(Long notificationId);

    List<Notification> getAllNotificationsBySalonId(Long salonId);
}
