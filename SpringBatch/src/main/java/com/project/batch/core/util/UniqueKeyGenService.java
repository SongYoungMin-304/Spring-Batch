package com.project.batch.core.util;

public class UniqueKeyGenService {

    protected static char[]	BASE_32_ARRAY	= {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    /** Creates a new instance of UniqueKey */
    public UniqueKeyGenService() {
    }

    public static long		LAST_TIME	= 0;

    private static char[]	returnValue	= new char[7];

    public synchronized static final String get() {
        long base = System.currentTimeMillis();

        base = base <= LAST_TIME ? LAST_TIME + 1 : base;

        LAST_TIME = base;

        int idx = 0;
        while (base > 36) {
            long slack = base % 36;
            returnValue[idx++] = BASE_32_ARRAY[(int) slack];
            base = base / 36;
        }

        return new String(returnValue);
    }

    public synchronized static final String getSerial() {
        long base = System.currentTimeMillis();
        base = base <= LAST_TIME ? LAST_TIME + 1 : base;
        LAST_TIME = base;
        return String.valueOf(base % 86400000);
    }

    public synchronized static final String getSerial(String serverId) {
        return getSerial() + "^" + serverId;
    }
}
