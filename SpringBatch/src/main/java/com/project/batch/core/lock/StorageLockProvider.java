package com.project.batch.core.lock;

import com.project.batch.core.jdbc.JdbcTemplateStorageAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class StorageLockProvider implements  LockProvider{

    @Autowired
    LockRecordRegistry lockRecordRegistry;

    @Autowired
    JdbcTemplateStorageAccessor jdbcTemplateStorageAccessor;

    @Override
    public boolean lock(LockConfiguration lockConfiguration) throws  Exception {
        log.info("LOCK 처리");
        boolean lockObtained = doLock(lockConfiguration);
        return lockObtained;
    }

    @Override
    public void unlock(LockConfiguration lockConfiguration) {
        log.info("UNLOCK 처리");
        doUnLock(lockConfiguration);
    }


    protected boolean doLock(LockConfiguration lockConfiguration) throws  Exception{
        String name = lockConfiguration.getName();

        if (!lockRecordRegistry.lockRecordRecentlyCreated(name)) {
            // create record in case it does not exist yet
            if (jdbcTemplateStorageAccessor.insertRecord(lockConfiguration)) {
                lockRecordRegistry.addLockRecord(name);
                // we were able to create the record, we have the lock
                return true;
            }
            // we were not able to create the record, it already exists, let's put it to the cache so we do not try again
            lockRecordRegistry.addLockRecord(name);
        }
        // let's try to update the record, if successful, we have the lock
        return lockRecordRegistry.updateRecord(lockConfiguration);
    }

    protected void doUnLock(LockConfiguration lockConfiguration){
        this.lockRecordRegistry.unlock(lockConfiguration);
    }

}
