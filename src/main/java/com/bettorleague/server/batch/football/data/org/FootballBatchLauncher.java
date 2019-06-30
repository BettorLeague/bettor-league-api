package com.bettorleague.server.batch.football.data.org;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class FootballBatchLauncher {


    private final List<String> competitions = Arrays.asList("2001", "2002", "2014", "2015", "2019", "2021","2003","2013");

    private final Job job;

    private final JobLauncher jobLauncher;

    @Autowired
    public FootballBatchLauncher(@Qualifier("restCompetitionJob") Job job,
                                 JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
    }

    @Scheduled(fixedRate = 1000)
    void launchCompetitionJob() throws
            JobParametersInvalidException,
            JobExecutionAlreadyRunningException,
            JobRestartException,
            JobInstanceAlreadyCompleteException {

        for (String competition : competitions) {

            jobLauncher.run(job, newExecution(competition));

            this.pause();
        }

    }

    private JobParameters newExecution(String competitionId) {
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        JobParameter competition = new JobParameter(competitionId);
        parameters.put("competitionId", competition);

        return new JobParameters(parameters);
    }

    private void pause() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        }
    }



}
