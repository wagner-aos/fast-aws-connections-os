package br.com.fast.aws.connection.commons.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import br.com.fast.aws.connection.commons.enumeration.DateFormatEnum;

/**
 * Utility class to format date with UTC
 * 
 * @author Wagner Alves
 */
public class DateFormatter {

    private DateFormatter() {
    }

    /**
     * @param date
     * @return date such as this: 17/08/17 16:30
     */
    public static String format(Date date, DateFormatEnum format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format.getFormato());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * @param date
     * @return date such as this: 17/08/17 16:30
     */
    public static String formatUTC(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * @param date
     * @return date such as this: Quinta-feira, 17 de Agosto de 2017
     */
    public static String formatZoneOffUTC(Date date) {
        DateFormat formatZoneOff = DateFormat.getDateInstance(DateFormat.FULL);
        formatZoneOff.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        return formatZoneOff.format(date);
    }

    /**
     * @param date
     * @return date such as this: 17/08/17
     */
    public static String formatShortUTC(Date date) {
        DateFormat formatShort = DateFormat.getDateInstance(DateFormat.SHORT);
        formatShort.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        return formatShort.format(date);

    }

    /**
     * @param date
     * @return date such as this: 17/08/2017
     */
    public static String formatMediumUTC(Date date) {
        DateFormat formatMedium = DateFormat.getDateInstance(DateFormat.MEDIUM);
        formatMedium.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        return formatMedium.format(date);
    }

    /**
     * @param date
     * @return date such as this: 17 de Agosto de 2017
     */
    public static String formatLongUTC(Date date) {
        DateFormat formatLong = DateFormat.getDateInstance(DateFormat.LONG);
        formatLong.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        return formatLong.format(date);
    }

    /**
     * @param date
     * @return date such as this: Quinta-feira, 17 de Agosto de 2017
     */
    public static String formatFullUTC(Date date) {
        DateFormat formatFull = DateFormat.getDateInstance(DateFormat.FULL);
        formatFull.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        return formatFull.format(date);
    }

}
