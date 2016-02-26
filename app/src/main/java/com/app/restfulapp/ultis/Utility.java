package com.app.restfulapp.ultis;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.restfulapp.MainActivity;
import com.app.restfulapp.models.Customer;
import com.app.restfulapp.models.Member;
import com.app.restfulapp.reports.FrgSLGDReport;

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

    }public static String convertDate(Date date){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormatter.format(date);
    }

    public static String convertSimpleDate(String fulldate){
        return fulldate.substring(0, fulldate.indexOf('T') > 0 ? fulldate.indexOf('T') : fulldate.length());
    }

    public static ArrayList<Customer> genCustType(){
        ArrayList<Customer> result = new ArrayList<>();
        result.add(new Customer("Area","1"));
        result.add(new Customer("Area","2"));
        result.add(new Customer("Area","3"));
        result.add(new Customer("Area","4"));
        return result;
    }
    //1: nupak; 3: Dachan; 5:Redstar
    public static ArrayList<Member> genFlag(){
        ArrayList<Member> result = new ArrayList<>();
        result.add(new Member("nupak","1"));
        result.add(new Member("Dachan","3"));
        result.add(new Member("Redstar","5"));
        return result;
    }
    //part_kind: A (Hỗn hợp), B (Đậm đặc)
    public static ArrayList<Member> genPartKind(){
        ArrayList<Member> result = new ArrayList<>();
        result.add(new Member("Hỗn hợp","A"));
        result.add(new Member("Đậm đặc","B"));
        result.add(new Member("Hỗn hợp & Đậm đặc",""));
        return result;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static List<Member> genProduct() {
        //V101, V801D, V801R, V801N
        ArrayList<Member> result = new ArrayList<>();
        result.add(new Member("V101","Product NO."));
        result.add(new Member("V801D","Product NO."));
        result.add(new Member("V801R","Product NO."));
        result.add(new Member("V801R","Product NO."));
        return result;
    }

    public static List<Member> genPeriodType() {
        ArrayList<Member> result = new ArrayList<>();
        for(int i=0;i<FrgSLGDReport.PeriodType.values().length;i++){
            result.add(new Member(FrgSLGDReport.PeriodType.values()[i].name(),""+i));
        }
        return result;
    }
    public static List<Member> genP1() {
        //00: Hỗn hợp, 03: Gà; 04: Cút
        ArrayList<Member> result = new ArrayList<>();
        result.add(new Member("Hỗn hợp","00"));
        result.add(new Member("Gà","03"));
        result.add(new Member("Cút","04"));
        return result;
    }

    public static List<Member> genP2() {
        //0301: Gà con; 0302: Gà hậu bị
        ArrayList<Member> result = new ArrayList<>();
        result.add(new Member("Gà con","0301"));
        result.add(new Member("Gà hậu bị", "0302"));
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

}
