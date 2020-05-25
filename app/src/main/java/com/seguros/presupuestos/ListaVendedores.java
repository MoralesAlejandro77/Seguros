package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
import com.seguros.Cocherias.BuscarAsegurado;
import com.seguros.Cocherias.Polizas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaVendedores extends AppCompatActivity {
    ProgressBar barra;
    RecyclerView lista;
    ArrayList<Vendedor> arraydir;
    ImageButton bvendedor;
    int Aplicacion_activa = 0;
    RecyclerAdapterNovedades adapter;
    SearchView buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_vendedores);
        lista      = (RecyclerView)  findViewById(R.id.listView1);
        bvendedor  = (ImageButton) findViewById(R.id.bvendedor);
        barra      = (ProgressBar) findViewById(R.id.barra);
        buscar     = (SearchView)  findViewById(R.id.ibuscar);

        barra.setVisibility(View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lista.setLayoutManager(layoutManager);

        arraydir = new ArrayList<Vendedor>();
        arraydir.clear();



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

        buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    adapter.getFilter().filter(newText);


                return false;
            }

        });

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
                    adapter=new RecyclerAdapterNovedades(getApplicationContext(),arraydir);
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
                Librerias.mostrar_error(ListaVendedores.this,2,e.toString() );
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200)
        {
            Lista_de_Vendedores();
        }
    }
//*********************************************************************************
public boolean onCreateOptionsMenu(android.view.Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.admin, menu);
    return true;
}

//*********************************************************************************
    class RecyclerAdapterNovedades extends  RecyclerView.Adapter<RecyclerAdapterNovedades.RecyclerViewHolder> implements Filterable {

        Context context;
        LayoutInflater inflater;
        ArrayList<Vendedor> datos;
    ArrayList<Vendedor> arrayList    = new ArrayList<>();
    ArrayList<Vendedor> originalList = new ArrayList<>();

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Vendedor> filteredList = new ArrayList<>();
            String query = constraint.toString();

            if (query.isEmpty()) {
                filteredList.addAll(originalList);
            }
            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Vendedor item : originalList) {
                    if (item.getNombre().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            arrayList.clear();
            arrayList.addAll((List) filterResults.values);
            adapter.notifyDataSetChanged();
        }

    };

    public RecyclerAdapterNovedades(Context c, ArrayList<Vendedor> v) {
        this.context = c;
        this.datos   = v;
        inflater     = LayoutInflater.from(c);
        this.arrayList = v;
        originalList.addAll(v);

    }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= inflater.inflate(R.layout.lista_vendedores, parent, false);

            RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            //      setAnimation(holder.container, position);


            holder.nombre.setText(datos.get(position).getNombre());
            holder.id.setText(datos.get(position).getId());
            holder.estado.setText("Activo");
            if (Integer.valueOf(datos.get(position).getEstado())==0)
                holder.estado.setText("Bloqueado");
            holder.vcarga.setText(datos.get(position).getFecha());


            holder.bedit.setTag(position);
            holder.bedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ListaVendedores.this,ActivityVendedor.class);
                    i.putExtra("accion","2");
                    i.putExtra("id"    ,datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getId());
                    i.putExtra("nombre",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getNombre());
                    i.putExtra("estado",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getEstado());
                    i.putExtra("clave" ,datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getClave());
                    i.putExtra("perfil",datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getPerfil());
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

    @Override
    public Filter getFilter() {
        return myFilter;
    }



    /**********************************************************************************************/

        public class RecyclerViewHolder extends RecyclerView.ViewHolder{
            FrameLayout container;
            TextView nombre, id, estado, vcarga;
            ImageButton bedit;

            public RecyclerViewHolder(View i) {
                super(i);

                // Generamos una convertView por motivos de eficiencia
                View v = i;

                //Asociamos el layout de la lista que hemos creado
                if(i == null){
                    LayoutInflater inf = (LayoutInflater) ListaVendedores.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inf.inflate(R.layout.lista_vendedores, null);
                }

                nombre    = (TextView) v.findViewById(R.id.vnombre);
                id        = (TextView) v.findViewById(R.id.vid);
                estado    = (TextView) v.findViewById(R.id.vestado);
                vcarga    = (TextView) v.findViewById(R.id.vcarga);
                bedit     = (ImageButton) v.findViewById(R.id.bedit);


            }



        }
/************************************/
    }
/*************************************************************************************************************/

}
