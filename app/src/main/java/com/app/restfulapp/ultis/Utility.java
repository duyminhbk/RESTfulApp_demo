package com.app.restfulapp.ultis;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;

import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.reports.FrgSLGDReport;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences savedValue = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return savedValue.getBoolean(key, false);
    }

    public static void saveBool(Context context, String key, boolean val) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, val);
        editor.commit();
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

    public static int getMaxScreen(Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return Math.max(height, width);
    }

    public static String convertFullDate(Date date){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return dateFormatter.format(date);

    }

    public static String convertDate(Date date){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormatter.format(date);
    }

    public static String convertSimpleDate(String fulldate){
        return fulldate.substring(0, fulldate.indexOf('T') > 0 ? fulldate.indexOf('T') : fulldate.length());
    }

    public static String getMonthYear(String fulldate) {

        String[] parts = fulldate.split("-");

        if(parts.length >= 2)
            return parts[0] + "-" + parts[1];

        return fulldate;
    }

    public static String getYear(String fulldate) {
        return fulldate.substring(0, fulldate.indexOf('-') > 0 ? fulldate.indexOf('-') : fulldate.length());
    }

    public static ArrayList<Customer> genCustType(){
        ArrayList<Customer> result = new ArrayList<>();

        result.add(new Customer("All",""));
        result.add(new Customer("Area","1"));
        result.add(new Customer("Area","1"));
        result.add(new Customer("Area","2"));
        result.add(new Customer("Area","3"));
        result.add(new Customer("Area","4"));
        result.add(new Customer("Area","5"));
        result.add(new Customer("Area","6"));
        result.add(new Customer("Area","7"));
        result.add(new Customer("Area","8"));
        result.add(new Customer("Area","9"));
        result.add(new Customer("Area","10"));
        result.add(new Customer("Area","11"));
        result.add(new Customer("Area","12"));
        result.add(new Customer("Area","13"));
        result.add(new Customer("Area","14"));
        result.add(new Customer("Area","15"));
        result.add(new Customer("Area","16"));
        result.add(new Customer("Area","17"));
        return result;
    }

    //part_kind: A (Hỗn hợp), B (Đậm đặc)
    public static ArrayList<Member> genPartKind(){
        ArrayList<Member> result = new ArrayList<>();
        result.add(new Member("Hỗn hợp & Đậm đặc",""));
        result.add(new Member("Hỗn hợp","A"));
        result.add(new Member("Đậm đặc","B"));
        return result;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static List<Member> genPeriodTypes() {
        ArrayList<Member> result = new ArrayList<>();
        for(int i=0;i<FrgSLGDReport.PeriodType.values().length;i++){
            result.add(new Member(FrgSLGDReport.PeriodType.values()[i].name(),""+i));
        }
        return result;
    }

    public static Point getScreenSize(Activity context){
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return new Point(metrics.widthPixels,metrics.heightPixels);
    }
    // hide keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Date convertStringToDate(String sDate){
        String dateString = "03/26/2012 11:49:00 AM";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       return convertedDate;
    }

    public static boolean isGreater(String sDateRight,String sDateLeft){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = formatter.parse(sDateRight);
            Date date2 = formatter.parse(sDateLeft);

            if (date1.compareTo(date2)<0)
            {
                return false;
            }

        }catch (ParseException e1){
            e1.printStackTrace();
        }
        return true;
    }

    public static String getDeviceId(Context context) {
        String deviceIdStr = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (deviceIdStr == null) {
            long rand = Double.doubleToRawLongBits(Math.random() * Long.MAX_VALUE);
            Log.w("deviceID", "No device ID found - created random ID " + rand);
            return rand+"";
        } else {
            return deviceIdStr;
        }
    }

    public static void showDatePickerParts(DatePickerDialog datepicker) {
        showDatePickerParts(datepicker, true, true, true);
    }

    public static void showDatePickerParts(DatePickerDialog datepicker, boolean visibleDay) {
        showDatePickerParts(datepicker, visibleDay, true, true);
    }

    public static void showDatePickerParts(DatePickerDialog datepicker, boolean visibleDay, boolean visibleMonth) {
        showDatePickerParts(datepicker, visibleDay, visibleMonth, true);
    }

    public static void showDatePickerParts(DatePickerDialog datepicker, boolean visibleDay, boolean visibleMonth, boolean visibleYear) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            findAndHideField(datepicker, "mDayPicker", visibleDay);
            findAndHideField(datepicker, "mMonthPicker", visibleMonth);
            findAndHideField(datepicker, "mYearPicker", visibleYear);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            findAndHideField(datepicker, "mDaySpinner", visibleDay);
            findAndHideField(datepicker, "mMonthSpinner", visibleMonth);
            findAndHideField(datepicker, "mYearSpinner", visibleYear);
        } else {
            final Object mDatePickerDelegate = findFieldInstance((DatePicker) findField(datepicker, "mDatePicker"), "mDelegate");
            findAndHideField(mDatePickerDelegate, "mHeaderMonthDay", visibleDay | visibleMonth);
            findAndHideField(mDatePickerDelegate, "mYearPickerView", visibleYear);
            findField(mDatePickerDelegate, "mYearPickerView").setBackgroundColor(Color.parseColor(visibleYear ? "#ffffff" : "#00ffffff"));
        }
    }

    public static void showYearPicker(DatePickerDialog datepicker, boolean flag){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            findAndHideField(datepicker, "mDayPicker", flag);
            findAndHideField(datepicker, "mMonthPicker", flag);
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            findAndHideField(datepicker, "mDaySpinner", flag);
            findAndHideField(datepicker, "mMonthSpinner", flag);
        }else{
            final Object mDatePickerDelegate = findFieldInstance((DatePicker) findField(datepicker, "mDatePicker"), "mDelegate");
//            findAndHideField(mDatePickerDelegate, "mDaySpinner",flag);
            findAndHideField(mDatePickerDelegate, "mHeaderMonthDay", flag);
            findAndHideField(mDatePickerDelegate, "mYearPickerView", !flag);
            findField(mDatePickerDelegate, "mYearPickerView").setBackgroundColor(Color.parseColor(flag?"#ffffff":"#00ffffff"));
        }

    }

    /** find a member field by given name and hide it */
    private static boolean findAndHideField(Object object, String name, boolean visible) {
        try {
            // Find DatePicker (mDatePicker) control first.
            Field[] datePickerDialogFields = object.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(object);
                    Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if (datePickerField.getName().equals(name)) {

                            datePickerField.setAccessible(true);
                            Object dayPicker = new Object();
                            dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(visible ? View.VISIBLE : View.GONE);

                            return true;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /** find a member field by given name and hide it */
    private static View findField(Object object, String name) {
        try {
            final Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            final View fieldInstance = (View) field.get(object);
            return fieldInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** find a member field by given name and return its instance value */
    private static Object findFieldInstance(DatePicker datepicker, String name) {
        try {
            final Field field = DatePicker.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(datepicker);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
