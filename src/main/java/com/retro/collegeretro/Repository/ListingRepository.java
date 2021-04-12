package com.retro.collegeretro.Repository;

import com.retro.collegeretro.Model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    Page<Listing> findAll(Pageable pageable);
}
