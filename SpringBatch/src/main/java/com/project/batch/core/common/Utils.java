package com.project.batch.core.common;

import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Utils {

    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendInstant(3)
            .toFormatter();
    private static final String PERIOD = ":";

    private static final String hostName = initHostName();

    private static final String engineName = initEngineName();

    private Utils() {}

//	public static String getHostname() {
//		return hostName;
//	}

    public static String getPeriod(){
        return PERIOD;
    }

    public static String getUniqueEngineName(String onlyEngineName) {
        if(ObjectUtils.isEmpty(onlyEngineName)) return engineName;

        else return hostName + onlyEngineName;
    }

    public static String getUniqueEngineName() {
        return engineName;
    }

    public static String toIsoString(Instant instant) {
        OffsetDateTime utc = instant.atOffset(ZoneOffset.UTC);
        return formatter.format(utc);
    }

    private static String initHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    private static String initEngineName() {
//        return hostName +"."+ SystemCheckTask.serverDaemonName() + PERIOD;
        return hostName + PERIOD;
        //return "";
    }
}
