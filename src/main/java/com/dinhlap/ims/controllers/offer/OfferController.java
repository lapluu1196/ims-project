package com.dinhlap.ims.controllers.offer;

import com.dinhlap.ims.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/offers")
public class OfferController {
    @Autowired
    private OfferService offerService;

    @GetMapping("")
    public String offerList(@RequestParam(required = false) String search,
                            @RequestParam(required = false) String department,
                            @RequestParam(required = false) String status,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        int size = 10;
        var pageable = PageRequest.of(page, size);
        var offerListDTO = offerService.searchAll(search, department, status, pageable);

        model.addAttribute("offers", offerListDTO);
        model.addAttribute("keyword", search);
        model.addAttribute("department", department);
        model.addAttribute("status", status);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", offerListDTO.getTotalPages());
        model.addAttribute("totalOffers", offerListDTO.getTotalElements());

        return "contents/offer/offer-list";
    }

    @GetMapping("/create")
    public String offerCreate() {

        return "contents/offer/offer-create";
    }
}
