package com.example.admin.helloandroid.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.admin.helloandroid.MainActivity;
import com.example.admin.helloandroid.R;


public class NotificationActivity extends AppCompatActivity {
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

//        b1=(Button)findViewById(R.id.button);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Notify("Time to review your TODOs.", "infos");
//            }
//        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_task) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
