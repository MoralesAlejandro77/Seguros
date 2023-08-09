//******************************************************************************************************************************* 
package com.seguros.Actualizacion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;
import com.seguros.presupuestos.Librerias;
import com.seguros.presupuestos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActual extends FragmentActivity {
    ProgressBar progressBar, progressBarv;
    Button boton_vida;
    ArrayList<DatosSepelio> Vecdatos;
    ArrayList<DatosVida> VecdatosVida;
    ArrayList<DatosTablaIndices> VecTablaIndices;
    ArrayList<DatosParametros> VecTablaparam;

    TextView fechaactsep, vigenciasep, vigenciavida, fechaactvida;
    int Aplicacion_activa;

    //*******************************************************************************************************************************
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_act);
        progressBar    = (ProgressBar) findViewById(R.id.progressBar1);
        progressBarv   = (ProgressBar) findViewById(R.id.progressBarv);
        boton_vida     = (Button) findViewById(R.id.button1);
        vigenciasep    = (TextView) findViewById(R.id.vigencia_sep);
        fechaactsep    = (TextView) findViewById(R.id.actualiza_sep);
        vigenciavida   = (TextView) findViewById(R.id.vigencia_vida);
        fechaactvida   = (TextView) findViewById(R.id.actualiza_vida);
        Aplicacion_activa = 0;

        leer_ultima_act_sepelio();
        leer_ultima_act_Vida();


        boton_vida.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (Librerias.verificaConexion(MenuActual.this)) {
                    Actualizar_precios_vida();
                    Actualizar_precios_sepelio();
                    Actualizar_precios_tope();

                } else
                    Toast.makeText(getBaseContext(), "No es posible establecer la conexion con el Servidor!", Toast.LENGTH_LONG).show();


            }
        });

    }
//**********************************************************************************

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Actualizar_precios_vida();
        }
        if (resultCode == 2) {
            Actualizar_precios_sepelio();
        }

    }
//**********************************************************************************

    private void leer_ultima_act_sepelio() {
        vigenciasep = (TextView) findViewById(R.id.vigencia_sep);
        fechaactsep = (TextView) findViewById(R.id.actualiza_sep);

        DatosBDTablas db = new DatosBDTablas(MenuActual.this);
        db.open();
        Cursor datos = db.obtenerControl(2);
        if (datos.getCount() > 0) {
            fechaactsep.setText(datos.getString(1));
            vigenciasep.setText(datos.getString(2));
            db.close();
        }
    }

    //**********************************************************************************
    private void leer_ultima_act_Vida() {
        vigenciavida = (TextView) findViewById(R.id.vigencia_vida);
        fechaactvida = (TextView) findViewById(R.id.actualiza_vida);

        DatosBDTablas db = new DatosBDTablas(MenuActual.this);
        db.open();
        Cursor datos = db.obtenerControl(1);
        if (datos.getCount() > 0) {
            fechaactvida.setText(datos.getString(1));
            vigenciavida.setText(datos.getString(2));
            db.close();
        }
    }

    //**********************************************************************************
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

 //**********************************************************************************
    private void Actualizar_precios_sepelio() {
        if (Librerias.verificaConexion(MenuActual.this)) {
            Obtener_Datos_Sepelio();
        } else
            Toast.makeText(getBaseContext(), "No es posible establecer la conexion con el Servidor!", Toast.LENGTH_LONG).show();

    }
//**********************************************************************************

    private void Actualizar_precios_vida() {
        if (Librerias.verificaConexion(MenuActual.this)) {
            Obtener_Datos_Vida();
        } else
            Toast.makeText(getBaseContext(), "No es posible establecer la conexion con el Servidor!", Toast.LENGTH_LONG).show();

    }
//**********************************************************************************
    private void Actualizar_precios_tope() {
        if (Librerias.verificaConexion(MenuActual.this)) {
            Obtener_Datos_Tope();
        } else
            Toast.makeText(getBaseContext(), "No es posible establecer la conexion con el Servidor!", Toast.LENGTH_LONG).show();

    }
//**********************************************************************************

    private void Obtener_Datos_Sepelio() {

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_sepelio(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        boton_vida.setEnabled(true);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("sepelio", "2");
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

//**********************************************************************************

    private void Obtener_Datos_Tope() {

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL34,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_tope(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        boton_vida.setEnabled(true);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("sepelio", "2");
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
        boton_vida.setEnabled(false);
    }

    /*************************************************************************************************************/
    public Map<String, String> preparar_Parametros(String tag, String producto) {
        String idunico = Librerias.unico_ID(MenuActual.this);
        String id = Datos.Obtener_id_login(MenuActual.this);

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("idunico", idunico);
        params.put("tags", tag);
        params.put("producto", producto);

        return params;
    }

    /*************************************************************************************************************/
    public void finalizar_sepelio(String response) {
        boolean errores = false;
        String producto = "2";
        try {
            Vecdatos = Leer_items_sep(response);

            try {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Actualizar_valores_sepelio(MenuActual.this);
                    leer_ultima_act_sepelio();
                    Toast.makeText(getApplicationContext(), "La Tabla de Sepelio fue Actualizada con Exito!, " + Vecdatos.size() + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show();
                    Librerias.Grabar_act_sep(getApplicationContext());
                    ObtenerTarifas();

                } else {
                    Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : ");
                }


            } catch (Exception e) {
                e.printStackTrace();
                Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    //*******************************************************************************************************************************
    public void finalizar_tope(String response) {
        boolean errores = false;
        String producto = "2";

        try {
            VecTablaparam = Leer_items_tope(response);

            try {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Librerias.Grabar_Tope1(getApplicationContext(),VecTablaparam.get(0).getTope1());
             ;


                } else {
                    Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : ");
                }


            } catch (Exception e) {
                e.printStackTrace();
                Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    //*******************************************************************************************************************************

    public ArrayList<DatosSepelio> Leer_items_sep(String response) {
        ArrayList<DatosSepelio> MiLista = new ArrayList<DatosSepelio>();

        Aplicacion_activa = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (arreglo == null) {
            Toast.makeText(getBaseContext(), "!No es posible consultar los Datos!", Toast.LENGTH_LONG).show();
            MiLista = null;
        } else {

            int cantidad = 0;
            cantidad = arreglo.length();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;

                do {
                    MiLista.add(new DatosSepelio());
                    try {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        MiLista.get(j).setTarifa(Integer.valueOf(json.getString("tarifa")));
                        MiLista.get(j).setPlan(json.getString("codplan"));
                        MiLista.get(j).setServicio(json.getInt("servicio"));
                        MiLista.get(j).setEdadi(json.getInt("edadi"));
                        MiLista.get(j).setEdadf(json.getInt("edadf"));
                        MiLista.get(j).setValor((float) json.getDouble("valor"));
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "error json : " + e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        break;
                    }
                    j++;
                } while (j < cantidad);
            }

        }
        return MiLista;
    }

    /*************************************************************************************************************/
    public ArrayList<DatosParametros> Leer_items_tope(String response) {
        ArrayList<DatosParametros> MiLista = new ArrayList<DatosParametros>();

        Aplicacion_activa = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (arreglo == null) {
            Toast.makeText(getBaseContext(), "!No es posible consultar los Datos!", Toast.LENGTH_LONG).show();
            MiLista = null;
        } else {

            int cantidad = 0;
            cantidad = arreglo.length();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;


                    MiLista.add(new DatosParametros());
                    try {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(0).toString());
                        MiLista.get(0).setTope1(json.getString("tope1"));
                 //       MiLista.get(0).setStatus(json.getString("status"));

                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), "error json : " + e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }

            }

        }
        return MiLista;
    }

    /*************************************************************************************************************/
    private void ObtenerTarifas() {
       preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL11,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_tarifa(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("tarifas", "2");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*************************************************************************************************************/
    public void finalizar_tarifa(String response) {
        try {
            int total = Leer_items_tarifas(response);
            Toast.makeText(getApplicationContext(), "Las tablas de Tarifas fueron Actualizadas con Exito!, " + total + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show();
            ObtenerControl("2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    //*******************************************************************************************************************************
    public int Leer_items_tarifas(String response) {
        Aplicacion_activa = 0;
        int cantidad = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (arreglo == null) {
            Toast.makeText(getBaseContext(), "No es posible consultar los Datos!", Toast.LENGTH_LONG).show();
        } else {

            cantidad = arreglo.length();

            DatosBDTablas db = new DatosBDTablas(MenuActual.this);
            db.open();
            db.EliminarTartifas();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;
                do {
                    try {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        db.AgregarTarifa(Integer.valueOf(json.getString("codigo")), json.getString("leyenda"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    j++;
                } while (j < cantidad);
            }
            db.close();
        }
        return cantidad;
    }

    /*************************************************************************************************************/
    private void ObtenerControl(final String producto) {
        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_Control(response, producto);
                        ObtenerTablaItems();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("Control", producto);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*************************************************************************************************************/
    public void finalizar_Control(String response, String producto) {
        try {
            int total = Leer_items_control(response, producto);
            Toast.makeText(getApplicationContext(), "Las tablas de Control fueron Actualizadas con Exito!, " + total + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show();
            if (producto.equals("2"))
                leer_ultima_act_sepelio();
            if (producto.equals("1"))
                leer_ultima_act_Vida();


        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    //*******************************************************************************************************************************
    public int Leer_items_control(String response, String producto) {
        Aplicacion_activa = 0;
        int cantidad = 0;
        JSONArray arreglo = null;

        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (arreglo == null) {
            Toast.makeText(getBaseContext(), "No es posible consultar los Datos de Control!", Toast.LENGTH_LONG).show();
        } else {

            cantidad = arreglo.length();

            if (cantidad > 0) {
                Aplicacion_activa = 1;
                try {
                    DatosBDTablas db = new DatosBDTablas(MenuActual.this);
                    db.open();
                    JSONObject json = new JSONObject(arreglo.getJSONObject(0).toString());
                    String[] fecha = json.getString("fechaact").split("-");
                    String fechaact = fecha[2] + "/" + fecha[1] + "/" + fecha[0];
                    fecha = json.getString("fechavig").split("-");
                    String fechavig = fecha[2] + "/" + fecha[1] + "/" + fecha[0];

                    db.AgregarControl(1, Integer.valueOf(producto), fechaact, fechavig, 1);
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return cantidad;
    }

    /*************************************************************************************************************/

    public void Actualizar_valores_sepelio(Context c) {
        DatosBDTablas db = new DatosBDTablas(c);
        db.open();
        db.EliminarPlanes();
        String plan = " ";
        for (int i = 0; i < Vecdatos.size(); i++) {
            plan = Vecdatos.get(i).getPlan();
            if (Vecdatos.get(i).getPlan().isEmpty() || Vecdatos.get(i).getPlan() == "")
                plan = " ";
            db.AgregarPlanes(Vecdatos.get(i).getTarifa(), plan, Vecdatos.get(i).getServicio(), Vecdatos.get(i).getEdadi(), Vecdatos.get(i).getEdadf(), Vecdatos.get(i).getValor());
        }
        db.close();

    }

    /************************************************************************************/
    public void Actualizar_valores_Vida(Context c) {
        DatosBDTablas db = new DatosBDTablas(c);
        db.open();
        db.EliminarServicios();
        boolean sololectura = false;
        for (int i = 0; i < VecdatosVida.size(); i++) {
            if (VecdatosVida.get(i).getSololectura() > 0)
                sololectura = true;
            db.AgregarServicio(VecdatosVida.get(i).getId(), VecdatosVida.get(i).getNombre(), VecdatosVida.get(i).getTipo_base(), VecdatosVida.get(i).getPorc(), VecdatosVida.get(i).getValor_base(), VecdatosVida.get(i).getEdad_max(), sololectura, VecdatosVida.get(i).getTipo_calculo(), VecdatosVida.get(i).getEdad_mes());
        }
        db.close();

    }
    /*************************************************************************************************************/
    private void Obtener_Datos_Vida() {

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_vida(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        boton_vida.setEnabled(true);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("vida", "1");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*************************************************************************************************************/
    public void finalizar_vida(String response) {
        boolean errores = false;
        String producto = "1";
        try {
            VecdatosVida = Leer_items_vida(response);

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Actualizar_valores_Vida(MenuActual.this);
                    leer_ultima_act_Vida();
                    Toast.makeText(getApplicationContext(), "La Tabla de Vida ha sido Actualizada con Exito!, " + VecdatosVida.size() + " filas afectadas en la actualizacion! " , Toast.LENGTH_LONG).show() ;
                 //   boton_vida.setText(response);
                    Librerias.Grabar_act_vida(getApplicationContext());
                    ObtenerGastos();
                }
                else
                {
                    Librerias.mostrar_error(MenuActual.this,2,"SE HA PRODUCIDO UN ERROR : ");
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Librerias.mostrar_error(MenuActual.this,2,"SE HA PRODUCIDO UN ERROR : " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Librerias.mostrar_error(MenuActual.this,2,"SE HA PRODUCIDO UN ERROR : " + e.toString());
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }
//*******************************************************************************************************************************
    public ArrayList<DatosVida> Leer_items_vida(String response) {
        ArrayList<DatosVida> MiLista = new ArrayList<DatosVida>();
        Aplicacion_activa = 0;
        int cantidad = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (arreglo == null)
        {
            Toast.makeText(getBaseContext(),"No es posible consultar los Datos!" ,Toast.LENGTH_LONG).show();
            MiLista = null;
        }
        else
        {

            cantidad = arreglo.length();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;
                do {
                    MiLista.add(new DatosVida());
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        MiLista.get(j).setId(json.getInt("Id"));
                        MiLista.get(j).setNombre(json.getString("nombre"));
                        MiLista.get(j).setTipo_base(json.getInt("tipo_base"));
                        MiLista.get(j).setPorc((float)json.getDouble("porc"));
                        MiLista.get(j).setValor_base(json.getInt("valor_base"));
                        MiLista.get(j).setEdad_max(json.getInt("edad_max"));
                        MiLista.get(j).setSololectura(json.getInt("solo_lectura"));
                        MiLista.get(j).setTipo_calculo(json.getInt("tipo_calculo"));
                        MiLista.get(j).setEdad_mes(json.getInt("tipo_mes"));

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    j++;
                } while(j<cantidad);
            }

        }
        return MiLista;
    }

//*******************************************************************************************************************************
private void ObtenerGastos() {

    preparar();
    StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL7,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    finalizar_gastos(response);

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    boton_vida.setEnabled(true);

                }
            }) {

        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = preparar_Parametros("gastos", "1");
            return params;
        }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);
}

/*************************************************************************************************************/
    public void finalizar_gastos(String response) {
        boolean errores = false;
        String producto = "1";
        try {
            int total = Leer_items_gastos(response);

            try {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Toast.makeText(getApplicationContext(), "La Tabla de Gastos fue Actualizada con Exito!, " +total + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show();
                    ObtenerCapital();

                } else {
                    Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : ");
                }


            } catch (Exception e) {
                e.printStackTrace();
                Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    //*******************************************************************************************************************************
    public int Leer_items_gastos(String response) {
        Aplicacion_activa = 0;
        int cantidad = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (arreglo == null)
        {
            Toast.makeText(getBaseContext(),"No es posible consultar los Datos!" ,Toast.LENGTH_LONG).show();
        }
        else
        {
            cantidad = arreglo.length();

            DatosBDTablas db = new DatosBDTablas(MenuActual.this);
            db.open();
            db.EliminarGastos();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;
                do {
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        db.AgregarGastos(j+1, (float)json.getDouble("importe"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    j++;
                } while(j<cantidad);
            }
            db.close();
        }
        return cantidad;
    }

    /*************************************************************************************************************/
    private void ObtenerCapital() {

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL8,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_capital(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        boton_vida.setEnabled(true);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("capital", "1");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*************************************************************************************************************/
    public void finalizar_capital(String response) {
        boolean errores = false;
        String producto = "1";
        try {
            int total = Leer_items_capital(response);

            try {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Toast.makeText(getApplicationContext(), "La Tabla de Capital fue Actualizada con Exito!, " +total + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show();
                    ObtenerControl("1");


                } else {
                    Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : ");
                }


            } catch (Exception e) {
                e.printStackTrace();
                Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    //*******************************************************************************************************************************
    public int Leer_items_capital(String response) {
        Aplicacion_activa = 0;
        int cantidad = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (arreglo == null)
        {
            Toast.makeText(getBaseContext(),"No es posible consultar los Datos!" ,Toast.LENGTH_LONG).show();
        }
        else
        {
            cantidad = arreglo.length();

            DatosBDTablas db = new DatosBDTablas(MenuActual.this);
            db.open();
            db.EliminarCapital();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;
                do {
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        db.AgregarCapital(j+1, (float)json.getDouble("importe"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    j++;
                } while(j<cantidad);
            }
            db.close();
        }
        return cantidad;
    }
    //*******************************************************************************************************************************
    private void ObtenerTablaItems() {

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL27,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar_items(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        boton_vida.setEnabled(true);

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = preparar_Parametros("tablaindices", "1");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*************************************************************************************************************/
    public void finalizar_items(String response) {
        boolean errores = false;
        String producto = "1";
        try {
            VecTablaIndices = Leer_items_Tabla(response);
            Actualizar_items();

            try {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    Toast.makeText(getApplicationContext(), "La Tabla de Indices fue Actualizada con Exito!, " + VecTablaIndices.size()  + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show();

                } else {
                    Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : ");
                }


            } catch (Exception e) {
                e.printStackTrace();
                Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Librerias.mostrar_error(MenuActual.this, 2, "SE HA PRODUCIDO UN ERROR : " + e.toString());
        }
        progressBar.setVisibility(View.GONE);
        boton_vida.setEnabled(true);

    }

    private void Actualizar_items() {
        DatosBDTablas db = new DatosBDTablas(MenuActual.this);
        db.open();

        int codigoint = 0;
        int valor = 0;
        for (int i = 0; i < VecTablaIndices.size(); i++) {
            valor = Integer.valueOf(VecTablaIndices.get(i).getCodigoint());
            if ((codigoint != valor) && (valor > 0))
                db.Borrar_Prima(valor);
            codigoint = valor;
        }


        int codi  = 0;
        int edadi = 0;
        int edadf = 0;
        float importe = 0;

        for (int i = 0; i < VecTablaIndices.size(); i++) {
            codi  = Integer.valueOf(VecTablaIndices.get(i).getCodigoint());
            edadi = Integer.valueOf(VecTablaIndices.get(i).getEdadi());
            edadf = Integer.valueOf(VecTablaIndices.get(i).getEdadf());
            importe = Float.valueOf(VecTablaIndices.get(i).getValor());
            db.AgregarPrima(codi,	edadi,	  edadf	,	 importe  );
        }

        db.close();



    }

    //*******************************************************************************************************************************
    public ArrayList<DatosTablaIndices> Leer_items_Tabla(String response) {
        ArrayList<DatosTablaIndices> MiLista = new ArrayList<DatosTablaIndices>();
        Aplicacion_activa = 0;
        int cantidad = 0;
        JSONArray arreglo = null;
        try {
            arreglo = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (arreglo == null)
        {
            Toast.makeText(getBaseContext(),"No es posible consultar los Datos!" ,Toast.LENGTH_LONG).show();
            MiLista = null;
        }
        else
        {

            cantidad = arreglo.length();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;
                do {
                    MiLista.add(new DatosTablaIndices());
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        MiLista.get(j).setCodigo(json.getString("codigo"));
                        MiLista.get(j).setDescripcion(json.getString("descripcion"));
                        MiLista.get(j).setEdadi(json.getString("edadi"));
                        MiLista.get(j).setEdadf(json.getString("edadf"));
                        MiLista.get(j).setValor(json.getString("valor"));
                        MiLista.get(j).setCodigoint(json.getString("codigoint"));
                        MiLista.get(j).setSexo(json.getString("sexo"));

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    j++;
                } while(j<cantidad);
            }

        }
        return MiLista;
    }

//*******************************************************************************************************************************

/*************************************************************************************************************/

}
