package com.config;

import javax.xml.bind.annotation.*;

/**
 * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
 * @since 2019/2/22
 */
@XmlRootElement(name = "config")
public class Config {

    private WebConfig webConfig;

    private DataBaseConfig dataBaseConfig;

    private ZookeeperConfig zookeeperConfig;

    @XmlElement(name = "web_config")
    public WebConfig getWebConfig() {
        return webConfig;
    }

    public void setWebConfig(WebConfig webConfig) {
        this.webConfig = webConfig;
    }


    @XmlElement(name = "database_config")
    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }
    @XmlElement(name = "zookeeper_config")
    public ZookeeperConfig getZookeeperConfig() {
        return zookeeperConfig;
    }

    public void setZookeeperConfig(ZookeeperConfig zookeeperConfig) {
        this.zookeeperConfig = zookeeperConfig;
    }
}
