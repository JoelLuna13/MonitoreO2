package com.proyecto.monitoreo2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Half;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView sal;
    Button subir, bajar;

    //Esto de abajo es para el medidor (el que parece velocimetro)
    HalfGauge Medidor;
    com.ekn.gruzer.gaugelibrary.Range Rango_1,Rango_2,Rango_3; //Estos rangos son para los colores
    int SetearGrafica;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se inicializan las variables
        subir = findViewById(R.id.btnsubir);
        bajar = findViewById(R.id.btnbajar);
        Medidor = findViewById(R.id.IDMedidor);



        Rango_1 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango_2 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango_3 = new com.ekn.gruzer.gaugelibrary.Range();

        Rango_1.setFrom(0);Rango_1.setTo(70);
        Rango_2.setFrom(70);Rango_2.setTo(85);
        Rango_3.setFrom(85);Rango_3.setTo(100);

        Medidor.addRange(Rango_1);
        Medidor.addRange(Rango_2);
        Medidor.addRange(Rango_3);

        Rango_1.setColor(Color.GREEN);
        Rango_2.setColor(Color.YELLOW);
        Rango_3.setColor(Color.RED);
        Medidor.setMinValue(0);
        Medidor.setMaxValue(100);
        Medidor.setValue(0);
        //Aqui abajo se escriben los procedimientos para los botones, cabe mencionar que son pruebas
        subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetearGrafica = SetearGrafica +10;
                if(SetearGrafica>100)
                {
                    SetearGrafica=100;
                }
                Medidor.setValue(SetearGrafica);
            }
        });
        bajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SetearGrafica = SetearGrafica -10;
                if(SetearGrafica<0)
                {
                    SetearGrafica=0;
                }
                Medidor.setValue(SetearGrafica);

            }
        });

        Medidor  = (HalfGauge) findViewById(R.id.IDMedidor);

        getData();



    }
    public void getData(){
        String sql = "https://dry-fjord-94565.herokuapp.com/valor";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        URL url = null;
        HttpURLConnection conn;

        try {
            url = new URL(sql);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;

            StringBuffer response = new StringBuffer();

            String json = "";

            while((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArr = null;

            jsonArr = new JSONArray(json);
            String mensaje = "";
            int men = 0;
            for(int i = 0;i<jsonArr.length();i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);

                //*****Todo esto muestra los datos  de la api solo en textview ya que esta como string
                /*Log.d("nombre",jsonObject.optString("nombre"));
                mensaje += "Fecha "+i+" "+jsonObject.optString("fecha")+"\n";
                Log.d("nombre",jsonObject.optString("nombre"));
                mensaje += "Gas "+i+" "+jsonObject.optString("gas")+"\n";
                Log.d("nombre",jsonObject.optString("nombre"));
                mensaje += "Humo "+i+" "+jsonObject.optString("hum")+"\n";
                Log.d("nombre",jsonObject.optString("nombre"));
                mensaje += "Ruido "+i+" "+jsonObject.optString("ruido")+"\n";
                Log.d("nombre",jsonObject.optString("nombre"));
                mensaje += "Temperatura "+i+" "+jsonObject.optString("temp")+"\n";
                Bundle medgas = new Bundle();
                medgas.putInt("gas", men);
                Gas.instantiate(medgas);*/
                men = jsonObject.getInt("gas");



            }
            Medidor.setValue(men);
            //sal.setText(mensaje);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}