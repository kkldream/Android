package com.example.location;

import android.util.Log;

public class Tag {
    public static final String tag = "tag";
    public static final String location = "tag";
    public static final String activity = "tag";

    public static void log(String msg) {
        Log.d(tag, msg);
    }
}
