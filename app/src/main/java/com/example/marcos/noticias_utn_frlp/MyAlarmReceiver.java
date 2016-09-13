package com.example.marcos.noticias_utn_frlp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.marcos.noticias_utn_frlp.MainActivity";

    // Desencadena por la alarma periódicamente (inicia el servicio para ejecutar la tarea)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, Notificacion.class);
        i.putExtra("foo", "bar");
        context.startService(i);
        /*
        Intent myIntent = new Intent(context, Notificacion.class);
        context.startService(myIntent);
        */
    }

}
