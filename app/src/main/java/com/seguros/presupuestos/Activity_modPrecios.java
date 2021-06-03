package com.seguros.presupuestos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

public class Activity_modPrecios extends AppCompatActivity {
    FrameLayout container;
    TextView tarifa, plan, edadi, edadf;
    EditText sepelio, parcela, luto;
    ImageButton bedit;
    ProgressBar progressBar;
    ImageButton bactu, banular;
    int Aplicacion_activa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_precios);

        tarifa   = (TextView) findViewById(R.id.vtarifa);
        plan     = (TextView) findViewById(R.id.vplan);
        edadi    = (TextView) findViewById(R.id.vedadi);
        edadf    = (TextView) findViewById(R.id.vedadf);
        sepelio  = (EditText) findViewById(R.id.vsepelio);
        parcela  = (EditText) findViewById(R.id.vparcela);
        luto     = (EditText) findViewById(R.id.vluto);
        progressBar  = (ProgressBar)findViewById(R.id.progressBar6);

        bedit     = (ImageButton) findViewById(R.id.bedit);
        bactu     = (ImageButton) findViewById(R.id.bactu);
        banular   = (ImageButton) findViewById(R.id.banular);

        progressBar.setVisibility(View.GONE);
        tarifa.setText(this.getIntent().getExtras().getString("tarifa"));
        plan.setText(this.getIntent().getExtras().getString("plan"));
        edadi.setText(this.getIntent().getExtras().getString("edadi"));
        edadf.setText(this.getIntent().getExtras().getString("edadf"));
        sepelio.setText(this.getIntent().getExtras().getString("sepelio"));
        parcela.setText(this.getIntent().getExtras().getString("parcela"));
        luto.setText(this.getIntent().getExtras().getString("luto"));
        
        bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Librerias.verificaConexion(getApplicationContext()))
                {
                    Actualizar_Precios();
                }
                else
                    Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();


            }
        });

        bactu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Activity_modPrecios.this,Activity_modPrecios2.class);
                i.putExtra("tarifa"    ,tarifa.getText().toString());
                i.putExtra("plan"      ,plan.getText().toString());
                i.putExtra("edadi"     ,edadi.getText().toString());
                i.putExtra("edadf"     ,edadf.getText().toString());
                i.putExtra("sepelio"   ,sepelio.getText().toString());
                i.putExtra("parcela"   ,parcela.getText().toString());
                i.putExtra("luto"      ,luto.getText().toString());
                startActivityForResult(i,200);

            }
        });

        banular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Librerias.verificaConexion(getApplicationContext()))
                {
                    Eliminar_Precios();
                }
                else
                    Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();


            }
        });

    }
//*********************************
public boolean onCreateOptionsMenu(android.view.Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.opc_precios, menu);
    return true;
}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_eli:
                Eliminar_items();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Eliminar_items() {
        if (Librerias.verificaConexion(getApplicationContext()))
        {
            Eliminar_Precios();
        }
        else
            Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();

    }

    //*******************************************************************************************
    private void Eliminar_Precios() {
        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL32,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar2(response);

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



    //*******************************************************************************************
    private void Actualizar_Precios() {
        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL29,
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
        params.put("tarifa"         , tarifa.getText().toString());
        params.put("plan"           , plan.getText().toString());
        params.put("edadi"          , edadi.getText().toString());
        params.put("edadf"          , edadf.getText().toString());
        params.put("sepelio"        , sepelio.getText().toString());
        params.put("parcela"        , parcela.getText().toString());
        params.put("luto"           , luto.getText().toString());
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
                    Librerias.mostrar_error(Activity_modPrecios.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");

                    Intent resultData = new Intent();
                    resultData.putExtra("listo", "ok");
                    setResult(200,resultData);
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Activity_modPrecios.this,2,"SE HA PRODUCIDO UN ERROR : " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Librerias.mostrar_error(Activity_modPrecios.this,2,"ERROR : " + e.toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Librerias.mostrar_error(Activity_modPrecios.this,2,"ERROR : " + e.toString());

        }
        progressBar.setVisibility(View.GONE);
        bedit.setEnabled(true);

    }

//****************************
public void finalizar2(String response) {
    Aplicacion_activa = 0;

    try {
        JSONArray jsonObject = new JSONArray(response);
        JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
        Aplicacion_activa    = valor.getInt("status");

        try
        {
            if (Aplicacion_activa == 1) // Exitoso
            {
                Librerias.mostrar_error(Activity_modPrecios.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");

                Intent resultData = new Intent();
                resultData.putExtra("listo", "ok");
                setResult(200,resultData);
                finish();

            }
            else
            {
                Librerias.mostrar_error(Activity_modPrecios.this,2,"SE HA PRODUCIDO UN ERROR : " );
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
            Librerias.mostrar_error(Activity_modPrecios.this,2,"ERROR : " + e.toString());

        }

    } catch (JSONException e) {
        e.printStackTrace();
        Librerias.mostrar_error(Activity_modPrecios.this,2,"ERROR : " + e.toString());

    }
    progressBar.setVisibility(View.GONE);
    bedit.setEnabled(true);

}


}
