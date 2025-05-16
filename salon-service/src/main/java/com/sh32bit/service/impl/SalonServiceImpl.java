package com.sh32bit.service.impl;

import com.sh32bit.model.Salon;
import com.sh32bit.repository.SalonRepository;
import com.sh32bit.request.SalonRequest;
import com.sh32bit.response.UserResponse;
import com.sh32bit.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {
    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(UserResponse res, SalonRequest req) {
        Salon salon = new Salon();
        salon.setOwnerId(res.id());
        mapRequestToSalon(salon, req);

        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(Long id, SalonRequest req, UserResponse userResponse) throws Exception {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new Exception("Salon not found"));

        if (userResponse.id().equals(salon.getOwnerId())) {
            mapRequestToSalon(salon, req);
        } else {
            throw new Exception("Owner id mismatch");
        }

        return salonRepository.save(salon);
    }

    @Override
    public List<Salon> getAllSalons() {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long id) throws Exception {
        return salonRepository.findById(id)
                .orElseThrow(() -> new Exception("Salon not found"));
    }

    @Override
    public List<Salon> searchSalonsByCity(String city) {
        return salonRepository.searchSalonsByCity(city);
    }

    @Override
    public Salon getSalonByOwnerId(long id) {
        return salonRepository.findByOwnerId(id);
    }

    private void mapRequestToSalon(Salon salon, SalonRequest req) {
        salon.setName(req.name());
        salon.setAddress(req.address());
        salon.setPhoneNumber(req.phoneNumber());
        salon.setEmail(req.email());
        salon.setCity(req.city());
        salon.setOpen(req.isOpen());
        salon.setHomeService(req.homeService());
        salon.setActive(req.active());
        salon.setOpenTime(req.openTime());
        salon.setCloseTime(req.closeTime());
        salon.setImages(req.images());
    }
}
