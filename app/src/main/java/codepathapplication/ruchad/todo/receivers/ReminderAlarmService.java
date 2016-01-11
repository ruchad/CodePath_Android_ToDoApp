package codepathapplication.ruchad.todo.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import codepathapplication.ruchad.todo.R;

/**
 * Created by ruchadeshmukh on 1/4/16.
 */
public class ReminderAlarmService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("Title");
        String description = intent.getStringExtra("Description");
        Random random = new Random();
        int id = random.nextInt(9999-1000)+1000;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_todo)
                .setContentTitle("ToDo: " + title)
                .setContentText(description);

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
