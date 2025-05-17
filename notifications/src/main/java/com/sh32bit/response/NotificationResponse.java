package com.sh32bit.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String type;
    private Boolean isRead= false;
    private String description;
    private Long userId;
    private Long bookingId;
    private Long salonId;
    private LocalDateTime createdAt;
    private BookingResponse bookingResponse;
}
