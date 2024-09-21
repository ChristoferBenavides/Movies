package com.christoferB.movies.service;

import com.christoferB.movies.models.Movie;
import com.christoferB.movies.repositories.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLOutput;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {


    @InjectMocks
    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;


    private Movie movie;

    @BeforeEach

    void setUp() {
        movie = new Movie(1L, "Test Movie", "Description", 2021, 10, 4.5, "url");
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Description");
        movie.setYear(2021);
        movie.setVotes(10);
        movie.setRating(4.5);
        movie.setImageUrl("url");

    }


    @Test
    void testVoteMovie() {

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);

        Optional<Movie> updatedMovieOptional = movieService.voteMovie(1L, 5.0);
        assertTrue(updatedMovieOptional.isPresent());
        Movie movieUpdated = updatedMovieOptional.get();

        assertEquals(4.55, movieUpdated.getRating(), 0.01);
        assertEquals(11, movieUpdated.getVotes());

        verify(movieRepository,times(1)).findById(1L);
        verify(movieRepository,times(1)).save(movie);

    }


}