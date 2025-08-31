package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);
        var answerNumber = 1;

        for (var question: questions) {
            ioService.printLine("Enter the right answer number");
            ioService.printLine(question.text());

            for (var answer: question.answers()) {
                ioService.printFormattedLine("\t%s. %s", answerNumber, answer.text());
                answerNumber++;
            }
            answerNumber = 1;
            var studentAnswer = ioService.readIntForRange(1, question.answers().size(),
                    "There is no response with this number. Please try again.");

            var isAnswerValid = question.answers().get(studentAnswer - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}