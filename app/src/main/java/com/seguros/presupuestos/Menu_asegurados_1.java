package com.seguros.presupuestos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.seguros.MainActivity;

public class Menu_asegurados_1 extends AppCompatActivity {

    Button opcion_contactenos, opcion_preguntas, opcion_cambiardatos,
            opcion_cambiarpago, opcion_productores, opcion_pagoenlinea,
            opcion_vencimientos, opcion_solicitudes,opcion_compania, opcion_pagodia,
            opcion_contrato, opcion_premio, opcion_pago_online, opcion_cerrarsesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_asegurados_1);

        opcion_pago_online     = (Button)findViewById(R.id.opcion_pago_online);
        opcion_contrato        = (Button)findViewById(R.id.opcion_contrato);
        opcion_cambiardatos    = (Button)findViewById(R.id.opcion_cambiardatos);
        opcion_solicitudes     = (Button)findViewById(R.id.opcion_solicitudes);
        opcion_cerrarsesion    = (Button)findViewById(R.id.opcion_cerrarsesion);
        opcion_contactenos     = (Button)findViewById(R.id.opcion_contactenos);


        this.setTitle("");

        opcion_pago_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
                i.putExtra("ayuda", "9");
                startActivity(i);
            }
        });


        opcion_contrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "5");

                i.putExtra("titulo", "SOLICITUD DE CONTRATAR SEGURO");
                startActivity(i);			}
        });

        opcion_cambiardatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Modifica_Asegurado.class);
                startActivity(i);
            }
        });

        opcion_contactenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MenuContactos.class);

                startActivity(i);
            }
        });

        opcion_cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               cerrar_sesion();
            }
        });

    }
//*****************************************
    private void cerrar_sesion() {
        Librerias.Cerrar_sesion(getApplication());
        finish();
        Librerias.mostrar_error(Menu_asegurados_1.this,1, "Su sesion ha sido cerrada correctamente!");

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
