package com.rain.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 数据源配置
 *
 * @author lazy cat
 * @since 2019-04-11
 **/
@Configuration
@MapperScan(value = {"com.rain.dao"})
public class MybatisConfig {

    private static final String MAPPER_LOCATION = "classpath:mapper/*.xml";

    @Bean(name = "druidDataSource")
    @ConditionalOnMissingBean(name = "druidDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "mysqlTransactionManager")
    @ConditionalOnMissingBean(name = "mysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("druidDataSource") DruidDataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }

    @Bean(name = "transactionTemplate")
    @ConditionalOnMissingBean(name = "transactionTemplate")
    public TransactionTemplate dbOneTransactionTemplate(@Qualifier("mysqlTransactionManager") PlatformTransactionManager platformTransactionManager) {
        return new TransactionTemplate(platformTransactionManager);
    }

    @Bean(name = "sqlSessionFactory")
    @ConditionalOnMissingBean(name = "sqlSessionFactory")
    public SqlSessionFactory dbOneSqlSessionFactory(@Qualifier("druidDataSource") DruidDataSource druidDataSource,
                                                    @Qualifier("paginationInterceptor") PaginationInterceptor paginationInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(druidDataSource);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(MAPPER_LOCATION));
        Interceptor[] plugins = {paginationInterceptor};
        bean.setPlugins(plugins);
        SqlSessionFactory factory;
        try {
            factory = bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return factory;
    }

    @Bean(name = "paginationInterceptor")
    @ConditionalOnMissingBean(name = "paginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }
}