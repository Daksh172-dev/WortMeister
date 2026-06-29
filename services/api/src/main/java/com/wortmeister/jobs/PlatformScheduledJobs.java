package com.wortmeister.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PlatformScheduledJobs {
    private static final Logger log = LoggerFactory.getLogger(PlatformScheduledJobs.class);

    @Scheduled(cron = "0 0 2 * * *")
    public void dailyRetentionMaintenance() {
        log.info("scheduled_job_completed job=dailyRetentionMaintenance");
    }

    @Scheduled(cron = "0 */15 * * * *")
    public void notificationDispatchSweep() {
        log.info("scheduled_job_completed job=notificationDispatchSweep");
    }
}
