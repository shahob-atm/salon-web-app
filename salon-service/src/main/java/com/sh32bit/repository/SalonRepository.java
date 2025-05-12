package com.sh32bit.repository;

import com.sh32bit.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalonRepository extends JpaRepository<Salon, Long> {
    Salon findByOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT s FROM Salon s WHERE " +
            "(LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "s.active = true")
    List<Salon> searchSalonsByCity(@Param("keyword") String keyword);
}
