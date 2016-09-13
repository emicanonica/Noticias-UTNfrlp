package com.example.marcos.noticias_utn_frlp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Proceso que se ejecuta en segundo plano para saber si hay noticias nuevas
 */

public class Notificacion extends Service {

    private static final String INTERNAL_FILENAME = "lastID.txt";
    ArrayList id= new ArrayList();


    public Notificacion() {
    }

    @Override
    public IBinder onBind(Intent intent) {
            return null;
        }


    @Override
    public void onCreate() {
        //Log.d(TAG, "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d(TAG, "Servicio iniciado...");
        notificar();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Log.d(TAG, "Servicio destruido...");
    }

    private void notificar() {

        scheduleAlarm();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://utn-frlp-noticias.ueuo.com/script/noticias.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            id.add(jsonArray.getJSONObject(i).getString("id"));
                        }

                        File File = new File(getFilesDir(), INTERNAL_FILENAME);

                        try {
                            BufferedReader bReader = new BufferedReader(new FileReader(File));
                            String textRead = bReader.readLine();

                            int tam = id.size() - 1;
                            String actualID = id.get(tam).toString();
                            int a1 = Integer.parseInt(textRead);
                            int a2 = Integer.parseInt(actualID);

                            if (a2 > a1) {
                                creaNotificacion(0, "Notificación Android!", "Noticia nueva", getApplicationContext());
                                FileWriter out = new FileWriter(File);
                                out.write(actualID);
                                out.close();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //creaNotificacion(0, "Notificación Android!", "fallo la conexión", getApplicationContext());

            }
        });
     }

    public static void creaNotificacion(long when, String notificationTitle, String notificationContent, Context ctx) {

        try {
            Intent notificationIntent;

            Bitmap largeIcon = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launcher_utn);
            int smalIcon = R.drawable.ic_launcher_utn;

            notificationIntent = new Intent(ctx, MainActivity.class);
            /* Crea PendingIntent */
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

            /* Construye la notificacion */
            long[] pattern = {500,500,500,500,500,500,500,500,500};
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ctx).setWhen(when).setContentText(notificationContent)
                        .setContentTitle(notificationTitle).setSmallIcon(smalIcon)
                        .setAutoCancel(true).setTicker(notificationTitle)
                        .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            notificationBuilder.setLights(Color.BLUE, 500, 500);
            notificationBuilder.setVibrate(pattern);

            notificationManager.notify((int) when, notificationBuilder.build());

        } catch (Exception e) {
                Log.e("Notificacion", "createNotification::" + e.getMessage());
        }

    }

    // Prepara un proceso que se ejecuta cada 15 minutos
    public void scheduleAlarm() {
        // Construye un intent que va a ejecutar el AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        // Crea un PendigIntent para ser disparado cuando la alarma se apaga
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Prepara una alarma periodica cada 5 segundos
        long firstMillis = System.currentTimeMillis(); // la alarma se establece de inmediato
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // El primer parametro es del tipo: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // El intervalo puede ser INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    }

}

