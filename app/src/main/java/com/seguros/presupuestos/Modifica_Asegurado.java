package com.seguros.presupuestos;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Modifica_Asegurado extends AppCompatActivity {
ProgressBar progressBar, progressBar2;
TextView tipodoc, nro_doc, nombrecompleto;
Spinner companias, provincias;
EditText  caract, celular, caractp, telefono;
EditText  departamento, localidad, cp, calle,  piso, depto;
Button bgrabar;

String nombre, stipodoc, fechanac, tel_car_1, tel_numero_1, tel_car_2, tel_numero_2, compania_1, email, provincia, sdepartamento, slocalidad, scp, scalle, spiso, sdepto;


int Aplicacion_activa;
int resultado = 0;
String usuario, estado, version_and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mod_aseg);
        progressBar     = (ProgressBar)findViewById(R.id.progressbar3);
        progressBar2    = (ProgressBar)findViewById(R.id.progressbar4);
        tipodoc         = (TextView)findViewById(R.id.tipodoc);
        nro_doc         = (TextView) findViewById(R.id.nro_doc);
        nombrecompleto  = (TextView) findViewById(R.id.nombrecompleto);
        caract          = (EditText) findViewById(R.id.caract);
        celular         = (EditText) findViewById(R.id.celular);
        caractp         = (EditText) findViewById(R.id.caractp);
        telefono        = (EditText) findViewById(R.id.telefono);
        companias       = (Spinner)  findViewById(R.id.companias);
        provincias      = (Spinner)  findViewById(R.id.provincia);

        departamento    = (EditText) findViewById(R.id.departamento);
        localidad       = (EditText) findViewById(R.id.localidad);
        cp              = (EditText) findViewById(R.id.cp);

        calle           = (EditText) findViewById(R.id.calle);
        piso            = (EditText) findViewById(R.id.piso);
        depto           = (EditText) findViewById(R.id.depto);
        bgrabar         = (Button)   findViewById(R.id.bgrabar);

        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        provincias.setSelection(12);
        bgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModificarUSuario();
            }
        });
        estado    = "";
        resultado = 0;
        this.setTitle("");
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());
        Consultar_datos();
    }
//****************************************************************************************
private void Consultar_datos(){
    progressBar2.setIndeterminate(true);
    progressBar2.setVisibility(View.VISIBLE);
    Aplicacion_activa = 0;
    bgrabar.setEnabled(false);

    StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL17,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    finalizar_datos(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    progressBar2.setVisibility(View.GONE);
                    bgrabar.setEnabled(true);
                }
            }){

        @Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("tag"           ,"3P197792S");
            params.put("tipodoc"       , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
            params.put("dni"           , Librerias.Leer_dni(getApplicationContext()));
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
//****************************************************************************************
public void finalizar_datos(String response) {
        Aplicacion_activa = 0;

        try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject json      = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = json.getInt("status");
            nombre               = json.getString("nombre");
            stipodoc             = json.getString("tipodoc");
            fechanac             = json.getString("fechanac");
            tel_car_1            = json.getString("tel_car_1");
            tel_numero_1         = json.getString("tel_numero_1");
            tel_car_2            = json.getString("tel_car_2");
            tel_numero_2         = json.getString("tel_numero_2");
            compania_1           = json.getString("compania_1");
            email                = json.getString("email");
            provincia            = json.getString("provincia");
            sdepartamento        = json.getString("departamento");
            slocalidad           = json.getString("localidad");
            scp                  = json.getString("cp");
            scalle               = json.getString("calle");
            spiso                = json.getString("piso");
            sdepto               = json.getString("depto");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    MostrarDatos(getApplicationContext());
                }
                else
                {
                    Librerias.mostrar_error(Modifica_Asegurado.this,2,"SE HA PRODUCIDO UN ERROR AL LEER DATOS PREVIAMENTE CARGADOS  " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar2.setVisibility(View.GONE);
        bgrabar.setEnabled(true);

    }


//*******************************************************************************************************************************

private void ModificarUSuario(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL18,
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
                        bgrabar.setEnabled(true);
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
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        Aplicacion_activa = 0;
        bgrabar.setEnabled(false);
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {

        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"           ,"3P197792S");
        params.put("tipodoc"       , String.valueOf(Librerias.Leer_tipodoc(getApplicationContext())));
        params.put("dni"           , Librerias.Leer_dni(getApplicationContext()));
        params.put("caract"        , caract.getText().toString());
        params.put("celular"       , celular.getText().toString());
        params.put("caractp"       , caractp.getText().toString());
        params.put("telefono"      , telefono.getText().toString());
        params.put("compania"      , companias.getSelectedItem().toString());
        params.put("provincia"     , provincias.getSelectedItem().toString());
        params.put("departamento"  , departamento.getText().toString());
        params.put("cp"            , cp.getText().toString());
        params.put("localidad"     , localidad.getText().toString());
        params.put("calle"         , calle.getText().toString());
        params.put("piso"          , piso.getText().toString());
        params.put("depto"         , depto.getText().toString());
        params.put("version"       , version_and);
        params.put("app"           , getString(R.string.version));
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
                    Librerias.mostrar_error(Modifica_Asegurado.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Modifica_Asegurado.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
        bgrabar.setEnabled(true);

    }


//*******************************************************************************************************************************
    private void MostrarDatos(Context c) {
        tipodoc.setText(stipodoc);
        nro_doc.setText(Librerias.Leer_dni(getApplicationContext()));
        nombrecompleto.setText(nombre);
        caract.setText(tel_car_1);
        celular.setText(tel_numero_1);
        caractp.setText(tel_car_2);
        telefono.setText(tel_numero_2);
        setSpinText(companias,compania_1);

        if (!provincia.isEmpty() && !provincia.equals(""))
           setSpinText(provincias,provincia);

        departamento.setText(sdepartamento);
        localidad.setText(slocalidad);
        cp.setText(scp);
        calle.setText(scalle);
        piso.setText(spiso);
        depto.setText(sdepto);
    }
    /*************************************************************************************************************/
    public void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }
/*************************************************************************************************************/

}
