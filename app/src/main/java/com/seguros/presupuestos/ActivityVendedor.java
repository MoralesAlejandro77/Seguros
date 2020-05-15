//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.seguros.Actualizacion.MenuActual;
import com.seguros.Actualizacion.UserFunctions;
import com.seguros.Cuentas.Cuentas;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityVendedor extends FragmentActivity  {
	EditText pass, id, nombre;
	ImageButton botongrab,botoneli;
	int opcion;
	ProgressBar progressBar;
	int Aplicacion_activa;
	String usuario = "";
	int resultado = 0;
	int accion    = 0;
	CheckBox estado,nvendedor,ncocheria,nadmin;
	ListView listdisp;
	ArrayList<VendedorD> arraydir;
//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendedor);
        id            = (EditText)findViewById(R.id.id);
		nombre        = (EditText)findViewById(R.id.txtnombre);
		estado        = (CheckBox)findViewById(R.id.txtestado);
		nvendedor     = (CheckBox)findViewById(R.id.nvendedor);
		ncocheria     = (CheckBox)findViewById(R.id.ncocheria);
		nadmin        = (CheckBox)findViewById(R.id.nadmin);

        pass          = (EditText)findViewById(R.id.txtpass);
        botongrab     = (ImageButton) findViewById(R.id.bgraba);
		botoneli      = (ImageButton) findViewById(R.id.belimina);
		progressBar   = (ProgressBar)findViewById(R.id.progressBar1);
		listdisp      = (ListView)findViewById(R.id.listdisp);


		progressBar.setVisibility(View.GONE);
		arraydir = new ArrayList<VendedorD>();

		accion = 0;
		try {
			accion = Integer.valueOf(this.getIntent().getExtras().getString("accion"));

		} catch (Exception e) {
			accion = 0;
		}

		if (accion == 2 && Librerias.verificaConexion(this.getApplicationContext()))
		{
            String iduser = this.getIntent().getExtras().getString("id");
            if (iduser.length() > 0)
			   Leer_Dispositivos();
		}
		else
			Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();



        botongrab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Verifica_clave();
					
			}
		});

		botoneli.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EliminarVendedor();


			}
		});


	botoneli.setVisibility(View.VISIBLE);

		if (accion == 1)
        	botoneli.setVisibility(View.INVISIBLE);

		if (accion == 2){
			id.setText(this.getIntent().getExtras().getString("id"));
			nombre.setText(this.getIntent().getExtras().getString("nombre"));
			pass.setText(this.getIntent().getExtras().getString("clave"));
            int perfil = this.getIntent().getExtras().getInt("perfil");

			nvendedor.setChecked(false);
			ncocheria.setChecked(false);
			nadmin.setChecked(false);
			if (perfil == 1)
				nvendedor.setChecked(true);
			if (perfil == 2)
				ncocheria.setChecked(true);

			if (perfil == 4){
				nvendedor.setChecked(true);
				ncocheria.setChecked(true);
			}

			if (perfil == 3)
				nadmin.setChecked(true);
			estado.setChecked(true);

			try
			{
				if (Integer.valueOf(this.getIntent().getExtras().getString("estado"))==0)
					estado.setChecked(false);

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}


		}
	}

	//*******************************************************************************************************************************
	private void EliminarVendedor() {
		AlertDialog.Builder MENSAJE = new AlertDialog.Builder(ActivityVendedor.this);

		MENSAJE.setMessage("¿Esta seguro de Eliminar este Vendedor?")
				.setTitle("Eliminacion de Vendedor!")
				.setCancelable(false)
				.setNegativeButton("Cancelar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						})
				.setPositiveButton("Aceptar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								eliminar();

							}
						});
		AlertDialog alert = MENSAJE.create();
		alert.show();
	}


/*************************************************************/
	private void eliminar() {

		if (Librerias.verificaConexion(ActivityVendedor.this))
		{
			accion = 3;
			Registrar_vendedor();
		}
		else
			Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();

	}


	protected void Verifica_clave() {
	boolean error = false;
	if (id.getText().toString().length()==0)
		{
		mostrar_error(2,"Debe indicar el Id del Usuario!!");
		error = true;
		}

	if (pass.getText().toString().length()==0)
	{
	mostrar_error(2,"La Clave No puede ser nula!");
	error = true;
	}

	if (!error)
	{
		
		if (Librerias.verificaConexion(this.getApplicationContext()))
		{
			Registrar_vendedor();
		}
		else
		    Toast.makeText(getBaseContext(),"No es posible establecer la conexion con el Servidor!" ,Toast.LENGTH_LONG).show();
		
		
		
	}
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
	private void Registrar_vendedor(){

		preparar();
		StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL1,
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
						botongrab.setEnabled(true);
						botoneli.setEnabled(true);

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
		botongrab.setEnabled(false);
		botoneli.setEnabled(false);

	}
	/*************************************************************************************************************/
	public Map<String,String> preparar_Parametros() {
		Cuentas cuentas = new Cuentas();
		cuentas        = Librerias.LeerCuentas(ActivityVendedor.this);
		String idunico = Librerias.unico_ID(ActivityVendedor.this);

		int esta = 1;
		if (!estado.isChecked())
			esta = 0;

		int perfil = 0;
		if (nvendedor.isChecked())
			perfil = 1;

		if (ncocheria.isChecked())
			perfil = 2;


		if ((nvendedor.isChecked())&&(ncocheria.isChecked()))
			perfil = 4;

		if (nadmin.isChecked())
			perfil = 3;


		Map<String,String> params = new HashMap<String, String>();

		int est = esta;
		if (accion == 1)
			est = 1;

		params.put("clave"      , pass.getText().toString());
		params.put("id"         , id.getText().toString());
		params.put("nombre"     , nombre.getText().toString());
		params.put("idunico"    , idunico);
		params.put("cuenta"     , cuentas.getNombre());
		params.put("version"    , getString(R.string.version));
		params.put("tag"        ,"3P197792S");
		params.put("accion"     , String.valueOf(accion));
		params.put("perfil"     , String.valueOf(perfil));
		params.put("estado"     , String.valueOf(est));
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

					mostrar_error(1, "La Operacion ha sido completada correctamente!!!! " );
					Intent resultData = new Intent();
					resultData.putExtra("listo", "ok");
					setResult(200,resultData);
					finish();

				}
				else
				{
					Librerias.mostrar_error(ActivityVendedor.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
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
		botongrab.setEnabled(true);
		botoneli.setEnabled(true);


	}
//*******************************************************************************************************************************
	/*************************************************************************************************************/
	private void Leer_Dispositivos(){

		preparar();
		StringRequest stringRequest = new StringRequest(Request.Method.POST, UserFunctions.loginURL12,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						finalizar_d(response);

					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
						progressBar.setVisibility(View.GONE);
						botongrab.setEnabled(true);
						botoneli.setEnabled(true);

					}
				}){

			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = preparar_Parametros_d();
				return params;
			}

		};

		RequestQueue requestQueue = Volley.newRequestQueue(this);
		requestQueue.add(stringRequest);
	}
	/*************************************************************************************************************/
	public Map<String,String> preparar_Parametros_d() {
		String iduser = this.getIntent().getExtras().getString("id");

		Map<String,String> params = new HashMap<String, String>();
		params.put("tags"      , "getvd");
		params.put("id"         , iduser);
		return params;
	}
	/*************************************************************************************************************/
	public void finalizar_d(String response) {
		Aplicacion_activa = 0;

		try {
			arraydir = getItems_d(response);
			try
			{
				if (Aplicacion_activa == 1) // Exitoso
				{

					AdapterVendedores adapter = new AdapterVendedores(ActivityVendedor.this, arraydir);
					listdisp.setAdapter(adapter);
				}
				else
				{
					Librerias.mostrar_error(ActivityVendedor.this,2,"SE HA PRODUCIDO UN ERROR : " + estado);
				}


			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		progressBar.setVisibility(View.GONE);
		botongrab.setEnabled(true);
		botoneli.setEnabled(true);


	}
//*******************************************************************************************************************************
	public  ArrayList<VendedorD>  getItems_d(String response) {
		ArrayList<VendedorD> MiLista = new ArrayList<VendedorD>();
		Aplicacion_activa = 0;
		JSONArray arreglo = null;
		try {
			arreglo = new JSONArray(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		if (arreglo == null)
		{
			Toast.makeText(getBaseContext(),"No se encontro Informacion de dispositivos instalados!" ,Toast.LENGTH_LONG).show();
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
					MiLista.add(new VendedorD());
					try
					{
						JSONObject json = new JSONObject(arreglo.getJSONObject(j).toString());
						MiLista.get(j).setId(json.getString("Id"));
						MiLista.get(j).setCuenta(json.getString("cuenta"));
						MiLista.get(j).setFechaact(json.getString("fechaact"));
						MiLista.get(j).setVersion(json.getString("version"));
						MiLista.get(j).setVersioninst(json.getString("versioninstalada"));
						MiLista.get(j).setFechau(json.getString("fechamod"));


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

	public class AdapterVendedores extends BaseAdapter {

		protected Activity activity;
		protected ArrayList<VendedorD> items;

		public AdapterVendedores(Activity activity, ArrayList<VendedorD> items) {
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
				v = inf.inflate(R.layout.lista_vendedoresd, null);
			}

			VendedorD dir = items.get(position);
			final TextView cuenta = (TextView) v.findViewById(R.id.dcuenta);
			cuenta.setText(dir.getCuenta());
			final TextView fecha = (TextView) v.findViewById(R.id.dfecha);
			fecha.setText(dir.getFechaact());
			final TextView vandroid = (TextView) v.findViewById(R.id.dvandroid);
			vandroid.setText(dir.getVersion());
			final TextView vapp = (TextView) v.findViewById(R.id.dvapp);
			vapp.setText(dir.getVersioninst());

			final TextView vfechau = (TextView) v.findViewById(R.id.dvfechau);
			vfechau.setText(dir.getFechau());




			return v;
		}
	}
	/*************************************************************************************************************/

}
