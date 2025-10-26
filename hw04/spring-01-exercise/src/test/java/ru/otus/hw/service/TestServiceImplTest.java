package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {CsvQuestionDao.class})

public class TestServiceImplTest {
    @Autowired
    private CsvQuestionDao questionDao;

    @MockitoBean
    private TestFileNameProvider fileNameProvider;

    @Test
    void loadResourceCorrectly() {
        when(fileNameProvider.getTestFileName()).thenReturn("testQuestions.csv");
        var questions = questionDao.findAll();

        Question firstQuestion = questions.get(0);
        assertThat(firstQuestion.text())
                .isEqualTo("Is there life on Venus?");

        Question secondQuestion = questions.get(1);
        assertThat(secondQuestion.text())
                .isEqualTo("How should resources be loaded form jar in Java?");

        Question thirdQuestion = questions.get(2);
        assertThat(thirdQuestion.text())
                .isEqualTo("Which option is a good way to handle the exception?");

        Question fourthQuestion = questions.get(3);
        assertThat(fourthQuestion.text())
                .isEqualTo("What is The Ultimate Question of Life, the Universe, and Everything answer?");
    }

   }
