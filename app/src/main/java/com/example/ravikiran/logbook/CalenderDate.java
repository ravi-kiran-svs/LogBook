package com.example.ravikiran.logbook;

import android.content.Context;
import android.widget.Toast;

class CalenderDate {

    //private static final String TAG = "CalenderDate";
    private Context context;

    CalenderDate(Context Context) {
        context = Context;
    }

    String getDate(String date) {
        String in_year = date.substring(0,4);
        String in_month = date.substring(4,6);
        String in_day = date.substring(6);

        String out_year = " " + in_year;
        String out_month = getMonth(in_month);
        int out_day = Integer.valueOf(in_day);

        if(out_day == 1)        return out_day + "st " + out_month + "," + out_year;
        else if(out_day == 2)   return out_day + "nd " + out_month + "," + out_year;
        else if(out_day == 3)   return out_day + "rd " + out_month + "," + out_year;
        else                    return out_day + "th " + out_month + "," + out_year;
    }

    private String getMonth(String in_month) {
        int m = Integer.valueOf(in_month);

        if(m == 1)          return "Jan";
        else if(m == 2)     return "Feb";
        else if(m == 3)     return "Mar";
        else if(m == 4)     return "Apr";
        else if(m == 5)     return "May";
        else if(m == 6)     return "Jun";
        else if(m == 7)     return "Jul";
        else if(m == 8)     return "Aug";
        else if(m == 9)     return "Sept";
        else if(m == 10)    return "Oct";
        else if(m == 11)    return "Nov";
        else if(m == 12)    return "Dec";
        else                return null;
    }

    void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
