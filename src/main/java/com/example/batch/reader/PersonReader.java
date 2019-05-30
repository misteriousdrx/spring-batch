package com.example.batch.reader;

import com.example.batch.dto.PersonBatchDto;
import com.example.batch.mapper.PersonMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.core.io.ClassPathResource;

public class PersonReader {

    public FlatFileItemReader personReader() {

        FlatFileItemReader<PersonBatchDto> reader = new FlatFileItemReader<PersonBatchDto>();
        DefaultLineMapper<PersonBatchDto> lineMapper = new DefaultLineMapper<PersonBatchDto>();
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();

        tokenizer.setNames(
                "fullName",
                "address",
                "birthDate"
        );

        tokenizer.setColumns(
                new Range(01, 50),
                new Range(51, 100),
                new Range(101, 110)
        );

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new PersonMapper());

        reader.setName("personItemReader");
        reader.setResource(new ClassPathResource("personInputFile.txt"));
        reader.setLineMapper(lineMapper);

        return reader;

    }

}
