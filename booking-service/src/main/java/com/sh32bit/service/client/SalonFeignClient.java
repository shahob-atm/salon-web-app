package com.sh32bit.service.client;

import com.sh32bit.response.SalonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALON-SERVICE")
public interface SalonFeignClient {
    @GetMapping("/api/salons/salon/by-owner")
    ResponseEntity<SalonResponse> getSalonByOwnerId(
            @RequestHeader("Authorization") String jwt) throws Exception;

    @GetMapping("/api/salons/salon/{id}")
    ResponseEntity<SalonResponse> getSalonById(@PathVariable("id") Long id) throws Exception;
}
