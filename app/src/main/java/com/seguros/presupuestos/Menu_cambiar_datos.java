package com.seguros.presupuestos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_cambiar_datos extends Activity {

    Button opcion_datospersonales, opcion_mediospago;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_cambiar_datos);

        opcion_datospersonales  = (Button)findViewById(R.id.opcion_datospersonales);
        opcion_mediospago       = (Button)findViewById(R.id.opcion_mediospago);



        this.setTitle("");

        opcion_datospersonales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Modifica_Asegurado.class);
                startActivity(i);
            }
        });


        opcion_mediospago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),Modifica_CBU.class);
                startActivity(i);
            }
        });



    }
  }
