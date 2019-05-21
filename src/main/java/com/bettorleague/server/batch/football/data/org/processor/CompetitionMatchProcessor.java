package com.bettorleague.server.batch.football.data.org.processor;

import com.bettorleague.server.dto.football.data.org.MatchDto;
import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.model.football.StandingGroup;
import com.bettorleague.server.model.football.StandingStage;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

import static com.bettorleague.server.model.football.StandingStage.REGULAR_SEASON;

public class CompetitionMatchProcessor implements ItemProcessor<MatchDto, Match> {

    private ModelMapper modelMapper;

    public CompetitionMatchProcessor(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public Match process(MatchDto matchDto) {
        Match match = modelMapper.map(matchDto,Match.class);
        match.setStage(parseStage(matchDto.getStage()));
        match.setGroup(parseGroupe(matchDto.getGroup()));
        return match;
    }

    private StandingStage parseStage(String stage){
        if(stage == null ) return null;
        switch (stage){
            case "REGULAR_SEASON" :
                return REGULAR_SEASON;
            case "GROUP_STAGE" :
                return StandingStage.GROUP_STAGE;
            case "ROUND_OF_16" :
                return  StandingStage.ROUND_OF_16;
            case "QUARTER_FINALS" :
                return StandingStage.QUARTER_FINALS;
            case "SEMI_FINALS" :
                return StandingStage.SEMI_FINALS;
            case "3RD_PLACE" :
                return StandingStage.SMALL_FINAL;
            case "FINAL":
                return StandingStage.FINAL;
            case "PRELIMINARY_FINAL":
                return StandingStage.PRELIMINARY_FINAL;
            case "PRELIMINARY_SEMI_FINALS":
                return StandingStage.PRELIMINARY_SEMI_FINALS;
            case "1ST_QUALIFYING_ROUND":
                return StandingStage.QUALIFYING_ROUND_1ST;
            case "2ND_QUALIFYING_ROUND":
                return StandingStage.QUALIFYING_ROUND_2ND;
            case "3RD_QUALIFYING_ROUND":
                return StandingStage.QUALIFYING_ROUND_3RD;
            case "PLAY_OFF_ROUND":
                return StandingStage.PLAY_OFF_ROUND;
            default:
                return null;


        }
    }

    private StandingGroup parseGroupe(String group){
        if(group == null ) return null;
        switch (group){
            case "Group A" :
                return StandingGroup.GROUP_A;
            case "Group B" :
                return StandingGroup.GROUP_B;
            case "Group C" :
                return StandingGroup.GROUP_C;
            case "Group D" :
                return StandingGroup.GROUP_D;
            case "Group E" :
                return StandingGroup.GROUP_E;
            case "Group F" :
                return StandingGroup.GROUP_F;
            case "Group G" :
                return StandingGroup.GROUP_G;
            case "Group H" :
                return StandingGroup.GROUP_H;
            case "Group I" :
                return StandingGroup.GROUP_I;
            case "Group J" :
                return StandingGroup.GROUP_J;
            case "Group K" :
                return StandingGroup.GROUP_K;
            case "Group L" :
                return StandingGroup.GROUP_L;
            case "GROUP_A" :
                return StandingGroup.GROUP_A;
            case "GROUP_B" :
                return StandingGroup.GROUP_B;
            case "GROUP_C" :
                return StandingGroup.GROUP_C;
            case "GROUP_D" :
                return StandingGroup.GROUP_D;
            case "GROUP_E" :
                return StandingGroup.GROUP_E;
            case "GROUP_F" :
                return StandingGroup.GROUP_F;
            case "GROUP_G" :
                return StandingGroup.GROUP_G;
            case "GROUP_H" :
                return StandingGroup.GROUP_H;
            case "GROUP_I" :
                return StandingGroup.GROUP_I;
            case "GROUP_J" :
                return StandingGroup.GROUP_J;
            case "GROUP_K" :
                return StandingGroup.GROUP_K;
            case "GROUP_L" :
                return StandingGroup.GROUP_L;
            default:
                return null;


        }
    }
}
