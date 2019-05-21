package com.bettorleague.server.dto.contest;

import com.bettorleague.server.model.bettor.ContestType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContestRequest {
    @NotNull
    @NotEmpty
    private String caption;

    @Valid
    private ContestType type;

    @NotNull
    private Long competitionId;
}
