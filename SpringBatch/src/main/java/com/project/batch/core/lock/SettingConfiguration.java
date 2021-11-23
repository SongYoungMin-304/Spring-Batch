package com.project.batch.core.lock;

import com.project.batch.core.common.Utils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.TimeZone;

import static java.util.Objects.requireNonNull;

public class SettingConfiguration {

    private static final String DEFAULT_TABLE_NAME = "LOCK";

    private String tableName;
    private ColumnNames columnNames;
    private String lockedByValue;

    public ColumnNames getColumnNames() {
        return columnNames;
    }

    public String getTableName() {
        return tableName;
    }


    public static final class ColumnNames {
        private final String name;
        private final String lockUntil;
        private final String lockedAt;
        private final String lockedBy;

        public ColumnNames(String name, String lockUntil, String lockedAt, String lockedBy) {
            this.name = requireNonNull(name, "'name' column name can not be null");
            this.lockUntil = requireNonNull(lockUntil, "'lockUntil' column name can not be null");
            this.lockedAt = requireNonNull(lockedAt, "'lockedAt' column name can not be null");
            this.lockedBy = requireNonNull(lockedBy, "'lockedBy' column name can not be null");
        }

        public String getName() {
            return name;
        }

        public String getLockUntil() {
            return lockUntil;
        }

        public String getLockedAt() {
            return lockedAt;
        }

        public String getLockedBy() {
            return lockedBy;
        }
    }

    public static final class Builder {
        private JdbcTemplate jdbcTemplate;
        private PlatformTransactionManager transactionManager;
        private String tableName = DEFAULT_TABLE_NAME;
        private TimeZone timeZone;
        private String lockedByValue = Utils.getUniqueEngineName();
        private ColumnNames columnNames = new ColumnNames("name", "lock_until", "locked_at", "locked_by");
        private boolean useDbTime = false;

        public Builder withJdbcTemplate(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
            return this;
        }

        public Builder withTransactionManager(PlatformTransactionManager transactionManager) {
            this.transactionManager = transactionManager;
            return this;
        }

        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder withTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public Builder withColumnNames(ColumnNames columnNames) {
            this.columnNames = columnNames;
            return this;
        }
    }
}
