package com.sh32bit.response;

import com.sh32bit.enums.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private SalonResponse salon;
    private UserResponse customer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<Long> serviceIds;
    private BookingStatus status;
    private int totalPrice;
    private Set<ServiceOfferingResponse> serviceOfferings;
}
