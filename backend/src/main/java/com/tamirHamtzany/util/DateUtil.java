package com.tamirHamtzany.util;

import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    }
}
