package com.seguros.fcm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.presupuestos.Librerias;
import com.seguros.presupuestos.PublicarToken;
import com.seguros.presupuestos.R;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CustomFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = CustomFirebaseInstanceIDService.class.getSimpleName();

   // private RequestQueue queue;



    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.canal_notify));

        Log.d(TAG, "Token Value: " + refreshedToken);


        Librerias.grabar_tokenlocal(getApplicationContext(),refreshedToken);

        if (Librerias.Esta_registrado_asegurado(getApplicationContext()))
        {
            sendtoServer(refreshedToken);
//        sendTheRegisteredTokenToWebServer(refreshedToken);

        }



    }

    private void sendtoServer(String token) {
  //      PublicarToken registro = new PublicarToken(getApplicationContext());
   //     registro.execute();
        EnviarToken();
    }

/*************************************************************************************************/
    private void EnviarToken(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.URL_SERVER+"token.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = preparar_Parametros();
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    /*************************************************************************************************************/
    public void preparar() {

    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {
        SharedPreferences Preferences;
        Preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String,String> params = new HashMap<String, String>();
        params.put("tipodoc"     , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
        params.put("dni"         , Librerias.Leer_dni(getApplicationContext()));
        params.put("token"       , Preferences.getString("token", ""));
        params.put("tag"         ,"3P197792S");
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
        MySharedPreference mySharedPreference;
        mySharedPreference = new MySharedPreference(getApplicationContext());
        try {

            try
            {
                JSONObject json = new JSONObject(response);

                if (json.getInt("estado") == 1){
                    Toast.makeText(getApplicationContext(), "Problemas al registrar dispositivo", Toast.LENGTH_LONG).show();
                    mySharedPreference.saveNotificationSubscription(false);
                }

                if (json.getInt("estado") == 0) {
                    Toast.makeText(getApplicationContext(), "Dispositivo registrado exitosamente!", Toast.LENGTH_SHORT).show();
                    mySharedPreference.saveNotificationSubscription(true);
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//*******************************************************************************************************************************

}



