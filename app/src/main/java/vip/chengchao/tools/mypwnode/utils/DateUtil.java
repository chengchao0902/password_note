package vip.chengchao.tools.mypwnode.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chengchao on 16/7/1.
 */
public final class DateUtil {

    public static String formatDate(Date date) {
        return getDefaultFormat().format(date);
    }

    public static Date parseDate(String sDate) {
        try {
            return getDefaultFormat().parse(sDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static SimpleDateFormat getDefaultFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
