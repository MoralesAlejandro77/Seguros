package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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

public class Registro_Asegurado extends AppCompatActivity {
ProgressBar progressBar;
Spinner tipo_doc, companias;
EditText nro_doc, password1, password2, apellido, nombres, fechanac, caract, celular, correo1, correo2;
Button bgrabar;
TextView link;
ImageView botreg;
RadioButton sexo_masc, sexo_fem;
int Aplicacion_activa;
int resultado = 0;
String usuario, estado;
String version_and;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_reg_aseg);
        progressBar   = (ProgressBar)findViewById(R.id.progressbar3);
        tipo_doc      = (Spinner)findViewById(R.id.tipo_doc);
        companias     = (Spinner)findViewById(R.id.companias);
        nro_doc       = (EditText) findViewById(R.id.nro_doc);
        password1     = (EditText) findViewById(R.id.password1);
        password2     = (EditText) findViewById(R.id.password2);
        apellido      = (EditText) findViewById(R.id.apellido);
        nombres       = (EditText) findViewById(R.id.nombres);
        fechanac      = (EditText) findViewById(R.id.fechanac);
        caract        = (EditText) findViewById(R.id.caract);
        celular       = (EditText) findViewById(R.id.celular);
        correo1       = (EditText) findViewById(R.id.correo1);
        correo2       = (EditText) findViewById(R.id.correo2);
        sexo_masc     = (RadioButton) findViewById(R.id.sexo_masc);
        sexo_fem      = (RadioButton) findViewById(R.id.sexo_fem);
        bgrabar       = (Button) findViewById(R.id.bgrabar);


        this.setTitle("");
        progressBar.setVisibility(View.GONE);
        version_and = Librerias.getAndroidVersion_new(getApplicationContext());

        bgrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistroUser();
            }
        });
        estado    = "";
        resultado = 0;

        fechanac.addTextChangedListener(MaskWatcher.insert("##/##/####", fechanac));

   /*     bfechanac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro_Asegurado.this,Calendario.class);
                i.putExtra("fecha",fechanac.getText().toString());
                startActivityForResult(i,77);
            }
        });*/


    }
//****************************************************************************************

    private void RegistroUser() {
        boolean error;

        error = false;

        correo1       = (EditText) findViewById(R.id.correo1);
        correo2       = (EditText) findViewById(R.id.correo2);
        password1     = (EditText) findViewById(R.id.password1);
        password2     = (EditText) findViewById(R.id.password2);


        if (!Librerias.es_entero_valido(nro_doc.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Numero de Documento!");
            nro_doc.requestFocus();
        }

        if (!error && apellido.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Apellido!");
            apellido.requestFocus();
        }

        if (!error && nombres.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Nombre del Asegurado!");
            nombres.requestFocus();
        }

        if (!error && fechanac.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar la Fecha de Nacimiento!");
            fechanac.requestFocus();
        }

        if (!error && fechanac.length() > 9){

            String valores[] = fechanac.getText().toString().split("/");
            int dia  = Integer.valueOf(valores[0]);
            int mes  = Integer.valueOf(valores[1]);
            int ano  = Integer.valueOf(valores[2]);

            if (!error && ((dia == 0) || (dia > 31)))
            {
                error = true;
                Librerias.mostrar_error(Registro_Asegurado.this,2,"Fecha de Nacimiento, dia ERRONEO!");
                fechanac.requestFocus();
            }

            if (!error && ((mes == 0) || (mes > 12)))
            {
                error = true;
                Librerias.mostrar_error(Registro_Asegurado.this,2,"Fecha de Nacimiento, mes ERRONEO!");
                fechanac.requestFocus();
            }

            if (!error && ((ano == 0) || (ano > 2030)))
            {
                error = true;
                Librerias.mostrar_error(Registro_Asegurado.this,2,"Fecha de Nacimiento, Año ERRONEO!");
                fechanac.requestFocus();
            }


        }
        if (!error && fechanac.length() < 9){
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Formato ERRONEO de Fecha de Nacimiento (dd/mm/aaaa)!");
            fechanac.requestFocus();
        }

        if (!error && !Librerias.es_entero_valido(caract.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el caracter telefonico!");
            caract.requestFocus();
        }
        if (!error && !Librerias.es_entero_valido(celular.getText().toString().trim()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Numero de Celular!");
            celular.requestFocus();
        }

        if (!error && correo1.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Correo Electronico!");
            correo1.requestFocus();
        }

        if (!error && !Librerias.isEmailValid(correo1.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"El formato del Correo Electronico indicado es INVALIDO!");
            correo1.requestFocus();
        }
        
                
        if (!error && correo2.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Correo Electronico!");
            correo2.requestFocus();
        }

        if (!error && !Librerias.isEmailValid(correo2.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"El formato del Correo Electronico indicado es INVALIDO!");
            correo2.requestFocus();
        }

        if (!error && password1.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Password para continuar!");
            password1.requestFocus();
        }

        if (!error && password2.getText().toString().isEmpty())
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"Debe ingresar el Password para continuar!");
            password2.requestFocus();
        }

        if (!error && !correo1.getText().toString().equals(correo2.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"ATENCION, No coinciden los correo electronicos indicados!");
            correo1.requestFocus();
        }
        if (!error && !password1.getText().toString().equals(password2.getText().toString()))
        {
            error = true;
            Librerias.mostrar_error(Registro_Asegurado.this,2,"ATENCION, No coinciden las Claves indicados!");
            password1.requestFocus();
        }


        if (!error)  {
            RegisterUser();
        }

    }
    /*************************************************************************************************************/
    private void RegisterUser(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL16,
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

        String sexo = "M";
        if (sexo_fem.isChecked())
            sexo = "F";

        Map<String,String> params = new HashMap<String, String>();

        params.put("tipodoc"     , tipo_doc.getSelectedItem().toString());
        params.put("dni"         , nro_doc.getText().toString());
        params.put("sexo"        , sexo);
        params.put("apellido"    , apellido.getText().toString());
        params.put("nombre"      , nombres.getText().toString());
        params.put("fechanac"    , fechanac.getText().toString());
        params.put("caract"      , caract.getText().toString());
        params.put("celular"     , celular.getText().toString());
        params.put("compania"    , companias.getSelectedItem().toString());
        params.put("clave"       , password1.getText().toString());
        params.put("mail"        , correo1.getText().toString());
        params.put("version"     , version_and);
        params.put("tag"         ,"3P197792S");
        params.put("app"         , getString(R.string.version));
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
    Aplicacion_activa = 0;

        try {
            JSONArray jsonObject = new JSONArray(response);
            JSONObject valor     = new JSONObject(jsonObject.get(0).toString());
            Aplicacion_activa    = valor.getInt("status");
            estado               = valor.getString("estado");

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Librerias.Registrar_asegurado_id(getApplication(), (int) tipo_doc.getSelectedItemId(), nro_doc.getText().toString());
                    Intent i = new Intent(getApplicationContext(),MostrarInfo.class);
                    startActivity(i);
                    finish();

                }
                else
                {
                    Librerias.mostrar_error(Registro_Asegurado.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 77)
        {
            fechanac.setText(data.getExtras().getString("fecha"));
        }
    }

/**************************************************************************************/
}
