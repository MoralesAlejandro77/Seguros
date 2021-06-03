package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.seguros.Actualizacion.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_AltaPrecios extends AppCompatActivity {
    FrameLayout container;
    TextView tarifa, plan;
    EditText sepelio, parcela, luto, edadi, edadf;
    ImageButton bedit;
    ProgressBar progressBar;
    Spinner btarifa, planes;
    String xtarifa, xplanes;


    int Aplicacion_activa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_precios);

        btarifa  = (Spinner)findViewById(R.id.btarifa);
        planes   = (Spinner)findViewById(R.id.bplanes);

        edadi    = (EditText) findViewById(R.id.vedadi);
        edadf    = (EditText) findViewById(R.id.vedadf);
        sepelio  = (EditText) findViewById(R.id.vsepelio);
        parcela  = (EditText) findViewById(R.id.vparcela);
        luto     = (EditText) findViewById(R.id.vluto);
        progressBar     = (ProgressBar)findViewById(R.id.progressBar6);

        bedit     = (ImageButton) findViewById(R.id.bedit);

        xplanes = "A";
        xtarifa = "3";
        btarifa.setSelection(0);
        planes.setSelection(0);

        progressBar.setVisibility(View.GONE);

        
        bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Librerias.verificaConexion(getApplicationContext()))
                {
                    Alta_Precios();
                }
                else
                    Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();


            }
        });

        btarifa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                xtarifa = btarifa.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
//********************************
        planes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                //      xplanes = planes.getSelectedItem().toString();

                xplanes = "";
                if (planes.getSelectedItemPosition()==0)
                    xplanes = "A";
                if (planes.getSelectedItemPosition()==1)
                    xplanes = "B";
                if (planes.getSelectedItemPosition()==2)
                    xplanes = "C";
                if (planes.getSelectedItemPosition()==3)
                    xplanes = "G";



            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



    }

    private void Alta_Precios() {
        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL31,
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
                        progressBar.setVisibility(View.GONE);
                        bedit.setEnabled(true);
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
//*****************
public void preparar() {
    progressBar.setIndeterminate(true);
    progressBar.setVisibility(View.VISIBLE);
    Aplicacion_activa = 0;
    bedit.setEnabled(false);
}
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {

        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"            ,"3P197792S");
        params.put("edadi"          , edadi.getText().toString());
        params.put("edadf"          , edadf.getText().toString());
        params.put("sepelio"        , sepelio.getText().toString());
        params.put("parcela"        , parcela.getText().toString());
        params.put("luto"           , luto.getText().toString());
        params.put("plan"           ,xplanes);
        params.put("tarifa"         ,xtarifa);
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
        Aplicacion_activa = 0;

        try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = valor.getInt("status");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Librerias.mostrar_error(Activity_AltaPrecios.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");

                    Intent resultData = new Intent();
                    resultData.putExtra("listo", "ok");
                    setResult(200,resultData);
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Activity_AltaPrecios.this,2,"SE HA PRODUCIDO UN ERROR : " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Librerias.mostrar_error(Activity_AltaPrecios.this,2,"ERROR : " + e.toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Librerias.mostrar_error(Activity_AltaPrecios.this,2,"ERROR : " + e.toString());

        }
        progressBar.setVisibility(View.GONE);
        bedit.setEnabled(true);

    }

//****************************

}
