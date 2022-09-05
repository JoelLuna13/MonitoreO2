package com.proyecto.monitoreo2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
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


public class Temp extends Fragment {

    HalfGauge Medidor, Medidor2;
    com.ekn.gruzer.gaugelibrary.Range Rango_1,Rango_2,Rango_3, Rango1, Rango2, Rango3; //Estos rangos son para los colores
    TextView t1,t2,text1,text2;

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
         View principal = inflater.inflate(R.layout.fragment_temp, container, false);

        t1 = (TextView) principal.findViewById(R.id.txttit2);
        t2 = (TextView) principal.findViewById(R.id.txttit3);
        text1 = (TextView) principal.findViewById(R.id.txttexto1);
        text2 = (TextView) principal.findViewById(R.id.txttexto2);
         //Medidor de temperatura
        Medidor = (HalfGauge) principal.findViewById(R.id.Medtemp);
        Rango_1 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango_2 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango_3 = new com.ekn.gruzer.gaugelibrary.Range();

        Rango_1.setFrom(20);Rango_1.setTo(24);
        Rango_2.setFrom(24);Rango_2.setTo(30);
        Rango_3.setFrom(30);Rango_3.setTo(40);

        Medidor.addRange(Rango_1);
        Medidor.addRange(Rango_2);
        Medidor.addRange(Rango_3);

        Rango_1.setColor(Color.GREEN);
        Rango_2.setColor(Color.YELLOW);
        Rango_3.setColor(Color.RED);

        Medidor.setMinValue(20);
        Medidor.setMaxValue(40);
        Medidor.setValue(0);

        //Medidor de humedad
        Medidor2 = (HalfGauge) principal.findViewById(R.id.MedHum);
        Rango1 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango2 = new com.ekn.gruzer.gaugelibrary.Range();
        Rango3 = new com.ekn.gruzer.gaugelibrary.Range();

        Rango1.setFrom(20);Rango1.setTo(60);
        Rango2.setFrom(60);Rango2.setTo(70);
        Rango3.setFrom(70);Rango3.setTo(85);

        Medidor2.addRange(Rango1);
        Medidor2.addRange(Rango2);
        Medidor2.addRange(Rango3);

        Rango1.setColor(Color.GREEN);
        Rango2.setColor(Color.YELLOW);
        Rango3.setColor(Color.RED);

        Medidor2.setMinValue(20);
        Medidor2.setMaxValue(85);
        Medidor2.setValue(0);

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
            String mensaje = "", mensaje2 = "";

            int men = 0, men2 = 0;
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
                men = jsonObject.getInt("temp");
                men2 = jsonObject.getInt("hum");
                Log.d("nombre",jsonObject.optString("nombre"));
                mensaje ="Temperatura: "+ jsonObject.getString("temp") + " ºC"+"\n";
                mensaje2 ="Humedad: "+ jsonObject.getString("hum") + " %"+"\n";



            }

            Medidor.setValue(men);
            Medidor2.setValue(men2);
            //sal.setText(mensaje);
            t1.setText(mensaje);
            t2.setText(mensaje2);

            if (men >= 20 && men <= 24) {
                text1.setText("La temperatura en la sala es adecuada");
                t1.setTextColor(Color.GREEN);
                text1.setTextColor(Color.GREEN);
            }
            else {
                if (men > 24 && men <= 30) {

                    text1.setText("¡Cuidado!, La temperatura en la sala esta llegando al estado critico");
                    t1.setTextColor(Color.GRAY);
                    text1.setTextColor(Color.GRAY);
                } else {
                    if (men > 30 && men <= 40) {

                        text1.setText("¡ADVERTENCIA! La temperatura de la sala puede causar daños en la salud");
                        t1.setTextColor(Color.RED);
                        text1.setTextColor(Color.RED);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("La temperatura de la sala puede causar daños en la salud")
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
                        builder.show();
                    } else {
                        if (men < 20) {

                            text1.setText("¡ADVERTENCIA! La temperatura de la sala puede causar daños en la salud");
                            t1.setTextColor(Color.RED);
                            text1.setTextColor(Color.RED);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage("La temperatura de la sala puede causar daños en la salud")
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
                            builder.show();
                        }
                    }
                }
            }
            //Humedad
            if (men >= 20 && men <= 60) {

                text2.setText("La Humedad en la sala es adecuada");
                t2.setTextColor(Color.GREEN);
                text2.setTextColor(Color.GREEN);
            } else {
                if (men > 60 && men <= 70) {

                    text2.setText("¡Cuidado!, La Humedad en la sala esta llegando al estado critico");
                    t2.setTextColor(Color.GRAY);
                    text2.setTextColor(Color.GRAY);
                } else {
                    if (men > 70 && men <= 85) {

                        text2.setText("¡ADVERTENCIA! La Humedad de la sala puede causar daños en la salud");
                        t2.setTextColor(Color.RED);
                        text2.setTextColor(Color.RED);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("La temperatura de la sala puede causar daños en la salud")
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
                        builder.show();
                    } else {
                        if (men < 20) {

                            text2.setText("¡ADVERTENCIA! La Humedad de la sala puede causar daños en la salud");
                            t2.setTextColor(Color.RED);
                            text2.setTextColor(Color.RED);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                            builder.setMessage("La temperatura de la sala puede causar daños en la salud")
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
                            builder.show();
                        }
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