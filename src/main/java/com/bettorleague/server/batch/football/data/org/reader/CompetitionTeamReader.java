package com.bettorleague.server.batch.football.data.org.reader;

import com.bettorleague.server.dto.football.data.org.TeamDto;
import com.bettorleague.server.dto.football.data.org.TeamsDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CompetitionTeamReader implements ItemReader<TeamDto> {
    private String competitionId;
    private RestTemplate httpClient;
    private String apiUrl;

    private int nexTeamIndex;
    private List<TeamDto> teamDtoList;

    public CompetitionTeamReader(RestTemplate restTemplate,String apiUrl,String competitionId) {
        this.httpClient = restTemplate;
        this.apiUrl = apiUrl;
        this.competitionId = competitionId;
        this.nexTeamIndex = 0;
    }

    @Override
    public TeamDto read() {
        if (competitionDataIsNotInitialized()) {
            teamDtoList = fetchTeamDataFromAPI();
        }

        TeamDto nextTeam = null;

        if (nexTeamIndex < teamDtoList.size()) {
            nextTeam = teamDtoList.get(nexTeamIndex);
            nexTeamIndex++;
        }

        return nextTeam;
    }

    private boolean competitionDataIsNotInitialized() {
        return this.teamDtoList == null;
    }


    private List<TeamDto> fetchTeamDataFromAPI() {
        return httpClient.getForObject(apiUrl+"competitions/"+competitionId+"/teams", TeamsDto.class).getTeams();
    }

}
