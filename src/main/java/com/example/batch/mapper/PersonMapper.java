package com.example.batch.mapper;

import com.example.batch.dto.PersonBatchDto;
import com.example.batch.model.Person;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;

public class PersonMapper implements FieldSetMapper<PersonBatchDto> {

    @Override
    public PersonBatchDto mapFieldSet(FieldSet fieldSet) throws BindException {
        PersonBatchDto person = new PersonBatchDto();

        person.setFullName(fieldSet.readString("fullName"));
        person.setAddress(fieldSet.readString("address"));
        person.setBirthDate(fieldSet.readString("birthDate"));

        return person;
    }

}
