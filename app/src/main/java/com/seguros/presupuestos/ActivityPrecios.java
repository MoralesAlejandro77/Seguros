package com.seguros.presupuestos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityPrecios extends AppCompatActivity {
    RecyclerView lista;
    ArrayList<ClaseListaPrecio> arraydir;
    ProgressBar barra;
    int Aplicacion_activa;
    RecyclerAdapterNovedades adapter;
    Spinner planes, btarifa;
    String xtarifa, xplanes;
    ImageButton bagregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precios);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lista          = (RecyclerView)  findViewById(R.id.lista1);
        barra          = (ProgressBar) findViewById(R.id.barra2);
        btarifa        = (Spinner)findViewById(R.id.btarifa);
        planes         = (Spinner)findViewById(R.id.bplanes);
        bagregar       = (ImageButton) findViewById(R.id.bagregar);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lista.setLayoutManager(layoutManager);

        arraydir = new ArrayList<ClaseListaPrecio>();
        arraydir.clear();

        xplanes = "";
        xtarifa = "0";
        btarifa.setSelection(0);
        planes.setSelection(0);


        if (Librerias.verificaConexion(this.getApplicationContext()))
        {
            Lista_de_Precios();
        }
        else
            Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();


        btarifa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
               xtarifa = btarifa.getSelectedItem().toString();
                if (btarifa.getSelectedItemPosition()==0)
                    xtarifa = "0";

                Lista_de_Precios();

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
                if (planes.getSelectedItemPosition()==1)
                    xplanes = "A";
                if (planes.getSelectedItemPosition()==2)
                    xplanes = "B";
                if (planes.getSelectedItemPosition()==3)
                    xplanes = "C";
                if (planes.getSelectedItemPosition()==4)
                    xplanes = "G";

                Lista_de_Precios();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        bagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityPrecios.this,Activity_AltaPrecios.class);
                startActivityForResult(i,200);

            }
        });

    }
    /*************************************************************************************************************/
    private void Lista_de_Precios(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL28,
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

    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {

        String idunico = Librerias.unico_ID(ActivityPrecios.this);

        Map<String,String> params = new HashMap<String, String>();

        params.put("id"      , idunico);
        params.put("idunico" , Librerias.getAndroidVersion());
        params.put("tag"     ,"getv");
        params.put("plan"    ,xplanes);
        params.put("tarifa"  ,xtarifa);
        params.put("dni"     ,Librerias.Leer_dni(getApplicationContext()));


        Aplicacion_activa = 0;
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {

        try {

      //      arraydir.clear();
            arraydir = getItems_ListPrecios(response);


            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    adapter=new RecyclerAdapterNovedades(getApplicationContext(),arraydir);
                    lista.setAdapter(adapter);

                }
                else
                {
                    Librerias.mostrar_error(ActivityPrecios.this,2,"SE HA PRODUCIDO UN ERROR : " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Librerias.mostrar_error(ActivityPrecios.this,2,e.toString() );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        barra.setVisibility(View.GONE);


    }
    /*************************************************************************************************************/
    public  ArrayList<ClaseListaPrecio>  getItems_ListPrecios(String response) {
        ArrayList<ClaseListaPrecio> MiLista = new ArrayList<ClaseListaPrecio>();
        MiLista.clear();
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
                    MiLista.add(new ClaseListaPrecio());
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        MiLista.get(j).setTarifa(json.getString("tarifa"));
                        MiLista.get(j).setPlan(json.getString("codplan"));
                        MiLista.get(j).setEdadi(json.getString("edadi"));
                        MiLista.get(j).setEdadf(json.getString("edadf"));
                        MiLista.get(j).setImporte_sepelio(json.getString("importe_sepelio"));
                        MiLista.get(j).setImporte_parcela(json.getString("importe_parcela"));
                        MiLista.get(j).setImporte_luto(json.getString("importe_luto"));


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
class RecyclerAdapterNovedades extends  RecyclerView.Adapter<RecyclerAdapterNovedades.RecyclerViewHolder>  {

    Context context;
    LayoutInflater inflater;
    ArrayList<ClaseListaPrecio> datos;
    ArrayList<ClaseListaPrecio> arrayList    = new ArrayList<>();
    ArrayList<ClaseListaPrecio> originalList = new ArrayList<>();



    public RecyclerAdapterNovedades(Context c, ArrayList<ClaseListaPrecio> v) {
        this.context = c;
        this.datos   = v;
        inflater     = LayoutInflater.from(c);
        this.arrayList = v;
        originalList.addAll(v);

    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= inflater.inflate(R.layout.lista_precios, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerAdapterNovedades.RecyclerViewHolder holder, int position) {
        //      setAnimation(holder.container, position);


        holder.tarifa.setText(datos.get(position).getTarifa());
        holder.plan.setText(datos.get(position).getPlan());
        holder.edadi.setText(datos.get(position).getEdadi());
        holder.edadf.setText(datos.get(position).getEdadf());
        holder.sepelio.setText(datos.get(position).getImporte_sepelio());
        holder.parcela.setText(datos.get(position).getImporte_parcela());
        holder.luto.setText(datos.get(position).getImporte_luto());


        holder.bedit.setTag(position);
        holder.bedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityPrecios.this,Activity_modPrecios.class);
                i.putExtra("tarifa"    ,datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getTarifa());
                i.putExtra("plan",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getPlan());
                i.putExtra("edadi",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getEdadi());
                i.putExtra("edadf" ,datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getEdadf());
                i.putExtra("sepelio",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getImporte_sepelio());
                i.putExtra("parcela",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getImporte_parcela());
                i.putExtra("luto",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getImporte_luto());
                startActivityForResult(i,200);

            }
        });



    }
    private void setAnimation(FrameLayout container, int position) {
        Animation animation = AnimationUtils.loadAnimation( this.context, android.R.anim.slide_in_left);
        container.startAnimation(animation);
    }
    @Override
    public int getItemCount() {
        return datos.size();
    }




    /**********************************************************************************************/

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        FrameLayout container;
        TextView tarifa, plan, edadi, edadf, sepelio, parcela, luto;
        ImageButton bedit;

        public RecyclerViewHolder(View i) {
            super(i);

            // Generamos una convertView por motivos de eficiencia
            View v = i;

            //Asociamos el layout de la lista que hemos creado
            if(i == null){
                LayoutInflater inf = (LayoutInflater) ActivityPrecios.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.lista_precios, null);
            }

            tarifa   = (TextView) v.findViewById(R.id.vtarifa);
            plan     = (TextView) v.findViewById(R.id.vplan);
            edadi    = (TextView) v.findViewById(R.id.vedadi);
            edadf    = (TextView) v.findViewById(R.id.vedadf);
            sepelio  = (TextView) v.findViewById(R.id.vsepelio);
            parcela  = (TextView) v.findViewById(R.id.vparcela);
            luto     = (TextView) v.findViewById(R.id.vluto);

            bedit     = (ImageButton) v.findViewById(R.id.bedit);


        }



    }
/************************************/
}
//*******************************************************************************************************************************
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == 200)
    {
        Lista_de_Precios();
    }
}


}
