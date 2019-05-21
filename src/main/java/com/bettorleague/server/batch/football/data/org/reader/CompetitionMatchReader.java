package com.bettorleague.server.batch.football.data.org.reader;

import com.bettorleague.server.dto.football.data.org.MatchDto;
import com.bettorleague.server.dto.football.data.org.MatchesDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CompetitionMatchReader implements ItemReader<MatchDto> {
    private String competitionId;
    private RestTemplate httpClient;
    private String apiUrl;

    private int nextMatchIndex;
    private List<MatchDto> matchDtoList;

    public CompetitionMatchReader(RestTemplate restTemplate, String apiUrl, String competitionId) {
        this.httpClient = restTemplate;
        this.apiUrl = apiUrl;
        this.competitionId = competitionId;
        this.nextMatchIndex = 0;
    }

    @Override
    public MatchDto read() {
        if (competitionDataIsNotInitialized()) {
            matchDtoList = fetchTeamDataFromAPI();
        }

        MatchDto nextMatch = null;

        if (nextMatchIndex < matchDtoList.size()) {
            nextMatch = matchDtoList.get(nextMatchIndex);
            nextMatchIndex++;
        }

        return nextMatch;
    }

    private boolean competitionDataIsNotInitialized() {
        return this.matchDtoList == null;
    }


    private List<MatchDto> fetchTeamDataFromAPI() {
        return httpClient.getForObject(apiUrl+"competitions/"+competitionId+"/matches", MatchesDto.class).getMatches();
    }

}
