package com.sh32bit.service;

import com.sh32bit.model.ServiceOffering;
import com.sh32bit.request.ServiceOfferingRequest;
import com.sh32bit.response.CategoryResponse;
import com.sh32bit.response.SalonResponse;

import java.util.List;
import java.util.Set;

public interface ServiceOfferingService {
    ServiceOffering create(ServiceOfferingRequest req, SalonResponse salonRes, CategoryResponse categoryRes);

    ServiceOffering update(ServiceOfferingRequest req, Long id) throws Exception;

    Set<ServiceOffering> getServiceOfferingsBySalonId(Long id, Long categoryId);

    ServiceOffering getServiceOfferingById(Long id) throws Exception;

    List<ServiceOffering> getServiceOfferingsByIds(Set<Long> ids);
}
