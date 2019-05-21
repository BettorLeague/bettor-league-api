package com.bettorleague.server.batch.football.data.org.processor;

import com.bettorleague.server.dto.football.data.org.CompetitionDto;
import com.bettorleague.server.model.football.Competition;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CompetitionProcessor implements ItemProcessor<CompetitionDto,Competition> {

    private ModelMapper modelMapper;
    private String competitionId;

    public CompetitionProcessor(ModelMapper modelMapper,
                                String competitionId){
        this.modelMapper = modelMapper;
        this.competitionId = competitionId;
    }

    @Override
    public Competition process(CompetitionDto competitionDto) {
        Competition competition = modelMapper.map(competitionDto,Competition.class);
        competition.setLogo( this.getCompetitionlogo(competitionId) );
        return competition;
    }

    private String getCompetitionlogo(String id){
        switch (id){
            case "2015": return "https://upload.wikimedia.org/wikipedia/fr/9/9b/Logo_de_la_Ligue_1_%282008%29.svg";
            case "2001": return "https://upload.wikimedia.org/wikipedia/fr/b/bf/UEFA_Champions_League_logo_2.svg";
            case "2021": return "https://upload.wikimedia.org/wikipedia/fr/f/f2/Premier_League_Logo.svg";
            case "2002": return "https://upload.wikimedia.org/wikipedia/en/d/df/Bundesliga_logo_%282017%29.svg";
            case "2014": return "https://upload.wikimedia.org/wikipedia/fr/2/23/Logo_La_Liga.png";
            case "2019": return "https://upload.wikimedia.org/wikipedia/en/f/f7/LegaSerieAlogoTIM.png";
            case "2000": return "https://upload.wikimedia.org/wikipedia/en/6/67/2018_FIFA_World_Cup.svg";
            case "2003": return "https://upload.wikimedia.org/wikipedia/fr/3/3e/Eredivisie-Logo.svg";
            case "2013": return "https://upload.wikimedia.org/wikipedia/en/4/42/Campeonato_Brasileiro_S%C3%A9rie_A_logo.png";
            case "2017": return "http://www.thefinalball.com/img/logos/edicoes/70079_imgbank_.png";
            default: return null;
        }
    }
}
