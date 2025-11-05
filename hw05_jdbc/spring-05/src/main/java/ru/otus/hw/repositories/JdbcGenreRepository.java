package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final JdbcTemplate jdbc;
    private final NamedParameterJdbcTemplate namedJdbc;

    @Override
    public List<Genre> findAll() {
        return jdbc.query("select ID, NAME from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        Genre genre = (Genre) namedJdbc.query("select ID, NAME from genres where ID in (:ids)", Map.of("id", id),
                new GenreRowMapper());
        return Optional.ofNullable(genre);
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("ID");
            String fullName = rs.getString("NAME");
            return new Genre(id, fullName);
        }
    }

}