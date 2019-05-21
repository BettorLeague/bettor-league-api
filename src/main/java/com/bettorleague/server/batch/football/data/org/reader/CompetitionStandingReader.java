package com.bettorleague.server.batch.football.data.org.reader;

import com.bettorleague.server.dto.football.data.org.StandingDto;
import com.bettorleague.server.dto.football.data.org.StandingsDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CompetitionStandingReader implements ItemReader<StandingDto> {
    private String competitionId;
    private RestTemplate httpClient;
    private String apiUrl;

    private int nextStandingIndex;
    private List<StandingDto> standingDtoList;

    public CompetitionStandingReader(RestTemplate restTemplate, String apiUrl, String competitionId) {
        this.httpClient = restTemplate;
        this.apiUrl = apiUrl;
        this.competitionId = competitionId;
        this.nextStandingIndex = 0;
    }

    @Override
    public StandingDto read() {
        if (competitionDataIsNotInitialized()) {
            standingDtoList = fetchTeamDataFromAPI();
        }

        StandingDto nextStanding = null;

        if (nextStandingIndex < standingDtoList.size()) {
            nextStanding = standingDtoList.get(nextStandingIndex);
            nextStandingIndex++;
        }

        return nextStanding;
    }

    private boolean competitionDataIsNotInitialized() {
        return this.standingDtoList == null;
    }


    private List<StandingDto> fetchTeamDataFromAPI() {
        return httpClient.getForObject(apiUrl+"competitions/"+competitionId+"/standings", StandingsDto.class).getStandings();
    }

}
