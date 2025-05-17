package com.sh32bit.request;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String type;
    private Boolean isRead;
    private String description;
    private Long userId;
    private Long bookingId;
    private Long salonId;
}
