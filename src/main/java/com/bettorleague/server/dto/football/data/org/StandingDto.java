package com.bettorleague.server.dto.football.data.org;

import com.bettorleague.server.model.football.StandingTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandingDto {
    private String stage;
    private String type;
    private String group;
    private List<StandingTable> table;
}
