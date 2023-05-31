package com.example.medamigo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Notificacao extends AppCompatActivity {

    private final Context context;

    private static long startTimeInMillis;
    private static long endTime;
    private static boolean timerRunning = false;
    public AlarmManager alarmManager;

    public Notificacao(Context context, AlarmManager alarmManager){
        this.context = context;
        this.alarmManager = alarmManager;
    }

    //Método que define o horário que a notificação será exibida para o usuário
    public void setNotificationAlarm() {
        Intent it = new Intent(context, NotificacaoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, it, 0);

        //AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, endTime,
                pendingIntent);

        Toast.makeText(context, "Alarme definido.", Toast.LENGTH_SHORT).show();
        timerRunning = true;
    }

    //Método que cancela a notificação que deveria ser exibida
    public void cancelNotificationAlarm(){
        Intent it = new Intent(context, NotificacaoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, it, 0);
        //AlarmManager  alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        timerRunning = false;
        //Toast.makeText(context, "Alarme cancelado.", Toast.LENGTH_SHORT).show();
    }


    //Seção de getters e setters da classe
    //Getters
    public static long getStartTimeInMillis() {
        return startTimeInMillis;
    }

    public static long getEndTime() {
        return endTime;
    }

    public static boolean isTimerRunning() {
        return timerRunning;
    }


    //Setters
    public static void setStartTimeInMillis(long _startTimeInMillis) {
        startTimeInMillis = _startTimeInMillis;
    }

    public static void setEndTime() {
        endTime = System.currentTimeMillis() + startTimeInMillis;
    }

    public static void setEndTime(long _endTime){
        endTime = _endTime;
    }

    public static void setTimerRunning(boolean _timerRunning) {
        timerRunning = _timerRunning;
    }
}