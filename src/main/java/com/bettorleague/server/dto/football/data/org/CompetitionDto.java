package com.bettorleague.server.dto.football.data.org;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionDto {
    private Long id;
    private String name;
    private String code;
    private String plan;
    private Date lastUpdated;
    private AreaDto area;
    private SeasonDto currentSeason;
    private List<SeasonDto> seasons;
    private String emblemUrl;
}
