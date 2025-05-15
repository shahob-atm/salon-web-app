package com.sh32bit.service.impl;

import com.sh32bit.enums.BookingStatus;
import com.sh32bit.model.Booking;
import com.sh32bit.repository.BookingRepository;
import com.sh32bit.request.BookingRequest;
import com.sh32bit.response.SalonReport;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.ServiceOfferingResponse;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    public Booking create(BookingRequest req,
                          UserResponse userResponse,
                          SalonResponse salonResponse,
                          Set<ServiceOfferingResponse> serviceOfferings) throws Exception {
        int totalDuration = serviceOfferings.stream()
                .mapToInt(ServiceOfferingResponse::getDuration)
                .sum();

        LocalDateTime bookingStartTime = req.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);
        Boolean isSlotAvailable = isTimeSlotAvailable(salonResponse, bookingStartTime, bookingEndTime);

        if (!isSlotAvailable) {
            throw new Exception("Slot is not available");
        }

        int totalPrice = serviceOfferings.stream()
                .mapToInt(ServiceOfferingResponse::getPrice)
                .sum();

        Set<Long> idList = serviceOfferings.stream()
                .map(ServiceOfferingResponse::getId)
                .collect(Collectors.toSet());

        Booking booking = Booking.builder()
                .salonId(salonResponse.getId())
                .customerId(userResponse.getId())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .serviceIds(idList)
                .status(BookingStatus.PENDING)
                .totalPrice(totalPrice)
                .build();

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByCustomer(UserResponse userResponse) {
        return bookingRepository.findByCustomerId(userResponse.getId());
    }

    @Override
    public SalonReport getSalonReport(SalonResponse salonResponse) {
        List<Booking> bookings = getBookingsBySalon(salonResponse.getId());

        SalonReport report = new SalonReport();

        // Total Earnings: Sum of totalPrice for all bookings
        Double totalEarnings = bookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        // Total Bookings: Count of all bookings
        Integer totalBookings = bookings.size();

        // Cancelled Bookings: Filter bookings with status CANCELLED
        List<Booking> cancelledBookings = bookings.stream()
                .filter(booking -> booking.getStatus().toString().equalsIgnoreCase("CANCELLED"))
                .toList();

        // Refunds: Calculate based on cancelled bookings (same totalPrice as refunded)
        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        report.setTotalEarnings(totalEarnings);
        report.setTotalBookings(totalBookings);
        report.setCancelledBookings(cancelledBookings.size());
        report.setTotalRefund(totalRefund);

        return report;
    }

    @Override
    public List<Booking> getBookingsBySalon(SalonResponse salonResponse) {
        return bookingRepository.findBySalonId(salonResponse.getId());
    }

    @Override
    public Booking getBookingById(Long bookingId) throws Exception {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new Exception("Booking not found"));
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, BookingStatus status) throws Exception {
        Booking existingBooking = getBookingById(bookingId);
        if (existingBooking == null) {
            throw new Exception("booking not found");
        }

        existingBooking.setStatus(status);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = bookingRepository.findBySalonId(salonId);

        if (date == null) {
            return allBookings;
        }

        return allBookings.stream()
                .filter(booking -> isSameDate(booking.getStartTime(), date) ||
                        isSameDate(booking.getEndTime(), date))
                .collect(Collectors.toList());
    }

    private Boolean isTimeSlotAvailable(SalonResponse salon,
                                        LocalDateTime bookingStartTime,
                                        LocalDateTime bookingEndTime) throws Exception {
        List<Booking> existingBookings = getBookingsBySalon(salon.getId());

        LocalDateTime salonOpenTime = salon.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salon.getCloseTime().atDate(bookingStartTime.toLocalDate());

        if (bookingStartTime.isBefore(salonOpenTime)
                || bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking time must be within salon's open hours.");
        }

        for (Booking existingBooking : existingBookings) {
            LocalDateTime existingStartTime = existingBooking.getStartTime();
            LocalDateTime existingEndTime = existingBooking.getEndTime();

            if ((bookingStartTime.isBefore(existingEndTime)
                    && bookingEndTime.isAfter(existingStartTime)) ||
                    bookingStartTime.isEqual(existingStartTime) || bookingEndTime.isEqual(existingEndTime)) {
                throw new Exception("slot not available, choose different time.");
            }
        }
        return true;
    }

    private List<Booking> getBookingsBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }
}
