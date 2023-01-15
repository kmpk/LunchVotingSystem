package com.github.lunchvotingsystem.service;

import com.github.lunchvotingsystem.exception.IllegalVoteDayException;
import com.github.lunchvotingsystem.exception.VoteChangeAfterDeadlineException;
import com.github.lunchvotingsystem.model.User;
import com.github.lunchvotingsystem.model.Vote;
import com.github.lunchvotingsystem.repository.RestaurantRepository;
import com.github.lunchvotingsystem.repository.UserRepository;
import com.github.lunchvotingsystem.repository.VoteRepository;
import com.github.lunchvotingsystem.to.VoteTo;
import com.github.lunchvotingsystem.util.ValidationUtil;
import com.github.lunchvotingsystem.util.VotesUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Optional;

@Service
public class VoteService {
    public static final LocalTime CHANGE_VOTE_TIME_RESTRICTION = LocalTime.of(11, 0, 0);
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private Clock clock = Clock.systemUTC();

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public Optional<VoteTo> findByDate(int userId, LocalDate date) {
        return voteRepository.findByUserIdAndDate(userId, date).map(VotesUtil::getToFromVote);
    }

    @Transactional
    public void update(int userId, LocalDate date, int restaurantId) {
        checkVoteDay(date);
        ValidationUtil.checkExisted(restaurantRepository.existsById(restaurantId), restaurantId);
        User user = ValidationUtil.checkExisted(userRepository.getReferenceById(userId), userId);
        Optional<Vote> voteOptional = voteRepository.findByUserIdAndDate(userId, date);
        Vote newVote = new Vote(user, restaurantRepository.getReferenceById(restaurantId), date);
        voteOptional.ifPresent(v -> {
            checkVoteUpdateRestriction();
            newVote.setId(v.getId());
        });
        voteRepository.save(newVote);
    }

    private void checkVoteDay(LocalDate date) {
        if (!getCurrentTime().toLocalDate().isEqual(date)) {
            throw new IllegalVoteDayException();
        }
    }

    private void checkVoteUpdateRestriction() {
        if (getCurrentTime().toLocalTime().isAfter(CHANGE_VOTE_TIME_RESTRICTION)) {
            throw new VoteChangeAfterDeadlineException(CHANGE_VOTE_TIME_RESTRICTION);
        }
    }

    private ZonedDateTime getCurrentTime() {
        return clock.instant().atZone(ZoneId.systemDefault());
    }
}
