package com.sh32bit.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.enums.PaymentOrderStatus;
import com.sh32bit.model.PaymentOrder;
import com.sh32bit.repository.PaymentOrderRepository;
import com.sh32bit.response.BookingResponse;
import com.sh32bit.response.PaymentLinkResponse;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.PaymentOrderService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {
    private final PaymentOrderRepository paymentOrderRepository;
    @Value("${stripe.api.key}")
    private String stripeSecretKey;
    @Value("${razorpay.api.key}")
    private String apiKey;
    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Override
    public PaymentLinkResponse createOrder(UserResponse userResponse,
                                           BookingResponse booking,
                                           PaymentMethod paymentMethod)
            throws StripeException, RazorpayException {
        Long amount = (long) booking.getTotalPrice();
        PaymentOrder order = PaymentOrder.builder()
                .amount(amount)
                .paymentMethod(paymentMethod)
                .bookingId(booking.getId())
                .salonId(booking.getSalon().getId())
                .build();

        paymentOrderRepository.save(order);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();
        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink payment = createRazorpayPaymentLink(userResponse,
                    order.getAmount(),
                    order.getId());
            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            paymentLinkResponse.setPayment_link_url(paymentUrl);
            order.setPaymentLinkId(paymentUrlId);
            paymentOrderRepository.save(order);
        } else {
            String paymentUrl = createStripePaymentLink(userResponse,
                    order.getAmount(),
                    order.getId());

            paymentLinkResponse.setPayment_link_url(paymentUrl);
        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentLink createRazorpayPaymentLink(UserResponse user,
                                                 Long Amount,
                                                 Long orderId
    ) throws RazorpayException {

        Long amount = Amount * 100;

        try {
            // Instantiate a Razorpay client with your key ID and secret
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amount);
            paymentLinkRequest.put("currency", "INR");

            // Create a JSON object with the customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getFirstName());

            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            // Create a JSON object with the notification settings
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            // Set the reminder settings
            paymentLinkRequest.put("reminder_enable", true);

            // Set the callback URL and method
            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success/" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            return razorpay.paymentLink.create(paymentLinkRequest);

        } catch (RazorpayException e) {

            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }

    @Override
    public String createStripePaymentLink(UserResponse user,
                                          Long amount,
                                          Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/" + orderId)
                .setCancelUrl("http://localhost:3000/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception {
        return paymentOrderRepository.findById(paymentOrderId)
                .orElseThrow(() -> new Exception("Payment order not found"));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentLinkId) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository
                .findByPaymentLinkId(paymentLinkId);

        if (paymentOrder == null) {
            throw new Exception("payment order not found with id " + paymentLinkId);
        }

        return paymentOrder;
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder,
                                       String paymentId, String paymentLinkId) throws RazorpayException {
        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)){

            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){

                RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
                Payment payment = razorpay.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")){
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);

                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            } else {
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);

                return true;
            }
        }

        return false;
    }
}
