package com.spendalyzer.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class SmsUtil {

    public final String FROM;
    public final String MESSAGE;
    public final String DATE;

    public final String TYPE;

    public SmsUtil(String FROM, String MESSAGE, String TYPE, String DATE) {
        this.FROM = FROM;
        this.MESSAGE = MESSAGE;
        this.TYPE = TYPE;
        this.DATE = DATE;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");
        return "FROM='" + FROM + '\'' +
                ", MESSAGE='" + MESSAGE + '\'' +
                ", TYPE='" + TYPE + '\'' +
                ", DATE='" + sdf.format(new Date(Long.valueOf(DATE))) + '\'';
    }
}
