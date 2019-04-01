package com.config;

import com.concert.ShiroAspect;
import org.springframework.context.annotation.*;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2018/11/8
 */
@Configuration
@ComponentScan("com.controller")
public class AspectConfig {

    @Bean
    public ShiroAspect audience(){
        return new ShiroAspect();
    }
}
