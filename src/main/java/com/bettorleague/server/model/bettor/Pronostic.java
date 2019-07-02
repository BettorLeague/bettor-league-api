package com.bettorleague.server.model.bettor;

import com.bettorleague.server.model.football.Match;
import com.bettorleague.server.model.football.ScoreResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "PRONOSTIC")
@NoArgsConstructor
@AllArgsConstructor
public class Pronostic {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="MATCH_ID",nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    private Player player;

    @Column(name = "RESULT")
    @Enumerated(EnumType.STRING)
    private ScoreResult result;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date date;

    @Column(name = "ASSIGNED")
    @JsonIgnore
    private boolean completed = false;

}
