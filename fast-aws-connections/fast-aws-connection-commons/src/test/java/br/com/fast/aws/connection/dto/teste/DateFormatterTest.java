package br.com.fast.aws.connection.dto.teste;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.fast.aws.connection.commons.enumeration.DateFormatEnum;
import br.com.fast.aws.connection.commons.utils.DateFormatter;

public class DateFormatterTest {

    private Date date;

    @Before
    public void setUp() {
        date = new Date(1502998339498L);
        System.out.println(date.getTime());
    }

    @Test
    public void format() {
        String expected = "2017-08-17 19:32:19.498";
        String format = DateFormatter.format(date, DateFormatEnum.YYYY_MM_DD_HH_MM_SS_SSS);
        System.out.println(format);
        Assert.assertEquals(expected, format);
    }

    @Test
    public void formatUTC() {
        String formato = "dd/MM/YYYY HH:mm:ss:SSS";
        String expected = "17/08/2017 19:32:19:498";
        String format = DateFormatter.formatUTC(date, formato);
        System.out.println(format);
        Assert.assertEquals(expected, format);
    }

    @Test
    public void formatZoneOffUTC() {
        String expected = "Quinta-feira, 17 de Agosto de 2017";
        String formatZoneOff = DateFormatter.formatZoneOffUTC(date);
        System.out.println(formatZoneOff);
        Assert.assertEquals(expected, formatZoneOff);
    }

    @Test
    public void formatShortUTC() {
        String expected = "17/08/17";
        String formatShort = DateFormatter.formatShortUTC(date);
        System.out.println(formatShort);
        Assert.assertEquals(expected, formatShort);
    }

    @Test
    public void formatMediumUTC() {
        String expected = "17/08/2017";
        String formatMedium = DateFormatter.formatMediumUTC(date);
        System.out.println(formatMedium);
        Assert.assertEquals(expected, formatMedium);
    }

    @Test
    public void formatLongUTC() {
        String expected = "17 de Agosto de 2017";
        String formatLong = DateFormatter.formatLongUTC(date);
        System.out.println(formatLong);
        Assert.assertEquals(expected, formatLong);
    }

    @Test
    public void formatFullUTC() {
        String expected = "Quinta-feira, 17 de Agosto de 2017";
        String formatFull = DateFormatter.formatFullUTC(date);
        System.out.println(formatFull);
        Assert.assertEquals(expected, formatFull);
    }

}
