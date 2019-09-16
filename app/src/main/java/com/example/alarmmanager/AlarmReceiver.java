package com.example.alarmmanager;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// extend broadcashReceiver
public class AlarmReceiver extends BroadcastReceiver {
    //initial static variable
    public static final String TYPE_ONE_TIME= "OneTimeAlarm";
    public static final String TYPE_ONE_REPEATING= "RepeatingAlarm";
    public static final String EXTRA_MESSAGE= "message";
    public static final String EXTRA_TYPE= "type";

    private final int ID_ONETIME=100;
    private final int ID_REPEATING=101;
    String DATE_FORMAT= "yyyy-MM-dd";
    String TIME_FORMAT= "HH:mm";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // take value of type from intent
        String type= intent.getStringExtra(EXTRA_TYPE);
        // take value of message
        String message= intent.getStringExtra(EXTRA_MESSAGE);
        // take title
        String title= type.equalsIgnoreCase(TYPE_ONE_TIME)? TYPE_ONE_TIME: TYPE_ONE_REPEATING;
        // give id ONE repeat and id repeat
        int notifId= type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONETIME : ID_REPEATING;
        showToast(context, title, message);
        showAlarmNotification(context, title, message, notifId);
    }
    private void showToast(Context context, String title, String message){
        Toast.makeText(context, title + ":" + message , Toast.LENGTH_SHORT).show();
    }

    // set alarm where one repating
    public void setOneTimeAlarm(Context context, String type, String date, String time, String message){
        // make format of time date android

        if(isDateInvalid(date, DATE_FORMAT) || isDateInvalid(time, TIME_FORMAT)) return;

        // make new alarm manager
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // intent to alaram receiver with put Extra
        Intent intent= new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        // put extra type to AlarmReceiver
        intent.putExtra(EXTRA_TYPE, type);

        Log.e("ONE TIME", date+""+time);
        // menyimpan hasil dari date ke dalam array
        String dataArray[]= date.split("-");
        String timeArray[]= time.split(":");

        // give  value of calender to array
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dataArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dataArray[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dataArray[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        int requestCode= ID_ONETIME;

        // membuat pending
        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, requestCode,intent,0);
        // jika hasil dari alaram manager tidak null
        if(alarmManager!=null){
            // mengaktifkan alarm
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(context, "One Time alarm set up", Toast.LENGTH_SHORT).show();

    }
    // make format time date
    public boolean isDateInvalid(String date, String format){
        try {
            DateFormat df= new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    // show notification
    private void showAlarmNotification(Context context, String title,  String message, int notifId){
        String CHANNEL_ID= "Channel_1";
        String CHANNEL_NAME= "AlarmManager channel";
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // get type alarm notification
        Uri alarmSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder= new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentText(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setSound(alarmSound);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000,1000,1000,1000,1000});
            builder.setChannelId(CHANNEL_ID);
            Notification notification= builder.build();
            if(notificationManager!= null){
                notificationManager.notify(notifId, notification);
            }
        }
    }
    public void setRepeatingAlarm (Context context, String type, String time, String message){
        if(isDateInvalid(time, TIME_FORMAT)) return;
        AlarmManager alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // intent to Alarm receiver with message title
        Intent intent= new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[]= time.split(":");

        Calendar calendar= Calendar.getInstance();
        // simpan time to array
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent= PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0);
        if(alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, "Repeating alarm set up", Toast.LENGTH_SHORT).show();
    }
}
