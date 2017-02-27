package com.example.admin.helloandroid;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.helloandroid.database.TaskDBHelper;
import com.example.admin.helloandroid.database.TaskDB;
import com.example.admin.helloandroid.notification.NotificationReceiver;
import com.example.admin.helloandroid.notification.NotificationView;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Jialin Liu on 17/08/2016.
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TaskDBHelper myDBHelper;
    private ListView myTaskListView;
    private ArrayAdapter<String> myAdapter;
    private Button b1;
    final CharSequence[] frequencies = {
            "No notification" , "Every 15 minute", "Every hour", "Every day"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialise the database helper
        myDBHelper = new TaskDBHelper(this);
        // ListView
        myTaskListView = (ListView) findViewById(R.id.list_todo);

        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskDB.TaskEntry.TABLE,
                new String[]{TaskDB.TaskEntry._ID, TaskDB.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskDB.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();

        updateUI();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = myDBHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskDB.TaskEntry.TABLE,
                new String[]{TaskDB.TaskEntry._ID, TaskDB.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskDB.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (myAdapter == null) {
            myAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_name,
                    taskList);
            myTaskListView.setAdapter(myAdapter);
        } else {
            myAdapter.clear();
            myAdapter.addAll(taskList);
            myAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.add_task:
                Log.d(TAG, "add a new task");
                addNewTask();
                return true;
            case R.id.task_done:
                Log.d(TAG, "A task done and deleted");
                return true;
            case R.id.task_delete:
                Log.d(TAG, "Delete a task");
                return true;
//            case R.id.add_notification:
//                Log.d(TAG, "Add a new notification");
//                return true;
//            case R.id.delete_notification:
//                Log.d(TAG, "Add a new notification");
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Add a new task and insert it into the database
    public void addNewTask() {
        final EditText taskEditText = new EditText(this);
        AlertDialog taskDialog = new AlertDialog.Builder(this)
                .setTitle("Add a new task")
                .setMessage("What's the new task?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskText = String.valueOf(taskEditText.getText());
                        SQLiteDatabase db = myDBHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(TaskDB.TaskEntry.COL_TASK_TITLE, taskText);
                        db.insertWithOnConflict(TaskDB.TaskEntry.TABLE,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE);
                        db.close();
                        updateUI();
                        addNewNotification();
                        Log.d(TAG, "New task: " + taskText);
                    }
                }).setNegativeButton("Cancel", null)
                .create();
        taskDialog.show();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_name);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        db.delete(TaskDB.TaskEntry.TABLE,
                TaskDB.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db.close();
        updateUI();
    }

//    public void addNewTaskAndNotif() {
//        final EditText taskEditText = new EditText(this);
//        AlertDialog taskDialog = new AlertDialog.Builder(this)
//                .setTitle("Add a new task")
//                .setMessage("What's the new task?")
//                .setView(taskEditText)
//                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String taskText = String.valueOf(taskEditText.getText());
//
//
//                        SQLiteDatabase db = myDBHelper.getWritableDatabase();
//                        ContentValues values = new ContentValues();
//                        values.put(TaskDB.TaskEntry.COL_TASK_TITLE, taskText);
//                        db.insertWithOnConflict(TaskDB.TaskEntry.TABLE,
//                                null,
//                                values,
//                                SQLiteDatabase.CONFLICT_REPLACE);
//                        db.close();
//                        updateUI();
//                        Log.d(TAG, "New task: " + taskText);
//                    }
//                }).setNegativeButton("Cancel", null)
//                .create();
//        taskDialog.show();
//    }

    // Add a new notification
    public void addNewNotification() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Pick a notification frequency")
                .setItems(frequencies, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                dialog.cancel();
                                break;
                            case 1:
                                onAddNotificationClick(1);
                                break;
                            case 2:
                                onAddNotificationClick(4);
                                break;
                            case 3:
                                onAddNotificationClick(96);
                                break;
                            default:
                                dialog.cancel();
                                break;
                        }
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public void onAddNotificationClick(int nbMinutes) {
        NotificationReceiver.NOTIFICATIONS_INTERVAL_IN_FIFTEEN_MINUTES = nbMinutes;
        NotificationReceiver.setupAlarm(getApplicationContext());
    }

    public void addNotifToDB(int id) {
        View parent = (View) myTaskListView.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_name);
        String task = String.valueOf(taskTextView.getText());

        SQLiteDatabase db = myDBHelper.getWritableDatabase();
// TODO: 14/10/2016  
//        db. 
//        db.replace()
        db.close();
        updateUI();
    }

    public void deleteNotification(View view) {

    }
        // Build a notification
    private void Notify(String notificationTitle, String notificationMessage) {
        // Step 1: Create Notification Builder
        // Step 2: Setting Notification Properties
        NotificationCompat.Builder notificationBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(notificationTitle)
                        .setContentText(notificationMessage);

        // Step 3: Attach Action
        Intent intent = new Intent(this, NotificationView.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent notificationIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        try {
            notificationIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        notificationBuilder.setContentIntent(notificationIntent);
        NotificationManager myNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(1111, notificationBuilder.build());
    }
}
