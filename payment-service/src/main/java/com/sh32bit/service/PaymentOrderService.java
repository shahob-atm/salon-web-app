package com.sh32bit.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.model.PaymentOrder;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.PaymentLinkResponse;
import com.sh32bit.response.UserResponse;
import com.stripe.exception.StripeException;

public interface PaymentOrderService {
    PaymentLinkResponse createOrder(UserResponse userResponse, BookingResponse booking, PaymentMethod paymentMethod) throws StripeException, RazorpayException;

    PaymentLink createRazorpayPaymentLink(UserResponse user,
                                          Long Amount,
                                          Long orderId) throws RazorpayException;

    String createStripePaymentLink(UserResponse user, Long Amount,
                                   Long orderId) throws StripeException;

    PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception;

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
}
