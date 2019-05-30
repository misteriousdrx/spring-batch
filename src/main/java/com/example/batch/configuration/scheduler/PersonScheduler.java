package com.example.batch.configuration.scheduler;

import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class PersonScheduler {

    @Bean
    public SimpleJobLauncher personJobLauncher() throws Exception {

        ResourcelessTransactionManager transactionManager = new ResourcelessTransactionManager();

        MapJobRepositoryFactoryBean repositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
        repositoryFactoryBean.afterPropertiesSet();

        JobRepository jobRepository = repositoryFactoryBean.getObject();

        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();

        simpleJobLauncher.setJobRepository(jobRepository);

        return simpleJobLauncher;

    }

}
