package com.project.batch.core.common;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class ClockProvider {
    private static Clock clock = Clock.systemUTC();

    public static void setClock(Clock clock) {
        ClockProvider.clock = clock;
    }

    public static Instant now() {
        return clock.instant().truncatedTo(ChronoUnit.MILLIS);
    }

}
