package com.config;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@Data
@Builder
@ComponentScan
@EnableTransactionManagement
@Profile( "!test" )
@Slf4j
public class DatabaseConnectionConfigs {
    
    /**
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//            dataSourceBuilder.driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSourceBuilder.url("jdbc:sqlserver://canopus-nonprod-613f0e56.database.windows.net:1433;databaseName=canopus;authentication=ActiveDirectoryPassword;encrypt=true;sendStringParametersAsUnicode=false");
            dataSourceBuilder.username("SVC-Canopus-Dev@svc.wmtcloud.com");
            dataSourceBuilder.password("Vu6V4fVcF97v5AnIH");
            return dataSourceBuilder.build();
    }
    
    
}
