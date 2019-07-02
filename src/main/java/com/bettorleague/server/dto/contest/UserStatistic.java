package com.bettorleague.server.dto.contest;

import com.bettorleague.server.model.bettor.Contest;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserStatistic{
    private List<Integer> pronostics = new ArrayList<>();
    private List<Integer> goodPronostics = new ArrayList<>();
    private Set<Contest> contests = new HashSet<>();
    public UserStatistic(){
        this.pronostics.add(0,0);
        this.pronostics.add(1,0);
        this.pronostics.add(2,0);
        this.pronostics.add(3,0);
        this.pronostics.add(4,0);
        this.pronostics.add(5,0);
        this.pronostics.add(6,0);
        this.pronostics.add(7,0);
        this.pronostics.add(8,0);
        this.pronostics.add(9,0);
        this.pronostics.add(10,0);
        this.pronostics.add(11,0);

        this.goodPronostics.add(0,0);
        this.goodPronostics.add(1,0);
        this.goodPronostics.add(2,0);
        this.goodPronostics.add(3,0);
        this.goodPronostics.add(4,0);
        this.goodPronostics.add(5,0);
        this.goodPronostics.add(6,0);
        this.goodPronostics.add(7,0);
        this.goodPronostics.add(8,0);
        this.goodPronostics.add(9,0);
        this.goodPronostics.add(10,0);
        this.goodPronostics.add(11,0);

        /*
        this.pronostics.put("JAN",0);
        this.pronostics.put("FEB",0);
        this.pronostics.put("MAR",0);
        this.pronostics.put("APR",0);
        this.pronostics.put("MAY",0);
        this.pronostics.put("JUN",0);
        this.pronostics.put("JUL",0);
        this.pronostics.put("AUG",0);
        this.pronostics.put("SEP",0);
        this.pronostics.put("OCT",0);
        this.pronostics.put("NOV",0);
        this.pronostics.put("DEC",0);

        this.goodPronostics.put("JAN",0);
        this.goodPronostics.put("FEB",0);
        this.goodPronostics.put("MAR",0);
        this.goodPronostics.put("APR",0);
        this.goodPronostics.put("MAY",0);
        this.goodPronostics.put("JUN",0);
        this.goodPronostics.put("JUL",0);
        this.goodPronostics.put("AUG",0);
        this.goodPronostics.put("SEP",0);
        this.goodPronostics.put("OCT",0);
        this.goodPronostics.put("NOV",0);
        this.goodPronostics.put("DEC",0);*/
    }

}
