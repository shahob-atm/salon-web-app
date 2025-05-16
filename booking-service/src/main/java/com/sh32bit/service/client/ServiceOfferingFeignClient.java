package com.sh32bit.service.client;

import com.sh32bit.response.ServiceOfferingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("SERVICE-OFFERING")
public interface ServiceOfferingFeignClient {
    @GetMapping("/api/service-offering/list/{ids}")
    ResponseEntity<Set<ServiceOfferingResponse>> getServiceOfferingsByIds(
            @PathVariable Set<Long> ids
    );
}
