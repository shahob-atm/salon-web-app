package com.sh32bit.controller;

import com.sh32bit.mapper.ServiceOfferingMapper;
import com.sh32bit.model.ServiceOffering;
import com.sh32bit.request.ServiceOfferingRequest;
import com.sh32bit.response.CategoryResponse;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.ServiceOfferingResponse;
import com.sh32bit.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    @PostMapping("/create")
    public ResponseEntity<ServiceOfferingResponse> create(@RequestBody ServiceOfferingRequest req) {
        SalonResponse salonRes = SalonResponse.builder().id(1L).build();
        CategoryResponse categoryRes = CategoryResponse.builder().id(2L).build();

        ServiceOffering serviceOffering = serviceOfferingService.create(req, salonRes, categoryRes);
        ServiceOfferingResponse res = ServiceOfferingMapper.toServiceOfferingResponse(serviceOffering);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceOfferingResponse> update(@PathVariable("id") Long id,
                                                          @RequestBody ServiceOfferingRequest req)
            throws Exception {
        ServiceOffering serviceOffering = serviceOfferingService.update(req, id);
        ServiceOfferingResponse res = ServiceOfferingMapper.toServiceOfferingResponse(serviceOffering);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/salon/{id}")
    public ResponseEntity<Set<ServiceOfferingResponse>> getServiceOfferingsBySalonId(
            @PathVariable("id") Long id,
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingService
                .getServiceOfferingsBySalonId(id, categoryId);

        Set<ServiceOfferingResponse> res = serviceOfferings.stream()
                .map(ServiceOfferingMapper::toServiceOfferingResponse).collect(Collectors.toSet());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOfferingResponse> getServiceOfferingById(@PathVariable("id") Long id)
            throws Exception
    {
        ServiceOffering serviceOffering = serviceOfferingService.getServiceOfferingById(id);
        ServiceOfferingResponse res = ServiceOfferingMapper.toServiceOfferingResponse(serviceOffering);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOfferingResponse>> getServiceOfferingsByIds(
            @PathVariable Set<Long> ids
    ) {
        List<ServiceOffering> serviceOfferings = serviceOfferingService.getServiceOfferingsByIds(ids);
        Set<ServiceOfferingResponse> res = serviceOfferings.stream().map(ServiceOfferingMapper::toServiceOfferingResponse)
                .collect(Collectors.toSet());

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
