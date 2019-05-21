package com.bettorleague.server.model.bettor;

import com.bettorleague.server.model.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PLAYER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"contest"})
public class Player implements Comparable<Player> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    private Contest contest;

    @OneToMany(mappedBy="player",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pronostic> pronostics = new ArrayList<>();

    @Column(name = "POINTS")
    private int points = 0;

    @Column(name = "GOOD_PRONOSTIC")
    private int goodPronostic = 0;

    @Column(name = "EXACT_PRONOSTIC")
    private int exactPronostic = 0;

    @Column(name = "TOTAL_PRONOSTIC")
    private int totalPronostic = 0;

    @OneToMany(mappedBy="player",cascade= CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.points,this.points);
    }

}
