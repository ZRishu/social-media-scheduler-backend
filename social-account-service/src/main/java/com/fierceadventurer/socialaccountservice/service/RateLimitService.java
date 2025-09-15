package com.fierceadventurer.socialaccountservice.service;

import java.util.UUID;

public interface RateLimitService {
    void checkAndDecrementQuota(UUID accountId) ;
}
