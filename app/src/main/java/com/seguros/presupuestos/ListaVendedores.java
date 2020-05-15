package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.seguros.Actualizacion.DatosVida;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Cuentas.Cuentas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaVendedores extends AppCompatActivity {
    ProgressBar barra;
    ListView lista;
    ArrayList<Vendedor> arraydir;
    ImageButton bvendedor;
    int Aplicacion_activa = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendedores);
        lista  = (ListView)    findViewById(R.id.listView1);
        bvendedor  = (ImageButton) findViewById(R.id.bvendedor);
        barra  = (ProgressBar) findViewById(R.id.barra);

        barra.setVisibility(View.GONE);
        arraydir = new ArrayList<Vendedor>();

        if (Librerias.verificaConexion(this.getApplicationContext()))
        {
            Lista_de_Vendedores();
        }
        else
            Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();

        bvendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListaVendedores.this,ActivityVendedor.class);
                i.putExtra("accion","1");
                startActivityForResult(i,200);
            }
        });


    }
/****************************************************************************************************************/
    public class AdapterVendedores extends BaseAdapter {

        protected Activity activity;
        protected ArrayList<Vendedor> items;

        public AdapterVendedores(Activity activity, ArrayList<Vendedor> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return Long.parseLong(items.get(position).getId());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Generamos una convertView por motivos de eficiencia
            View v = convertView;

            //Asociamos el layout de la lista que hemos creado
            if(convertView == null){
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.lista_vendedores, null);
            }

            Vendedor dir = items.get(position);
            final TextView nombre = (TextView) v.findViewById(R.id.vnombre);
            nombre.setText(dir.getNombre());
            final TextView id = (TextView) v.findViewById(R.id.vid);
            id.setText(dir.getId());
            final TextView estado = (TextView) v.findViewById(R.id.vestado);
            estado.setText("Activo");
            if (Integer.valueOf(dir.getEstado())==0)
                estado.setText("Bloqueado");
            final TextView vcarga = (TextView) v.findViewById(R.id.vcarga);
            vcarga.setText(dir.getFecha());


            final ImageButton bedit = (ImageButton) v.findViewById(R.id.bedit);
            bedit.setTag(position);
            bedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ListaVendedores.this,ActivityVendedor.class);
                    i.putExtra("accion","2");
                    i.putExtra("id",arraydir.get(Integer.valueOf(bedit.getTag().toString())).getId());
                    i.putExtra("nombre",arraydir.get(Integer.valueOf(bedit.getTag().toString())).getNombre());
                    i.putExtra("estado",arraydir.get(Integer.valueOf(bedit.getTag().toString())).getEstado());
                    i.putExtra("clave",arraydir.get(Integer.valueOf(bedit.getTag().toString())).getClave());
                    i.putExtra("perfil",arraydir.get(Integer.valueOf(bedit.getTag().toString())).getPerfil());
                    startActivityForResult(i,200);

              //      Toast.makeText(ListaVendedores.this, "position " + arraydir.get(Integer.valueOf(bedit.getTag().toString())).getNombre(), Toast.LENGTH_SHORT).show();

                }
            });


            return v;
        }
    }

    /*************************************************************************************************************/
    private void Lista_de_Vendedores(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL10,
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
        barra.setIndeterminate(true);
        barra.setVisibility(View.VISIBLE);
        Aplicacion_activa = 0;
        bvendedor.setEnabled(false);
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {

        String idunico = Librerias.unico_ID(ListaVendedores.this);

        Map<String,String> params = new HashMap<String, String>();

        params.put("id"      , idunico);
        params.put("idunico" , Librerias.getAndroidVersion());
        params.put("tag"     ,"getv");
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
        Aplicacion_activa = 0;

        try {

            arraydir = getItems_ListVendedores(response);

            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    AdapterVendedores adapter = new AdapterVendedores(ListaVendedores.this, arraydir);
                    lista.setAdapter(adapter);
                }
                else
                {
                    Librerias.mostrar_error(ListaVendedores.this,2,"SE HA PRODUCIDO UN ERROR : " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        barra.setVisibility(View.GONE);
        bvendedor.setEnabled(true);

    }
    /*************************************************************************************************************/
    public  ArrayList<Vendedor>  getItems_ListVendedores(String response) {
        ArrayList<Vendedor> MiLista = new ArrayList<Vendedor>();

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

            int cantidad = 0;
            cantidad = arreglo.length();
            int j = 0;

            if (cantidad > 0) {
                Aplicacion_activa = 1;

                do {
                    MiLista.add(new Vendedor());
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        MiLista.get(j).setId(json.getString("Id"));
                        MiLista.get(j).setNombre(json.getString("nombre"));
                        MiLista.get(j).setEstado(json.getString("estado"));
                        MiLista.get(j).setClave(json.getString("clave"));
                        MiLista.get(j).setFecha(json.getString("fecha"));
                        MiLista.get(j).setPerfil(Integer.valueOf(json.getString("perfil")));


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
    public void mostrar_error(int tipo, String mensaje) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView  text     = (TextView) layout.findViewById(R.id.toastText);
        ImageView iconimag = (ImageView) layout.findViewById(R.id.toastImage);
        text.setText(mensaje);
        iconimag.setImageResource(0);

        if (tipo==1) //info
            iconimag.setImageResource(R.drawable.info);

        if (tipo==2) //error
            iconimag.setImageResource(R.drawable.error);

        Toast t = new Toast(getApplicationContext());
        t.setDuration(Toast.LENGTH_LONG);
        t.setView(layout);
        t.show();
    }
/*************************************************************************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {
            Lista_de_Vendedores();
        }
    }
//*******************************************************************
public boolean onCreateOptionsMenu(android.view.Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin, menu);
    return true;
}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_log:
                log_asegurados();
                return true;
            case R.id.mn_mensaje:
                mensajes_fcm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void mensajes_fcm() {
        Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
        i.putExtra("ayuda", "7");
        startActivity(i);

    }

    private void log_asegurados() {
        Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
        i.putExtra("ayuda", "8");
        startActivity(i);

    }

}
