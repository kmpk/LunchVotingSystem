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
            SELECT v.restaurant.id AS restaurantId, count(v) AS votesCount
            FROM Vote v
            WHERE v.date = :date
            GROUP BY v.restaurant.id
            ORDER BY votesCount DESC , restaurantId
                 """)
    List<VoteCountProjection> countVotesByDate(LocalDate date);

    interface VoteCountProjection {
        Integer getRestaurantId();

        Integer getVotesCount();
    }
}
