package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;

import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TestServiceImplTest {
    private CsvQuestionDao csvQuestionDao;

    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {

        fileNameProvider = mock(TestFileNameProvider.class);
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }
    @Test
    void loadResourceCorrectly() {
        when(fileNameProvider.getTestFileName()).thenReturn("testQuestions.csv");
        var questions = csvQuestionDao.findAll();

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
