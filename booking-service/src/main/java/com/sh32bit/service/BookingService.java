package com.sh32bit.service;

import com.sh32bit.enums.BookingStatus;
import com.sh32bit.model.Booking;
import com.sh32bit.request.BookingRequest;
import com.sh32bit.response.SalonReport;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.ServiceOfferingResponse;
import com.sh32bit.response.UserResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {
    Booking create(BookingRequest req,
                   UserResponse userResponse,
                   SalonResponse salonResponse,
                   Set<ServiceOfferingResponse> serviceOfferings) throws Exception;

    List<Booking> getBookingsByCustomer(UserResponse userResponse);

    SalonReport getSalonReport(SalonResponse salonResponse);

    List<Booking> getBookingsBySalon(SalonResponse salonResponse);

    Booking getBookingById(Long bookingId) throws Exception;

    Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception;

    List<Booking> getBookingsByDate(LocalDate date, Long salonId);
}
