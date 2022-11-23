package com.knd.front.schedule;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class ScheduleConfig implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context != null ? context.getBean(clazz) : null;
    }

}