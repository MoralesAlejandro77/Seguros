package com.seguros;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.seguros.Cocherias.BuscarAsegurado;
import com.seguros.Datos.Datos;
import com.seguros.presupuestos.ActivityAyuda;
import com.seguros.presupuestos.Librerias;
import com.seguros.presupuestos.LoginApp;
import com.seguros.presupuestos.Login_Asegurado;
import com.seguros.presupuestos.Menu;
import com.seguros.presupuestos.MenuAsegurado;
import com.seguros.presupuestos.MenuContactos;
import com.seguros.presupuestos.Menu_asegurados_1;

import com.seguros.presupuestos.Prelogin;
import com.seguros.presupuestos.R;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_OVERLAY_PERMISSION = 88;
    Button bloginaseg, bcompania, bcontactos, bpreguntas, bpromotores, bprestadores;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu2);

        bloginaseg    = (Button)findViewById(R.id.bloginaseg);
        bcompania     = (Button)findViewById(R.id.bcompania);
        bcontactos    = (Button)findViewById(R.id.bcontactos);
        bpreguntas    = (Button)findViewById(R.id.bpreguntas);
        bpromotores   = (Button)findViewById(R.id.bpromotores);
        bprestadores  = (Button)findViewById(R.id.bprestadores);
        progressBar   = (ProgressBar)findViewById(R.id.progressBar2);


        this.setTitle("");
        createNotificationChannel();
        if (Librerias.Esta_registrado_asegurado(getApplicationContext()))
        {
//            Intent i = new Intent(getApplicationContext(), MenuAsegurado.class);
            Intent i = new Intent(getApplicationContext(), Menu_asegurados_1.class);
            startActivity(i);
            finish();
        }

        bloginaseg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login_Asegurado.class);
                startActivity(i);
            }
        });

        bcompania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
                i.putExtra("ayuda", "3");
                startActivity(i);
            }
        });
        bcontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MenuContactos.class);
                startActivity(i);
            }
        });
        bpreguntas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
                i.putExtra("ayuda", "5");
                startActivity(i);
            }
        });

        bpromotores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccesoSistema();
            }
        });

        bprestadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccesoEmpresas();
            }
        });


        progressBar.setVisibility(View.GONE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 700)
        {

            Intent i = new Intent(getApplicationContext(),Menu.class);
            startActivity(i);
        }


    }


/*************************************************************************************************************/

public void AccesoEmpresas(){
    if (Datos.Esta_Logueadaemp(getApplicationContext()))
    {

        Intent i = new Intent(getApplicationContext(), BuscarAsegurado.class);
        startActivity(i);

    }
    else
    {
        Intent i = new Intent(getApplicationContext(),LoginApp.class);
        i.putExtra("producto", "600");
        i.putExtra("actual", "0");
        i.putExtra("tipoacceso", "2");
        startActivityForResult(i, 600);
    }
}
    //**********************************************************************************
    public void AccesoSistema(){
        if (Datos.Esta_Logueada(getApplicationContext()))
        {
            Intent i = new Intent(getApplicationContext(),Menu.class);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(getApplicationContext(),LoginApp.class);
            i.putExtra("producto", "700");
            i.putExtra("actual", "0");
            i.putExtra("tipoacceso", "1");
            startActivityForResult(i, 700);
        }
    }
    /*************************************************************************************************************/
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Tres Provinias Seguros";
            String description = "Canal de Informacion para nuestros Asegurados.";
            String canal = getString(R.string.canal_notify);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(canal, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
