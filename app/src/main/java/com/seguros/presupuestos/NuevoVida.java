//******************************************************************************************************************************* 
package com.seguros.presupuestos;
/***********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Fecha 02-12-2014
 * Version : 1.1
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/	
import java.util.ArrayList;
import java.util.Calendar;

import com.seguros.Datos.Datos;
import com.seguros.Datos.DatosBDTablas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
//******************************************************************************************************************************* 
import android.widget.Toast;

public class NuevoVida extends FragmentActivity  {
ProgressBar progressBar;
EditText  edad_titular,edad_conyugue;
TextView total_Prima;
CheckBox check_conyugue;
RadioButton sex_hom_cony,sex_muj_cony, sex_hom_tit,sex_muj_tit;
Button button1;
int codigo;
boolean primero;


//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vida_new);

        edad_titular   = (EditText)findViewById(R.id.edad_titular);
        edad_conyugue  = (EditText)findViewById(R.id.edad_conyugue);
        check_conyugue = (CheckBox)findViewById(R.id.check_conyugue);
        sex_hom_cony   = (RadioButton)findViewById(R.id.sex_hom_cony);
        sex_muj_cony   = (RadioButton)findViewById(R.id.sex_muj_cony);
        sex_hom_tit    = (RadioButton)findViewById(R.id.sex_hom_tit);
        sex_muj_tit    = (RadioButton)findViewById(R.id.sex_muj_tit);

        button1        = (Button)findViewById(R.id.button2);
        progressBar    = (ProgressBar)findViewById(R.id.progressBar1);

        if (savedInstanceState != null) {
     	   recuperar_datos(savedInstanceState);
        }
    	
        primero = true;
        

		

	    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        preparar_Conyugue(false);
		
		check_conyugue.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        preparar_Conyugue(isChecked);
			}
		});
 

	     
   button1.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
	seleccionar();
		
	}
});
  inicializarValores(); 
}

//*******************************************************************************************************************************

private void inicializarValores() {
    int edad = 0;
    try {
		edad = this.getIntent().getExtras().getInt("edad",0);
	} catch (Exception e) {
	}
    edad_titular.setText(String.valueOf(edad));

    int edad_cony = 0;
    try {
    	edad_cony = this.getIntent().getExtras().getInt("edad_cony",0);
	} catch (Exception e) {
	}
    
    edad_conyugue.setText(String.valueOf(edad_cony));
    
    sex_hom_tit.setChecked(true);
    sex_muj_tit.setChecked(false);
    if (this.getIntent().getExtras().getInt("sexo",0) == 1)
    {
        sex_hom_tit.setChecked(true);
        sex_muj_tit.setChecked(false);
    }
    if (this.getIntent().getExtras().getInt("sexo",0) == 2)
    {
        sex_hom_tit.setChecked(false);
        sex_muj_tit.setChecked(true);
    }
    
    sex_hom_cony.setChecked(true);
    sex_muj_cony.setChecked(false);
    if (this.getIntent().getExtras().getInt("sexo_cony",0) == 1)
    {
      sex_hom_cony.setChecked(true);
      sex_muj_cony.setChecked(false);
    }
    if (this.getIntent().getExtras().getInt("sexo_cony",0) == 2)
    {
        sex_hom_cony.setChecked(false);
        sex_muj_cony.setChecked(true);
      }
	preparar_Conyugue(false);
	check_conyugue.setChecked(false);
	if (this.getIntent().getExtras().getBoolean("tieneconyugue",false))
	{
		preparar_Conyugue(true);
		check_conyugue.setChecked(true);
	}




}

protected void seleccionar() {

		     Intent resultData = new Intent();
		     int edad = 0, edad_cony = 0;
		     
		     try {
		    	 edad = Integer.valueOf(edad_titular.getText().toString());				
			} catch (Exception e) {
				// TODO: handle exception

			}
		     

		     int sexo = 0;
		     if (sex_hom_tit.isChecked())
		    	 sexo = 1;
		     else
		    	 sexo = 2;

		     int sexo_cony = 0;
		     

		     if (check_conyugue.isChecked())
		     {


		    	 try {
			    	 edad_cony = Integer.valueOf(edad_conyugue.getText().toString());				
				} catch (Exception e) {
					// TODO: handle exception

				}

			     if (sex_hom_cony.isChecked())
			    	 sexo_cony = 1;
			     else
			    	 sexo_cony = 2;
		     }
		     
		     resultData.putExtra("sexo", sexo);
		     resultData.putExtra("sexo_cony", sexo_cony);
		     resultData.putExtra("edad", edad);
		     resultData.putExtra("edad_cony", edad_cony);
	         resultData.putExtra("tieneconyugue", check_conyugue.isChecked());
		     setResult(50,resultData);
		     finish();

	    }

	//*******************************************************************************************************************************

	@Override
    public void onSaveInstanceState(Bundle estadoGuardado) {
    	super.onSaveInstanceState(estadoGuardado);

    	estadoGuardado.putString("edad_titular" , edad_titular.getText().toString());
    	estadoGuardado.putString("edad_conyugue", edad_conyugue.getText().toString());
    	estadoGuardado.putBoolean("tieneconyugue", check_conyugue.isChecked());
    	estadoGuardado.putBoolean("sex_hom_cony", sex_hom_cony.isChecked());
    	estadoGuardado.putBoolean("sex_muj_cony", sex_muj_cony.isChecked());
    	estadoGuardado.putBoolean("sex_hom_tit", sex_hom_tit.isChecked());
    	estadoGuardado.putBoolean("sex_muj_tit", sex_muj_tit.isChecked());

        
        
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
	   check_conyugue.setChecked(estadoGuardado.getBoolean("tieneconyugue"));
	   sex_hom_cony.setChecked(estadoGuardado.getBoolean("sex_hom_cony"));
	   sex_muj_cony.setChecked(estadoGuardado.getBoolean("sex_muj_cony"));
	   sex_hom_tit.setChecked(estadoGuardado.getBoolean("sex_hom_tit"));
	   sex_muj_tit.setChecked(estadoGuardado.getBoolean("sex_muj_tit"));

	 
}
/*******************************************************************************************************/
private void preparar_Conyugue(boolean valor) {
	edad_conyugue.setEnabled(valor);
	sex_hom_cony.setEnabled(valor);
	sex_muj_cony.setEnabled(valor);

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

}
