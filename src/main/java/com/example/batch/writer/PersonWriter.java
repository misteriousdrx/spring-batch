package com.example.batch.writer;

import com.example.batch.model.Person;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class PersonWriter implements ItemWriter<Person> {

    @Override
    public void write(List<? extends Person> list) throws Exception {

        System.out.println("Writing");

        list.forEach(person -> {
            System.out.println(person);
        });

    }

}
