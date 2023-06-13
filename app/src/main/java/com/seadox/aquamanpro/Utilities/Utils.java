package com.seadox.aquamanpro.Utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static void hideKeyboardFrom(Context context, Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static class MyChecks {
        public static boolean checkDistance(String distance) {
            if (checkNegativeNumber(distance) || !isInteger(distance))
                return false;

            int dist = Integer.parseInt(distance);
            return dist % 25 == 0;
        }

        public static boolean checkTime(String time) {
            String regex = "^\\d{2}+:\\d{2}+$";//Format 00:00

            if (Integer.parseInt(time.split(":")[0]) > 60 || Integer.parseInt(time.split(":")[1]) > 60)// max 60minutes and 60 seconds
                return false;

            return time.matches(regex);
        }

        public static boolean checkNegativeNumber(String num) {
            return num.matches("^-\\d+$") || num.equals("-");
        }

        public static boolean isInteger(String num) {
            double n = Double.parseDouble(num);
            return !(n > Integer.MAX_VALUE) && !(n < Integer.MIN_VALUE);
        }
    }
}
