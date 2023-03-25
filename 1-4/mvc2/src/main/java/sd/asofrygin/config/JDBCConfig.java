package sd.asofrygin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import sd.asofrygin.dao.ListJDBCDao;

import javax.sql.DataSource;

@Configuration
public class JDBCConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:taskList.db");
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public ListJDBCDao listJDBCDao(DataSource source) {
        return new ListJDBCDao(source);
    }
}
