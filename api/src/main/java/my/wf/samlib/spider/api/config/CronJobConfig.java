package my.wf.samlib.spider.api.config;

import my.wf.samlib.spider.engine.SpiderEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CronJobConfig {

    @Value("${cron.job.enabled}")
    private boolean cronJobEnabled;
    private String delay = "222";

    @Autowired
    private SpiderEngine spiderEngine;

    @Scheduled(fixedDelayString = "${cron.job.fixed-delay}")
    public void doScheduledUpdate() {
        spiderEngine.checkAllAuthors();
    }
}
