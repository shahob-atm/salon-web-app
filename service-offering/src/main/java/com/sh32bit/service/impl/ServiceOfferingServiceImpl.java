package com.sh32bit.service.impl;

import com.sh32bit.model.ServiceOffering;
import com.sh32bit.repository.ServiceOfferingRepository;
import com.sh32bit.request.ServiceOfferingRequest;
import com.sh32bit.response.CategoryResponse;
import com.sh32bit.response.SalonResponse;
import com.sh32bit.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {
    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering create(ServiceOfferingRequest req, SalonResponse salonRes, CategoryResponse categoryRes) {
        ServiceOffering serviceOffering = ServiceOffering.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .duration(req.getDuration())
                .salonId(salonRes.getId())
                .available(req.isAvailable())
                .categoryId(categoryRes.getId())
                .image(req.getImage())
                .build();

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering update(ServiceOfferingRequest req, Long id) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new Exception("No such service offering"));

        serviceOffering.setName(req.getName());
        serviceOffering.setDescription(req.getDescription());
        serviceOffering.setPrice(req.getPrice());
        serviceOffering.setDuration(req.getDuration());
        serviceOffering.setAvailable(req.isAvailable());
        serviceOffering.setImage(req.getImage());

        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public Set<ServiceOffering> getServiceOfferingsBySalonId(Long id, Long categoryId) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingRepository.findBySalonId(id);

        if (categoryId != null) {
            serviceOfferings = serviceOfferings.stream()
                    .filter(s -> s.getCategoryId() != null && s.getCategoryId().equals(categoryId))
                    .collect(Collectors.toSet());
        }
        return serviceOfferings;
    }

    @Override
    public ServiceOffering getServiceOfferingById(Long id) throws Exception {
        return serviceOfferingRepository
                .findById(id).orElseThrow(() -> new Exception("No such service offering"));
    }

    @Override
    public List<ServiceOffering> getServiceOfferingsByIds(Set<Long> ids) {
        return serviceOfferingRepository.findAllById(ids);
    }
}
