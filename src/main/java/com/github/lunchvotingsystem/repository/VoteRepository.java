package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Optional<Vote> findByUserIdAndDate(int userId, LocalDate date);

    @Query(value = """
            SELECT RESTAURANT_ID AS restaurantId, COUNT(RESTAURANT_ID) AS count
            FROM RESTAURANT INNER JOIN VOTE V on RESTAURANT.ID = V.RESTAURANT_ID
            WHERE DATE = :date
            GROUP BY RESTAURANT_ID
            ORDER BY count DESC, restaurantId
            """, nativeQuery = true)
    List<VoteCountProjection> countVotesByDate(LocalDate date);

    interface VoteCountProjection {
        Integer getRestaurantId();

        Integer getCount();
    }
}
