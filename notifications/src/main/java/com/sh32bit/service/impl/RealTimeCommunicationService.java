package com.sh32bit.service.impl;

import com.sh32bit.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealTimeCommunicationService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendNotification(NotificationResponse notification) {
        simpMessagingTemplate.convertAndSend(
                "/notification/user/"+notification.getUserId(),
                notification
        );
        simpMessagingTemplate.convertAndSend(
                "/notification/salon/"+notification.getSalonId(),
                notification
        );
    }
}
