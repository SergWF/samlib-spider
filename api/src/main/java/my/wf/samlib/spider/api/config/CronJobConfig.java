package my.wf.samlib.spider.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class CronJobConfig {

    @Value("${cron.job.enabled}")
    private boolean cronJobEnabled;

    @Scheduled()
    public void doScheduledUpdate(){

    }
}
