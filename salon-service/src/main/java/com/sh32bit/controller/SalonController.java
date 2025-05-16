package com.sh32bit.controller;

import com.sh32bit.mapper.SalonMapper;
import com.sh32bit.model.Salon;
import com.sh32bit.request.SalonRequest;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.SalonService;
import com.sh32bit.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {
    private final SalonService salonService;
    private final UserFeignClient userFeignClient;

    @PostMapping("/create")
    public ResponseEntity<SalonResponse> createSalon(
            @RequestBody SalonRequest salonRequest,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();

        Salon salon = salonService.createSalon(userResponse, salonRequest);
        SalonResponse res = SalonMapper.mapToSalonResponse(salon);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SalonResponse> updateSalon(@PathVariable("id") Long id,
                                                     @RequestBody SalonRequest salonRequest,
                                                     @RequestHeader("Authorization") String jwt) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();

        Salon salon = salonService.updateSalon(id, salonRequest, userResponse);
        SalonResponse res = SalonMapper.mapToSalonResponse(salon);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SalonResponse>> getAllSalons() {
        List<Salon> salons = salonService.getAllSalons();
        List<SalonResponse> res = salons.stream().map(SalonMapper::mapToSalonResponse).toList();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/salon/{id}")
    public ResponseEntity<SalonResponse> getSalonById(@PathVariable("id") Long id) throws Exception {
        Salon salon = salonService.getSalonById(id);
        SalonResponse res = SalonMapper.mapToSalonResponse(salon);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SalonResponse>> searchSalonsByCity(@RequestParam("city") String city) {
        List<Salon> salons = salonService.searchSalonsByCity(city);
        List<SalonResponse> res = salons.stream().map(SalonMapper::mapToSalonResponse).toList();

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/salon/by-owner")
    public ResponseEntity<SalonResponse> getSalonByOwnerId(
            @RequestHeader("Authorization") String jwt) throws Exception {
        UserResponse userResponse = userFeignClient.getUserByJwtToken(jwt).getBody();
        assert userResponse != null;
        Salon salon = salonService.getSalonByOwnerId(userResponse.id());
        SalonResponse res = SalonMapper.mapToSalonResponse(salon);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
