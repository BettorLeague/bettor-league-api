package com.bettorleague.server.model.football;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "COMPETITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(of = {"id"})
public class Competition {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name="TYPE")
    private Competitiontype type;

    @Column(name = "CODE",unique = true)
    private String code;

    @Column(name = "LOGO")
    private String logo;

    @Column(name = "UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AREA_ID")
    private Area area;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CURRENT_SEASON_ID")
    private Season currentSeason;

    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Season> seasons = new ArrayList<>();

    @ManyToMany(mappedBy = "competitions",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy="competition",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Standing> standings = new ArrayList<>();

    @OneToMany(mappedBy="competition",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Match> matches = new ArrayList<>();

    @ElementCollection(targetClass=StandingStage.class)
    @Enumerated(EnumType.STRING)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "COMPETITION_AVAILABLE_STAGE", joinColumns = @JoinColumn(name = "COMPETITION_ID"))
    @Column(name = "AVAILABLE_STAGE")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<StandingStage> availableStage = new HashSet<>();

    @ElementCollection(targetClass=StandingGroup.class)
    @Enumerated(EnumType.STRING)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "COMPETITION_AVAILABLE_GROUP", joinColumns = @JoinColumn(name = "COMPETITION_ID"))
    @Column(name = "AVAILABLE_GROUP")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<StandingGroup> availableGroup = new HashSet<>();

}
