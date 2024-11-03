package com.mai.db_cw_rbl.SpecializationPackage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Specialization {

    private UUID id;

    private String name;

    private String code;
}
