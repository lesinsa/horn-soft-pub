package ru.prbb.common.lock;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author lesinsa
 */
@Entity
@Table(name = "global_lock")
@SuppressWarnings("JpaDataSourceORMInspection")
public class GlobalLock {
    @Id
    @Column(name = "id")
    private String name;
    @NotNull
    @Column(name = "locked")
    private Integer locked;
    @Version
    @Column(name = "row_version")
    private int version;

    public GlobalLock() {
        //for JPA
    }

    public GlobalLock(String name, Integer locked) {
        this.name = name;
        this.locked = locked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int row_version) {
        this.version = row_version;
    }
}
