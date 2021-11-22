package com.project.batch.core.lock;

import java.util.Optional;

public interface LockProvider {

    Optional<SimpleLock> lock(LockConfiguration lockConfiguration);

}
