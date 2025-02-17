package com.dinhlap.ims.dtos.offer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfferCreateDTO {

    private Long candidateId;

    private String contractType;

    private String level;

    private String position;

    private Long approverId;

    private String department;

    private String notes;

    private String Status;
}
