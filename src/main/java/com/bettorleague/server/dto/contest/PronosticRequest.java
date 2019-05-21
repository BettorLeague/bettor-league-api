package com.bettorleague.server.dto.contest;


import com.bettorleague.server.model.bettor.PronosticResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PronosticRequest {

    @Valid
    @NotNull
    private PronosticResult result;

    @NotNull
    private Long matchId;

}
