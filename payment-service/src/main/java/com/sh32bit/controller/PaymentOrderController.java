package com.sh32bit.controller;

import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.model.PaymentOrder;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.PaymentLinkResponse;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.PaymentOrderService;
import com.sh32bit.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentOrderController {
    private final PaymentOrderService paymentOrderService;
    private final UserFeignClient userFeignClient;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingResponse booking,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();

        PaymentLinkResponse response = paymentOrderService.createOrder(userResponse, booking, paymentMethod);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
            @PathVariable Long paymentOrderId) {
        try {
            PaymentOrder paymentOrder = paymentOrderService.getPaymentOrderById(paymentOrderId);
            return ResponseEntity.ok(paymentOrder);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
            @RequestParam String paymentId,
            @RequestParam String paymentLinkId) throws Exception {

        PaymentOrder paymentOrder = paymentOrderService.
                getPaymentOrderByPaymentId(paymentLinkId);
        Boolean success = paymentOrderService.proceedPaymentOrder(
                paymentOrder,
                paymentId, paymentLinkId);
        return ResponseEntity.ok(success);

    }
}
