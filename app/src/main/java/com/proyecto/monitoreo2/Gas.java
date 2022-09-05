package com.proyecto.monitoreo2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.MultiGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Gas extends Fragment {

    MultiGauge Medidor;
    com.ekn.gruzer.gaugelibrary.Range Rango_1,Rango_2,Rango_3,Rango_4; //Estos rangos son para los colores
    TextView txttit, txtmen;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View principal = inflater.inflate(R.layout.fragment_gas, container, false);

        txttit = (TextView) principal.findViewById(R.id.txtTit);
        txtmen = (TextView) principal.findViewById(R.id.txtmensaje);

        Medidor = (MultiGauge) principal.findViewById(R.id.Medgas);
        Rango_1 = new Range();
        Rango_2 = new Range();
        Rango_3 = new Range();
        Rango_4 = new Range();

        Rango_1.setFrom(0);Rango_1.setTo(55);
        Rango_2.setFrom(55);Rango_2.setTo(70);
        Rango_3.setFrom(70);Rango_3.setTo(350);
        Rango_4.setFrom(350);Rango_3.setTo(400);

        Medidor.addRange(Rango_1);
        Medidor.addRange(Rango_2);
        Medidor.addRange(Rango_3);
        Medidor.addRange(Rango_4);

        Rango_1.setColor(Color.GREEN);
        Rango_2.setColor(Color.YELLOW);
        Rango_3.setColor(Color.BLUE);
        Rango_4.setColor(Color.RED);

        Medidor.setMinValue(0);
        Medidor.setMaxValue(400);
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
                Log.d("nombre",jsonObject.optString("nombre"));
                men = jsonObject.getInt("gas");
                mensaje = jsonObject.getString("gas") +" PPM";


            }

            Medidor.setValue(men);
            txttit.setText(mensaje);
            if (men>=0 &&  men<= 55)
            {
                txtmen.setText("No hay presencia");
                txttit.setTextColor(Color.GREEN);
                txtmen.setTextColor(Color.GREEN);
            }
            else
            {
                if (men>55 &&  men<= 70)
                {
                    //mensaje = "Aire limpio con poca presencia de dioxido de carbono";

                    txtmen.setText("Hay poca presencia de dioxido de carbono en el ambiente");
                    txttit.setTextColor(Color.GREEN);
                    txtmen.setTextColor(Color.GREEN);

                }
                else
                {
                    if (men>70 &&  men<= 350)
                    {

                        txtmen.setText("Peligro: Presencia de dioxido de carbono en el ambiente");
                        txttit.setTextColor(Color.BLUE);
                        txtmen.setTextColor(Color.BLUE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                        builder.setMessage("Aire contaminado, presencia de dioxido de carbono")
                                .setTitle("Advertencia");
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
                       // builder.show();

                    }
                    else
                    {
                        if (men>350)
                        {

                            txtmen.setText("Peligro: Presencia de gases toxicos en el ambiente");
                            txttit.setTextColor(Color.RED);
                            txtmen.setTextColor(Color.RED);

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

// 2. Chain together various setter methods to set the dialog characteristics
                           builder.setMessage("Aire contaminado, presencia de gases toxicos (Propano, Butano)")
                                    .setTitle("Advertencia");
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
