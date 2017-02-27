package com.example.admin.helloandroid.database;

import android.provider.BaseColumns;

/**
 * Created by Jialin Liu on 17/08/2016.
 */
public class TaskDB {
    public static final String DB_NAME = "com.example.admin.helloandroid.database";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COL_TASK_TITLE = "title";
        public static final String NOTIF_FREQUENCY = "frequency";
    }
}
