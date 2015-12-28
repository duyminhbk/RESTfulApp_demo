package com.app.restfulapp.ultis;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by minhpham on 12/16/15.
 */
public class Utility {
    private static final String PREFS_KEY = "PREFS_KEY";
    private static final String SECURITY_REF = "SECURITY_REF";
    //Email Pattern
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static Pattern pattern;
    private static Matcher matcher;

    /**
     * Validate Email with regular expression
     *
     * @param email
     * @return true for Valid Email and false for Invalid Email
     */
    public static boolean validate(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public static String getString(Context context, String fref) {
        SharedPreferences savedValue = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return savedValue.getString(fref, "");
    }

    public static boolean saveString(Context context, String fref, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(fref, value);
        boolean result = editor.commit();
        return result;
    }

    public static void saveSecurity(Context context, String code) {
        saveString(context, SECURITY_REF, code);
    }
}
