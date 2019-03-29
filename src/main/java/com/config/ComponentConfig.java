package com.config;


import com.StartListener;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.service.MonitorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Created by yuchen on 2018/11/5.
 */
@Configuration
public class ComponentConfig {

    @Bean
    public StartListener startListener(){
        StartListener startListener = new StartListener();
        return startListener;
    }

    @Bean
    public MonitorService monitorService(){
        MonitorService monitorService = new MonitorService();
        return monitorService;
    }
}
