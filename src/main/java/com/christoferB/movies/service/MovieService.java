package com.christoferB.movies.service;

import com.christoferB.movies.models.Movie;
import com.christoferB.movies.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Optional<Movie> voteMovie(Long id, double rating) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (movieOpt.isPresent()) {
            Movie movie = movieOpt.get();
            double newRating = (movie.getVotes() * movie.getRating() + rating) / (movie.getVotes() + 1);
            movie.setVotes(movie.getVotes() + 1);
            movie.setRating(newRating);
            return Optional.ofNullable(movieRepository.save(movie));
        }
        System.out.println(movieOpt.isPresent());
        return Optional.empty();
    }
}
