package com.seguros.presupuestos;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.seguros.Cuentas.Cuentas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Modifica_CBU extends AppCompatActivity {
ProgressBar progressBar, progressBar2;
TextView tipodoc, nro_doc, nombrecompleto;
Spinner cobros;
EditText  cbu, banco;
Button bgrabar;

String nombre, stipodoc, scbu, sbanco, scobro;


int Aplicacion_activa;
int resultado = 0;
String usuario, estado,  version_and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mod_cbu);
        progressBar     = (ProgressBar)findViewById(R.id.progressbar3);
        progressBar2    = (ProgressBar)findViewById(R.id.progressbar4);
        tipodoc         = (TextView)findViewById(R.id.tipodoc);
        nro_doc         = (TextView) findViewById(R.id.nro_doc);
        nombrecompleto  = (TextView) findViewById(R.id.nombrecompleto);
        cbu             = (EditText) findViewById(R.id.cbu);
        banco           = (EditText) findViewById(R.id.banco);
        cobros          = (Spinner)  findViewById(R.id.cobro);
        bgrabar         = (Button)   findViewById(R.id.bgrabar);

        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
        cobros.setSelection(0);
        bgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroUser();
            }
        });
        estado    = "";
        resultado = 0;
        this.setTitle("");
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());

        Consultar_datos();

    }
//****************************************************************************************

    private void RegistroUser() {
        boolean error;
        error = false;


        if (banco.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Modifica_CBU.this,2,"Debe ingresar la Entidad Bancaria!");
            banco.requestFocus();
        }

        if (!error && !Librerias.es_entero_valido(cbu.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Modifica_CBU.this,2,"El formato del numero de CBU es incorrecto!(Solo numeros)");
            cbu.requestFocus();
        }

        if  (!error &&(cbu.getText().toString().length() != 22))
        {
            error = true;
            Librerias.mostrar_error(Modifica_CBU.this,2,"El formato del numero de CBU es incorrecto!");
            cbu.requestFocus();
        }

        if (!error) {
            ModificarUSuario();
        }

    }

    /*************************************************************************************************************/
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
            scbu                 = json.getString("cbu");
            sbanco               = json.getString("banco");
            scobro               = json.getString("dias");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    MostrarDatos(getApplicationContext());
                }
                else
                {
                    Librerias.mostrar_error(Modifica_CBU.this,2,"SE HA PRODUCIDO UN ERROR AL LEER DATOS PREVIAMENTE CARGADOS  " );
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL19,
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
        params.put("cbu"           , cbu.getText().toString());
        params.put("banco"         , banco.getText().toString());
        params.put("dias"          , cobros.getSelectedItem().toString());
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
                    Librerias.mostrar_error(Modifica_CBU.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Modifica_CBU.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
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


/*****************************************************************************************************************/
    private void MostrarDatos(Context c) {
        tipodoc.setText(stipodoc);
        nro_doc.setText(Librerias.Leer_dni(getApplicationContext()));
        nombrecompleto.setText(nombre);
        cbu.setText(scbu);
        banco.setText(sbanco);
        int indice = Integer.valueOf(scobro) - 1;
        cobros.setSelection(indice);
    }
//*******************************************************************************************************************************


}
