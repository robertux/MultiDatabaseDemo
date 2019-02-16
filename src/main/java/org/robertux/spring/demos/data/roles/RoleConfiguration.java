package org.robertux.spring.demos.data.roles;

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
@EnableJpaRepositories(entityManagerFactoryRef = "rolesEntityManager", 
		transactionManagerRef = "rolesTransactionManager", 
		basePackages = {"org.robertux.spring.demos.data.roles" })
public class RoleConfiguration {

	@Bean(name = "mysqlBean")
	@ConfigurationProperties(prefix = "spring.datasource.mysql")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "rolesEntityManager")
	public LocalContainerEntityManagerFactoryBean getRolesEntityManager(EntityManagerFactoryBuilder builder,
			@Qualifier("rolesDataSource") DataSource rolesDataSource) {

		return builder.dataSource(rolesDataSource).packages("spring.datasource.mysql")
				.persistenceUnit("roles").properties(additionalJpaProperties()).build();
	}

	Map<String, ?> additionalJpaProperties() {
		Map<String, String> map = new HashMap<>();

		map.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		map.put("hibernate.show_sql", "true");

		return map;
	}
	
	@Bean("rolesDataSourceProperties")
    @Primary
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSourceProperties rolesDataSourceProperties(){
        return new DataSourceProperties();
    }



    @Bean("rolesDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource.mysql")
    public DataSource rolesDataSource(@Qualifier("rolesDataSourceProperties") DataSourceProperties rolesDataSourceProperties) {
        return rolesDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "rolesTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("rolesEntityManager") EntityManagerFactory rolesEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(rolesEntityManager);

        return transactionManager;
    }
}
