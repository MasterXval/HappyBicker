package com.happybiker;

import android.content.Context;
import android.os.Handler;

public class Help {

    /* ------------------------------------------------------------------------
     * Main Looper handler
     */

    private static Handler MAIN_LOOPER_HANDLER;

    public static Handler mainHandler() {
        return MAIN_LOOPER_HANDLER;
    }

    public static void setMainThreadHandler(final Handler handler) {
        Help.MAIN_LOOPER_HANDLER = handler;
    }

    /* ------------------------------------------------------------------------
     * Application context
     */

    private static Context APP_CTX;

    public static Context appCtx() {
        return APP_CTX;
    }

    public static void setAppCtx(final Context context) {
        APP_CTX = context;
    }

    public static String getString(int resId) {
        return APP_CTX.getString(resId);
    }
    public static String getString(int resId, Object...objects) {
        return APP_CTX.getString(resId, objects);
    }

    public static int getColor(int colorResId) {
        return APP_CTX.getResources().getColor(colorResId);
    }
}
