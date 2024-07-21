package com.devsuperior.dsmovie.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;
	
	@Mock
	private UserService userService;
	
	@Mock
	private MovieService movieService;
	
	@Mock
	private ScoreRepository repository;
	
	@Mock
	private MovieRepository movieRepository;
	
	private Long existingMovieId, nonExistingMovieId;
	private ScoreEntity score;
	private MovieEntity movie;
	private ScoreDTO scoreDto;
	private UserEntity user;
	
	@BeforeEach
	public void setUp() {
		
		existingMovieId = 1L;
		nonExistingMovieId = 20L;
		movie = MovieFactory.createMovieEntity();
		score = ScoreFactory.createScoreEntity();
		movie.getScores().add(score);
		
		user = UserFactory.createUserEntity();
		scoreDto = ScoreFactory.createScoreDTO();
		
		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.saveAndFlush(score)).thenReturn(score);
		Mockito.when(movieRepository.save(movie)).thenReturn(movie);
		Mockito.when(userService.authenticated()).thenReturn(user);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result = service.saveScore(scoreDto);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		movie.setId(nonExistingMovieId);
		score.setMovie(movie);
		scoreDto = new ScoreDTO(score);
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			MovieDTO result = service.saveScore(scoreDto);
		});
	}
}
