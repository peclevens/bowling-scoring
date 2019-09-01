package com.clivenspetit.bowlingscoring.domain.game.validator;

import java.util.List;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public interface Validator<T> {

    boolean isValid(T t);

    boolean isValid(List<T> t);

    void validate(T t);

    void validate(List<T> t);
}
