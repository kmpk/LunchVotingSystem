package com.github.lunchvotingsystem.util;

import com.github.lunchvotingsystem.HasId;
import com.github.lunchvotingsystem.HasLocalDate;
import com.github.lunchvotingsystem.exception.IllegalRequestDataException;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;

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

    public static void assureDateConsistent(HasLocalDate bean, LocalDate date) {
        if (!bean.isDateSet()) {
            bean.setDate(date);
        } else if (!bean.getDate().equals(date)) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has date=" + date);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    public static <T> T checkExisted(T obj, int id) {
        if (obj == null) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
        return obj;
    }

    public static void checkExisted(boolean exists, int id) {
        if (!exists) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }
}