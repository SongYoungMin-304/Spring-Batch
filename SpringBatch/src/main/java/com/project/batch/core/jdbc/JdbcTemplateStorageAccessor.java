package com.project.batch.core.jdbc;

import com.project.batch.core.common.ClockProvider;
import com.project.batch.core.common.Utils;
import com.project.batch.core.lock.LockConfiguration;
import com.project.batch.core.lock.SettingConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Component
public class JdbcTemplateStorageAccessor extends AbstractStorageAccessor {

    //protected SettingConfiguration settingConfiguration = new SettingConfiguration();

    private final NamedParameterJdbcTemplate jdbcTemplate;


    public JdbcTemplateStorageAccessor(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // private ColumnNames columnNames = new ColumnNames("name", "lock_until", "locked_at", "locked_by");

    private final String insertSql = "INSERT INTO S_LOCK (name, lock_until, locked_at, locked_by"
                            + ") VALUES(:name, :lockUntil, :now, :lockedBy)";

    private final String updateSql ="UPDATE S_LOCK SET lock_until  = :lockUntil,  locked_at  = :now,"
                            + "locked_by   = :lockedBy WHERE   name   = :name AND  + lock_until   <= :now";

    private final String getUnlockUpdateSql = "UPDATE S_LOCK SET lock_until = :now WHERE NAME = :name";


    @Override
    public boolean insertRecord(LockConfiguration lockConfiguration) {

        int cnt = 0;

        try {
           cnt = jdbcTemplate.update(insertSql, params(lockConfiguration));
        } catch (DuplicateKeyException e){
            log.info("이미 존재");
            return false;
        } catch (Exception e){
            log.warn("Unexpected excpetion", e);
        }

        if(cnt > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateRecord(LockConfiguration lockConfiguration) throws Exception {

        int cnt = jdbcTemplate.update(updateSql, params(lockConfiguration));

        //Thread.sleep(10000);

        if(cnt > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void unlock(LockConfiguration lockConfiguration) {
        log.info("송영민송송"+getUnlockUpdateSql);
        log.info("송영민송채"+params(lockConfiguration));
        jdbcTemplate.update(getUnlockUpdateSql, params(lockConfiguration));
    }

    @Override
    public boolean extend(LockConfiguration lockConfiguration) {
        return super.extend(lockConfiguration);
    }

    @Override
    public void allUnlockOfThisDaemon() {

    }

    private Map<String, Object> params(LockConfiguration lockConfiguration){

        Map<String, Object> params = new HashMap<>();
        params.put("name", lockConfiguration.getName());

        params.put("lockUntil",  LocalTime.now().plusSeconds(60));
        params.put("now",        LocalTime.now());

        //params.put("lockUntil",  timestamp(lockConfiguration.getLockAtMostUntil()));
        //params.put("now",        timestamp(ClockProvider.now()));
        params.put("lockedBy",   lockConfiguration.getLockedBy());
        params.put("engineName", Utils.getUniqueEngineName() + "%");
        params.put("unlockTime", timestamp(lockConfiguration.getUnlockTime()));
        params.put("alone",      lockConfiguration.getAlone());
        return params;
    }

    private Object timestamp(Instant time) {
        TimeZone timeZone = new TimeZone() {
            @Override
            public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
                return 0;
            }

            @Override
            public void setRawOffset(int offsetMillis) {

            }

            @Override
            public int getRawOffset() {
                return 0;
            }

            @Override
            public boolean useDaylightTime() {
                return false;
            }

            @Override
            public boolean inDaylightTime(Date date) {
                return false;
            }
        };
        if (timeZone == null) {
            return Timestamp.from(time);
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Date.from(time));
            calendar.setTimeZone(timeZone);
            return calendar;
        }
    }

   /* String name() {
        return settingConfiguration.getColumnNames().getName();
    }
    String lockUntil() {
        return settingConfiguration.getColumnNames().getLockUntil();
    }
    String lockedAt() {
        return settingConfiguration.getColumnNames().getLockedAt();
    }
    String lockedBy() {
        return settingConfiguration.getColumnNames().getLockedBy();
    }
    String likeLockedBy() {
        return settingConfiguration.getColumnNames().getLockedBy() + '%';
    }
    String tableName() {
        return settingConfiguration.getTableName();
    }*/
}
