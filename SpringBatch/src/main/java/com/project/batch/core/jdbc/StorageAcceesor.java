package com.project.batch.core.jdbc;

import com.project.batch.core.lock.LockConfiguration;

public interface StorageAcceesor {
    /**
     * Inserts a record, if it does not already exists. If it exists, returns false.
     *
     * @param lockConfiguration LockConfiguration
     * @return true if inserted
     */
    boolean insertRecord(LockConfiguration lockConfiguration);

    /**
     * Tries to update the lock record. If there is already a valid lock record (the lock is held by someone else)
     * update should not do anything and this method returns false.
     *
     * @param lockConfiguration LockConfiguration
     * @return true if updated
     */
    boolean updateRecord(LockConfiguration lockConfiguration) throws Exception;

    void unlock(LockConfiguration lockConfiguration);

    default boolean extend(LockConfiguration lockConfiguration) {
        throw new UnsupportedOperationException();
    }

    void allUnlockOfThisDaemon();
}
