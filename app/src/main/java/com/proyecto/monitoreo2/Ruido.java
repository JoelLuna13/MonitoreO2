package com.proyecto.monitoreo2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Ruido extends Fragment {

    TextView tit, texto;

    HalfGauge Medidor;
    com.ekn.gruzer.gaugelibrary.Range Rango_1,Rango_2,Rango_3; //Estos rangos son para los colores

    public Ruido() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View principal = inflater.inflate(R.layout.fragment_ruido, container, false);

        tit = (TextView) principal.findViewById(R.id.txttitulo);
        texto = (TextView) principal.findViewById(R.id.txttexto);
        Medidor = (HalfGauge) principal.findViewById(R.id.Medruido);
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

        getData();


        return principal;
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
                men = jsonObject.getInt("ruido");
                mensaje = jsonObject.getString("ruido")+ " dB";
            }
            Medidor.setValue(men);
            //sal.setText(mensaje);
            tit.setText(mensaje);
            if (men>=0 &&  men<= 70)
            {
                texto.setText("El ruido en la sala se encuentra dentro del limite");
                tit.setTextColor(Color.GREEN);
                texto.setTextColor(Color.GREEN);
            }
            else
            {
                if (men>70 &&  men<= 85)
                {

                    texto.setText("¡Cuidado!, El ruido en la sala esta llegando al estado critico");
                    tit.setTextColor(Color.GRAY);
                    texto.setTextColor(Color.GRAY);
                }
                else
                {
                    if (men>85 &&  men<= 85)
                    {

                        texto.setText("¡ADVERTENCIA! El ruido de la sala puede causar daños auditivos");
                        tit.setTextColor(Color.RED);
                        texto.setTextColor(Color.RED);
                        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                      /*  builder.setMessage("El ruido de la sala puede causar daños auditivos")
                                .setTitle("¡ADVERTENCIA!");
                        builder.setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });
               /* builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });*/
                        //builder.show();
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}