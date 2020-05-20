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

public class MenuContactos extends AppCompatActivity {
	ProgressBar progressBar;
	Button menu_contacto, menu_email, menu_whatsapp, menu_telefono;


//******************************************************************************************************************************* 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menucontactos);

		menu_email      = (Button)findViewById(R.id.menu_mail);
		menu_contacto   = (Button)findViewById(R.id.menu_contacto);
		menu_telefono   = (Button)findViewById(R.id.menu_telefono);
		menu_whatsapp   = (Button)findViewById(R.id.menu_watsapp);
		this.setTitle("");
		menu_email.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"casacentral@tresprovinciassa.com.ar"});
				i.putExtra(Intent.EXTRA_SUBJECT, "Necesito Informacion");
				i.putExtra(Intent.EXTRA_TEXT   , "Necesito contactarme con Ustedes.");
				i.setType("message/rfc822");
				try {
					startActivity(Intent.createChooser(i, "Enviando mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(MenuContactos.this, "No existe una aplicacion de Correo instalada en su dispositivo.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		menu_contacto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),ActivityAyuda.class);
				i.putExtra("ayuda", "4");
				startActivity(i);

			}
		});

		menu_telefono.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			llamarTelefono();

			}
		});

		menu_whatsapp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				try {
					String mensaje = "*MENSAJE DESDE LA APP* " ;
					String Nro     = Librerias.Nro_Whatsapp.replace("+", "").replace(" " , "");

					String url = "https://api.whatsapp.com/send?phone="+ Nro + "&text=" + URLEncoder.encode(mensaje, "UTF-8");

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setPackage("com.whatsapp");
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
				catch (Exception e){
					e.printStackTrace();
					Toast.makeText(MenuContactos.this, e.toString(), Toast.LENGTH_SHORT).show();
				}


			}
		});

}

	private void llamarTelefono() {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		String telefono = Librerias.Nro_0800;
		intent.setData(Uri.parse("tel:" + telefono));
		startActivity(intent);
	}
//******************************************************************************



}
