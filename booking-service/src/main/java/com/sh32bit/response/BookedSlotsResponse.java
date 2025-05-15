package com.sh32bit.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookedSlotsResponse {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
