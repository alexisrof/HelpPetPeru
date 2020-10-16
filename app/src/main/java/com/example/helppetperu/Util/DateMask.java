package com.example.helppetperu.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateMask implements TextWatcher {
    private final static String TAG = DateMask.class.getSimpleName();
    private String currentString = "";
    private String separator = "/";
    private DateMaskingCallback dateMaskingCallback;
    private static final String INVALID_DATE_MSG = "Enter valid Date";
    private static final String INVALID_MONTH_MSG = "Enter valid Month";
    private String errorString;

    public DateMask(DateMaskingCallback dateMaskingCallback, String separator) {
        this.dateMaskingCallback = dateMaskingCallback;
        this.separator = separator;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        errorString = "";
        try {
            String dateString = s.toString();
            Log.e(TAG, "date current string "+currentString);
            if (!currentString.equalsIgnoreCase(dateString)) {
                currentString = dateString;
                if (dateString.length() == 2 && before == 0) {
                    String day = dateString.substring(0,2);
                    dateString = dateString+separator;
                    if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31) {
                        errorString = "Ingresa un día correcto";
                    }else{
                        errorString = "";
                    }
                } else if (dateString.length() == 5 && before == 0) {
                    String month = dateString.substring(3, 5);
                    dateString = dateString+separator;
                    if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
                        errorString = "Ingresa un mes correcto";
                    }else{
                        errorString = "";
                    }
                } else if (dateString.length() == 10 && before == 0) {
                    String yearString = dateString.substring(6,10);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Etc/GMT-0"));
                    Date date1 = sdf.parse(dateString+" 00:00:00");
                    Date date2 = Calendar.getInstance().getTime();
                    String strDate2 = sdf.format(date2);
                    date2 = sdf.parse(strDate2.substring(0,10)+" 00:00:00");

                    Boolean v;

                    if (yearString.startsWith("0")) {
                        errorString = "Ingresa un año correcto";
                    }else if (date1.after(date2)) {
                        errorString = "Ingresa una fecha de nacimiento correcta";
                    }else{
                        errorString = "";
                    }
                }else if (dateString.length()>10) {
                    dateString = dateString.substring(0,10);
                }

                Log.e(TAG, "dateString "+dateString +" error String "+errorString);
                if (errorString.isEmpty()) {
                    dateMaskingCallback.dateOfBirthValidation(true, dateString, errorString);
                } else {
                    dateMaskingCallback.dateOfBirthValidation(false, dateString, errorString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface DateMaskingCallback {
        void dateOfBirthValidation(final boolean isValid, final String dateOfBirth, final String error) throws Exception;
    }
}
