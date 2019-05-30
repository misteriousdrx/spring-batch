package com.example.batch.configuration.batch;

import com.example.batch.dto.PersonBatchDto;
import com.example.batch.model.Person;
import com.example.batch.processor.PersonProcessor;
import com.example.batch.reader.PersonReader;
import com.example.batch.writer.PersonWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class PersonBatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    JobLauncher jobLauncher;


    public Step importPersonStep() {

        System.out.println("Executing step one");

        return stepBuilderFactory.get("importPersonStep")
                                 .<PersonBatchDto, Person> chunk(100)
                                 .reader(new PersonReader().personReader())
                                 .processor(new PersonProcessor())
                                 .writer(new PersonWriter())
                                 .build();

    }

    public Step asyncStep() {

        System.out.println("Executing step one");

        return stepBuilderFactory.get("importPersonStep")
                .<PersonBatchDto, Person> chunk(100)
                .reader(new PersonReader().personReader())
                .processor(asyncPersonProcessor())
                .writer(asyncPersonWriter())
                .build();

    }



    public Job importPersonJob() {
        return jobBuilderFactory.get("importPersonJob")
                                .incrementer(new RunIdIncrementer())
                                .flow(importPersonStep())
                                .end()
                                .build();
    }

    public Job asyncPersonJob() {
        return jobBuilderFactory.get("importPersonJob")
                .incrementer(new RunIdIncrementer())
                .flow(asyncStep())
                .end()
                .build();
    }




    @Scheduled(fixedRate = 30000)
    public void jobExecutionScheduler() throws Exception {
        System.out.println(" Job Started at :"+ new Date());
        long initialTimes = System.currentTimeMillis();

        JobParameters param = new JobParametersBuilder().addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
//        JobExecution execution = jobLauncher.run(importPersonJob(), param);
        JobExecution execution = jobLauncher.run(asyncPersonJob(), param);

        System.out.println("Job finished with status :" + execution.getStatus());
        System.out.println("It took " + (System.currentTimeMillis() - initialTimes) + "ms.");
    }




    public AsyncItemProcessor<PersonBatchDto, Person> asyncPersonProcessor() {
        AsyncItemProcessor<PersonBatchDto, Person> processor = new AsyncItemProcessor<>();

        processor.setDelegate(new PersonProcessor());
        processor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return processor;
    }

    public AsyncItemWriter<Person> asyncPersonWriter() {
        AsyncItemWriter<Person> writer = new AsyncItemWriter<>();

        writer.setDelegate(new PersonWriter());

        return writer;
    }


}
