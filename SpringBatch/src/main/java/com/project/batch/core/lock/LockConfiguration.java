package com.project.batch.core.lock;

import com.project.batch.core.common.Utils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class LockConfiguration {

    private final Instant createdAt;

    private final String name;

    private final Duration lockAtMostFor;

    private final Duration lockAtLeastFor;

    private final String who;

    private final String alone;

    public LockConfiguration(Instant createdAt, String name, Duration lockAtMostFor, Duration lockAtLeastFor, String who, String alone) {
        this.createdAt = createdAt;
        this.name = name;
        this.lockAtMostFor = lockAtMostFor;
        this.lockAtLeastFor = lockAtLeastFor;
        this.who = who;
        this.alone = alone;
    }

    public LockConfiguration(String name,
                             Instant createdAt,
                             Duration lockAtLeastFor,
                             Duration lockAtMostFor,
                             String who,
                             String alone){

        this.who 	= Utils.getUniqueEngineName() + ((StringUtils.isEmpty(who)) ? "": who) +"."+name ;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.name = Objects.requireNonNull(name);
        this.lockAtLeastFor = Objects.requireNonNull(lockAtLeastFor);  //최소 잠금
        this.lockAtMostFor  = Objects.requireNonNull(lockAtMostFor);   //최대 잠금
        if (lockAtLeastFor.compareTo(lockAtMostFor) > 0) {
            throw new IllegalArgumentException("lockAtLeastFor is longer than lockAtMostFor for lock '" + name + "'.");
        }
        if (lockAtMostFor.isNegative()) {
            throw new IllegalArgumentException("lockAtMostFor is negative '" + name + "'.");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("lock name can not be empty");
        }
        this.alone = alone;
    }

    public String getAlone(){ return alone; }

    public String getLockedBy(){
        return who;
    }


}
