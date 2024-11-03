package com.mai.db_cw_rbl.LawyerPackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Lawyer {

    private UUID id;

    private UUID userId;

    private UUID specializationId;

    private int yearsExperience;

    private String licenceNumber;
}
