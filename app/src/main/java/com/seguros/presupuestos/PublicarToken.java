package com.seguros.presupuestos;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.seguros.Actualizacion.UserFunctions;
import com.seguros.fcm.MySharedPreference;

import org.json.JSONObject;




/**
 * Created by ALE77 on 30/01/2017.
 */

public class PublicarToken extends AsyncTask<String, Float, Integer> {
    JSONObject json;
    String resultado;
    boolean error;
    String token;
    Context c;
     MySharedPreference mySharedPreference;
    public PublicarToken(Context c) {
        this.c = c;
        mySharedPreference = new MySharedPreference(c);
    }

    protected void onPreExecute() {
        error    = false;

    }

    protected Integer doInBackground(String... urls) {
        error = false;

        try {

            resultado = UserFunctions.Registrar_token(c,String.valueOf(Librerias.Leer_tipodoc(c)), Librerias.Leer_dni(c));
            json = new JSONObject(resultado);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            error = true;
        }


        return 1;

    }

    protected void onPostExecute(Integer bytes) {
        try {
            if (!error) {
                if (json.getInt("estado") == 1){
                    Toast.makeText(c, "Problemas al registrar dispositivo", Toast.LENGTH_LONG).show();
                    mySharedPreference.saveNotificationSubscription(false);
                }

                if (json.getInt("estado") == 0) {
                    Toast.makeText(c, "Dispositivo registrado exitosamente!", Toast.LENGTH_SHORT).show();
                    mySharedPreference.saveNotificationSubscription(true);
                }
            }
            else {
                mySharedPreference.saveNotificationSubscription(false);
                Toast.makeText(c, "Problemas al registrar dispositivo", Toast.LENGTH_LONG).show();
            }




        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(c, "Problemas de comunicacion!!!!", Toast.LENGTH_LONG).show();
        }



    }

}
