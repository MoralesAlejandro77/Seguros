package com.seguros.Cocherias;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.seguros.Actualizacion.Familia;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;
import com.seguros.Datos.DatosBDTablas_tmp;
import com.seguros.presupuestos.Librerias;
import com.seguros.presupuestos.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BuscarAsegurado extends AppCompatActivity {
    ProgressBar progressBar;
    TextView nombre, dni, fechanac, edad, beneficiario;
    RecyclerView recyclerlist;
    ImageButton bfamilia;
    int Aplicacion_activa;
    ArrayList<Polizas> MiLista  = new ArrayList<Polizas>();


    ArrayList<Familia> lfamilia = new ArrayList<Familia>();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 101;

    String lsexo, ldni, status, lnombre, ledad, lfechanac, lbeneficiario;
    int cantidad = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_asegurado);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar   = (ProgressBar)findViewById(R.id.progressBar1);
        nombre        = (TextView) findViewById(R.id.nombre);
        dni           = (TextView) findViewById(R.id.nrodoc);
        fechanac      = (TextView) findViewById(R.id.fechanac);
        edad          = (TextView) findViewById(R.id.edad);
        beneficiario  = (TextView) findViewById(R.id.beneficiario);
        recyclerlist  = (RecyclerView) findViewById(R.id.recyclerlist);
        bfamilia      = (ImageButton) findViewById(R.id.bfamilia);

        this.setTitle("");
        Aplicacion_activa = 0;

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerlist.setLayoutManager(layoutManager);

        Limpiartablas();


        progressBar.setVisibility(View.GONE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),BuscaxDni.class);
                startActivityForResult(i, 10);
            }
        });
        bfamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),FamiliaActivity.class);
                startActivity(i);
            }
        });

        AccesoEmpresas();
    }
    /*************************************************************************************************************/
    private void Buscar_Info(){

        preparar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL14,
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
                        bfamilia.setEnabled(true);
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
        bfamilia.setEnabled(false);
    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros() {

        Map<String,String> params = new HashMap<String, String>();
        String id = Datos.Obtener_id_login(BuscarAsegurado.this);
        params.put("id"         , id);
        params.put("tags"         ,"3P197792S");
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar(String response) {
        Aplicacion_activa = 0;
        int pperfil = 0;

        try {
            JSONArray arreglo = new JSONArray(response);

            if (arreglo != null)
            {
                int cantidad = arreglo.length();
                int j = 0;
                if (cantidad > 0) {
                    JSONObject json     = new JSONObject(arreglo.get(0).toString());

                    String resultado = json.getString("status");
                    if (resultado.equals("1"))
                        Aplicacion_activa = 1;

                    pperfil   = Integer.valueOf(json.getString("perfil"));
                }


            }


            try
            {
                if (Aplicacion_activa == 1)
                {
                    if (pperfil == 2){
                        String identi = Datos.Obtener_id_login(BuscarAsegurado.this);
                        DatosBDTablas db = new DatosBDTablas(BuscarAsegurado.this);
                        db.open();
                        db.RegistraEmpresa(Integer.valueOf(identi));
                        db.close();
                    }

                }
                else
                {
                    Librerias.mostrar_error(BuscarAsegurado.this,2,"No tiene permisos para acceder a esta opcion..!!");
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
        bfamilia.setEnabled(true);

    }
    /****************************************************************************************/

    public void AccesoEmpresas(){
        if (Datos.Esta_Logueadaemp(getApplicationContext()))
        {
            Buscar_Info();
         //   VerificaEmp verif = new VerificaEmp(BuscarAsegurado.this,progressBar);
         //   verif.execute();

        }
    }
    /*************************************************************************************************************/
    /**
     * @param sexo
     * @param dni***********************************************************************************************************/
    private void Buscar_Info2(String sexo, String dni){

        preparar2(sexo, dni);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL13,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalizar2(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        bfamilia.setEnabled(true);
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = preparar_Parametros2();
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
    public void preparar2(String sexo, String dni) {
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        Aplicacion_activa = 0;
        bfamilia.setEnabled(false);

        lsexo = sexo;
        ldni  = dni;
        cantidad = 0;
        status = "";
        lnombre = "";
        ledad   = "";
        lfechanac   = "";
        lbeneficiario   = "";

        progressBar.setVisibility(View.VISIBLE);
        Limpiartablas();

    }
    /*************************************************************************************************************/
    public Map<String,String> preparar_Parametros2() {
        String identi = Datos.Obtener_id_login(BuscarAsegurado.this);

        Map<String,String> params = new HashMap<String, String>();
        params.put("id"         , identi);
        params.put("tags"         ,"3P197792S");
        params.put("sexo"         ,lsexo);
        params.put("dni"         ,ldni);
        return params;
    }
    /*************************************************************************************************************/
    public void finalizar2(String response) {
        Aplicacion_activa = 0;
        boolean errores = false;

        try
        {
            JSONArray arreglo = new JSONArray(response);
            if (arreglo == null)
            {
                errores = true;

            }
            else
            {


                cantidad = arreglo.length();
                int j = 0;

                if (cantidad > 0) {
                    JSONObject json     = new JSONObject(arreglo.get(0).toString());
                    status         = json.getString("status");
                    lnombre        = json.getString("nombre");
                    ldni           = json.getString("dni");
                    lbeneficiario  = json.getString("beneficiario");
                    lfechanac      = json.getString("fechanac");
                    ledad          = json.getString("edad");

                    JSONArray arreglo_polizas = new JSONArray(json.getString("polizas").toString());
                    for (int i = 0; i < arreglo_polizas.length(); i++) {

                        MiLista.add(new Polizas());
                        try
                        {
                            JSONObject objet_poliza = new JSONObject(arreglo_polizas.getJSONObject(i).toString());
                            MiLista.get(i).setNropoliza(Integer.valueOf(objet_poliza.getString("nropoliza")));
                            MiLista.get(i).setPlan(objet_poliza.getString("plan"));
                            MiLista.get(i).setTieneg(objet_poliza.getInt("tienegf"));
                            MiLista.get(i).setUltimop(objet_poliza.getString("ultimopago"));
                            MiLista.get(i).setCodserv(objet_poliza.getInt("servicio"));
                            MiLista.get(i).setServicio(objet_poliza.getString("descripcion"));
                            MiLista.get(i).setMotivob(objet_poliza.getString("motivob"));
                            MiLista.get(i).setFechaa(objet_poliza.getString("fechaa"));
                            MiLista.get(i).setFechab(objet_poliza.getString("fechab"));
                            if (objet_poliza.getString("fechab").equals("00/00/0000"))
                                MiLista.get(i).setFechab("");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            break;
                        }

                    }
/****************************************************************************************************/
                    DatosBDTablas_tmp db = new DatosBDTablas_tmp(getApplicationContext());
                    db.open();

                    JSONArray arreglo_flia = new JSONArray(json.getString("familia").toString());
                    for (int i = 0; i < arreglo_flia.length(); i++) {


                        //       lfamilia.add(new Familia());
                        try
                        {
                            JSONObject objet_flia = new JSONObject(arreglo_flia.getJSONObject(i).toString());
                            db.AgregarFamilia(objet_flia.getString("dnimiemb"),"",objet_flia.getString("fechanac"),objet_flia.getString("nombre"));


                            //           lfamilia.get(i).setDnimiemb(objet_flia.getString("dnimiemb"));
                            //           lfamilia.get(i).setDnititular(objet_flia.getString("dnititular"));
                            //           lfamilia.get(i).setNombre(objet_flia.getString("nombre"));
                            //           lfamilia.get(i).setFechanac(objet_flia.getString("fechanac"));

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            break;
                        }

                    }

                    db.close();

                }


            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            errores = true;
        }
        mostrar_datos2(errores);
        progressBar.setVisibility(View.GONE);
        bfamilia.setEnabled(true);

    }
/***********************************************************************/
    private void mostrar_datos2(boolean errores) {
        try
        {
            if (errores)
                Librerias.mostrar_error(BuscarAsegurado.this,2,"!!No Hay Datos registrados!!");

            else
            {
                if (cantidad == 0)
                    Librerias.mostrar_error(BuscarAsegurado.this,2,"!No Hay Datos registrados!");
                else
                {
                    if (status.equals("1")){
                        nombre.setText(lnombre);
                        dni.setText(ldni);
                        beneficiario.setText(lbeneficiario);
                        fechanac.setText(lfechanac);
                        edad.setText(ledad);
                        RecyclerAdapterNovedades adapter=new RecyclerAdapterNovedades(BuscarAsegurado.this,MiLista);
                        recyclerlist.setAdapter(adapter);

                    }
                    else
                        Librerias.mostrar_error(BuscarAsegurado.this,2,"Problemas al identificar : " + lnombre);
                }
            }

            progressBar.setVisibility(View.GONE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void Limpiartablas() {
        DatosBDTablas_tmp db = new DatosBDTablas_tmp(getApplicationContext());
        db.open();
        db.LimpiarTablaflia();
        db.close();
    }

    /*************************************************************************************************************/

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10)
        {
            String sexo = data.getExtras().getString("sexo");
            String dni  = data.getExtras().getString("dni");

            Buscardatos(sexo,dni);
        }

    }
//*******************************************************************************
    private void Buscardatos(String sexo, String dni) {
        boolean permiso = false;

            permiso = true;
        if (permiso){
            Buscar_Info2(sexo,dni);
        }

    }


/*************************************************************************************************************/
class RecyclerAdapterNovedades extends  RecyclerView.Adapter<RecyclerAdapterNovedades.RecyclerViewHolder> {

    Context context;
    ArrayList<Polizas> datos;
    LayoutInflater inflater;


    public RecyclerAdapterNovedades(Context context, ArrayList<Polizas> Vecdatos) {
        this.context=context;
        this.datos    = Vecdatos;
        inflater= LayoutInflater.from(context);

    }
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= inflater.inflate(R.layout.fila_polizas, parent, false);

        RecyclerViewHolder viewHolder=new RecyclerViewHolder(v);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        //      setAnimation(holder.container, position);
        final int pos;
        holder.nropoliza.setText(String.valueOf(datos.get(position).getNropoliza()));
        holder.plan.setText(datos.get(position).getPlan());
        holder.codserv.setText(String.valueOf(datos.get(position).getCodserv()));
        holder.servicio.setText(datos.get(position).getServicio());
        holder.fechaa.setText(datos.get(position).getFechaa());
        holder.fechab.setText(datos.get(position).getFechab());
        holder.motivob.setText(datos.get(position).getMotivob());
        holder.fechap.setText(datos.get(position).getUltimop());





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
        TextView  nropoliza, plan, codserv, servicio, fechaa, fechab, motivob, fechap;
        FrameLayout container;

        public RecyclerViewHolder(View item) {
            super(item);

            nropoliza     = (TextView) item.findViewById(R.id.nropoliza);
            plan          = (TextView) item.findViewById(R.id.plan);
            codserv       = (TextView) item.findViewById(R.id.codserv);
            servicio      = (TextView) item.findViewById(R.id.servicio);
            fechaa        = (TextView) item.findViewById(R.id.fechaa);
            fechab        = (TextView) item.findViewById(R.id.fechab);
            motivob       = (TextView) item.findViewById(R.id.motivob);
            fechap        = (TextView) item.findViewById(R.id.fechap);

        }



    }
/************************************/
}
}
