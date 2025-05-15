package com.sh32bit.model;

import com.sh32bit.enums.PaymentMethod;
import com.sh32bit.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status;

    private PaymentMethod paymentMethod;

    private String paymentLinkId;

    private Long userId;

    private Long bookingId;

    @Column(nullable = false)
    private Long salonId;

    @PrePersist
    protected void defaultPaymentOrderStatus() {
        this.status = PaymentOrderStatus.PENDING;
    }
}
