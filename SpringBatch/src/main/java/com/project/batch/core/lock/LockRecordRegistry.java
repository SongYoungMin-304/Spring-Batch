package com.project.batch.core.lock;

import com.project.batch.core.jdbc.JdbcTemplateStorageAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Component
@Slf4j
public class LockRecordRegistry {

    @Autowired
    JdbcTemplateStorageAccessor jdbcTemplateStorageAccessor;

    private final Set<String> lockRecords = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    public synchronized boolean lockRecordRecentlyCreated(String lockName) {
        return lockRecords.contains(lockName);
    }

    public synchronized  void addLockRecord(String lockName){
        lockRecords.add(lockName);
    }

    public synchronized boolean updateRecord(LockConfiguration lockConfiguration) throws Exception{
        return jdbcTemplateStorageAccessor.updateRecord(lockConfiguration);
    }

    public void unlock(LockConfiguration lockConfiguration){
        try{
            lockRecords.remove(lockConfiguration.getName());
            jdbcTemplateStorageAccessor.unlock(lockConfiguration);
        }catch (Exception e){
            log.error("unLock Error {}", e.toString());
        }
    }



}
