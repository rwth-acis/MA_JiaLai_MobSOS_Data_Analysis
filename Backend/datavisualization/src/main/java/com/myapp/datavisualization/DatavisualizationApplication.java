package com.myapp.datavisualization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DatavisualizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatavisualizationApplication.class, args);
    }
}
