package com.sh32bit.service.client;

import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.PaymentLinkResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {
    @PostMapping("/api/payments/create")
    ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingResponse booking,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt) throws Exception;
}
