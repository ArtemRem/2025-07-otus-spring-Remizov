package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        String testFileName = fileNameProvider.getTestFileName();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(testFileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + testFileName);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        List<QuestionDto> questionDtos = new CsvToBeanBuilder(inputStreamReader)
                .withType(QuestionDto.class)
                .withSkipLines(1)
                .withSeparator(';')
                .build()
                .parse();


        return questionDtos.stream().map(QuestionDto::toDomainObject).toList();
    }
}
