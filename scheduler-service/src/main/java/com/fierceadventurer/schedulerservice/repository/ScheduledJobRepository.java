package com.fierceadventurer.schedulerservice.repository;

import com.fierceadventurer.schedulerservice.entities.ScheduledJob;
import com.fierceadventurer.schedulerservice.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ScheduledJobRepository extends JpaRepository<ScheduledJob, UUID> {

    List<ScheduledJob> findTop10ByStatusAndScheduledAtBeforeAndDeletedAtIsNullOrderByScheduledAtAsc(
        JobStatus status, LocalDateTime currentTime);
}
