package my.wf.samlib.spider.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.wf.samlib.spider.engine.SpiderEngine;
import my.wf.samlib.spider.spider.PageParser;
import my.wf.samlib.spider.spider.PageReader;
import my.wf.samlib.spider.spider.Spider;
import my.wf.samlib.spider.spider.SpiderSamlib;
import my.wf.samlib.spider.spider.impl.SamlibPageParser;
import my.wf.samlib.spider.spider.impl.SamlibPageReader;
import my.wf.samlib.spider.storage.AuthorStorage;
import my.wf.samlib.spider.storage.AuthorStorageImpl;
import my.wf.samlib.spider.storage.DataKeeper;
import my.wf.samlib.spider.storage.FileDataKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class BeanConfig {
    @Value("${access.check.page.url}")
    private String accessCheckPageUrl;
    @Value("${storage.file.path}")
    private String storageFilePath;

    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    public PageReader pageReader(){
        return new SamlibPageReader();
    }

    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    PageParser pageParser(){
        return new SamlibPageParser();
    }

    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    public Spider spider(){
        return new SpiderSamlib(pageReader(), pageParser());
    }

    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    public SpiderEngine spiderEngine(){
        return new SpiderEngine(spider(), authorStorage(), accessCheckPageUrl);
    }

    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    public DataKeeper dataKeeper() {
        return new FileDataKeeper(storageFilePath, objectMapper());
    }

    @Bean
    public AuthorStorage authorStorage() {
        return new AuthorStorageImpl(dataKeeper());
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
