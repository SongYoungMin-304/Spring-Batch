package com.project.batch.core.lock;

import java.util.Optional;

public interface LockProvider {

    boolean lock(LockConfiguration lockConfiguration) throws Exception;

    void unlock(LockConfiguration lockConfiguration);

}
