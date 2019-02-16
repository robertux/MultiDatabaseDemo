package org.robertux.spring.demos.data.users;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "usersEntityManager",
        transactionManagerRef = "usersTransactionManager",
        basePackages = {"org.robertux.spring.demos.data.users"}
        )
public class UserConfiguration {

	@Bean(name="postgresBean")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.postgres")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
	
	@Bean(name = "usersEntityManager")
	public LocalContainerEntityManagerFactoryBean getUsersEntityManager(EntityManagerFactoryBuilder builder,
			@Qualifier("usersDataSource") DataSource usersDataSource) {

		return builder.dataSource(usersDataSource).packages("spring.datasource.postgres")
				.persistenceUnit("users").properties(additionalJpaProperties()).build();
	}

	Map<String, ?> additionalJpaProperties() {
		Map<String, String> map = new HashMap<>();

		map.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
		map.put("hibernate.show_sql", "true");

		return map;
	}
	
	@Bean("usersDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.postgres")
    public DataSourceProperties usersDataSourceProperties(){
        return new DataSourceProperties();
    }



    @Bean("usersDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSource usersDataSource(@Qualifier("usersDataSourceProperties") DataSourceProperties usersDataSourceProperties) {
        return usersDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "usersTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("usersEntityManager") EntityManagerFactory usersEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(usersEntityManager);

        return transactionManager;
    }
}
