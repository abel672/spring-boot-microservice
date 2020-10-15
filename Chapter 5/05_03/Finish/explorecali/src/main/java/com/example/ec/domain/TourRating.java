package com.example.ec.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Rating of a Tour by a Customer
 *
 * Created by Mary Ellen Bowman
 */
@Document
public class TourRating {

    @Id
    private String id;

    private String tourId;

    @NotNull
    private Integer customerId;

    @Min(0)
    @Max(5)
    private Integer score;

    @Size(max = 255)
    private String comment;

    /**
     * Construct a new Tour Rating.
     *
     * @param tourId         primiary key of a tour and customer id.
     * @param score      Integer score (1-5)
     * @param comment    Optional comment from the customer
     */
    public TourRating(String tourId, Integer customerId, Integer score, String comment) {
        this.tourId = tourId;
        this.score = score;
        this.comment = comment;
        this.customerId = customerId;
    }

    protected TourRating() {
    }

    @Override
    public String toString() {
        return "TourRating{" +
                "tourId=" + tourId +
                ", score=" + score +
                ", comment='" + comment + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TourRating that = (TourRating) o;
        return Objects.equals(tourId, that.tourId) &&
                Objects.equals(score, that.score) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tourId, score, comment);
    }


    public Integer getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
