package ru.prbb.common.jpa;

import javax.persistence.PrePersist;

/**
 * @author lesinsa on 08.04.2015.
 */
public class WhenCreatedListener extends WhenListener {

    @PrePersist
    public void prePersist(Object o) {
        processEntity(o, WhenCreated.class);
    }
}
