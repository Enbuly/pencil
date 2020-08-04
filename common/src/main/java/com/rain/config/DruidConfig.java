package com.rain.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 数据源配置
 *
 * @author lazy cat
 * @since  2019-04-11
 **/
@Configuration
@MapperScan(value = {"com.rain.dao"}, sqlSessionFactoryRef = "dbOneSqlSessionFactory")
public class DruidConfig {

    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Bean(name = "dbOneDruidDataSource")
    @ConditionalOnMissingBean(name = "dbOneDruidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dbOneTransactionManager")
    @ConditionalOnMissingBean(name = "dbOneTransactionManager")
    @Primary
    public DataSourceTransactionManager dbOneTransactionManager(@Qualifier("dbOneDruidDataSource") DruidDataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }

    @Bean(name = "dbOneTransactionTemplate")
    @ConditionalOnMissingBean(name = "dbOneTransactionTemplate")
    public TransactionTemplate dbOneTransactionTemplate(@Qualifier("dbOneTransactionManager") PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }

    @Bean(name = "dbOneSqlSessionFactory")
    @ConditionalOnMissingBean(name = "dbOneSqlSessionFactory")
    public SqlSessionFactory dbOneSqlSessionFactory(@Qualifier("dbOneDruidDataSource") DruidDataSource druidDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(druidDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}