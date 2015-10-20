package com.horn.common.jpa;

import javax.persistence.PreUpdate;

/**
 * @author lesinsa on 08.04.2015.
 */
public class WhenUpdatedListener extends WhenListener {

    @PreUpdate
    public void preUpdate(Object o) {
        processEntity(o, WhenUpdated.class);
    }
}
