package com.example.ec.repo;

import com.example.ec.domain.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Tour Repository Interface
 *
 * Created by Mary Ellen Bowman
 */
public interface TourRepository extends PagingAndSortingRepository<Tour,String > {
    /**
     * Only return the main fields of a Tour, not the details
     *
     * @param code
     * @param pageable
     * @return tours without details
     */
    @Query(value = "{'tourPackCode' : ?0}",
            fields = "{ 'id':1, 'title':1, 'tourPackageCode':1, 'tourPackageName':1}")
    Page<Tour> findByTourPackageCode(@Param("code") String code, Pageable pageable);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    <S extends Tour> S save(S s);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    <S extends Tour> Iterable<S> saveAll(Iterable<S> iterable);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void deleteById(String string);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void delete(Tour tour);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void deleteAll(Iterable<? extends Tour> iterable);

    //Not exposed by Spring Data REST
    @Override
    @RestResource(exported = false)
    void deleteAll();
}
