package com.assign.organization.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class JpaDatabaseConfig {

    private static final String PATH_OF_PACKAGES_TO_SCAN = "com.assign.organization.domain";
    private static final String JPA_DIALECT = "org.hibernate.dialect.MariaDBDialect";

    @Bean
    public DataSource getDataSource() {

        return
                DataSourceBuilder.create()
                .driverClassName("org.mariadb.jdbc.Driver")
                .username("test")
                .password("1234")
                .url("jdbc:mariadb://localhost:3306/assign")
                .build();
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter =
                new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(true);

        return hibernateJpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(getDataSource());
        factory.setJpaProperties(properties());
        factory.setPackagesToScan(PATH_OF_PACKAGES_TO_SCAN);
        factory.setPersistenceUnitName("mariadb");
        factory.setJpaVendorAdapter(jpaVendorAdapter());

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", JPA_DIALECT);
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "create");
        properties.put("hibernate.format_sql", "true");

        return properties;
    }

}
