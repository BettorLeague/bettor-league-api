package com.bettorleague.server.batch.football.data.org;


import com.bettorleague.server.batch.football.data.org.processor.CompetitioStandingProcessor;
import com.bettorleague.server.batch.football.data.org.processor.CompetitionMatchProcessor;
import com.bettorleague.server.batch.football.data.org.processor.CompetitionProcessor;
import com.bettorleague.server.batch.football.data.org.processor.CompetitionTeamProcessor;
import com.bettorleague.server.batch.football.data.org.reader.CompetitionMatchReader;
import com.bettorleague.server.batch.football.data.org.reader.CompetitionReader;
import com.bettorleague.server.batch.football.data.org.reader.CompetitionStandingReader;
import com.bettorleague.server.batch.football.data.org.reader.CompetitionTeamReader;
import com.bettorleague.server.batch.football.data.org.writer.*;
import com.bettorleague.server.dto.football.data.org.CompetitionDto;
import com.bettorleague.server.dto.football.data.org.MatchDto;
import com.bettorleague.server.dto.football.data.org.StandingDto;
import com.bettorleague.server.dto.football.data.org.TeamDto;
import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.model.football.Standing;
import com.bettorleague.server.model.football.Team;
import com.bettorleague.server.repository.bettor.ContestRepository;
import com.bettorleague.server.repository.football.*;
import com.bettorleague.server.repository.security.UserRepository;
import com.bettorleague.server.service.ContestService;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableBatchProcessing
public class FootballBatchConfig {

    @Value("${football.url}")
    private String apiUrl;


    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private StandingRepository standingRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private ContestRepository contestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContestService contestService;

    @Bean
    @StepScope
    public ItemReader<CompetitionDto> restCompetitionReader(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionReader(restTemplate, apiUrl,competitionId);
    }

    @Bean
    @StepScope
    public ItemProcessor<CompetitionDto, Competition> restCompetitionProcessor(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionProcessor(mapper,competitionId);
    }

    @Bean
    public ItemWriter<Competition> restCompetitionWriter() {
        return new CompetitionWriter(competitionRepository,seasonRepository,areaRepository);
    }

    @Bean
    @StepScope
    public ItemReader<TeamDto> restCompetitionTeamReader(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionTeamReader(restTemplate, apiUrl,competitionId);
    }

    @Bean
    public ItemProcessor<TeamDto, Team> restCompetitionTeamProcessor() {
        return new CompetitionTeamProcessor(mapper);
    }

    @Bean
    @StepScope
    public ItemWriter<Team> restCompetitionTeamWriter(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionTeamWriter(competitionRepository,teamRepository,areaRepository,competitionId);
    }

    @Bean
    @StepScope
    public ItemReader<MatchDto> restCompetitionMatchReader(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionMatchReader(restTemplate, apiUrl,competitionId);
    }

    @Bean
    public ItemProcessor<MatchDto, Match> restCompetitionMatchProcessor() {
        return new CompetitionMatchProcessor(mapper);
    }

    @Bean
    @StepScope
    public ItemWriter<Match> restCompetitionMatchWriter(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionMatchWriter(competitionRepository,matchRepository,teamRepository,seasonRepository,scoreRepository,competitionId);
    }

    @Bean
    @StepScope
    public ItemReader<StandingDto> restCompetitionStandingReader(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionStandingReader(restTemplate, apiUrl,competitionId);
    }

    @Bean
    public ItemProcessor<StandingDto, Standing> restCompetitionStandingProcessor() {
        return new CompetitioStandingProcessor(mapper);
    }

    @Bean
    @StepScope
    public ItemWriter<Standing> restCompetitionStandingWriter(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionStandingWriter(competitionRepository,teamRepository,standingRepository,tableRepository,competitionId);
    }

    @Bean
    @StepScope
    public CompetitionInformationWriter restCompetitionInformationWriter(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionInformationWriter(competitionRepository,seasonRepository,matchRepository,standingRepository,teamRepository,competitionId);
    }

    @Bean
    @StepScope
    public CompetitionPublicContestWriter restCompetitionPublicContestWriter(@Value("#{jobParameters[competitionId]}") String competitionId) {
        return new CompetitionPublicContestWriter(competitionRepository,contestRepository,userRepository,contestService,competitionId);
    }


    @Bean
    Step restCompetitionStep(ItemReader<CompetitionDto> restCompetitionReader,
                             ItemProcessor<CompetitionDto, Competition> restCompetitionProcessor,
                             ItemWriter<Competition> restCompetitionWriter,
                             StepBuilderFactory stepBuilderFactory) {

        return stepBuilderFactory.get("restCompetitionStep")
                .<CompetitionDto, Competition>chunk(1)
                .reader(restCompetitionReader)
                .processor(restCompetitionProcessor)
                .writer(restCompetitionWriter)
                .build();
    }

    @Bean
    Step restCompetitionTeamStep(ItemReader<TeamDto> restCompetitionTeamReader,
                             ItemProcessor<TeamDto, Team> restCompetitionTeamProcessor,
                             ItemWriter<Team> restCompetitionTeamWriter,
                             StepBuilderFactory stepBuilderFactory) {

        return stepBuilderFactory.get("restCompetitionTeamStep")
                .<TeamDto, Team>chunk(1)
                .reader(restCompetitionTeamReader)
                .processor(restCompetitionTeamProcessor)
                .writer(restCompetitionTeamWriter)
                .build();
    }

    @Bean
    Step restCompetitionMatchStep(ItemReader<MatchDto> restCompetitionMatchReader,
                                 ItemProcessor<MatchDto, Match> restCompetitionMatchProcessor,
                                 ItemWriter<Match> restCompetitionMatcWriter,
                                 StepBuilderFactory stepBuilderFactory) {

        return stepBuilderFactory.get("restCompetitionMatchStep")
                .<MatchDto, Match>chunk(1)
                .reader(restCompetitionMatchReader)
                .processor(restCompetitionMatchProcessor)
                .writer(restCompetitionMatcWriter)
                .build();
    }

    @Bean
    Step restCompetitionStandingStep(ItemReader<StandingDto> restCompetitionStandingReader,
                                  ItemProcessor<StandingDto, Standing> restCompetitionStandingProcessor,
                                  ItemWriter<Standing> restCompetitionStandingWriter,
                                  StepBuilderFactory stepBuilderFactory) {

        return stepBuilderFactory.get("restCompetitionStandingStep")
                .<StandingDto, Standing>chunk(1)
                .reader(restCompetitionStandingReader)
                .processor(restCompetitionStandingProcessor)
                .writer(restCompetitionStandingWriter)
                .build();
    }

    @Bean
    Step restCompetitionInformationStep(CompetitionInformationWriter restCompetitionInformationWriter) {
        return steps
                .get("restCompetitionInformationStep")
                .tasklet(restCompetitionInformationWriter)
                .build();
    }

    @Bean
    Step restCompetitionPublicContestStep(CompetitionPublicContestWriter restCompetitionPublicContestWriter) {
        return steps
                .get("restCompetitionPublicContestStep")
                .tasklet(restCompetitionPublicContestWriter)
                .build();
    }

    @Bean
    Job restCompetitionJob(JobBuilderFactory jobBuilderFactory,
                           @Qualifier("restCompetitionStep") Step restCompetitionStep,
                           @Qualifier("restCompetitionTeamStep") Step restCompetitionTeamStep,
                           @Qualifier("restCompetitionMatchStep") Step restCompetitionMatchStep,
                           @Qualifier("restCompetitionStandingStep") Step restCompetitionStandingStep,
                           @Qualifier("restCompetitionInformationStep") Step restCompetitionInformationStep,
                           @Qualifier("restCompetitionPublicContestStep") Step restCompetitionPublicContestStep) {

        return jobBuilderFactory.get("restCompetitionJob")
                .incrementer(new RunIdIncrementer())
                .start(restCompetitionStep)
                .next(restCompetitionTeamStep)
                .next(restCompetitionMatchStep)
                .next(restCompetitionStandingStep)
                .next(restCompetitionInformationStep)
                .next(restCompetitionPublicContestStep)
                .build();

    }
}
