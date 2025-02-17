package com.dinhlap.ims.repositories;

import com.dinhlap.ims.entities.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
    @Query("SELECT o FROM Offer o " +
            "LEFT JOIN o.candidate c " +
            "LEFT JOIN o.approver apv " +
            "WHERE (:search IS NULL OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(apv.fullName) LIKE LOWER(CONCAT('%', :search, '%')))" +
            "AND (:department IS NULL OR o.department = :department) " +
            "AND (:offerStatus IS NULL OR o.offerStatus = :offerStatus) ")
    Page<Offer> searchAll(@Param("search") String search,
                          @Param("department") String department,
                          @Param("offerStatus") String offerStatus,
                          Pageable pageable);
}
