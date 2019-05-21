package com.bettorleague.server.dto.football.data.org;

import com.bettorleague.server.model.football.Score;
import com.bettorleague.server.model.football.Team;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDto {
    private Long id;
    private SeasonDto season;
    private Date utcDate;
    private String status;
    private Integer matchday;
    private String stage;
    private String group;
    private Team homeTeam;
    private Team awayTeam;
    private Score score;
    private Date lastUpdated;
}
