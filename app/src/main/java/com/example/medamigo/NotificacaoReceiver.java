package com.example.medamigo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificacaoReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "MedAmigo";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        String message = context.getString(R.string.HoraDeTomarRemedio);
        String title = context.getString(R.string.Atencao);

        Intent activityIntent = new Intent(context, ConfirmacaoDose.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, activityIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_alarm_24) //Definir o ícone da notificação
                .setContentTitle(title) //Definir o título da notificação
                .setContentText(message) //Definir o conteúdo no corpo da notificação
                .setPriority(NotificationCompat.PRIORITY_MAX) //Definir a prioridade da notificação
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true) //Fazer com que a notificação seja fechada após o evento de click nela e a execução da tarefa determinada
                .setOnlyAlertOnce(true)//A notificação só irá notificar uma vez
                .setContentIntent(contentIntent)//Ação que a notificação irá fazer quando for clicada
                .setOngoing(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }
}
