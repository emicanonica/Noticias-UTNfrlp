package com.emi.noticias_utnfrlp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImage;
import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    ArrayList name=new ArrayList();
    ArrayList photo=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.listView);
        descargarImagen();

    }

    private void descargarImagen() {
        name.clear();
        photo.clear();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        AsyncHttpClient client=new AsyncHttpClient();
        client.get("http://192.168.0.13/NoticiasServices/script/noticias.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    progressDialog.dismiss();
                    try {
                        JSONArray jsonArray=new JSONArray(new String(responseBody));
                        for (int i=0;i<jsonArray.length();i++){
                            name.add(jsonArray.getJSONObject(i).getString("name"));
                            photo.add(jsonArray.getJSONObject(i).getString("photo"));
                        }

                        listView.setAdapter(new ImagenAdapter(getApplicationContext()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private class ImagenAdapter extends BaseAdapter{

        Context ctx;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView tvname;

        public ImagenAdapter(Context applicationContext) {
            this.ctx=applicationContext;
            layoutInflater=(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return photo.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewGroup viewgroup=(ViewGroup)layoutInflater.inflate(R.layout.activity_main_item, null);

            smartImageView=(SmartImageView)viewgroup.findViewById(R.id.photo1);
            tvname=(TextView)viewgroup.findViewById(R.id.tvname);

            String urlfinal="http://192.168.0.13/NoticiasServices/uploads/"+photo.get(position).toString();
            Rect rect=new Rect(smartImageView.getLeft(), smartImageView.getTop(), smartImageView.getRight(), smartImageView.getBottom());

            smartImageView.setImageUrl(urlfinal, rect);

            tvname.setText(name.get(position).toString());

            return viewgroup;
        }
    }
}
