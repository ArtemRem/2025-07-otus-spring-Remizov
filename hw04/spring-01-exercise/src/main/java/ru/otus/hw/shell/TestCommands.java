package ru.otus.hw.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
public class TestCommands {

    private final TestRunnerService testRunnerService;

    public TestCommands(TestRunnerService testRunnerService) {
        this.testRunnerService = testRunnerService;
    }

    @ShellMethod(value = "Start student test", key = {"start", "st"})
    public void startTest() {
        testRunnerService.run();
    }
}