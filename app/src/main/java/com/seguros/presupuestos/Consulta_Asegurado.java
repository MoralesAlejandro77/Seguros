package com.seguros.presupuestos;

import android.content.Context;
import android.content.Intent;
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

public class Consulta_Asegurado extends AppCompatActivity {
ProgressBar progressBar, progressBar2;
TextView tipodoc, nro_doc, nombrecompleto;
TextView companias, provincias;
TextView  caract, celular, caractp, telefono;
TextView  departamento, localidad, cp, calle,  piso, depto;
TextView cobros, cbu, banco, fechareg, version_app, version_android,email;
Spinner activo;

Button bgrabar;

String nombre, stipodoc, stipodoc2, fechanac, tel_car_1, tel_numero_1, tel_car_2, tel_numero_2, compania_1, semail, provincia, sdepartamento, slocalidad, scp, scalle, spiso, sdepto, scobros, scbu, sbanco;
String vtipodoc, vdni, sfehareg, sversion_app, sversion_android, sestado, sapellido, snombre;

int Aplicacion_activa;
int resultado = 0;
String usuario, estado, version_and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cons_aseg);
        progressBar     = (ProgressBar)findViewById(R.id.progressbar3);
        progressBar2    = (ProgressBar)findViewById(R.id.progressbar4);
        tipodoc         = (TextView)findViewById(R.id.tipodoc);
        nro_doc         = (TextView) findViewById(R.id.nro_doc);
        nombrecompleto  = (TextView) findViewById(R.id.nombrecompleto);
        caract          = (TextView) findViewById(R.id.caract);
        celular         = (TextView) findViewById(R.id.celular);
        caractp         = (TextView) findViewById(R.id.caractp);
        telefono        = (TextView) findViewById(R.id.telefono);
        companias       = (TextView)  findViewById(R.id.companias);
        provincias      = (TextView)  findViewById(R.id.provincia);

        departamento    = (TextView) findViewById(R.id.departamento);
        localidad       = (TextView) findViewById(R.id.localidad);
        cp              = (TextView) findViewById(R.id.cp);

        calle           = (TextView) findViewById(R.id.calle);
        piso            = (TextView) findViewById(R.id.piso);
        depto           = (TextView) findViewById(R.id.depto);
        bgrabar         = (Button)   findViewById(R.id.bgrabar);

        cbu             = (TextView) findViewById(R.id.cbu);
        banco           = (TextView) findViewById(R.id.banco);
        cobros          = (TextView)  findViewById(R.id.cobro);

        fechareg        = (TextView)  findViewById(R.id.fechareg);
        version_app     = (TextView)  findViewById(R.id.version_app);
        version_android = (TextView)  findViewById(R.id.version_android);
        email           = (TextView)  findViewById(R.id.email);
        activo          = (Spinner)  findViewById(R.id.estado);

        activo.setSelection(0);

        vtipodoc = "0";
        vdni     = "0";

        try {
            vtipodoc = this.getIntent().getExtras().getString("tipodoc");
            vdni     = this.getIntent().getExtras().getString("dni");

        } catch (Exception e) {
        }



        progressBar.setVisibility(View.GONE);
        progressBar2.setVisibility(View.GONE);
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

    StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL25,
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
            params.put("tipodoc"       , vtipodoc);
            params.put("dni"           , vdni);
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
            stipodoc2            = json.getString("tipodoc2");
            fechanac             = json.getString("fechanac");
            tel_car_1            = json.getString("tel_car_1");
            tel_numero_1         = json.getString("tel_numero_1");
            tel_car_2            = json.getString("tel_car_2");
            tel_numero_2         = json.getString("tel_numero_2");
            compania_1           = json.getString("compania_1");
            semail               = json.getString("email");
            provincia            = json.getString("provincia");
            sdepartamento        = json.getString("departamento");
            slocalidad           = json.getString("localidad");
            scp                  = json.getString("cp");
            scalle               = json.getString("calle");
            spiso                = json.getString("piso");
            sdepto               = json.getString("depto");
            scobros              = json.getString("dias");
            scbu                 = json.getString("cbu");
            sbanco               = json.getString("banco");
            sapellido            = json.getString("apellido");
            snombre              = json.getString("nombres");

            sfehareg              = json.getString("fecha_o") + " " + json.getString("hora_o");
            sversion_app          = json.getString("version_app");
            sversion_android      = json.getString("version_android");
            sestado               = json.getString("estado");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    MostrarDatos(getApplicationContext());
                }
                else
                {
                    Librerias.mostrar_error(Consulta_Asegurado.this,2,"SE HA PRODUCIDO UN ERROR AL LEER DATOS PREVIAMENTE CARGADOS  " );
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL26,
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
        params.put("tipodoc"       , stipodoc2);
        params.put("dni"           , vdni);
        params.put("email"         , email.getText().toString());
        params.put("estado"        , activo.getSelectedItem().toString());
        params.put("apellido"      , sapellido);
        params.put("nombre"        , snombre);
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
                    Librerias.mostrar_error(Consulta_Asegurado.this,1, "La actualizacion de datos fue realizada Exitosamente!!!!");
                    Intent resultData = new Intent();
                    resultData.putExtra("listo", "ok");
                    setResult(200,resultData);
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Consulta_Asegurado.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
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
        nro_doc.setText(vdni);
        nombrecompleto.setText(nombre);
        caract.setText(tel_car_1);
        celular.setText(tel_numero_1);
        caractp.setText(tel_car_2);
        telefono.setText(tel_numero_2);
        companias.setText(compania_1);

        if (!provincia.isEmpty() && !provincia.equals(""))
            provincias.setText(provincia);

        departamento.setText(sdepartamento);
        localidad.setText(slocalidad);
        cp.setText(scp);
        calle.setText(scalle);
        piso.setText(spiso);
        depto.setText(sdepto);
        cbu.setText(scbu);
        banco.setText(sbanco);
        cobros.setText(scobros);
        fechareg.setText(sfehareg);
        version_app.setText(sversion_app);
        version_android.setText(sversion_android);
        email.setText(semail);
        activo.setSelection(0);

        try {
            if (Integer.valueOf(sestado)==0)
                activo.setSelection(1);
        }
        catch (Exception e)
        {
            activo.setSelection(2);
        }

        fechareg.setText(sfehareg);
        version_app.setText(sversion_app);
        version_android.setText(sversion_android);


    }
    /*************************************************************************************************************/

}
