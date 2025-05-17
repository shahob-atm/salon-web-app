package com.sh32bit.service.client;

import com.sh32bit.response.BookingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("BOOKING-SERVICE")
public interface BookingFeignClient {
    @GetMapping("/api/bookings/booking/{bookingId}")
    ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId
    ) throws Exception;
}
