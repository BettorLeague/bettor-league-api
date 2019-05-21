package com.bettorleague.server.model.football;

import com.bettorleague.server.model.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "TEAM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(of = {"id"})
public class Team {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    private Area area;

    @NotNull
    @Column(unique = true,name = "NAME")
    private String name;

    @Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "TLA")
    private String tla;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "WEBSITE")
    private String website;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOGO")
    private String logo;

    @Column(name = "FOUNDED")
    private Long founded;

    @Column(name = "CLUB_COLORS")
    private String clubColors;

    @Column(name = "STADIUM")
    private String stadium;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TEAM_COMPETITION",
            joinColumns = { @JoinColumn(name = "TEAM_ID") },
            inverseJoinColumns = { @JoinColumn(name = "COMPETITION_ID") })
    @JsonIgnore
    private Set<Competition> competitions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TEAM_USER",
            joinColumns = { @JoinColumn(name = "TEAM_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @Formula("(select count(*) from TEAM_USER where TEAM_USER.TEAM_ID=id)")
    private long totalUser;
}
