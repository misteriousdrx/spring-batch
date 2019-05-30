package com.example.batch.processor;

import com.example.batch.dto.PersonBatchDto;
import com.example.batch.model.Person;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class PersonProcessor implements ItemProcessor<PersonBatchDto, Person> {

    @Override
    public Person process(PersonBatchDto personBatchDto) throws Exception {
        System.out.println("Item is being processed");

        Person person = new Person();
        List<String> names = Arrays.asList(personBatchDto.getFullName().split(" "));
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyy");

        person.setFirstName("");
        person.setLastName("");

        names.forEach( (name) -> {
            if (person.getFirstName() == "") {
                person.setFirstName(name);
            } else {
                person.setLastName(person.getLastName().concat(" ").concat(name));
            }
        });

        person.setAddress(personBatchDto.getAddress());
        person.setBirthDate(LocalDate.parse(personBatchDto.getBirthDate(), dateFormat));

        return person;
    }

}
