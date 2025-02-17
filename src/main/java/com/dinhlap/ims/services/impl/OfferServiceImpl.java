package com.dinhlap.ims.services.impl;

import com.dinhlap.ims.dtos.offer.OfferListDTO;
import com.dinhlap.ims.repositories.OfferRepository;
import com.dinhlap.ims.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {
    @Autowired
    private OfferRepository offerRepository;

    @Override
    public Page<OfferListDTO> searchAll(String search, String department, String status, Pageable pageable) {
        var result = offerRepository.searchAll(search, department, status, pageable);
        return result.map(offer -> {
            var offerListDTO = new OfferListDTO();
            offerListDTO.setOfferId(offer.getOfferId());
            offerListDTO.setCandidateName(offer.getCandidate().getFullName());
            offerListDTO.setCandidateEmail(offer.getCandidate().getEmail());
            offerListDTO.setApproverName(offer.getApprover().getFullName());
            offerListDTO.setDepartment(offer.getDepartment());
            offerListDTO.setNotes(offer.getNotes());
            offerListDTO.setStatus(offer.getOfferStatus());
            return offerListDTO;
        });
    }

}
