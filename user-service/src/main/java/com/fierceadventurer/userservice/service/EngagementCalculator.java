package com.fierceadventurer.userservice.service;

public class EngagementCalculator {

    private static final double SHARE_WEIGHT = 3.0;
    private static final double COMMENT_WEIGHT = 2.0;
    private static final double LIKE_WEIGHT = 1.0;

    private static double calculate(int likes , int comments , int shares , int impressions){
        double weightedInteractions = (likes* LIKE_WEIGHT) +
                (comments * COMMENT_WEIGHT) +
                (shares * SHARE_WEIGHT);

        if (impressions > 0){
            return (weightedInteractions / impressions) * 1000;
        }

        return weightedInteractions;
    }
}
