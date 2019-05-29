package com;



import com.config.Config;
import com.util.FTPUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.EntityManagerFactory;
import javax.xml.bind.JAXB;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * Created by yuchen on 2018/11/1.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com"}
        ,excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = EnableWebMvc.class)})
@EnableJpaRepositories(basePackages = "com.dao")
public class DataSourceConfig {

    @Bean
    public Config config(){
        Config config = JAXB.unmarshal(getClass().getResourceAsStream("/config.xml"), Config.class);
        //设置FTP相关配置
        FTPUtil.setConfig(config.getFtpConfig());
        return config;
    }

    @Bean
    public HikariDataSource dataSource(Config config){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        hikariConfig.setJdbcUrl("jdbc:oracle:thin:@" + config.getDataBaseConfig().getIp()+":" + config.getDataBaseConfig().getPort() + ":" + config.getDataBaseConfig().getDatabase_name());
        hikariConfig.setUsername(config.getDataBaseConfig().getUsername());
        hikariConfig.setPassword(config.getDataBaseConfig().getPassword());
        hikariConfig.setConnectionTestQuery("select 1 from dual");
        hikariConfig.setAutoCommit(false);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.setMaximumPoolSize(5);

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(HikariDataSource dataSource) throws PropertyVetoException {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(dataSource);
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.HSQL);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.entity");
        Properties properties = new Properties();
        properties.setProperty("hibernate.ejb.naming_strategy","org.hibernate.cfg.ImprovedNamingStrategy");
        properties.setProperty("hibernate.dialect","org.hibernate.dialect.Oracle10gDialect");
        properties.setProperty("hibernate.show_sql","true");
        properties.setProperty("hibernate.format_sql","true");
        properties.setProperty("hibernate.hbm2ddl.auto","none");

        localContainerEntityManagerFactoryBean.setJpaProperties(properties);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("jpa");
        localContainerEntityManagerFactoryBean.afterPropertiesSet();
        return localContainerEntityManagerFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(HikariDataSource dataSource) throws PropertyVetoException {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(dataSource);
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory(dataSource));
        return jpaTransactionManager;
    }
}
