package com.seguros.Cocherias;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.seguros.Actualizacion.Familia;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;
import com.seguros.Datos.DatosBDTablas_tmp;
import com.seguros.MainActivity;
import com.seguros.presupuestos.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class BuscarAsegurado extends AppCompatActivity {
    ProgressBar progressBar;
    TextView nombre, dni, fechanac, edad, beneficiario;
    RecyclerView recyclerlist;
    ImageButton bfamilia;
    ArrayList<Familia> lfamilia = new ArrayList<Familia>();
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 101;

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
    //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
     //                   .setAction("Action", null).show();
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
    /****************************************************************************************/

    public void AccesoEmpresas(){
        if (Datos.Esta_Logueadaemp(getApplicationContext()))
        {

            VerificaEmp verif = new VerificaEmp(BuscarAsegurado.this,progressBar);
            verif.execute();

        }
    }
    /****************************************************************************************/
    public class VerificaEmp extends AsyncTask<String, Integer, Void> {
        Context c;
        boolean errores = false;
        boolean Aplicacion_activa = false;
        public VerificaEmp(Context c, ProgressBar progressBar) {
            this.c = c;
            progressBar.setIndeterminate(true);
            Aplicacion_activa = false;
        }


        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            errores = false;


        }

        @Override
        protected Void doInBackground(String... urls)
        {
            try
            {
                String id = Datos.Obtener_id_login(BuscarAsegurado.this);

                UserFunctions userFunction = new UserFunctions();
                JSONArray arreglo = userFunction.verifemp(id,"3P197792S");

                if (arreglo != null)
                {
                    int cantidad = arreglo.length();
                    int j = 0;
                    if (cantidad > 0) {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
                        String resultado = json.getString("status");
                        if (resultado.equals("1"))
                            Aplicacion_activa = true;

                        int pperfil   = Integer.valueOf(json.getString("perfil"));
                        if (pperfil == 2){
                            String identi = Datos.Obtener_id_login(BuscarAsegurado.this);
                            DatosBDTablas db = new DatosBDTablas(BuscarAsegurado.this);
                            db.open();
                            db.RegistraEmpresa(Integer.valueOf(identi));
                            db.close();
                        }
                    }


                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
                errores = true;
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void unused)
        {
            try
            {

                if (!Aplicacion_activa)
                {
                    mostrar_error(2,"No tiene permisos para acceder a esta opcion..!");
                    finish();
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }

    }

    /*************************************************************************************************************/
    public void mostrar_error(int tipo, String mensaje) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView text     = (TextView) layout.findViewById(R.id.toastText);
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

    private void Buscardatos(String sexo, String dni) {
        boolean permiso = false;/*
         //   <uses-permission android:name="android.permission.READ_PHONE_STATE" />
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE)) {
                    /*********************************************************/
            /*        Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_INTERNET);
                    /*************************************************************/
        /*        } else {

                    Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_INTERNET);

                }
            }
            else
                permiso = true;
        }
        else*/
            permiso = true;
        if (permiso){
            BuscarDCocheria itesmsrutas = new BuscarDCocheria(BuscarAsegurado.this,progressBar,sexo,dni);
            itesmsrutas.execute();

        }

    }

    /*************************************************************************************************************/
    private class BuscarDCocheria extends AsyncTask<String, Integer, Void> {
        Context c;
        boolean errores = false;
        String lsexo, ldni, status, lnombre, ledad, lfechanac, lbeneficiario;
        int cantidad = 0;
        ArrayList<Polizas> MiLista  = new ArrayList<Polizas>();

        public BuscarDCocheria(Context c, ProgressBar progressBar, String sexo, String dni) {
            this.c = c;
            progressBar.setIndeterminate(true);
            this.lsexo = sexo;
            this.ldni  = dni;
            cantidad = 0;
            status = "";
            lnombre = "";
            ledad   = "";
            lfechanac   = "";
            lbeneficiario   = "";


        }


        @Override
        protected void onPreExecute()
        {
            progressBar.setVisibility(View.VISIBLE);
            errores = false;
            Limpiartablas();

        }

        @Override
        protected Void doInBackground(String... urls)
        {
            try
            {
                String identi = Datos.Obtener_id_login(BuscarAsegurado.this);
                UserFunctions userFunction = new UserFunctions();
                JSONArray arreglo = userFunction.BuscarDatosCocheria("3P197792S",lsexo,ldni,identi);

                if (arreglo == null)
                {
                    errores = true;

                }
                else
                {


                    cantidad = arreglo.length();
                    int j = 0;

                    if (cantidad > 0) {
                        JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
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
            return null;

        }

        @Override
        protected void onPostExecute(Void unused)
        {
            try
            {
                if (errores)
                   Toast.makeText(getBaseContext(),"!No es posible consultar los Datos!" , Toast.LENGTH_LONG).show();
                else
                {
                   if (cantidad == 0)
                       Toast.makeText(getBaseContext(),"!No Hay Datos registrados!" , Toast.LENGTH_LONG).show();
                    else
                     {
                         if (status.equals("1")){
                             Toast.makeText(getBaseContext(),"!Datos Identificados! ", Toast.LENGTH_LONG).show();
                             nombre.setText(lnombre);
                             dni.setText(ldni);
                             beneficiario.setText(lbeneficiario);
                             fechanac.setText(lfechanac);
                             edad.setText(ledad);
                             RecyclerAdapterNovedades adapter=new RecyclerAdapterNovedades(BuscarAsegurado.this,MiLista);
                             recyclerlist.setAdapter(adapter);

                         }
                         else
                             Toast.makeText(getBaseContext(),"Problemas al identificar : " + lnombre , Toast.LENGTH_LONG).show();



                     }
                }
                //   Toast.makeText(c, "Los datos han sido Actualizados con Exito!, " + Vecdatos.size() + " filas afectadas en la actualizacion!", Toast.LENGTH_LONG).show() ;
            
                progressBar.setVisibility(View.GONE);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);

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
