package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            //ioService.printLineLocalized("TestService.answer.input.number");
            ioService.printLine(question.text());

            for (int i = 0; i < question.answers().size(); i++) {
                ioService.printFormattedLine("\t%s. %s", i + 1, question.answers().get(i).text());
                }
            var studentAnswer = ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                    "TestService.answer.input.number",
                    "TestService.answer.input.number.error");

            var isAnswerValid = question.answers().get(studentAnswer - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}