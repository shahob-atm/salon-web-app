package com.sh32bit.controller;

import com.sh32bit.enums.BookingStatus;
import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.mapper.BookingMapper;
import com.sh32bit.model.Booking;
import com.sh32bit.request.BookingRequest;
import com.sh32bit.response.*;
import com.sh32bit.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> create(
            @RequestBody BookingRequest req,
            @RequestParam Long salonId,
            @RequestParam PaymentMethod paymentMethod
    ) throws Exception {
        UserResponse userResponse = UserResponse.builder().id(1L).build();
        SalonResponse salonResponse = SalonResponse.builder()
                .id(1L)
                .openTime(LocalTime.parse("09:00:00"))
                .closeTime(LocalTime.parse("20:00:00"))
                .build();

        if (salonResponse.getId() == null) {
            throw new Exception("Salon not found");
        }

        Set<ServiceOfferingResponse> serviceOfferings = Set.of(ServiceOfferingResponse.builder().id(1L).build());

        Booking booking = bookingService.create(req, userResponse, salonResponse, serviceOfferings);
        PaymentLinkResponse res = PaymentLinkResponse.builder().build();

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/by-customer")
    public ResponseEntity<Set<BookingResponse>> getBookingsByCustomer() {
        UserResponse userResponse = UserResponse.builder().id(1L).build();
        List<Booking> bookings = bookingService.getBookingsByCustomer(userResponse);

        return new ResponseEntity<>(getBookingResponses(bookings), HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport() {
        UserResponse userResponse = UserResponse.builder().id(1L).build();
        SalonResponse salonResponse = SalonResponse.builder().id(1L).build();

        SalonReport report = bookingService.getSalonReport(salonResponse);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/by-salon")
    public ResponseEntity<Set<BookingResponse>> getBookingsBySalon() {
        SalonResponse salonResponse = SalonResponse.builder().id(1L).build();
        List<Booking> res = bookingService.getBookingsBySalon(salonResponse);

        return new ResponseEntity<>(getBookingResponses(res), HttpStatus.OK);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);

        Set<ServiceOfferingResponse> serviceOfferingResponses =
                Set.of(ServiceOfferingResponse.builder().id(1L).build());

        BookingResponse response = BookingMapper.toBookingResponse(
                booking,
                serviceOfferingResponses,
                null,
                null
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponse> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status
    ) throws Exception {
        Booking booking = bookingService.updateBookingStatus(bookingId, status);
        Set<ServiceOfferingResponse> serviceOfferingResponses = Set.of(ServiceOfferingResponse.builder().id(1L).build());
        SalonResponse salonResponse;

        try {
            salonResponse = SalonResponse.builder().id(1L).build();
        } catch (Exception e) {
            throw new Exception(e);
        }

        BookingResponse response = BookingMapper.toBookingResponse(booking,
                serviceOfferingResponses,
                salonResponse,
                null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookedSlotsResponse>> getBookedSlots(
            @PathVariable Long salonId,
            @PathVariable LocalDate date
    ) throws Exception {
        List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);

        List<BookedSlotsResponse> slotsDTOS = bookings.stream()
                .map(booking -> {
                    BookedSlotsResponse slotDto = new BookedSlotsResponse();

                    slotDto.setStartTime(booking.getStartTime());
                    slotDto.setEndTime(booking.getEndTime());

                    return slotDto;
                })
                .toList();

        return new ResponseEntity<>(slotsDTOS, HttpStatus.OK);
    }

    private Set<BookingResponse> getBookingResponses(List<Booking> bookings) {

        return bookings.stream()
                .map(booking -> {
                    UserResponse user;
                    Set<ServiceOfferingResponse> serviceOfferingResponses =
                            Set.of(ServiceOfferingResponse.builder().id(1L).build());

                    SalonResponse salon;

                    try {
                        salon = SalonResponse.builder().id(1L).build();
                        user = UserResponse.builder().id(1L).build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return BookingMapper.toBookingResponse(
                            booking,
                            serviceOfferingResponses,
                            salon,
                            user
                    );
                })
                .collect(Collectors.toSet());
    }
}
