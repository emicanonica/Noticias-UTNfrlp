package com.example.marcos.noticias_utn_frlp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity {


    SmartImageView smartImageView;
    ArrayList photo=new ArrayList();
    ArrayList id= new ArrayList();
    public int a = 0;

    private static final String INTERNAL_FILENAME = "lastID.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        descargarImagen();
        scheduleAlarm();

    }


    /**
     * Conecta con la base de datos y coloca las imagenes en el listView
     */
    private void descargarImagen() {
        photo.clear();
        id.clear();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.0.13/NoticiasServices/script/noticias.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    progressDialog.dismiss();
                    try {
                            JSONArray jsonArray = new JSONArray(new String(responseBody));
                            for (int i=0;i<jsonArray.length();i++){
                                photo.add(jsonArray.getJSONObject(i).getString("photo"));
                                id.add(jsonArray.getJSONObject(i).getString("id"));
                            }

                            final int tam = photo.size() - 1;
                            smartImageView=(SmartImageView)findViewById(R.id.imagen);
                            final Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());

                        setImagen(tam,rect);

                            final Button button1 = (Button)findViewById(R.id.buttonBack);
                            button1.setOnClickListener(new View.OnClickListener() {
                                public void onClick (View v) {
                                    if ( a < tam ) {
                                        a = a + 1;
                                        setImagen(tam - a, rect);
                                    }
                                }
                            }) ;

                            final Button button2 = (Button)findViewById(R.id.buttonNext);
                            button2.setOnClickListener(new View.OnClickListener() {
                                public void onClick (View v) {
                                    if ( a > 0  ) {
                                        a = a - 1;
                                        setImagen(tam - a, rect);
                                    }
                                }
                            }) ;


                            if (!(tam < 0)) {
                                File File = new File(getFilesDir(), INTERNAL_FILENAME);
                                try {

                                    String str = id.get(tam).toString();
                                    FileWriter out = new FileWriter(File);
                                    out.write(str);
                                    out.close();

                                } catch (IOException e) {
                                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

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


    public void setImagen (int tam, Rect rect) {
        if (!(tam < 0)) {
            String urlfinal = "http://192.168.0.13/NoticiasServices/uploads/" + photo.get(tam).toString();
            //Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());

            smartImageView.setImageUrl(urlfinal, rect);
        }
    }

}

