package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        String testFileName = fileNameProvider.getTestFileName();
        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(testFileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + testFileName);
        }
        List<QuestionDto> questionDtos = new CsvToBeanBuilder(inputStreamReader)
                .withType(QuestionDto.class)
                .withSkipLines(1)
                .withSeparator(';')
                .build()
                .parse();


        return questionDtos.stream().map(QuestionDto::toDomainObject).toList();
        } catch (IOException e) {
            throw new QuestionReadException("Error reading from CSV", e);
            }
    }
}
