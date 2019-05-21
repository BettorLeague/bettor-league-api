package com.bettorleague.server.batch.football.data.org.reader;

import com.bettorleague.server.dto.football.data.org.CompetitionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class CompetitionReader implements ItemReader<CompetitionDto> {
    private String competitionId;
    private RestTemplate httpClient;
    private String apiUrl;
    private CompetitionDto competitionDto;

    public CompetitionReader(RestTemplate restTemplate,String apiUrl,String competitionId) {
        this.httpClient = restTemplate;
        this.apiUrl = apiUrl;
        this.competitionId = competitionId;
    }

    @Override
    public CompetitionDto read() {
        if (competitionDataIsNotInitialized()) {
            competitionDto = fetchTeamDataFromAPI();
            log.warn("[ "+competitionDto.getName()+ " ] fetching ...");
            return competitionDto;

        }else{
            return null;
        }
    }

    private boolean competitionDataIsNotInitialized() {
        return this.competitionDto == null;
    }


    private CompetitionDto fetchTeamDataFromAPI() {
        return httpClient.getForObject(apiUrl+"competitions/"+competitionId, CompetitionDto.class);
    }

}
