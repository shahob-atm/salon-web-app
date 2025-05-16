package com.sh32bit.service;

import com.sh32bit.model.Salon;
import com.sh32bit.request.SalonRequest;
import com.sh32bit.response.UserResponse;

import java.util.List;

public interface SalonService {
    Salon createSalon(UserResponse userResponse, SalonRequest salonRequest);

    Salon updateSalon(Long id, SalonRequest salonRequest, UserResponse userResponse) throws Exception;

    List<Salon> getAllSalons();

    Salon getSalonById(Long id) throws Exception;

    List<Salon> searchSalonsByCity(String city);

    Salon getSalonByOwnerId(long id);
}
