package com.bettorleague.server.batch.football.data.org.processor;

import com.bettorleague.server.dto.football.data.org.StandingDto;
import com.bettorleague.server.model.football.Standing;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CompetitioStandingProcessor implements ItemProcessor<StandingDto, Standing> {

    private ModelMapper modelMapper;

    public CompetitioStandingProcessor(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public Standing process(StandingDto standingDto) {
        Standing standing = modelMapper.map(standingDto,Standing.class);
        return standing;
    }
}
