package com.example.gerald.tarea4_top_movies;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public ArrayList<String> nombres_peliculas;
    public ArrayList<String> estrella_peliculas;
    public ArrayList<String> metascore_peliculas;
    public ArrayList<String> imagenes_peliculas;

    public class DownLoadTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings) {
            String xmlString;
            HttpURLConnection urlConnection = null;
            URL url = null;

            try {
                url = new URL("http://www.imdb.com/list/ls064079588/");
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuilder xmlResponse = new StringBuilder();
                    BufferedReader input = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        xmlResponse.append(strLine);
                    }
                    xmlString = xmlResponse.toString();
                    input.close();
                    return xmlString;

                }else{
                    return "error1";
                }
            }
            catch (Exception e) {
                return "error2";
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombres_peliculas = new ArrayList<>();
        estrella_peliculas = new ArrayList<>();
        metascore_peliculas = new ArrayList<>();
        imagenes_peliculas = new ArrayList<>();

        DownLoadTask downLoadTask = new DownLoadTask();
        try {
            String result = downLoadTask.execute("https://www.google.com").get();

            org.jsoup.nodes.Document document = Jsoup.parse(result);


            Log.d("M:","putavida");
            for(int i = 0; i < 21; i++){
                String nombre = document.getElementsByClass ("lister-item-header").get(i).getElementsByIndexEquals(1).text();
                String estrellas = document.getElementsByClass ("value").get(i).text();
                String metascore = document.getElementsByClass ("metascore  favorable").get(i).text();
                String image = document.getElementsByClass ("loadlate").get(i).attributes().get("loadlate").toString();
                nombres_peliculas.add(nombre);
                estrella_peliculas.add(estrellas);
                metascore_peliculas.add(metascore);
                imagenes_peliculas.add(image);
            }

            for(int i = 0; i < 21;  i++){
                String nombre = nombres_peliculas.get(i);
                nombre = Integer.toString(i+1)+". "+nombre;
                String estrellas = estrella_peliculas.get(i);
                String metascore = metascore_peliculas.get(i);
                String image = imagenes_peliculas.get(i);

                ImageView image_view = this.findViewById(this.getResources().getIdentifier("imageView"+Integer.toString(i+1)+"0","id",this.getPackageName()));

                TextView text_nombre = this.findViewById(this.getResources().getIdentifier("textView"+Integer.toString(i+1)+"0","id",this.getPackageName()));

                TextView text_estrellas = this.findViewById(this.getResources().getIdentifier("textView"+Integer.toString(i+1)+"1","id",this.getPackageName()));

                TextView text_metascore = this.findViewById(this.getResources().getIdentifier("textView"+Integer.toString(i+1)+"2","id",this.getPackageName()));

                text_estrellas.setText(estrellas);

                text_metascore.setText(metascore);

                text_nombre.setText(nombre);

                Picasso.with(this).load(image).into(image_view);
            }

        } catch (InterruptedException e) {
            Toast.makeText(this,"error1",1000);
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(this,"error2",1000);
            e.printStackTrace();
        }

    }
}
