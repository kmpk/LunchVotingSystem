package com.github.lunchvotingsystem.web.user;

import com.github.lunchvotingsystem.exception.ModificationRestrictionException;
import com.github.lunchvotingsystem.model.User;
import com.github.lunchvotingsystem.repository.UserRepository;
import com.github.lunchvotingsystem.util.UsersUtil;
import com.github.lunchvotingsystem.util.validation.UniqueMailValidator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class AbstractUserController {
    protected boolean modificationRestriction;

    @Autowired
    @SuppressWarnings("deprecation")
    public void setModificationRestrictionFlag(Environment environment) {
        this.modificationRestriction = environment.acceptsProfiles("VDS");
    }

    protected final Logger log = getLogger(getClass());

    @Autowired
    protected UserRepository repository;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public ResponseEntity<User> get(int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    public void delete(int id) {
        log.info("delete {}", id);
        checkModificationRestriction(id);
        repository.deleteExisted(id);
    }

    protected User prepareAndSave(User user) {
        return repository.save(UsersUtil.prepareToSave(user));
    }

    protected void checkModificationRestriction(int userId) {
        if (modificationRestriction && (userId == 1 || userId == 2)) {
            throw new ModificationRestrictionException(userId);
        }
    }
}