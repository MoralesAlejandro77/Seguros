package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.seguros.MainActivity;

public class Menu_solicitudes extends Activity {

    Button opcion_misproductores, opcion_solicitud_poliza, opcion_solicitud_pago_dia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_solicitudes);

        opcion_misproductores          = (Button)findViewById(R.id.opcion_misproductores);
        opcion_solicitud_poliza        = (Button)findViewById(R.id.opcion_solicitud_poliza);
        opcion_solicitud_pago_dia      = (Button)findViewById(R.id.opcion_solicitud_pago_dia);


        this.setTitle("");

        opcion_misproductores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "1");
                i.putExtra("titulo", "SOLICITUD DE MIS PRODUCTORES");
                startActivity(i);
            }
        });


        opcion_solicitud_poliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "3");
                i.putExtra("titulo", "SOLICITUD DE POLIZA");
                startActivity(i);
            }
        });

        opcion_solicitud_pago_dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "4");
                i.putExtra("titulo", "SOLICITUD PAGO AL DIA");
                startActivity(i);
            }
        });



    }
  }
