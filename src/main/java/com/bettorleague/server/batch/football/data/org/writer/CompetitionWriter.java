package com.bettorleague.server.batch.football.data.org.writer;

import com.bettorleague.server.model.football.Area;
import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.football.Season;
import com.bettorleague.server.repository.football.AreaRepository;
import com.bettorleague.server.repository.football.CompetitionRepository;
import com.bettorleague.server.repository.football.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Optional;

@Slf4j
public class CompetitionWriter implements ItemWriter<Competition> {

    private final CompetitionRepository competitionRepository;
    private final SeasonRepository seasonRepository;
    private final AreaRepository areaRepository;

    public CompetitionWriter(CompetitionRepository competitionRepository,
                             SeasonRepository seasonRepository,
                             AreaRepository areaRepository) {
        this.competitionRepository = competitionRepository;
        this.seasonRepository = seasonRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    public void write(List<? extends Competition> competitions){

        for(Competition competition: competitions){

            Optional<Competition> competitionOptional = competitionRepository.findById(competition.getId());

            //Update
            if(competitionOptional.isPresent()) {
                log.warn("[ "+competition.getName()+" ] already added, starting the update...");

                Competition current = competitionOptional.get();
                Season currentSeason = current.getCurrentSeason();
                currentSeason.setCurrentMatchday(competition.getCurrentSeason().getCurrentMatchday());
                seasonRepository.save(currentSeason);

                current.setLastUpdated(competition.getLastUpdated());
                competition = competitionRepository.save(current);

            } else{
                log.warn("[ "+competition.getName()+" ] is new, starting the fetch...");

                Optional<Area> areaOptional = areaRepository.findById(competition.getArea().getId());

                if(areaOptional.isPresent()){
                    competition.setArea(areaOptional.get());
                }else{
                    competition.setArea(areaRepository.save(competition.getArea()));
                }

                List<Season> seasons = competition.getSeasons();

                competition.setSeasons(null);
                competition.setCurrentSeason(null);

                competition = competitionRepository.save(competition);

                for(Season season: seasons){
                    season.setCompetition(competition);
                    season = seasonRepository.save(season);
                }

                competition.setSeasons(seasons);
                competition.setCurrentSeason(seasons.get(0));
                competition = competitionRepository.save(competition);

            }


        }

    }
}
