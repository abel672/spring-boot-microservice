package com.example.ec.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * The Tour contains all attributes of an Explore California Tour.
 *
 * Created by Mary Ellen Bowman
 */
@Document
public class Tour implements Serializable{
    @Id
    private String id;

    @Indexed
    private String title;

    @Indexed
    private String tourPackageCode;

    private Map<String, String> details;

    private String tourPackageName;


    public Tour(String title, Map<String, String> details, TourPackage tourPackage) {
        this.title = title;
        this.details = details;
        this.tourPackageName = tourPackage.getName();
        this.tourPackageCode = tourPackage.getCode();
    }

    protected Tour() {
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tour{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", tourPackageCode='").append(tourPackageCode).append('\'');
        sb.append(", details=").append(details);
        sb.append(", tourPackageName='").append(tourPackageName).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return Objects.equals(id, tour.id) &&
                Objects.equals(title, tour.title) &&
                Objects.equals(tourPackageCode, tour.tourPackageCode) &&
                Objects.equals(details, tour.details) &&
                Objects.equals(tourPackageName, tour.tourPackageName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
