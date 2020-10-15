package com.example.ec.web;

import com.example.ec.domain.*;
import com.example.ec.repo.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Tour Rating Controller
 *
 * Created by Mary Ellen Bowman
 */
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {
    TourRatingRepository tourRatingRepository;
    TourRepository tourRepository;

    @Autowired
    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    protected TourRatingController() {

    }

    /**
     * Create a Tour Rating.
     *
     * @param tourId
     * @param tourRating data transfer object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") String tourId, @RequestBody @Validated TourRating tourRating) {
        verifyTour(tourId);
        tourRatingRepository.save(new TourRating(tourId, tourRating.getCustomerId(),
                tourRating.getScore(), tourRating.getComment()));
    }

    /**
     * Lookup a the Ratings for a tour.
     *
     * @param tourId
     * @param pageable
     * @return
     */
    @GetMapping
    public Page<TourRating> getAllRatingsForTour(@PathVariable(value = "tourId") String tourId, Pageable pageable) {
        return tourRatingRepository.findByTourId(tourId, pageable);
    }

    /**
     * Calculate the average Score of a Tour.
     *
     * @param tourId
     * @return Tuple of "average" and the average value.
     */
    @GetMapping(path = "/average")
    public Map<String, Double> getAverage(@PathVariable(value = "tourId") String  tourId) {
        verifyTour(tourId);
        return Map.of("average", tourRatingRepository.findByPkTourId(tourId).stream()
                .mapToInt(TourRating::getScore).average()
                .orElseThrow(() ->
                        new NoSuchElementException("Tour has not Ratings")));
    }

    /**
     * Update score and comment of a Tour Rating
     *
     * @param tourId
     * @param tourRating
     * @return The modified Rating DTO.
     */
    @PutMapping
    public TourRating updateWithPut(@PathVariable(value = "tourId") String tourId, @RequestBody @Validated TourRating tourRating) {
        TourRating rating = verifyTourRating(tourId, tourRating.getCustomerId());
        rating.setScore(tourRating.getScore());
        rating.setComment(tourRating.getComment());
        return tourRatingRepository.save(rating);
    }
    /**
     * Update score or comment of a Tour Rating
     *
     * @param tourId
     * @param tourRating
     * @return The modified Rating DTO.
     */
    @PatchMapping
    public TourRating updateWithPatch(@PathVariable(value = "tourId") String tourId, @RequestBody @Validated TourRating tourRating) {
        TourRating rating = verifyTourRating(tourId, tourRating.getCustomerId());
        if (tourRating.getScore() != null) {
            rating.setScore(tourRating.getScore());
        }
        if (tourRating.getComment() != null) {
            rating.setComment(tourRating.getComment());
        }
        return tourRatingRepository.save(rating);
    }

    /**
     * Delete a Rating of a tour made by a customer
     *
     * @param tourId
     * @param customerId
     */
    @DeleteMapping(path = "/{customerId}")
    public void delete(@PathVariable(value = "tourId") String tourId, @PathVariable(value = "customerId") int customerId) {
        TourRating rating = verifyTourRating(tourId, customerId);
        tourRatingRepository.delete(rating);
    }

    /**
     * Verify and return the TourRating for a particular tourId and Customer
     * @param tourId
     * @param customerId
     * @return the found TourRating
     * @throws NoSuchElementException if no TourRating found
     */
    private TourRating verifyTourRating(String tourId, int customerId) throws NoSuchElementException {
        TourRating rating = tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId);
        if (rating == null) {
            throw new NoSuchElementException("Tour-Rating pair for request("
                    + tourId + " for customer" + customerId);
        }
        return rating;
    }

    /**
     * Verify and return the Tour given a tourId.
     *
     * @param tourId
     * @return the found Tour
     * @throws NoSuchElementException if no Tour found.
     */
    private Tour verifyTour(String tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow(()->
                new NoSuchElementException("Tour does not exist " + tourId));
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     *
     * @param ex
     * @return Error message String.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();

    }

}
