package com.sh32bit.messaging;

import com.sh32bit.request.NotificationRequest;
import com.sh32bit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {
    private final NotificationService notificationService;

    @RabbitListener(queues = "notification-queue")
    public void sentBookingUpdateEvent(NotificationRequest notification) {
        notificationService.createNotification(notification);
    }
}
