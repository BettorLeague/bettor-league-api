package com.bettorleague.server.batch.football.data.org.processor;

import com.bettorleague.server.dto.football.data.org.TeamDto;
import com.bettorleague.server.model.football.Team;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CompetitionTeamProcessor implements ItemProcessor<TeamDto, Team> {

    private ModelMapper modelMapper;

    public CompetitionTeamProcessor(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public Team process(TeamDto teamDto) {
        Team team = modelMapper.map(teamDto,Team.class);
        team.setLogo(teamDto.getCrestUrl());
        team.setStadium(teamDto.getVenue());
        return team;
    }
}
