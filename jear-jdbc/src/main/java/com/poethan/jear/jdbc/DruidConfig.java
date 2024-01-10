package com.poethan.jear.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DruidConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
/*
    @Bean
    public ServletRegistrationBean<StatViewServlet> startAdmin() {
        StatViewServlet servlet = new StatViewServlet();
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
                new ServletRegistrationBean(servlet, "/druid/*");
        Map<String, String> login = new HashMap<>();
        login.put("loginUsername", "admin");
        login.put("loginPassword", "admin");
        login.put("allow", "");
        servletRegistrationBean.setInitParameters(login);
        return servletRegistrationBean;
    }*/
}
