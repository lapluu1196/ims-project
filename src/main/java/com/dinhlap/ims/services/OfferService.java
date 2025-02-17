package com.dinhlap.ims.services;

import com.dinhlap.ims.dtos.offer.OfferListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfferService {
    Page<OfferListDTO> searchAll(String search, String department, String status, Pageable pageable);
}
