package com.christoferB.movies.controllers;

import com.christoferB.movies.models.Movie;
import com.christoferB.movies.repositories.MovieRepository;
import com.christoferB.movies.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/movies")
public class MovieController {


    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;
    @CrossOrigin
    @GetMapping
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }
    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id){
        Optional<Movie> movie = movieRepository.findById(id);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody  Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }
    @CrossOrigin
    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteMovie(@PathVariable Long id){
        if(!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        else{
            movieRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id,@RequestBody Movie updatedMovie){
        if(!movieRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }else{
            updatedMovie.setId(id);
            Movie savedMovie= movieRepository.save(updatedMovie);
            return ResponseEntity.ok(savedMovie);
        }
    }
    @CrossOrigin
    @GetMapping("/vote/{id}/{rating}")
    public ResponseEntity<Movie> voteMovie(@PathVariable Long id, @PathVariable double rating) {
        Optional<Movie> movie = movieService.voteMovie(id, rating);
        return movie.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}

