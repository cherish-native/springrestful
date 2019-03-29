package com.service;

import com.cloud.CloudManager;
import com.config.Config;
import com.dao.InformationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/2/22
 */
@Service
public class MonitorService {

    @Autowired
    Config config;

    @Autowired
    private InformationDao informationDao;

    public void StartMonitor(){

        CloudManager cloudManager = new CloudManager(config.getZookeeperConfig().getConnectString(),informationDao);
        cloudManager.createProjectPersistNode();

        informationDao.findByIsStart(1).stream().forEach(
                t ->
                        cloudManager.exist(CloudManager.ZK_PERJECT_NODE
                                + "/"
                                + t.getCode())
        );
    }
}
