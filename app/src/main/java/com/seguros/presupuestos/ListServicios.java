//******************************************************************************************************************************* 
package com.seguros.presupuestos;
/***********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Fecha 02-12-2014
 * Versin : 1.1
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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

import java.util.ArrayList;

//*******************************************************************************************************************************

public class ListServicios extends FragmentActivity  {
AdaptadorDatos adaptador; 
ListView lstOpciones;
ArrayList<Servicios> Vecdatos;
boolean[] valores_select = new boolean[50];
ProgressBar progressBar;
TextView edad_titular, edad_conyugue, total_Prima, Ssex_tit, Ssex_cony,ley_conyugue;
CheckBox chk_todos;
Button bfechanaccon;
ImageButton bayuda;
Spinner capital;
int codigo;
boolean primero;
float gasto_base     = 0;
int   gasto_base_pos = 0;
ArrayAdapter<String> spinnerArrayAdapter ;
ArrayAdapter<String> spinnerCapital ;
int sexo, sexo_cony;
int edad, edad_cony;
boolean tieneconyugue;
ImageButton botonedicion, bcompartir;
//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vida2);
        lstOpciones    = (ListView)findViewById(R.id.listView1);
        edad_titular   = (TextView)findViewById(R.id.edad_titular);
        edad_conyugue  = (TextView)findViewById(R.id.edad_conyugue);
        Ssex_cony      = (TextView)findViewById(R.id.Ssex_cony);
        Ssex_tit       = (TextView)findViewById(R.id.Ssex_tit);
        ley_conyugue   = (TextView)findViewById(R.id.ley_conyugue);
        chk_todos      = (CheckBox)findViewById(R.id.chk_todos);
        bfechanaccon   = (Button)findViewById(R.id.bfechanaccon);
        capital        = (Spinner)findViewById(R.id.bgastos);
        total_Prima    = (TextView)findViewById(R.id.totalPrima);
        progressBar    = (ProgressBar)findViewById(R.id.progressBar1);
		botonedicion   = (ImageButton)findViewById(R.id.imageButton1);
		bcompartir     = (ImageButton)findViewById(R.id.bcompartir);
		bayuda         = (ImageButton)findViewById(R.id.bayuda);
        
        for (int i = 0; i < 50; i++) {
        	if (i==0)
            	valores_select[i] = true;
        	else
        	    valores_select[i] = false;
		}
    	sexo      = 0;
    	sexo_cony = 0;
    	edad      = 0;
    	edad_cony = 0;
    	gasto_base = 10000;
    	gasto_base_pos = 0;
		tieneconyugue = false;

    	Ssex_cony.setText("");
    	Ssex_tit.setText("");
    	ley_conyugue.setEnabled(false);

		spinnerArrayAdapter = new ArrayAdapter<String>(ListServicios.this,   android.R.layout.simple_spinner_item, Datos.Obtener_Gastos(ListServicios.this));
		spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
    	
		spinnerCapital = new ArrayAdapter<String>(ListServicios.this,   android.R.layout.simple_spinner_item, Datos.Obtener_Capital(ListServicios.this));
		spinnerCapital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
    	
        if (savedInstanceState != null) {
     	   recuperar_datos(savedInstanceState);
        }
    	
        primero = true;
        capital.setAdapter(spinnerCapital);

        capital.setSelection(0);
        capital.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
	        		calcular_prima();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);	         
		CargarDatos itesmsrutas = new CargarDatos(ListServicios.this);
		itesmsrutas.execute();
		
		chk_todos.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!primero)
		        seleccionar_todos(isChecked);				
			}
		});
		
		
			DatosBDTablas db = new DatosBDTablas(getApplicationContext());
			db.open(); 		
			if (!db.EstActivo(1))
			{
				capital.setEnabled(false);
				lstOpciones.setEnabled(false);
				botonedicion.setEnabled(false);
				chk_todos.setEnabled(false);
			    mostrar_error(2,"Debe Actualizar la Listas de Precios!" );
			}
			db.close();
		bcompartir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GeneraPDF();
			}
		});


		bayuda.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
	//			Intent i = new Intent(ListServicios.this,ActivityAyuda.class);
				Intent i = new Intent(ListServicios.this,Adicionales.class);
				i.putExtra("ayuda", "1");
				startActivity(i);
			}
		});

		if (!Librerias.Esta_act_vida(ListServicios.this) || !Librerias.Esta_act_sep(ListServicios.this))
		{
			ActualizarPreios();
		}
}
protected void seleccionar_todos(boolean valor) {
	for (int i = 0; i < valores_select.length; i++) {
		if (i > 0)
			valores_select[i] = valor;
	}
    adaptador.notifyDataSetChanged(); 
	totalizar();
	}
void calcular_prima(){
if (!primero)
{
	String text = capital.getSelectedItem().toString().replace(".", "");
	float valor = Float.valueOf(text);
	
	Vecdatos = Datos.getItems(getApplicationContext(),valor,Long.valueOf(edad_titular.getText().toString()),sexo,Long.valueOf(edad_conyugue.getText().toString()),sexo_cony);
	
	if (Vecdatos.size()>0)
	{
		int pos = lstOpciones.getFirstVisiblePosition();
	    adaptador = new AdaptadorDatos(ListServicios.this,Vecdatos);
	    lstOpciones.setAdapter(adaptador);
	    adaptador.notifyDataSetChanged(); 
	    lstOpciones.setSelection(pos);
	    totalizar();
	    
	}
}	

botonedicion.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
        Intent i = new Intent(ListServicios.this,NuevoVida.class); 
        i.putExtra("edad", edad);
        i.putExtra("edad_cony", edad_cony);
        i.putExtra("sexo", sexo);
		i.putExtra("sexo_cony", sexo_cony);
		i.putExtra("tieneconyugue", tieneconyugue);

        startActivityForResult(i,50);
		
	}
});
}
	//*******************************************************************************************************************************
	private void ActualizarPreios() {
		AlertDialog.Builder MENSAJE = new AlertDialog.Builder(ListServicios.this);

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

								if (Datos.Esta_Logueada(ListServicios.this))
								{
									Intent i = new Intent(ListServicios.this,MenuActual.class);
									startActivity(i);
								}
							}
						});
		AlertDialog alert = MENSAJE.create();
		alert.show();
	}


//******************************************************************************************************************************* 
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == 50) 
       {

    	edad          = data.getExtras().getInt("edad",0);
    	edad_cony     = data.getExtras().getInt("edad_cony",0);
    	sexo          = data.getExtras().getInt("sexo",0);
		sexo_cony     = data.getExtras().getInt("sexo_cony",0);
		tieneconyugue = data.getExtras().getBoolean("tieneconyugue",false);

    	ley_conyugue.setEnabled(true);
    	if (!tieneconyugue)
    	{
    		sexo_cony = 0;
    		edad_cony = 0;
        	ley_conyugue.setEnabled(false);
    	}
    	setear_sexo();
    	edad_titular.setText(String.valueOf(edad));
    	edad_conyugue.setText(String.valueOf(edad_cony));
    	calcular_prima();		
       }
    
}   

private void setear_sexo() {
	Ssex_tit.setText("");
	if (sexo == 1)
	  Ssex_tit.setText("MASC.");

	if (sexo == 2)
  	  Ssex_tit.setText("FEM.");
	
	Ssex_cony.setText("");
	if (sexo_cony == 1)
		Ssex_cony.setText("MASC.");

	if (sexo_cony == 2)
		Ssex_cony.setText("FEM.");
}
/**************************************************************************************/        

    @Override
    public void onSaveInstanceState(Bundle estadoGuardado) {
    	super.onSaveInstanceState(estadoGuardado);
    	estadoGuardado.putString("edad_titular" , edad_titular.getText().toString());
    	estadoGuardado.putString("edad_conyugue", edad_conyugue.getText().toString());
    	estadoGuardado.putInt("sexo", sexo);
    	estadoGuardado.putInt("sexo_cony",sexo_cony);
        estadoGuardado.putInt("capital",capital.getSelectedItemPosition());
        estadoGuardado.putBooleanArray("valores_select", valores_select);
        estadoGuardado.putFloat("gasto_base",gasto_base);
        estadoGuardado.putInt("gasto_base_pos",gasto_base_pos);
    	estadoGuardado.putBoolean("tieneconyugue", tieneconyugue);

        
    }
 /*******************************************************************************************************/
    @Override
    protected void onRestoreInstanceState(Bundle estadoGuardado){
           super.onRestoreInstanceState(estadoGuardado);
           if (estadoGuardado != null) {
        	   recuperar_datos(estadoGuardado);
           }
           
    }
    /*******************************************************************************************************/
 private void recuperar_datos(Bundle estadoGuardado) {
	   edad_titular.setText(estadoGuardado.getString("edad_titular"));
	   edad_conyugue.setText(estadoGuardado.getString("edad_conyugue"));
	   sexo      = estadoGuardado.getInt("sexo");
	   sexo_cony = estadoGuardado.getInt("sexo_cony");
	   capital.setSelection(estadoGuardado.getInt("capital"));        	
	   valores_select = estadoGuardado.getBooleanArray("valores_select"); 
	   gasto_base = estadoGuardado.getFloat("gasto_base");
	   gasto_base_pos = estadoGuardado.getInt("gasto_base_pos");
	 tieneconyugue = estadoGuardado.getBoolean("tieneconyugue");
	   try {
	       edad      = Integer.valueOf(edad_titular.getText().toString());
		
	} catch (Exception e) {
		edad = 0;
	}
	   try {
	       edad_cony = Integer.valueOf(edad_conyugue.getText().toString());
		
	} catch (Exception e) {
	       edad_cony = 0;
	}
   	setear_sexo();
	 
}
/*******************************************************************************************************/
        

//******************************************************************************************************************************* 
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
		 String text = capital.getSelectedItem().toString().replace(".", "");
		 float valor = Float.valueOf(text);
	     Vecdatos = Datos.getItems(ListServicios.this,valor,Long.valueOf(edad_titular.getText().toString()),sexo,Long.valueOf(edad_conyugue.getText().toString()),sexo_cony);
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
		primero = false;
   }		

}
//******************************************************************************************************************************* 
public void totalizar(){
	float total  = 0;	
	int redondeo = 0;

	for (int i = 0; i < Vecdatos.size(); i++) {
		 if (valores_select[i])	
		 {
		  if (Vecdatos.get(i).getId()==11) // Gastos,  Recalculamos valor segn base de gasto (5000, 10000)
	        	Vecdatos.get(i).RecalcularValor(gasto_base);
		  
	      total += Vecdatos.get(i).getValor();	
		 }
	}
	redondeo = (int) Math.round(total);

	total_Prima.setText(String.valueOf(redondeo)+".00");
//	total_Prima.setText(String.format("%.02f", total));
	
}
//******************************************************************************************************************************* 
public void GeneraPDF(){
	Intent i = new Intent(this,GenerarPDFActivity.class);
	i.putExtra("edad", edad);
	i.putExtra("edad_cony", edad_cony);
	i.putExtra("sexo", sexo);
	i.putExtra("sexo_cony", sexo_cony);

	String text = capital.getSelectedItem().toString().replace(".", "");
	float valor = Float.valueOf(text);
	i.putExtra("capital", valor);
	i.putExtra("base", gasto_base);
	i.putExtra("total", Vecdatos.size());
	i.putExtra("totalgral", total_Prima.getText().toString());

	for (int n = 0; n < Vecdatos.size(); n++) {
		i.putExtra("n" + n, valores_select[n]);
	}
	startActivity(i);
}

//******************************************************************************************************************************* 
class AdaptadorDatos extends ArrayAdapter {
	
	Activity context;
	ArrayList<Servicios> Vecdatos;
	AdaptadorDatos(Activity context,ArrayList<Servicios> Vecdatos) {
		super(context, R.layout.lista_servicios, Vecdatos);
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
			item = inflater.inflate(R.layout.lista_servicios, null);
			
			holder = new ViewHolder();
			holder.servicio  = (CheckBox)item.findViewById(R.id.chk_servicio);
    		holder.base      = (TextView)item.findViewById(R.id.base_servicio);
    		holder.valor     = (TextView)item.findViewById(R.id.valor_servicio);
    		holder.bgastos   = (Spinner)item.findViewById(R.id.bgastos);
    		holder.pos       = position;
			item.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)item.getTag();
		}
		holder.servicio.setText(Vecdatos.get(position).getNombre());
		holder.base.setText(String.valueOf(Vecdatos.get(position).getBase()));
		holder.valor.setText(String.format("%.02f", Vecdatos.get(position).getValor()));
		
		holder.servicio.setClickable(!Vecdatos.get(position).isLectura());
		holder.servicio.setChecked(Vecdatos.get(position).isLectura());
		holder.pos       = position;
		
		holder.bgastos.setVisibility(View.GONE);
		holder.base.setVisibility(View.VISIBLE);
        if (Vecdatos.get(position).getId()==11)
        {
    		holder.bgastos.setVisibility(View.VISIBLE);
    		holder.base.setVisibility(View.GONE);
	        holder.bgastos.setAdapter(spinnerArrayAdapter);
			holder.bgastos.setSelection(gasto_base_pos);
			

        }
        
		holder.servicio.setChecked(valores_select[position]);
		
		if (!holder.servicio.isChecked())
			holder.valor.setText("0.00");
			

		holder.servicio.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View arg0) {
		    	if (holder.pos == 0)
		    		holder.servicio.setChecked(true);
		        final boolean isChecked = holder.servicio.isChecked();
		        valores_select[holder.pos] = isChecked;

		        View padre = (View) arg0.getParent();
		    	if (!isChecked)
		    		{
			        ((TextView)padre.findViewById(R.id.valor_servicio)).setText("0.00");
		    		}
		    	else
			    	{
			        if (Vecdatos.get(holder.pos).getId()==11) // Gastos,  Recalculamos valor segn base de gasto (5000, 10000)
			        	Vecdatos.get(holder.pos).RecalcularValor(gasto_base);
		    		
			        ((TextView)padre.findViewById(R.id.valor_servicio)).setText(String.format("%.02f", Vecdatos.get(holder.pos).getValor()));

			    	}
		    		
			     totalizar();
		        	
		    }
		    
		});
		
		
		
		holder.bgastos.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					gasto_base     = Float.valueOf(holder.bgastos.getSelectedItem().toString().replace(".", ""));
					gasto_base_pos = holder.bgastos.getSelectedItemPosition();
					
			        final boolean isChecked = holder.servicio.isChecked();
			        View padre = (View) arg0.getParent();
			    	if (!isChecked)
			    		{
				        ((TextView)padre.findViewById(R.id.valor_servicio)).setText("0.00");
			    		}
			    	else
				    	{
				        if (Vecdatos.get(holder.pos).getId()==11) // Gastos,  Recalculamos valor segn base de gasto (5000, 10000)
			        	Vecdatos.get(holder.pos).RecalcularValor(gasto_base);
			    		
				        ((TextView)padre.findViewById(R.id.valor_servicio)).setText(String.format("%.02f", Vecdatos.get(holder.pos).getValor()));
				    	}
				    totalizar();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});		
 	    return(item);
	}

       
    
}
//________________________________________________________________        

class ViewHolder {
	CheckBox servicio;
	TextView base;
	TextView valor;    
	int pos;
	Spinner bgastos;
	
	
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
