package com.ahmeterdogan.data.service;

import com.ahmeterdogan.data.entity.Movie;
import com.ahmeterdogan.data.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public int deleteById(long id) {
        return movieRepository.deleteById(id);
    }

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(long id) {
        return movieRepository.findMovieById(id);
    }

    public Optional<Movie> getMovieByName(String name) {
        return movieRepository.findMovieByName(name);
    }

    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> saveAll(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    public List<Movie> getMovieByDirector(String director) {
        return movieRepository.findMovieByDirector(director);
    }

    public List<Movie> getMoviesByYear(int year) {
        return movieRepository.findMoviesByYear(year);
    }

    public List<Movie> getMoviesByYearAndDirector(String directorName, Integer year) {
        return movieRepository.findMoviesByYearAndDirector(directorName, year);
    }
}
