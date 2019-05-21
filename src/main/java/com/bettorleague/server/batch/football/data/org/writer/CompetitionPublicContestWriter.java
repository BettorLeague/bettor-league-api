package com.bettorleague.server.batch.football.data.org.writer;

import com.bettorleague.server.dto.contest.ContestRequest;
import com.bettorleague.server.model.bettor.Contest;
import com.bettorleague.server.model.bettor.ContestType;
import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.security.User;
import com.bettorleague.server.repository.bettor.ContestRepository;
import com.bettorleague.server.repository.football.CompetitionRepository;
import com.bettorleague.server.repository.security.UserRepository;
import com.bettorleague.server.service.ContestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Optional;

@Slf4j
public class CompetitionPublicContestWriter implements Tasklet {

    private final Optional<Competition> competitionOptional;
    private final CompetitionRepository competitionRepository;
    private final ContestRepository contestRepository;
    private final UserRepository userRepository;
    private final ContestService contestService;

    public CompetitionPublicContestWriter(CompetitionRepository competitionRepository,
                                          ContestRepository contestRepository,
                                          UserRepository userRepository,
                                          ContestService contestService,
                                          String competitionId){
        this.competitionRepository = competitionRepository;
        this.contestRepository = contestRepository;
        this.userRepository = userRepository;
        this.contestService = contestService;
        this.competitionOptional = competitionRepository.findById(Long.valueOf(competitionId));
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) {
        if(competitionOptional.isPresent()){
            Competition competition = competitionOptional.get();

            Optional<Contest> contestOptional = contestRepository.findByTypeAndCompetitionId(ContestType.PUBLIC,competition.getId());
            Optional<User> userOptional = userRepository.findByEmail("admin@admin.com");
            if(!contestOptional.isPresent() && userOptional.isPresent()){
                ContestRequest contestRequest = new ContestRequest();
                contestRequest.setCaption(competition.getName());
                contestRequest.setCompetitionId(competition.getId());
                contestRequest.setType(ContestType.PUBLIC);
                Contest contest = contestService.addContest(contestRequest,userOptional.get().getId());

                log.info("[ " + contest.getCaption() + " ] contest added.");
            }


            log.info("[ " + competition.getName() + " ] updated.");
        }
        return RepeatStatus.FINISHED;
    }
}
