package com.mai.db_cw_rbl.restoring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DBProcessResponse {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("bytes")
    private long bytes;
}
