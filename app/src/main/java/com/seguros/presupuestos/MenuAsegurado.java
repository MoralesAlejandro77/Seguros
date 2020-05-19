//******************************************************************************************************************************* 
package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.seguros.MainActivity;


public class MenuAsegurado extends AppCompatActivity {
	ProgressBar progressBar;
	Button  opcion_contactos, opcion_preguntas, opcion_cambiardatos,
			opcion_cambiarpago, opcion_productores, opcion_pagoenlinea,
			opcion_vencimientos, opcion_poliza,opcion_compania, opcion_pagodia,
			opcion_contrato, opcion_premio, opcion_pago_online;

//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuasegurados);
		this.setTitle("");

		opcion_contactos     = (Button)findViewById(R.id.opcion_contactos);
		opcion_preguntas     = (Button)findViewById(R.id.opcion_preguntas);
		opcion_cambiardatos  = (Button)findViewById(R.id.opcion_cambiardatos);
		opcion_cambiarpago   = (Button)findViewById(R.id.opcion_cambiarpago);
		opcion_productores   = (Button)findViewById(R.id.opcion_productores);
		opcion_vencimientos  = (Button)findViewById(R.id.opcion_vencimientos);
		opcion_poliza        = (Button)findViewById(R.id.opcion_poliza);
        opcion_pagodia       = (Button)findViewById(R.id.opcion_pagodia);
		opcion_pagoenlinea   = (Button)findViewById(R.id.opcion_enlinea);
		opcion_contrato      = (Button)findViewById(R.id.opcion_contrato);
		opcion_premio        = (Button)findViewById(R.id.opcion_premio);
		opcion_compania      = (Button)findViewById(R.id.opcion_compania);
		opcion_pago_online   = (Button)findViewById(R.id.opcion_pago_online);

		this.setTitle("");
		opcion_contactos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),MenuContactos.class);

				startActivity(i);
			}
		});


		opcion_productores.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
				i.putExtra("titulo", "SOLICITUD DE MIS PRODUCTORES");
				startActivity(i);
			}
		});

		opcion_vencimientos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
				i.putExtra("titulo", "SOLICITUD DE PAGOS Y VENCIMIENTOS");
				startActivity(i);			}
		});


		opcion_preguntas.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "5");
				startActivity(i);
			}
		});

		opcion_compania.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "3");
				startActivity(i);
			}
		});

		opcion_cambiardatos.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),Modifica_Asegurado.class);
				startActivity(i);
			}
		});
		opcion_cambiarpago.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),Modifica_CBU.class);
				startActivity(i);
			}
		});
        opcion_poliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("titulo", "SOLICITUD DE POLIZA");
                startActivity(i);
            }
        });
        opcion_pagodia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("titulo", "SOLICITUD PAGO AL DIA");
                startActivity(i);
            }
        });


		opcion_pago_online.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "9");
				startActivity(i);
			}
		});


		opcion_pagoenlinea.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "6");
				startActivity(i);
			}
		});


		opcion_contrato.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
				i.putExtra("titulo", "SOLICITUD DE CONTRATAR SEGURO");
				startActivity(i);			}
		});

		opcion_premio.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),Activity_Premios.class);
				startActivity(i);
			}
		});

    }


//*******************************************************************************************************************************
  public boolean onCreateOptionsMenu(android.view.Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.sesiones, menu);
	  return true;
  }
//*******************************************************************************************************************************

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sesion:
				Librerias.Cerrar_sesion(getApplication());
				finish();
				Librerias.mostrar_error(MenuAsegurado.this,1, "Su sesion ha sido cerrada correctamente!");

				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(i);

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
