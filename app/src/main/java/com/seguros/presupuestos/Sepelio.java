//******************************************************************************************************************************* 
package com.seguros.presupuestos;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.seguros.Actualizacion.MenuActual;
import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;
import com.seguros.pdf.GenerarPDFActivity;
import com.seguros.pdf.GenerarPDFActivitySep;

public class Sepelio extends FragmentActivity  {
Spinner planes, btarifa;
ProgressBar progressBar;
AdaptadorDatos adaptador; 
ListView lstOpciones;
ArrayList<Integrantes> Vecdatos;
CheckBox chk_parcela, chk_luto, gf;
TextView total_Prima, tot_parcela, tot_luto, tot_sepelio;
ImageButton bayuda,bcompartir;
Button bagregar;
int posicionseleccionado;
int codigoseleccionado;
	int ttarifa;
	ArrayAdapter<String> spinnertarifas ;
//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sep);
		btarifa        = (Spinner)findViewById(R.id.btarifa);
        planes        = (Spinner)findViewById(R.id.bplanes);
        progressBar   = (ProgressBar)findViewById(R.id.progressBar1);
        lstOpciones   = (ListView)findViewById(R.id.listView1);
        chk_parcela   = (CheckBox)findViewById(R.id.chk_parcela);
        chk_luto      = (CheckBox)findViewById(R.id.chk_luto);
        total_Prima   = (TextView)findViewById(R.id.totalPrima);
        tot_sepelio   = (TextView)findViewById(R.id.tot_sepelio);
        tot_parcela   = (TextView)findViewById(R.id.tot_parcela);
        tot_luto      = (TextView)findViewById(R.id.tot_luto);
		bagregar      = (Button)findViewById(R.id.bagregar);
		bayuda        = (ImageButton)findViewById(R.id.bayuda);
		gf            = (CheckBox)findViewById(R.id.gf);
		bcompartir     = (ImageButton)findViewById(R.id.bcompartir);


		spinnertarifas = new ArrayAdapter<String>(Sepelio.this,   android.R.layout.simple_spinner_item, Datos.Obtener_Tarifas(Sepelio.this));
		spinnertarifas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
		btarifa.setAdapter(spinnertarifas);

		btarifa.setSelection(0);

		tot_sepelio.setText("0.00");
        tot_parcela.setText("0.00");
        tot_luto.setText("0.00");
        
        
        planes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
	        		calcular_prima(ttarifa);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});  
        
        chk_parcela.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        calcular_prima(ttarifa);
			}
		});
        
        chk_luto.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        calcular_prima(ttarifa);
			}
		});

		ttarifa = 3;
		btarifa.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				ttarifa = 3;
				String t = btarifa.getSelectedItem().toString();
				ttarifa = Integer.valueOf(t);
		//		if (btarifa.getSelectedItemPosition()==1)
		//			ttarifa = 29;
				calcular_prima(ttarifa);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});


		CargarDatos itesmsrutas = new CargarDatos(Sepelio.this);
		itesmsrutas.execute();

		lstOpciones.setOnItemLongClickListener(new OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
            	codigoseleccionado   = Vecdatos.get(pos).getId();
     		    posicionseleccionado = pos;
            	preguntar_opciones();
                return true;
            }
        }); 		
		
		DatosBDTablas db = new DatosBDTablas(getApplicationContext());
		db.open(); 		
		if (!db.EstActivo(2))
		{
		    bagregar.setEnabled(false);
		    chk_parcela.setClickable(false);
		    chk_luto.setClickable(false);
		    planes.setEnabled(false);
		    mostrar_error(2,"Debe Actualizar la Listas de Precios!" );
		}
		db.close();

		bayuda.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
/*				Intent i = new Intent(Sepelio.this,ActivityAyuda.class);
				i.putExtra("ayuda", "1");
				startActivity(i);*/

				Intent i = new Intent(Sepelio.this,Adicionales.class);
				i.putExtra("ayuda", "2");
				startActivity(i);
			}
		});

		gf.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				calcular_prima(ttarifa);
			}
		});
		bcompartir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GeneraPDF();
			}
		});

		if (!Librerias.Esta_act_vida(Sepelio.this) || !Librerias.Esta_act_sep(Sepelio.this))
		{
			ActualizarPreios();
		}
}


	//*******************************************************************************************************************************
	public void GeneraPDF(){
		Intent i = new Intent(this,GenerarPDFActivitySep.class);
		String t = btarifa.getSelectedItem().toString();

		String p = "A";
		if (planes.getSelectedItemPosition()==1)
			p = "B";
		if (planes.getSelectedItemPosition()==2)
			p = "C";
		if (planes.getSelectedItemPosition()==3)
			p = "G";


		i.putExtra("tarifa" , t);
		i.putExtra("plan"   , p);
		i.putExtra("gf"     , gf.isChecked());
		i.putExtra("luto"   , chk_luto.isChecked());
		i.putExtra("parcela", chk_parcela.isChecked());
		i.putExtra("total"  , total_Prima.getText().toString());
		startActivity(i);
	}
	//*******************************************************************************************************************************
	private void ActualizarPreios() {
		android.support.v7.app.AlertDialog.Builder MENSAJE = new android.support.v7.app.AlertDialog.Builder(Sepelio.this);

		MENSAJE.setMessage("Es necesario Actualizar las Lista de Precios¿Desea continuar?")
				.setTitle("Hay Listas de Precios Nuevas!")
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

								if (Datos.Esta_Logueada(Sepelio.this))
								{
									Intent i = new Intent(Sepelio.this,MenuActual.class);
									startActivity(i);
								}
							}
						});
		android.support.v7.app.AlertDialog alert = MENSAJE.create();
		alert.show();
	}


//*******************************************************************************************************************************

    private void preguntar_opciones() {
    	final String[] items = {"Editar", "Eliminar", "Cancelar"};
    	 AlertDialog.Builder builder =
    	 new AlertDialog.Builder(Sepelio.this);			  
    	 builder.setTitle("Integrante seleccionado: ");
    	 builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
    	@Override
    		 public void onClick(DialogInterface dialog, int item) 
    		 {
    		     if (item == 0) // editar
    		       {      
    	    		    Intent i = new Intent(Sepelio.this,NuevoSepelio.class);

    	    		    i.putExtra("estado", 2);
    	    		    i.putExtra("edad", Vecdatos.get(posicionseleccionado).getEdad());
    	    		    startActivityForResult(i,30);
    		    	 
    		       }
    		     if (item == 1) // eliminar
  		           {      
    		    	 
    		  		DatosBDTablas db = new DatosBDTablas(getApplicationContext());
    		 		db.open(); 
    				db.borrarTmpPlan(ttarifa,codigoseleccionado);
    				db.close();
  	    		    calcular_prima(ttarifa);
  		           }
    		       dialog.dismiss();
    		  }
    	     });
    	 
    		AlertDialog alert = builder.create();
    		alert.show();

    	}
    	/*******************************************************************************************************/
    
    
    protected void calcular_prima(int tarifa) {
    	String plan = "A";
		if (planes.getSelectedItemPosition()==1)
			plan = "B";
    	if (planes.getSelectedItemPosition()==2)
    		plan = "C";
		if (planes.getSelectedItemPosition()==3)
			plan = "G";
   	    Vecdatos = Datos.getItemsTmp(tarifa, Sepelio.this,plan,chk_parcela.isChecked(),chk_luto.isChecked(),gf.isChecked());

	    adaptador = new AdaptadorDatos(Sepelio.this,Vecdatos);
	    lstOpciones.setAdapter(adaptador);
	    adaptador.notifyDataSetChanged(); 
    	if (Vecdatos.size()>0)
    	{
    		int pos = lstOpciones.getFirstVisiblePosition();
    	    lstOpciones.setSelection(pos);
    	}
	    totalizar();
		
	}
  //******************************************************************************************************************************* 
    @Override
    public void onSaveInstanceState(Bundle estadoGuardado) {
    	super.onSaveInstanceState(estadoGuardado);
    	estadoGuardado.putBoolean("chk_parcela", chk_parcela.isChecked());
    	estadoGuardado.putBoolean("chk_luto", chk_luto.isChecked());
        estadoGuardado.putInt("planes",planes.getSelectedItemPosition());
        
    }
 /*******************************************************************************************************/
    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
           super.onRestoreInstanceState(estadoGuardado);
           if (estadoGuardado != null) {
        	   chk_parcela.setChecked(estadoGuardado.getBoolean("chk_parcela"));
        	   chk_luto.setChecked(estadoGuardado.getBoolean("chk_luto"));
      		   planes.setSelection(estadoGuardado.getInt("planes"));   
           }
           
    }
 /*******************************************************************************************************/
    public void totalizar(){
    	float total = 0;
    	float totsepelio = 0;    	
    	float totparcela = 0;
    	float totluto    = 0;

    	for (int i = 0; i < Vecdatos.size(); i++) 
    	{
   		total      += Vecdatos.get(i).getPremio();
   		totsepelio += Vecdatos.get(i).getSepelio();
   		totparcela += Vecdatos.get(i).getParcela();
   		totluto    += Vecdatos.get(i).getLuto();
    	}
    	total_Prima.setText(String.format("%.02f", total));
    	tot_sepelio.setText(String.format("%.02f", totsepelio));
    	tot_parcela.setText(String.format("%.02f", totparcela));
    	tot_luto.setText(String.format("%.02f", totluto));
    }
    /**********************************************************************************/
	public void nuevosepelio(View v){
//	    Intent i = new Intent(Sepelio.this,NuevoSepelio.class);
//		i.putExtra("estado", 1);

		String p = "A";
		if (planes.getSelectedItemPosition()==1)
			p = "B";
		if (planes.getSelectedItemPosition()==2)
			p = "C";
		if (planes.getSelectedItemPosition()==3)
			p = "G";
		String t = btarifa.getSelectedItem().toString();


        Intent i = new Intent(Sepelio.this,SepelioCalc.class);

        boolean parce = false;
        if (chk_parcela.isChecked())
            parce = true;

        boolean lut = false;
        if (chk_luto.isChecked())
            lut = true;

        boolean gfb = false;
        if (gf.isChecked())
            gfb = true;

        i.putExtra("tarifa", t);
		i.putExtra("plan", p);
		i.putExtra("gf", gfb);
		i.putExtra("parcela", parce);
		i.putExtra("luto", lut);


	    startActivityForResult(i,77);
	  
  }    
   /************************************************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
		/*****************************************************************************************************/
        if (resultCode == 20) 
           {

        	int    edad  = Integer.valueOf(data.getExtras().getString("edad"));
 
        	String plan = "A";

			   if (planes.getSelectedItemPosition()==1)
				   plan = "B";
			   if (planes.getSelectedItemPosition()==2)
				   plan = "C";
			   if (planes.getSelectedItemPosition()==3)
				   plan = "G";

    		DatosBDTablas db = new DatosBDTablas(getApplicationContext());
    		db.open(); 
    		float [] valores = Datos.calcular_prima_sep(ttarifa,db, edad, plan);
    		float sepelio = valores[0];
    		float parcela = valores[1];
    		float luto    = valores[2];
    		float prima   = valores[3];
    		int contador = (int) db.ultimo_tmp_Plan(ttarifa);
			db.AgregarTmpPlanes(ttarifa,contador, "00/00/0000", edad, luto, parcela, sepelio, prima);
        	db.close();
        	calcular_prima(ttarifa);
           }
/*****************************************************************************************************/
        if (resultCode == 30) 
        {
	     	String fecha = data.getExtras().getString("fecha");
	     	int    edad  = Integer.valueOf(data.getExtras().getString("edad"));
	
	     	String plan = "A";
			if (planes.getSelectedItemPosition()==1)
				plan = "B";
			if (planes.getSelectedItemPosition()==2)
				plan = "C";
			if (planes.getSelectedItemPosition()==3)
				plan = "G";
	
	
	 		DatosBDTablas db = new DatosBDTablas(getApplicationContext());
	 		db.open(); 
	 		float [] valores = Datos.calcular_prima_sep(ttarifa,db, edad, plan);
	 		float sepelio = valores[0];
	 		float parcela = valores[1];
	 		float luto    = valores[2];
	 		float prima   = valores[3];
	
	 		db.actualizarTmpPlan(ttarifa,codigoseleccionado, fecha, edad, luto, parcela, sepelio, prima);
	 		db.close();
	     	calcular_prima(ttarifa);
        }
/*****************************************************************************************************/
		if (resultCode == 77)
		{

			int edad = 0;
			try
			{
			edad  = Integer.valueOf(data.getExtras().getString("edad"));
			}
			catch (Exception e)
			{
				e.printStackTrace();

			}
			String plan  = data.getExtras().getString("plan");

     		if (plan.equals("A"))
	  		    planes.setSelection(0);
			if (plan.equals("B"))
				planes.setSelection(1);
			if (plan.equals("C"))
				planes.setSelection(2);
			if (plan.equals("G"))
				planes.setSelection(3);

			String tarifa  = data.getExtras().getString("tarifa");
			btarifa.setSelection(0);
			if (tarifa.equals("29"))
				btarifa.setSelection(1);

			String t = btarifa.getSelectedItem().toString();
			ttarifa = Integer.valueOf(t);

			gf.setChecked(data.getExtras().getBoolean("gf"));
			chk_parcela.setChecked(data.getExtras().getBoolean("parcela"));
			chk_luto.setChecked(data.getExtras().getBoolean("luto"));

			DatosBDTablas db = new DatosBDTablas(getApplicationContext());
			db.open();
			float [] valores = Datos.calcular_prima_sep(ttarifa,db, edad, plan);
			float sepelio = valores[0];
			float parcela = valores[1];
			float luto    = valores[2];
			float prima   = valores[3];
			int contador = (int) db.ultimo_tmp_Plan(ttarifa);
			db.AgregarTmpPlanes(ttarifa,contador, "00/00/0000", edad, luto, parcela, sepelio, prima);
			db.close();
			calcular_prima(ttarifa);
		}
    }   
    
/**************************************************************************************/        
    private class CargarDatos extends AsyncTask<String, Integer, Void> {
   	 Context c; 
   	 boolean errores = false;
   	 
   	public CargarDatos(Context c) {
   		this.c = c;
   		progressBar.setIndeterminate(true);
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
    	String plan = "A";
		   if (planes.getSelectedItemPosition()==1)
			   plan = "B";
		   if (planes.getSelectedItemPosition()==2)
			   plan = "C";
		   if (planes.getSelectedItemPosition()==3)
			   plan = "G";
   	    Vecdatos = Datos.getItemsTmp(ttarifa,Sepelio.this,plan,chk_parcela.isChecked(),chk_luto.isChecked(),gf.isChecked());
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
   	     if (!errores)
   	     {
   			 adaptador = new AdaptadorDatos((Activity) c,Vecdatos);
   		     lstOpciones.setAdapter(adaptador);
   		     totalizar();
   	    }
   	        
         
          } 
   	    catch (Exception e) 
          {
            e.printStackTrace();
          }
   		progressBar.setVisibility(View.GONE);
      }		

   }
//******************************************************************************************************************************* 
 
    class AdaptadorDatos extends ArrayAdapter {
    	
    	Activity context;
    	ArrayList<Integrantes> Vecdatos;
    	AdaptadorDatos(Activity context,ArrayList<Integrantes> Vecdatos) {
    		super(context, R.layout.lista_integrant, Vecdatos);
    		this.context  = context;
    		this.Vecdatos = Vecdatos;
    	}
    	
    	
    //________________________________________________________________      	
        @Override
    	public View getView(int position, View convertView, ViewGroup parent) 
    	{
    		View item = convertView;
    		final ViewHolder holder;
    		if(item == null)
    		{
    			LayoutInflater inflater = context.getLayoutInflater();
    			item = inflater.inflate(R.layout.lista_integrant, null);
    			
    			holder = new ViewHolder();
        		holder.totprima = (TextView)item.findViewById(R.id.totprima);
        		holder.luto     = (TextView)item.findViewById(R.id.luto);
        		holder.parcela  = (TextView)item.findViewById(R.id.parcela);
        		holder.sepelio  = (TextView)item.findViewById(R.id.sepelio);
        		holder.edad     = (TextView)item.findViewById(R.id.edad);

    			item.setTag(holder);
    		}
    		else
    		{
    			holder = (ViewHolder)item.getTag();
    		}

    		holder.edad.setText(String.valueOf(Vecdatos.get(position).getEdad()));
    		holder.luto.setText(String.format("%.02f", Vecdatos.get(position).getLuto()));
    		holder.parcela.setText(String.format("%.02f", Vecdatos.get(position).getParcela()));
    		holder.sepelio.setText(String.format("%.02f", Vecdatos.get(position).getSepelio()));
    		holder.totprima.setText(String.format("%.02f", Vecdatos.get(position).getPremio()));
    		
    		

    		
      	    return(item);
    	}

           
        
    }
    //________________________________________________________________        

    class ViewHolder {
    	TextView totprima;
    	TextView luto;
    	TextView parcela;
    	TextView sepelio;
    	TextView edad;

    	
    	
    }   
    
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
    
}
