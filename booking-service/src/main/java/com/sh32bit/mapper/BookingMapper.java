package com.sh32bit.mapper;

import com.sh32bit.model.Booking;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.ServiceOfferingResponse;
import com.sh32bit.response.UserResponse;

import java.util.Set;

public class BookingMapper {
    public static BookingResponse toBookingResponse(
            Booking booking,
            Set<ServiceOfferingResponse> serviceOfferingResponses,
            SalonResponse salonResponse,
            UserResponse userResponse
    ) {
        return BookingResponse.builder()
                .id(booking.getId())
                .salon(salonResponse)
                .customer(userResponse)
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .serviceIds(booking.getServiceIds())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .serviceOfferings(serviceOfferingResponses)
                .build();
    }


}
