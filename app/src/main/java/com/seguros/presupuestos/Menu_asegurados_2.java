package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.seguros.MainActivity;

public class Menu_asegurados_2 extends AppCompatActivity {

    Button opcion_misproductores, opcion_pago_linea, opcion_cerrarsesion, opcion_vencimient,opcion_politica, opcion_condiciones ;
    ImageView anterior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_asegurados_2);

        opcion_misproductores    = (Button)findViewById(R.id.opcion_misproductores);
        opcion_pago_linea        = (Button)findViewById(R.id.opcion_pago_linea);
        opcion_vencimient        = (Button)findViewById(R.id.opcion_vencimientos);

        opcion_politica          = (Button)findViewById(R.id.opcion_politica);
        opcion_cerrarsesion      = (Button)findViewById(R.id.opcion_cerrarsesion);
        opcion_condiciones       = (Button)findViewById(R.id.opcion_condiciones);
        anterior                 = (ImageView) findViewById(R.id.anterior);


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

        anterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        opcion_pago_linea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
                i.putExtra("ayuda", "9");
                startActivity(i);
            }
        });

        opcion_vencimient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),OtrosServicios.class);
                i.putExtra("tipo"  , "2");
                i.putExtra("titulo", "SOLICITUD DE PAGOS Y VENCIMIENTOS");
                startActivity(i);
            }
        });

        opcion_politica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        opcion_condiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        Librerias.mostrar_error(Menu_asegurados_2.this,1, "Su sesion ha sido cerrada correctamente!");

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
