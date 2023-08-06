package com.ahmeterdogan.controller;

import com.ahmeterdogan.data.entity.Movie;
import com.ahmeterdogan.service.MovieService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Movie>> getAll() {
        return ResponseEntity.ok(movieService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable("id") long id) {
        Optional<Movie> movie = movieService.getMovieById(id);

        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<Integer> deleteById(@RequestParam long id) {
        return ResponseEntity.ok(movieService.deleteById(id));
    }

    @GetMapping("/search-by-name")
    private ResponseEntity<Movie> getMovieByName(@RequestParam("name") String movieName) {
        Optional<Movie> movie = movieService.getMovieByName(movieName);

        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/search-by-director")
    private ResponseEntity<List<Movie>> getMoviesByDirectorOrYear(@RequestParam("name") String directorName) {
        List<Movie> movies = movieService.getMovieByDirector(directorName);

        if(movies.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(movies);
    }

    @GetMapping("/search-by-year")
    public ResponseEntity<List<Movie>> getMoviesByYear(@RequestParam("year") int year) {
        List<Movie> movies = movieService.getMoviesByYear(year);

        if(movies.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(movies);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchMovies(
            @RequestParam(name = "name", required = false) String movieName,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "director", required = false) String directorName)
    {
        if (movieName != null) {
            Optional<Movie> movie = movieService.getMovieByName(movieName);
            return movie.isPresent() ? ResponseEntity.ok(movie) : ResponseEntity.notFound().build();
        } else if (year != null && directorName != null) {
            List<Movie> movies = movieService.getMoviesByYearAndDirector(directorName,year);
            return movies.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(movies);
        } else if (year != null) {
            List<Movie> movies = movieService.getMoviesByYear(year);
            return movies.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(movies);
        } else if (directorName != null) {
            List<Movie> movies = movieService.getMovieByDirector(directorName);
            return movies.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.badRequest().body("Invalid search parameters");
        }
    }

    @PostMapping("/save")
    public Movie save(@RequestBody Movie movie) {
        return movieService.save(movie);
    }

    @PostMapping(value = "/saveAll", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> saveAll(@RequestBody List<Movie> movie) {
        return movieService.saveAll(movie);
    }
}
