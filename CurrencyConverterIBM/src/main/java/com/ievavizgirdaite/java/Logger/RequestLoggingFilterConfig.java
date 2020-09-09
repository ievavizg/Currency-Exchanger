package com.ievavizgirdaite.java.Logger;

import com.ievavizgirdaite.java.Entity.Activity;
import com.ievavizgirdaite.java.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class RequestLoggingFilterConfig extends AbstractRequestLoggingFilter {

    @Autowired
    ActivityService activityService;

    @Bean
    public AbstractRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(false);
        loggingFilter.setIncludeHeaders(false);
        loggingFilter.setMaxPayloadLength(10000);
        return loggingFilter;
    }

    @Override
    protected void beforeRequest(HttpServletRequest httpServletRequest, String s) {
        logger.info(s);
        activityService.add(activityService.createActivity(s, getCurrentDateTime()));

    }

    @Override
    protected void afterRequest(HttpServletRequest httpServletRequest, String s) {

    }

    private String getCurrentDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
