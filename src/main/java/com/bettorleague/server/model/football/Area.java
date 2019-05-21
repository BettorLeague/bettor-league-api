package com.bettorleague.server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Data
@Table(name = "AREA")
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Area {

    @Id
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(unique = true,name = "NAME")
    private String name;
}
