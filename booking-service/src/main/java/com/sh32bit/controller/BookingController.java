package com.sh32bit.controller;

import com.sh32bit.enums.BookingStatus;
import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.mapper.BookingMapper;
import com.sh32bit.model.Booking;
import com.sh32bit.request.BookingRequest;
import com.sh32bit.response.*;
import com.sh32bit.service.BookingService;
import com.sh32bit.service.client.PaymentFeignClient;
import com.sh32bit.service.client.SalonFeignClient;
import com.sh32bit.service.client.ServiceOfferingFeignClient;
import com.sh32bit.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final UserFeignClient userFeignClient;
    private final ServiceOfferingFeignClient serviceOfferingFeignClient;
    private final SalonFeignClient salonFeignClient;
    private final PaymentFeignClient paymentFeignClient;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> create(
            @RequestBody BookingRequest req,
            @RequestParam Long salonId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();
        SalonResponse salonResponse = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        assert salonResponse != null;
        if (salonResponse.getId() == null) {
            throw new Exception("Salon not found");
        }

        Set<ServiceOfferingResponse> serviceOfferings =
                serviceOfferingFeignClient.getServiceOfferingsByIds(req.getServiceIds()).getBody();

        Booking booking = bookingService.create(req, userResponse, salonResponse, serviceOfferings);
        BookingResponse response = BookingMapper.toBookingResponse(
                booking,
                serviceOfferings,
                salonResponse,
                userResponse);
        PaymentLinkResponse res = paymentFeignClient.createPaymentLink(response, paymentMethod, jwt).getBody();

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/by-customer")
    public ResponseEntity<Set<BookingResponse>> getBookingsByCustomer(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();
        List<Booking> bookings = bookingService.getBookingsByCustomer(userResponse);

        return new ResponseEntity<>(getBookingResponses(bookings), HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();
        SalonResponse salonResponse = salonFeignClient.getSalonByOwnerId(jwt).getBody();

        SalonReport report = bookingService.getSalonReport(salonResponse);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/by-salon")
    public ResponseEntity<Set<BookingResponse>> getBookingsBySalon(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        SalonResponse salonResponse = salonFeignClient.getSalonByOwnerId(jwt).getBody();
        List<Booking> res = bookingService.getBookingsBySalon(salonResponse);

        return new ResponseEntity<>(getBookingResponses(res), HttpStatus.OK);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResponse> getBookingById(
            @PathVariable Long bookingId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);

        Set<ServiceOfferingResponse> serviceOfferingResponses =
                serviceOfferingFeignClient.getServiceOfferingsByIds(booking.getServiceIds()).getBody();

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
            @RequestParam BookingStatus status,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        Booking booking = bookingService.updateBookingStatus(bookingId, status);
        Set<ServiceOfferingResponse> serviceOfferingResponses =
                serviceOfferingFeignClient.getServiceOfferingsByIds(booking.getServiceIds()).getBody();
        SalonResponse salonResponse;

        try {
            salonResponse = salonFeignClient.getSalonByOwnerId(jwt).getBody();
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
                            serviceOfferingFeignClient.getServiceOfferingsByIds(booking.getServiceIds()).getBody();

                    SalonResponse salon;

                    try {
                        salon = salonFeignClient.getSalonById(booking.getSalonId()).getBody();
                        user = userFeignClient.getUserById(booking.getCustomerId()).getBody();
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
