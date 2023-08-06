package com.ahmeterdogan.data.repository;

import com.ahmeterdogan.data.entity.Movie;
import com.ahmeterdogan.util.JDBCUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MovieRepository {
    private static final String FIND_ALL_SQL = "select * from movies";
    private static final String FIND_MOVIE_BY_NAME_SQL = "select * from movies where name = ?";
    private static final String FIND_MOVIE_BY_ID_SQL = "select * from movies where id = ?";
    private static final String FIND_MOVIE_BY_DIRECTOR_SQL = "select * from movies m  where m.director = ?";
    private static final String DELETE_BY_ID_SQL = "delete from movies where id = ?";
    private static final String FIND_MOVIE_BY_YEAR_SQL = "select * from movies where extract(year from release_date) = ?";
    private static final String FIND_MOVIES_BY_YEAR_AND_DIRECTOR = "select * from movies m where m.director = ? and extract(year from release_date) = ?";

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public MovieRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        initSimpleJdbcInsert();
    }

    private void initSimpleJdbcInsert() {
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("movies")
                .usingGeneratedKeyColumns("id");
    }

    public List<Movie> findMoviesByYearAndDirector(String directorName, Integer year) {
        return jdbcTemplate.query(FIND_MOVIES_BY_YEAR_AND_DIRECTOR, new MovieRowMapper(), JDBCUtil.getArguments(directorName, year));
    }

    public static class MovieRowMapper implements RowMapper<Movie> {
        @Override
        public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong(1);
            var name = rs.getString(2);
            var releaseDate = rs.getDate(3).toLocalDate();
            var director = rs.getString(4);

            return new Movie(id, name, releaseDate, director);
        }
    }

    public List<Movie> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, new MovieRowMapper());
    }

    public Optional<Movie> findMovieByName(String movieName) {
        return jdbcTemplate.query(FIND_MOVIE_BY_NAME_SQL, new MovieRowMapper(), JDBCUtil.getArguments(movieName)).stream().findFirst();
    }

    public Optional<Movie> findMovieById(long id) {
        return jdbcTemplate.query(FIND_MOVIE_BY_ID_SQL, new MovieRowMapper(), JDBCUtil.getArguments(id)).stream().findFirst();
    }

    public List<Movie> findMovieByDirector(String directorName) {
        return jdbcTemplate.query(FIND_MOVIE_BY_DIRECTOR_SQL, new MovieRowMapper(), JDBCUtil.getArguments(directorName));
    }

    public List<Movie> findMoviesByYear(int year) {
        return jdbcTemplate.query(FIND_MOVIE_BY_YEAR_SQL, new MovieRowMapper(), JDBCUtil.getArguments(year));
    }

    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID_SQL, JDBCUtil.getArguments(id));
    }

    public Movie save(Movie movie) {
        Map<String, Object> arguments = new HashMap();
        arguments.put("name", movie.getName());
        arguments.put("release_date", movie.getReleaseDate());
        arguments.put("director", movie.getDirector());
        long id = simpleJdbcInsert.executeAndReturnKey(arguments).intValue();
        movie.setId(id);
        return movie;
    }

    public List<Movie> saveAll(List<Movie> movies) {
        return movies.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }
}
