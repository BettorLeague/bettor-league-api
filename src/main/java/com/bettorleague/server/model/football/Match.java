package com.bettorleague.server.model.football;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "FIXTURE")
public class Match implements Comparable<Match> {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SEASON_ID")
    @JsonIgnore
    private Season season;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    private Date utcDate;

    @Column(name="STATUS")
    private String status;

    @Column(name="MATCHDAY")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer matchday;

    @Enumerated(EnumType.STRING)
    @Column(name="STANDING_STAGE")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StandingStage stage;

    @Column(name = "GROUPE")
    @Enumerated(EnumType.STRING)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StandingGroup group;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="HOME_TEAM_ID")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="AWAY_TEAM_ID")
    private Team awayTeam;

    @OneToOne(cascade= CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="SCORE_ID")
    private Score score;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPETITION_ID", nullable = false)
    private Competition competition;

    @Override
    public int compareTo(Match other){
        if(this.matchday != null && other.getMatchday() != null){
            int matchday = this.matchday.compareTo(other.getMatchday());
            if (matchday != 0) {
                return matchday;
            }
        }
        int day = this.utcDate.compareTo(other.getUtcDate());
        if (day != 0) {
            return day;
        }
        int homeTeam = this.getHomeTeam().getName().compareTo(other.getHomeTeam().getName());
        if (homeTeam != 0) {
            return homeTeam;
        }
        return this.getAwayTeam().getName().compareTo(other.getAwayTeam().getName());
    }

}
