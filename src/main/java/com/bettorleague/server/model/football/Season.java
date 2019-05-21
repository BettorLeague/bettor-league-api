package com.bettorleague.server.model.football;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "SEASON")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(of = {"id","currentLeader"})
public class Season{

    @Id
    @Column(name = "ID")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date endDate;

    @Column(name = "CURRENT_MATCHDAY")
    private Long currentMatchday;

    @Column(name = "AVAILABLE_MATCHDAY")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long availableMatchday;

    @Column(name = "AVAILABLE_MATCH_PER_DAY")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long availableMatchPerDay;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPETITION_ID", nullable = false)
    @JsonIgnore
    private Competition competition;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LEADER_TEAM_ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Team currentLeader;
}
