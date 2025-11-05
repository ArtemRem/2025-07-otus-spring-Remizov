package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jdbc для работы с жанрами ")
@JdbcTest
@Import({JdbcGenreRepository.class, JdbcBookRepository.class})
public class JdbcGenreRepositoryTest {

    @Autowired
    private JdbcGenreRepository jdbcGenreRepository;


    List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

        @DisplayName("должен загружать все жанры")
    @Test
    public void shouldFindAllGenres() {
        var expected = dbGenres;
        var actual = jdbcGenreRepository.findAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }


    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(i -> new Genre(i, "Genre_" + i))
                .toList();
    }
}