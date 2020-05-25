//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URLEncoder;

public class MenuAdmin extends AppCompatActivity {
	ProgressBar progressBar;
	Button menu_menasegurados, menu_vendedores, menu_asegurados, menu_logasegurados;


//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuadmin);

		menu_logasegurados   = (Button)findViewById(R.id.menu_logasegurados);
		menu_menasegurados   = (Button)findViewById(R.id.menu_menasegurados);
		menu_vendedores      = (Button)findViewById(R.id.menu_vendedores);
		menu_asegurados      = (Button)findViewById(R.id.menu_asegurados);
		this.setTitle("");

		menu_logasegurados.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "8");
				startActivity(i);

			}
		});

		menu_menasegurados.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "7");
				startActivity(i);

			}
		});

		menu_vendedores.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MenuAdmin.this,ListaVendedores.class);
				startActivity(i);


			}
		});

		menu_asegurados.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent i = new Intent(MenuAdmin.this,ListaAsegurados.class);
				startActivity(i);

			}
		});

}

//******************************************************************************



}
