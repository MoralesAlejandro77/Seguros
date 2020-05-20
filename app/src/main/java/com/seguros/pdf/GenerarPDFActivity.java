package com.seguros.pdf;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.seguros.Datos.Datos;
import com.seguros.presupuestos.R;
import com.seguros.presupuestos.Servicios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


public class GenerarPDFActivity extends Activity  {
	private static final int WRITE_REQUEST_CODE = 43;
	private Uri urifile = null;

	private final static String NOMBRE_DIRECTORIO = "MiPdf";
	private final static String NOMBRE_DOCUMENTO = "presupuestoVida.pdf";
	private final static String ETIQUETA_ERROR = "ERROR";
	private static final int MY_PERMISSIONS_REQUEST_STORAGE = 205;
	private static Context contexto;
	ProgressBar barra;

	ArrayList<Servicios> Vecdatos;
	Button boton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compartir);
		barra    = (ProgressBar)findViewById(R.id.progressBar1);
		boton    = (Button)findViewById(R.id.bcompartir);

		contexto = getApplicationContext();
		boton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PDF();

			}
		});





	}

	private void PDF() {
		boolean permiso = false;
		if (Build.VERSION.SDK_INT >= 23)
		{
			if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

				if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
					/*********************************************************/
					Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
					/*************************************************************/
				} else {

					Toast.makeText(getBaseContext(),"Se necesita este permiso para poder completar la operacion!" ,Toast.LENGTH_LONG).show();
					ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);

				}
			}
			else
				permiso = true;
		}
		else
			permiso = true;


		if (permiso){
				CargarDatos itesmsrutas = new CargarDatos(GenerarPDFActivity.this);
				itesmsrutas.execute();
			}
	}

/*******************************************************************************/
	private void createFile(String mimeType, String fileName) {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("application/pdf");
		intent.putExtra(Intent.EXTRA_TITLE, fileName);
		startActivityForResult(intent, WRITE_REQUEST_CODE);
	}

	//******************************************************************
	public void generarPDF() {
    // * * * * * * * * * * * * * * * *
		float capital = 0;
		File f = null;
		try {
			capital = this.getIntent().getExtras().getFloat("capital", 0);
		} catch (Exception e) {
		}

		long edad_titular = 0;
		try {
			edad_titular = this.getIntent().getExtras().getInt("edad",0);
		} catch (Exception e) {
		}

		long edad_conyugue = 0;
		try {
			edad_conyugue = this.getIntent().getExtras().getInt("edad_cony",0);
		} catch (Exception e) {
		}

		int sexotit = 0;
		try {
			sexotit = this.getIntent().getExtras().getInt("sexo",0);
		} catch (Exception e) {
		}

		int sexo_cony = 0;
		try {
			sexo_cony = this.getIntent().getExtras().getInt("sexo_cony", 0);
		} catch (Exception e) {
		}

		float gasto_base     = 0;
		try {
			gasto_base = this.getIntent().getExtras().getFloat("base", 0);
		} catch (Exception e) {
		}

		String ssexotit = "";
		if (sexotit == 1)
			ssexotit = "MASC.";
		if (sexotit == 2)
			ssexotit = "FEM.";

		String ssexocony = "";
		if (sexo_cony == 1)
			ssexocony = "MASC.";
		if (sexo_cony == 2)
			ssexocony = "FEM.";



		Vecdatos = Datos.getItems(getApplicationContext(), capital, edad_titular, sexotit, edad_conyugue, sexo_cony);

		int totalitems = this.getIntent().getExtras().getInt("total", 0);

		boolean[] valores_select = new boolean[50];

		for (int n = 0; n < totalitems; n++) {
			valores_select[n] = this.getIntent().getExtras().getBoolean("n"+n);
		}




		// * * * * * * * * * * * * * * * *
		Bitmap bitmap = null;
		Bitmap bitmap2 = null;
		Font ServiciosFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.ITALIC, new CMYKColor(255, 0, 0, 0));
		Font TituloFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 255, 255));
		Font sentenciasFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL, new CMYKColor(255, 255, 255, 255));
		Document document = new Document();
		try
		{

			ParcelFileDescriptor pfd = GenerarPDFActivity.this.getContentResolver().openFileDescriptor(urifile, "w");
			FileOutputStream ficheroPdf = new FileOutputStream(pfd.getFileDescriptor());


			PdfWriter writer = PdfWriter.getInstance(document, ficheroPdf);
			document.open();


			document.addCreationDate();
			document.addCreator("Tres Provincias Seguro de Personas SA");
			document.addTitle("Presupuesto de Seguro de Vida");
			document.addAuthor("App Tres Provincias Seguros ");


			// ****************** Encabezado ***********

			bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.encabezado);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
			Image imagen = Image.getInstance(stream.toByteArray());
			imagen.scaleAbsolute(490,75);
			imagen.setAbsolutePosition(30f, 710f);
			document.add(imagen);

			// **********************************************

//			Chapter chapter1 = new Chapter(chapterTitle, 0);
//			chapter1.setNumberDepth(0);
//			document.add(chapter1);

			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

//			Paragraph chapterTitle = new Paragraph("Presupuesto Seguro de Vida", blueFont);
//			document.add(chapterTitle);

			//**************************************
			PdfPTable tabla_sep = new PdfPTable(1);
			tabla_sep.setWidthPercentage(100); //Width 100%

			PdfPCell celdasep = new PdfPCell(new Paragraph("Presupuesto Seguro de Vida"));
			celdasep.setBorderColor(BaseColor.BLACK);
			celdasep.setPaddingLeft(10);
			celdasep.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdasep.setVerticalAlignment(Element.ALIGN_MIDDLE);
			celdasep.setBackgroundColor(BaseColor.LIGHT_GRAY);
			celdasep.setBorder(0);
			tabla_sep.addCell(celdasep);

			document.add(tabla_sep);
			//**************************************


			document.add(new Paragraph(" "));
			Paragraph paragraphOne = new Paragraph("Datos del Titular : ", TituloFont);
			document.add(paragraphOne);


			document.add(new Paragraph("- Edad : " + edad_titular  + " años.",sentenciasFont));
			document.add(new Paragraph("- Sexo : " + ssexotit  ,sentenciasFont));

			if (sexo_cony == 1 ||sexo_cony == 2){
				document.add(new Paragraph(" "));
				document.add(new Paragraph("Conyugue Edad : " + edad_conyugue + " años , Sexo : " + ssexocony,TituloFont));

			}

			document.add(new Paragraph(" "));
			document.add(new Paragraph("Capital Asegurado : $ " +  String.format("%.02f",capital),sentenciasFont));
			document.add(new Paragraph(" "));


			document.add(new Paragraph(" "));
			Paragraph paragraphdos = new Paragraph("Servicios : ", TituloFont);
			document.add(paragraphdos);




			PdfPTable tabla = new PdfPTable(1);
			tabla.setWidthPercentage(80); //Width 100%
			tabla.setSpacingBefore(10f); //Space before table
			tabla.setSpacingAfter(10f); //Space after table

			//Set Column widths
			float[] columnWidths = {1f};
			tabla.setWidths(columnWidths);

			PdfPCell tcell1 = new PdfPCell(new Paragraph(""));
			tcell1.setBorderColor(BaseColor.BLACK);
			tcell1.setPaddingLeft(10);
			tcell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell1.setBackgroundColor(BaseColor.LIGHT_GRAY);

	/*		PdfPCell tcell2 = new PdfPCell(new Paragraph("Base"));
			tcell2.setBorderColor(BaseColor.BLACK);
			tcell2.setPaddingLeft(10);
			tcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);

			PdfPCell tcell3 = new PdfPCell(new Paragraph("Importe"));
			tcell3.setBorderColor(BaseColor.BLACK);
			tcell3.setPaddingLeft(10);
			tcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell3.setBackgroundColor(BaseColor.LIGHT_GRAY);*/

			tabla.addCell(tcell1);
	//		tabla.addCell(tcell2);
	//		tabla.addCell(tcell3);

	//		PdfPCell celdabase, celdaimporte;


			Paragraph paragraphService;

			if (Vecdatos.size()>0) {
				for (int i = 0; i < Vecdatos.size(); i++) {
					if (Vecdatos.get(i).getId()==11) // Gastos,  Recalculamos valor segn base de gasto (5000, 10000)
						Vecdatos.get(i).RecalcularValor(gasto_base);
					if (valores_select[i])
					{

/*						celdabase = new PdfPCell(new Paragraph(String.format("%.02f", Vecdatos.get(i).getBase())));
						celdabase.setBorderColor(BaseColor.BLACK);
						celdabase.setPaddingLeft(10);
						celdabase.setHorizontalAlignment(Element.ALIGN_RIGHT);
						celdabase.setVerticalAlignment(Element.ALIGN_MIDDLE);

						celdaimporte = new PdfPCell(new Paragraph(String.format("%.02f", Vecdatos.get(i).getValor())));
						celdaimporte.setBorderColor(BaseColor.BLACK);
						celdaimporte.setPaddingLeft(10);
						celdaimporte.setHorizontalAlignment(Element.ALIGN_RIGHT);
						celdaimporte.setVerticalAlignment(Element.ALIGN_MIDDLE);*/


						paragraphService = new Paragraph(Vecdatos.get(i).getNombre(), ServiciosFont);

						tabla.addCell(paragraphService);
//						tabla.addCell(celdabase);
//						tabla.addCell(celdaimporte);

					}
				}
/*				tabla.addCell("");
				tabla.addCell("");
				tabla.addCell("");
				tabla.addCell("TOTAL");
				tabla.addCell("");

				PdfPCell celdatotal = new PdfPCell(new Paragraph(this.getIntent().getExtras().getString("totalgral")));
				celdatotal.setBorderColor(BaseColor.BLACK);
				celdatotal.setPaddingLeft(10);
				celdatotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
				celdatotal.setVerticalAlignment(Element.ALIGN_MIDDLE);


				tabla.addCell(celdatotal);*/
			}

			document.add(tabla);


			//**************************************
			PdfPTable tabla_pie = new PdfPTable(1);
			tabla_pie.setWidthPercentage(100); //Width 100%

			String ptotal = this.getIntent().getExtras().getString("totalgral");

			PdfPCell celdapie = new PdfPCell(new Paragraph("PREMIO TOTAL $ " +ptotal));
			celdapie.setBorderColor(BaseColor.BLACK);
			celdapie.setPaddingLeft(10);
			celdapie.setHorizontalAlignment(Element.ALIGN_CENTER);
			celdapie.setVerticalAlignment(Element.ALIGN_MIDDLE);
			celdapie.setBackgroundColor(BaseColor.LIGHT_GRAY);
			celdapie.setBorder(0);
			tabla_pie.addCell(celdapie);
			document.add(tabla_pie);
			//**************************************



			// ****************** pie de pagina ***********

			bitmap2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.pie);
			ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
			bitmap2.compress(Bitmap.CompressFormat.JPEG, 50, stream2);
			Image imagen2 = Image.getInstance(stream2.toByteArray());
			imagen2.scaleAbsolute(490,65);
			imagen2.setAbsolutePosition(30f, 15f);
			document.add(imagen2);

			// **********************************************

			document.close();
			writer.close();
			ficheroPdf.close();
			pfd.close();

// ********** compartimos *****************************************
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			String email = "app@tresprovinciassa.com.ar";
			shareIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { email });
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Prespuesto Seguro de Vida (Android)");
			CharSequence seq = Html.fromHtml("Presupuesto de Seguro de Vida. ");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, seq);
			shareIntent.setType("application/pdf");
			shareIntent.putExtra(Intent.EXTRA_STREAM, urifile);
			//		shareIntent.putExtra(Intent.EXTRA_STREAM, f.getAbsolutePath());
			startActivity(Intent.createChooser(shareIntent, "Seleccione correo"));

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}


	//*****************************************************************************


	@Override
	public void onActivityResult(int requestCode, int resultCode,
								 Intent resultData) {

		// The ACTION_OPEN_DOCUMENT intent was sent with the request code
		// READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
		// response to some other intent, and the code below shouldn't run at all.

		if (requestCode == WRITE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			// The document selected by the user won't be returned in the intent.
			// Instead, a URI to that document will be contained in the return intent
			// provided to this method as a parameter.
			// Pull that URI using resultData.getData().
			urifile = null;
			if (resultData != null) {
				urifile = resultData.getData();
				generarPDF();
			}
		}
	}

	//*******************************************************************************************************************************
	private class CargarDatos extends AsyncTask<String, Integer, Void> {
		Context c;
		boolean errores = false;

		public CargarDatos(Context c) {
			this.c = c;
			barra.setIndeterminate(true);
			boton.setEnabled(false);
		}


		@Override
		protected void onPreExecute()
		{
			barra.setVisibility(View.VISIBLE);
			errores = false;

		}

		@Override
		protected Void doInBackground(String... urls)
		{
			try
			{
			//	generarPDF(NOMBRE_DOCUMENTO);
				createFile("text/plain",NOMBRE_DOCUMENTO);


			}
			catch (Exception e)
			{
				e.printStackTrace();
				errores = true;
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void unused)
		{
			try
			{
				if (!errores)
				{
					boton.setEnabled(true);
				}


			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			barra.setVisibility(View.GONE);
		}

	}
//*******************************************************************************************************************************

}
