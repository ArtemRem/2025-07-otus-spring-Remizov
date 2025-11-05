package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbc;

    @Override
    public Optional<Book> findById(long id) {
        Book book = jdbc.query(
                "select b.id as book_id, b.title as book_title, b.author_id as author_id, a.full_name as author_full_name, g.id as genre_id, g.name as genre_name "
                        + "from books b "
                        + "left join authors a ON b.author_id = a.id "
                        + "left join books_genres bg ON b.id = bg.book_id "
                        + "left join genres g ON bg.genre_id = g.id "
                        + "where b.id = :id", Map.of("id", id),
                new BookResultSetExtractor());

        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {

        var books = jdbc.query(
                "select b.id as book_id, b.title as book_title, b.author_id as author_id, a.full_name as author_full_name, g.id as genre_id, g.name as genre_name "
                        + "from books b "
                        + "left join authors a ON b.author_id = a.id "
                        + "left join books_genres bg ON b.id = bg.book_id "
                        + "left join genres g ON bg.genre_id = g.id",  new BookRowMapper());
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbc.update("delete from books where id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var bgKeyHolder = new GeneratedKeyHolder();



        jdbc.update("insert into books (title, author_id) values (:title, :authorId)",
                new MapSqlParameterSource()
                        .addValue("title", book.getTitle())
                        .addValue("authorId", book.getAuthor().getId())
                , keyHolder);

//noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));

        jdbc.update("insert into books_genres (book_id, genre_id) values (:bookId, :genreId)",
                new MapSqlParameterSource()
                        .addValue("bookId", book.getId())
                        .addValue("genreId", book.getGenre().getId())
                ,bgKeyHolder);
        return book;
    }

    private Book update(Book book) {

        jdbc.update("UPDATE books_genres SET book_id=:bookId, genre_id=:genreId where book_id = :bookId", Map.of("bookId", book.getId(), "genreId", book.getGenre().getId()));

        int updatedRows = jdbc.update("UPDATE books SET title = :title, author_id = :authorId WHERE id = :id",
                Map.of( "id", book.getId(), "title", book.getTitle(), "authorId", book.getAuthor().getId()));
        if (updatedRows == 0) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }
        // Выбросить EntityNotFoundException если не обновлено ни одной записи в БД
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Book(
                    rs.getLong("book_id"),
                    rs.getString("book_title"),
                    new Author(rs.getLong("author_id"), rs.getString("author_full_name")),
                    new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
        }
    }


    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }
            var book = new Book(
                    rs.getLong("book_id"),
                    rs.getString("book_title"),
                    new Author(rs.getLong("author_id"), rs.getString("author_full_name")),
                    new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            return book;
        }
    }


}