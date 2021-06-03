package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
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

public class Activity_modPrecios2 extends AppCompatActivity {
    FrameLayout container;
    TextView tarifa, plan, edadi, edadf;
    EditText sepelio, parcela, luto;
    ImageButton bedit;
    ProgressBar progressBar;
    int Aplicacion_activa;
    Spinner btarifa, planes;
    CheckBox chk_tarifa, chk_plan, chk_parcela, chk_sepelio, chk_luto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_precios2);

        tarifa       = (TextView) findViewById(R.id.vtarifa);
        plan         = (TextView) findViewById(R.id.vplan);
        edadi        = (TextView) findViewById(R.id.vedadi);
        edadf        = (TextView) findViewById(R.id.vedadf);
        sepelio      = (EditText) findViewById(R.id.vsepelio);
        parcela      = (EditText) findViewById(R.id.vparcela);
        luto         = (EditText) findViewById(R.id.vluto);
        progressBar  = (ProgressBar)findViewById(R.id.progressBar6);
        btarifa      = (Spinner)  findViewById(R.id.btarifa);
        planes       = (Spinner)  findViewById(R.id.bplanes);
        chk_tarifa   = (CheckBox) findViewById(R.id.chk_tarifa);
        chk_plan     = (CheckBox) findViewById(R.id.chk_plan);
        chk_parcela  = (CheckBox) findViewById(R.id.chk_parcela);
        chk_sepelio  = (CheckBox) findViewById(R.id.chk_sepelio);
        chk_luto     = (CheckBox) findViewById(R.id.chk_luto);
        bedit        = (ImageButton) findViewById(R.id.bedit);

        progressBar.setVisibility(View.GONE);
        chk_tarifa.setChecked(false);
        chk_plan.setChecked(false);
        chk_parcela.setChecked(false);
        chk_sepelio.setChecked(false);
        chk_luto.setChecked(false);


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

    }

    private void Actualizar_Precios() {
        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL30,
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
        String xplanes, xtarifa;
        String ck_tarifa , ck_plan, ck_parcela, ck_sepelio, ck_luto;

        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"            ,"3P197792S");
        params.put("tarifa"         , tarifa.getText().toString());
        params.put("plan"           , plan.getText().toString());
        params.put("edadi"          , edadi.getText().toString());
        params.put("edadf"          , edadf.getText().toString());
        params.put("sepelio"        , sepelio.getText().toString());
        params.put("parcela"        , parcela.getText().toString());
        params.put("luto"           , luto.getText().toString());

        ck_tarifa   = "0";
        if (chk_tarifa.isChecked())
            ck_tarifa   = "1";

        ck_plan     = "0";
        if (chk_plan.isChecked())
            ck_plan   = "1";

        ck_parcela  = "0";
        if (chk_parcela.isChecked())
            ck_parcela   = "1";

        ck_sepelio  = "0";
        if (chk_sepelio.isChecked())
            ck_sepelio   = "1";

        ck_luto     = "0";
        if (chk_luto.isChecked())
            ck_luto   = "1";

        xplanes = "";
        if (planes.getSelectedItemPosition()==1)
            xplanes = "A";
        if (planes.getSelectedItemPosition()==2)
            xplanes = "B";
        if (planes.getSelectedItemPosition()==3)
            xplanes = "C";
        if (planes.getSelectedItemPosition()==4)
            xplanes = "G";

        xtarifa = btarifa.getSelectedItem().toString();


        params.put("ck_tarifa"      , ck_tarifa);
        params.put("ck_plan"        , ck_plan);
        params.put("ck_parcela"     , ck_parcela);
        params.put("ck_sepelio"     , ck_sepelio);
        params.put("ck_luto"        , ck_luto);
        params.put("plan_elegido"   , xplanes);
        params.put("tarifa_elegido" , xtarifa);

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
                    Librerias.mostrar_error(Activity_modPrecios2.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");

                    Intent resultData = new Intent();
                    resultData.putExtra("listo", "ok");
                    setResult(200,resultData);
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Activity_modPrecios2.this,2,"SE HA PRODUCIDO UN ERROR : " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Librerias.mostrar_error(Activity_modPrecios2.this,2,"ERROR : " + e.toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Librerias.mostrar_error(Activity_modPrecios2.this,2,"ERROR : " + e.toString());

        }
        progressBar.setVisibility(View.GONE);
        bedit.setEnabled(true);

    }

//****************************

}
