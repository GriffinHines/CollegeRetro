package com.retro.collegeretro.Repository;

import com.retro.collegeretro.Model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Long> {
    Page<Listing> findAll(Pageable pageable);
    Page<Listing> findAllByCategory(Pageable pageable, String category);
    Page<Listing> findAllByListingNameLike(Pageable pageable, String query);

    // IDEA says it cannot resolve `IsOpen` but I tried just `Open` and that didn't work
    Page<Listing> findAllByCategoryAndIsOpenTrue(Pageable pageable, String category);
    Page<Listing> findAllByListingNameLikeAndIsOpenTrue(Pageable pageable, String query);
}
