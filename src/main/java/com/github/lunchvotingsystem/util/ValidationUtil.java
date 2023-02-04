package com.github.lunchvotingsystem.util;

import com.github.lunchvotingsystem.HasId;
import com.github.lunchvotingsystem.exception.IllegalRequestDataException;
import com.github.lunchvotingsystem.exception.NotFoundException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Optional;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static <T> T checkExistedStrict(T obj, int id) {
        if (obj == null) {
            throw new IllegalRequestDataException(getMessage(id));
        }
        return obj;
    }

    public static void checkExistedStrict(boolean exists, int id) {
        if (!exists) {
            throw new IllegalRequestDataException(getMessage(id));
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new NotFoundException(getMessage(id));
        }
    }

    public static void checkModification(int count, LocalDate date) {
        if (count == 0) {
            throw new NotFoundException(getMessage(date));
        }
    }

    public static <T> T checkExisted(T obj, int id) {
        if (obj == null) {
            throw new NotFoundException(getMessage(id));
        }
        return obj;
    }

    public static <T> T checkExisted(Optional<T> obj, int id) {
        if (obj.isEmpty()) {
            throw new NotFoundException(getMessage(id));
        }
        return obj.get();
    }

    public static void checkExisted(boolean exists, int id) {
        if (!exists) {
            throw new NotFoundException(getMessage(id));
        }
    }

    private static String getMessage(int id) {
        return "Entity with id= " + id + " not found";
    }

    private static String getMessage(LocalDate date) {
        return "Entity with date= " + date + " not found";
    }
}