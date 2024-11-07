package com.mai.db_cw_rbl;

import com.mai.db_cw_rbl.InfrastructurePackage.CommonPackage.CustomExceptions.EntityNotFoundException;
import com.mai.db_cw_rbl.RatingPackage.Dao.LawyerRatingDao;
import com.mai.db_cw_rbl.RatingPackage.Dto.LawyerRatingResponse;
import com.mai.db_cw_rbl.RatingPackage.LawyerRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LawyerRatingServiceTest {

    @Mock
    private LawyerRatingDao lawyerRatingDao;

    @InjectMocks
    private LawyerRatingService lawyerRatingService;

    private UUID lawyerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lawyerId = UUID.randomUUID();
    }

    @Test
    void calculateAverageRating_ShouldReturnCorrectRating_WhenRatingsExist() {
        // Arrange
        List<Double> ratings = List.of(4.0, 5.0, 3.5);
        when(lawyerRatingDao.findAllRatingsByLawyerId(lawyerId)).thenReturn(ratings);

        // Act
        LawyerRatingResponse response = lawyerRatingService.calculateAverageRating(lawyerId);

        // Assert
        assertEquals(lawyerId, response.lawyerId());
        assertEquals(3, response.answerCount());
        assertEquals(ratings.stream().reduce(0.0, Double::sum) / 3, response.rating());
    }

    @Test
    void calculateAverageRating_ShouldReturnZeroRating_WhenRatingsAreAllZero() {
        // Arrange
        List<Double> ratings = List.of(0.0, 0.0, 0.0);
        when(lawyerRatingDao.findAllRatingsByLawyerId(lawyerId)).thenReturn(ratings);

        // Act
        LawyerRatingResponse response = lawyerRatingService.calculateAverageRating(lawyerId);

        // Assert
        assertEquals(lawyerId, response.lawyerId());
        assertEquals(3, response.answerCount());
        assertEquals(0.0, response.rating());
    }
}
