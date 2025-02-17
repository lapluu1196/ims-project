package com.dinhlap.ims.dtos.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferListDTO {
    private Long offerId;

    private String candidateName;

    private String candidateEmail;

    private String approverName;

    private String department;

    private String notes;

    private String Status;
}
