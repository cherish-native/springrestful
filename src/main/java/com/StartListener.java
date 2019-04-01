package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/2/22
 */
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    MonitorService monitorService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(event.getApplicationContext().getParent() == null)
        {
            //monitorService.StartMonitor();
        }
    }


}
