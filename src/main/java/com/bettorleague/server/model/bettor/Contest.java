package com.bettorleague.server.model.bettor;

import com.bettorleague.server.model.football.Competition;
import com.bettorleague.server.model.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CONTEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"players"})
public class Contest implements Comparable<Contest>{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CAPTION")
    @NotNull
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="OWNER_ID")
    private Player owner;

    @Column(name = "TYPE")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContestType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="COMPETITION_ID")
    private Competition competition;

    @OneToMany(mappedBy="contest",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy="contest",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Message> messages = new HashSet<>();

    @Formula("(select count(*) from player where player.contest_id=id)")
    private long totalPlayer;

    @Override
    public int compareTo(Contest other) {
        return Integer.compare(other.getPlayers().size(),this.players.size());
    }

}
