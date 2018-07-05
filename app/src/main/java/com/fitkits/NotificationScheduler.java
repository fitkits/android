package com.fitkits;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.support.v4.app.NotificationCompat;
import java.util.Calendar;

/**
 * Created by akshay on 16/01/18.
 */

class NotificationScheduler {
  static int WEIGHT_CODE=1,WATER_CODE=0;

  public static void setReminder(Context context,Class<?> cls,int hour, int min,int code)
  {
    Calendar calendar = Calendar.getInstance();
    Calendar setcalendar = Calendar.getInstance();
    setcalendar.set(Calendar.HOUR_OF_DAY, hour);
    setcalendar.set(Calendar.MINUTE, min);
    setcalendar.set(Calendar.SECOND, 0);
    // cancel already scheduled reminders
    cancelReminder(context,cls,code);

    if(setcalendar.before(calendar))
      setcalendar.add(Calendar.DATE,1);

    // Enable a receiver
    ComponentName receiver = new ComponentName(context, cls);
    PackageManager pm = context.getPackageManager();
    pm.setComponentEnabledSetting(receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP);

    Intent intent1 = new Intent(context, cls);
    intent1.putExtra("requestCode",code);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
        code, intent1,
        PendingIntent.FLAG_UPDATE_CURRENT);
    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),
        1000, pendingIntent);
  }

  public static void cancelReminder(Context context,Class<?> cls,int code)
  {
    // Disable a receiver
    ComponentName receiver = new ComponentName(context, cls);
    PackageManager pm = context.getPackageManager();
    pm.setComponentEnabledSetting(receiver,
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP);

    Intent intent1 = new Intent(context, cls);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
        code, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
    AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
    am.cancel(pendingIntent);
    pendingIntent.cancel();
  }
  public static void showNotification(Context context,Class<?> cls,String title,String content,int code,String goal_id)
  {
    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    Intent notificationIntent = new Intent(context, cls);
    notificationIntent.putExtra("goal_id",goal_id);
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    stackBuilder.addParentStack(cls);
    stackBuilder.addNextIntent(notificationIntent);

    PendingIntent pendingIntent = stackBuilder.getPendingIntent(
        code,PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    Notification notification = builder.setContentTitle(title)
        .setContentText(content).setAutoCancel(true)
        .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentIntent(pendingIntent).build();

    NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(code, notification);
  }
}
