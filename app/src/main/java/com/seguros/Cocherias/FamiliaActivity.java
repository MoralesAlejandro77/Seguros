package com.seguros.Cocherias;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.seguros.Datos.DatosBDTablas_tmp;
import com.seguros.presupuestos.R;


public class FamiliaActivity extends Activity {
	private FliarAdapter adapter;
	private ListView     familiares;

	ProgressBar barra = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_familia);
        familiares = (ListView)findViewById(R.id.lista);
        barra      = (ProgressBar)findViewById(R.id.progressBar1);


	    BuscarDatos flia = new BuscarDatos();
	    flia.execute();

	}

//******************************************************************************************************
    private class BuscarDatos extends AsyncTask<String, Float, Integer>{
	Cursor datosflia;
	DatosBDTablas_tmp db;

   	 protected void onPreExecute() {
      barra.setVisibility(View.VISIBLE);
   	 }

         protected Integer doInBackground(String... urls) {
			 db = new DatosBDTablas_tmp(getApplicationContext());
			 db.open();
			 datosflia  = db.obtenerFamilia();
        	 return 1;
        	 
         }

         protected void onPostExecute(Integer bytes) {
             try {
          	    adapter = new FliarAdapter(FamiliaActivity.this, datosflia);
         		familiares.setAdapter(adapter);		
     	        
     		} catch (Exception e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     	    	Toast.makeText(getApplicationContext(), "No Hay datos registrados!", 1).show();
     		}

        barra.setVisibility(View.INVISIBLE);
			 db.close();
         }
   }    
    
  //******************************************************************************************************    
  public class FliarAdapter extends  CursorAdapter {
	  public FliarAdapter(Context context, Cursor c) {
		  super(context, c,0);
	  }


	  @Override
	  public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		  return LayoutInflater.from(context).inflate(R.layout.fila_flia, viewGroup, false);

	  }

	  @Override
	  public void bindView(View view, Context context, Cursor cursor) {
		  TextView dni       = (TextView) view.findViewById(R.id.dni);
		  TextView nombre    = (TextView) view.findViewById(R.id.nombre);
		  TextView fechanac  = (TextView) view.findViewById(R.id.fechanac);

		  String ldni      = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
		  String lnombre   = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
		  String lfechanac = cursor.getString(cursor.getColumnIndexOrThrow("fechanac"));

		  dni.setText(ldni);
		  nombre.setText(lnombre);
		  fechanac.setText(lfechanac);
	  }

  }
}
