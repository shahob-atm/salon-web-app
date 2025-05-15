package com.sh32bit.response;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLinkResponse {
    private String payment_link_url;
    private String payment_link_id;
}
