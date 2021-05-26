package com.assign.organization.config;

import com.assign.organization.utils.CSVReader;
import com.assign.organization.utils.DuplicateNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigCustom {

    @Bean
    public CSVReader CSVReader() {
        return new CSVReader();
    }

    @Bean
    public DuplicateNameGenerator nameGenerator() {
        return new DuplicateNameGenerator();
    }
}
