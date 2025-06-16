package smartmunimji.backend.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import smartmunimji.backend.daos.OrderDao;
import smartmunimji.backend.daos.ProductDao;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    basePackages = "smartmunimji.backend.daos",
    includeFilters = {
        @org.springframework.context.annotation.ComponentScan.Filter(
            type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
            classes = {ProductDao.class, OrderDao.class}
        )
    },
    entityManagerFactoryRef = "radheShyamEntityManager",
    transactionManagerRef = "radheShyamTransactionManager"
)
public class RadheShyamDataSourceConfig {

    @Bean(name = "radheShyamDataSource")
    @ConfigurationProperties(prefix = "spring.second-datasource")
    public DataSource radheShyamDataSource() {
        return DataSourceBuilder.create()
            .type(com.zaxxer.hikari.HikariDataSource.class)
            .build();
    }

    @Bean(name = "radheShyamEntityManager")
    public LocalContainerEntityManagerFactoryBean radheShyamEntityManager(
            @Qualifier("radheShyamDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("smartmunimji.backend.entities");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        properties.put("hibernate.hbm2ddl.auto", "validate");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "radheShyamTransactionManager")
    public PlatformTransactionManager radheShyamTransactionManager(
            @Qualifier("radheShyamEntityManager") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory.getObject());
    }
}