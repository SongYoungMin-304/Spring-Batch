package com.project.batch.core.lock;

import java.util.Optional;

public class StorageLockProvider implements  LockProvider{
    @Override
    public Optional<SimpleLock> lock(LockConfiguration lockConfiguration) {
        return Optional.empty();
    }
}
