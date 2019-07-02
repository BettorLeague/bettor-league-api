package com.bettorleague.server.dto.contest;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class StatisticResponse {
    private Map<Integer,UserStatistic> stats = new HashMap<>();
}


