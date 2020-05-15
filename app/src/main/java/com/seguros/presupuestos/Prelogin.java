package com.seguros.presupuestos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Prelogin extends AppCompatActivity {
Button bsi, bno, bcondiciones, bpoliticas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);
        bsi           = (Button) findViewById(R.id.bsi);
        bno           = (Button) findViewById(R.id.bno);
        bpoliticas    = (Button) findViewById(R.id.bpoliticas);
        bcondiciones  = (Button) findViewById(R.id.bcondiciones);
        this.setTitle("");

        bsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Login_Asegurado.class);
                startActivity(i);

            }
        });


        bno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Registro_Asegurado.class);
                startActivity(i);
            }
        });



        bpoliticas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        bcondiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
