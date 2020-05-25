package com.seguros.presupuestos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaAsegurados extends AppCompatActivity {
    ProgressBar barra;
    RecyclerView lista;
    ArrayList<Asegurado> arraydir;

    int Aplicacion_activa = 0;
    RecyclerAdapterNovedades adapter;
    SearchView buscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_asegurados);
        lista      = (RecyclerView)  findViewById(R.id.listView1);
        barra      = (ProgressBar) findViewById(R.id.barra);
        buscar     = (SearchView)  findViewById(R.id.ibuscar);

        barra.setVisibility(View.GONE);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lista.setLayoutManager(layoutManager);

        arraydir = new ArrayList<Asegurado>();
        arraydir.clear();



        if (Librerias.verificaConexion(this.getApplicationContext()))
        {
            Lista_de_Asegurados();
        }
        else
            Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();



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
    private void Lista_de_Asegurados(){

        preparar();
        arraydir.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL24,
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

        Map<String,String> params = new HashMap<String, String>();
        params.put("tag"     ,"3P197792S");
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
        Aplicacion_activa = 0;

        try {

            arraydir = getItems_ListAsegurados(response);


            try
            {
                if (Aplicacion_activa == 1) // Exitoso
                {
                    adapter=new RecyclerAdapterNovedades(getApplicationContext(),arraydir);
                    lista.setAdapter(adapter);

                }
                else
                {
                    Librerias.mostrar_error(ListaAsegurados.this,2,"SE HA PRODUCIDO UN ERROR : " );
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                Librerias.mostrar_error(ListaAsegurados.this,2,e.toString() );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        barra.setVisibility(View.GONE);


    }
    /*************************************************************************************************************/
    public  ArrayList<Asegurado>  getItems_ListAsegurados(String response) {
        ArrayList<Asegurado> MiLista = new ArrayList<Asegurado>();
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
                    MiLista.add(new Asegurado());
                    try
                    {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        MiLista.get(j).setTipodoc(json.getString("tipodoc"));
                        MiLista.get(j).setDni(json.getString("dni"));
                        MiLista.get(j).setApellido(json.getString("apellido"));
                        MiLista.get(j).setNombre(json.getString("nombre"));
                        MiLista.get(j).setActivo(json.getString("activo"));
                        MiLista.get(j).setVersion_android(json.getString("version_android"));
                        MiLista.get(j).setVersion_app(json.getString("version_app"));
                        MiLista.get(j).setTel_car_1(json.getString("tel_car_1"));
                        MiLista.get(j).setTel_car_1(json.getString("tel_car_2"));
                        MiLista.get(j).setTel_numero_1(json.getString("tel_numero_1"));
                        MiLista.get(j).setTel_numero_2(json.getString("tel_numero_2"));
                        MiLista.get(j).setSexo(json.getString("sexo"));
                        MiLista.get(j).setFechanac(json.getString("fechanac"));
                        MiLista.get(j).setCompania_1(json.getString("compania_1"));
                        MiLista.get(j).setCalle(json.getString("calle"));
                        MiLista.get(j).setNro(json.getString("nro"));
                        MiLista.get(j).setPiso(json.getString("piso"));
                        MiLista.get(j).setDepto(json.getString("depto"));
                        MiLista.get(j).setCbu(json.getString("cbu"));
                        MiLista.get(j).setBanco(json.getString("banco"));
                        MiLista.get(j).setDias(json.getString("dias"));
                        MiLista.get(j).setFecha_o(json.getString("fecha_o"));
                        MiLista.get(j).setHora_o(json.getString("hora_o"));
                        MiLista.get(j).setEmail(json.getString("email"));
                        MiLista.get(j).setProvincia(json.getString("provincia"));
                        MiLista.get(j).setDepartamento(json.getString("departamento"));
                        MiLista.get(j).setLocalidad(json.getString("localidad"));
                        MiLista.get(j).setCp(json.getString("cp"));


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
            Lista_de_Asegurados();
        }
    }


//*********************************************************************************
    class RecyclerAdapterNovedades extends  RecyclerView.Adapter<RecyclerAdapterNovedades.RecyclerViewHolder> implements Filterable {

        Context context;
        LayoutInflater inflater;
        ArrayList<Asegurado> datos;
    ArrayList<Asegurado> arrayList    = new ArrayList<>();
    ArrayList<Asegurado> originalList = new ArrayList<>();

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Asegurado> filteredList = new ArrayList<>();
            String query = constraint.toString();

            if (query.isEmpty()) {
                filteredList.addAll(originalList);
            }
            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Asegurado item : originalList) {
                    if (item.getApellido().toLowerCase().contains(filterPattern))
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

    public RecyclerAdapterNovedades(Context c, ArrayList<Asegurado> v) {
        this.context = c;
        this.datos   = v;
        inflater     = LayoutInflater.from(c);
        this.arrayList = v;
        originalList.addAll(v);

    }

        @Override
        public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= inflater.inflate(R.layout.lista_asegurado, parent, false);

            RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
            //      setAnimation(holder.container, position);


            holder.nombre.setText(datos.get(position).getApellido() + ", " + datos.get(position).getNombre());
            holder.id.setText(datos.get(position).getTipodoc() + "  " + datos.get(position).getDni());
            holder.estado.setText("Activo");
            try {
                if (Integer.valueOf(datos.get(position).getActivo())==0)
                    holder.estado.setText("Bloqueado");
            }
            catch (Exception e)
            {
                holder.estado.setText("No Confirmo");
                e.printStackTrace();
            }

            holder.vcarga.setText(datos.get(position).getFecha_o() + " " + datos.get(position).getHora_o());
            holder.vmail.setText(datos.get(position).getEmail());
            holder.vversionandroid.setText(datos.get(position).getVersion_android());
            holder.vversionapp.setText(datos.get(position).getVersion_app());


            holder.bedit.setTag(position);
            holder.bedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ListaAsegurados.this,Consulta_Asegurado.class);
                    i.putExtra("tipodoc" ,datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getTipodoc());
                    i.putExtra("dni"     ,datos.get(Integer.valueOf(holder.bedit.getTag().toString())).getDni());
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
            TextView nombre, id, estado, vcarga, vmail, vversionandroid, vversionapp;
            ImageButton bedit;

            public RecyclerViewHolder(View i) {
                super(i);

                // Generamos una convertView por motivos de eficiencia
                View v = i;

                //Asociamos el layout de la lista que hemos creado
                if(i == null){
                    LayoutInflater inf = (LayoutInflater) ListaAsegurados.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inf.inflate(R.layout.lista_asegurado, null);
                }

                nombre           = (TextView) v.findViewById(R.id.vnombre);
                id               = (TextView) v.findViewById(R.id.vid);
                estado           = (TextView) v.findViewById(R.id.vestado);
                vcarga           = (TextView) v.findViewById(R.id.vcarga);
                vmail            = (TextView) v.findViewById(R.id.vmail);
                vversionandroid  = (TextView) v.findViewById(R.id.vversionandroid);
                vversionapp      = (TextView) v.findViewById(R.id.vversionapp);
                bedit            = (ImageButton) v.findViewById(R.id.bedit);


            }



        }
/************************************/
    }
/*************************************************************************************************************/

}
