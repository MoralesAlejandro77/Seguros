//******************************************************************************************************************************* 
package com.seguros.presupuestos;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
//******************************************************************************************************************************* 
import android.widget.Toast;

public class NuevoSepelio extends FragmentActivity  {
	EditText edad_titular;
	TextView  total_Prima;
	int opcion;

//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sep_new);

        edad_titular   = (EditText)findViewById(R.id.edad_titular);

        
		opcion = this.getIntent().getExtras().getInt("estado");
		if (opcion == 2) // Edicion
			{
    		edad_titular.setText(String.valueOf(this.getIntent().getExtras().getInt("edad")));
			}
        
}


    //******************************************************************************************************************************* 

    public void seleccionar(View view) {

	    	Intent resultData = new Intent();

	    	resultData.putExtra("edad", edad_titular.getText().toString());
	    	if (opcion == 2) 
		    	setResult(30,resultData);
	    	else
	    	    setResult(20,resultData);

		finish();


    }
    //******************************************************************************************************************************* 

}
