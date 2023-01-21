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
            SELECT restaurant_id AS restaurantId, COUNT(restaurant_id) AS count
            FROM restaurant INNER JOIN vote v on restaurant.id = v.restaurant_id
            WHERE date = :date
            GROUP BY restaurant_id
            ORDER BY count DESC, restaurantId
            """, nativeQuery = true)
    List<VoteCountProjection> countVotesByDate(LocalDate date);

    interface VoteCountProjection {
        Integer getRestaurantId();

        Integer getCount();
    }
}
