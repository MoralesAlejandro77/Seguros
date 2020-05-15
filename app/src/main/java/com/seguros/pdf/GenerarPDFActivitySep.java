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
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import com.seguros.presupuestos.Integrantes;
import com.seguros.presupuestos.Librerias;
import com.seguros.presupuestos.R;
import com.seguros.presupuestos.Servicios;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class GenerarPDFActivitySep extends Activity  {
	private static final int WRITE_REQUEST_CODE = 43;
	private Uri urifile = null;

	private final static String NOMBRE_DIRECTORIO = "MiPdf";
	private final static String NOMBRE_DOCUMENTO = "presupuestoSep.pdf";
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
				} else
				{
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
			CargarDatos itesmsrutas = new CargarDatos(GenerarPDFActivitySep.this);
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


	private void generarPDF() {
		// * * * * * * * * * * * * * * * *

		File f = null;
		String tarifa = "";
		try {
			tarifa = this.getIntent().getExtras().getString("tarifa");
		} catch (Exception e) {
		}

		String plan = "";
		try {
			plan = this.getIntent().getExtras().getString("plan");
		} catch (Exception e) {
		}
		String gf = "NO";
		try {
			if (this.getIntent().getExtras().getBoolean("gf"))
			gf = "SI";
		} catch (Exception e) {
		}

		// * * * * * * * * * * * * * * * *
		Bitmap bitmap = null;
		Bitmap bitmap2 = null;
		Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new CMYKColor(255, 0, 0, 0));
		Font TituloFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(255, 255, 255, 255));
		Font sentenciasFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.NORMAL, new CMYKColor(255, 255, 255, 255));
		Document document = new Document();
		try
		{

			ParcelFileDescriptor pfd = GenerarPDFActivitySep.this.getContentResolver().openFileDescriptor(urifile, "w");
			FileOutputStream ficheroPdf = new FileOutputStream(pfd.getFileDescriptor());


			PdfWriter writer = PdfWriter.getInstance(document, ficheroPdf);
			document.open();


			document.addCreationDate();
			document.addCreator("Tres Provincias Seguro de Personas SA");
			document.addTitle("Presupuesto de Seguro de Sepelio");
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

//			Paragraph chapterTitle = new Paragraph("Presupuesto Seguro de Sepelio", blueFont);
//			document.add(chapterTitle);

			//**************************************
			PdfPTable tabla_sep = new PdfPTable(1);
			tabla_sep.setWidthPercentage(100); //Width 100%

			PdfPCell celdasep = new PdfPCell(new Paragraph("Presupuesto Seguro de Sepelio"));
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

			document.add(new Paragraph("TARIFA : " + tarifa,sentenciasFont));
			document.add(new Paragraph("PLAN : " + plan ,sentenciasFont));
			document.add(new Paragraph("GRUPO FAMILIAR: " + gf ,sentenciasFont));

			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));

			PdfPTable tabla = new PdfPTable(5);
			tabla.setWidthPercentage(80); //Width 100%
			tabla.setSpacingBefore(10f); //Space before table
			tabla.setSpacingAfter(10f); //Space after table

			//Set Column widths
			float[] columnWidths = {1f, 1f, 1f, 1f, 1f};
			tabla.setWidths(columnWidths);



			PdfPCell tcell2 = new PdfPCell(new Paragraph("Edad"));
			tcell2.setBorderColor(BaseColor.BLACK);
			tcell2.setPaddingLeft(10);
			tcell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell2.setBackgroundColor(BaseColor.LIGHT_GRAY);

			PdfPCell tcell3 = new PdfPCell(new Paragraph("Sepelio"));
			tcell3.setBorderColor(BaseColor.BLACK);
			tcell3.setPaddingLeft(10);
			tcell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell3.setBackgroundColor(BaseColor.LIGHT_GRAY);

			PdfPCell tcell4 = new PdfPCell(new Paragraph("Parcela"));
			tcell4.setBorderColor(BaseColor.BLACK);
			tcell4.setPaddingLeft(10);
			tcell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell4.setBackgroundColor(BaseColor.LIGHT_GRAY);

			PdfPCell tcell5 = new PdfPCell(new Paragraph("Luto"));
			tcell5.setBorderColor(BaseColor.BLACK);
			tcell5.setPaddingLeft(10);
			tcell5.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell5.setBackgroundColor(BaseColor.LIGHT_GRAY);

			PdfPCell tcell6 = new PdfPCell(new Paragraph("Subtotal"));
			tcell6.setBorderColor(BaseColor.BLACK);
			tcell6.setPaddingLeft(10);
			tcell6.setHorizontalAlignment(Element.ALIGN_CENTER);
			tcell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			tcell6.setBackgroundColor(BaseColor.LIGHT_GRAY);

			tabla.addCell(tcell2);
			tabla.addCell(tcell3);
			tabla.addCell(tcell4);
			tabla.addCell(tcell5);
			tabla.addCell(tcell6);


			PdfPCell celdaedad, celdasepelio, celdaparcela, celdaluto, celdapremio;

			ArrayList<Integrantes> Vecdatos = new ArrayList<Integrantes>();
			Vecdatos = Datos.getItemsTmp(Integer.valueOf(tarifa), getApplicationContext(),plan,this.getIntent().getExtras().getBoolean("parcela"),this.getIntent().getExtras().getBoolean("luto"),this.getIntent().getExtras().getBoolean("gf"));

			if (Vecdatos.size()>0) {
				for (int i = 0; i < Vecdatos.size(); i++) {

					celdaedad = new PdfPCell(new Paragraph(String.valueOf(Vecdatos.get(i).getEdad())));
					celdaedad.setBorderColor(BaseColor.BLACK);
					celdaedad.setPaddingLeft(10);
					celdaedad.setHorizontalAlignment(Element.ALIGN_CENTER);
					celdaedad.setVerticalAlignment(Element.ALIGN_MIDDLE);

					celdasepelio = new PdfPCell(new Paragraph(String.format("%.02f", Vecdatos.get(i).getSepelio())));
					celdasepelio.setBorderColor(BaseColor.BLACK);
					celdasepelio.setPaddingLeft(10);
					celdasepelio.setHorizontalAlignment(Element.ALIGN_RIGHT);
					celdasepelio.setVerticalAlignment(Element.ALIGN_MIDDLE);


					celdaparcela = new PdfPCell(new Paragraph(String.format("%.02f", Vecdatos.get(i).getParcela())));
					celdaparcela.setBorderColor(BaseColor.BLACK);
					celdaparcela.setPaddingLeft(10);
					celdaparcela.setHorizontalAlignment(Element.ALIGN_RIGHT);
					celdaparcela.setVerticalAlignment(Element.ALIGN_MIDDLE);

					celdaluto = new PdfPCell(new Paragraph(String.format("%.02f", Vecdatos.get(i).getLuto())));
					celdaluto.setBorderColor(BaseColor.BLACK);
					celdaluto.setPaddingLeft(10);
					celdaluto.setHorizontalAlignment(Element.ALIGN_RIGHT);
					celdaluto.setVerticalAlignment(Element.ALIGN_MIDDLE);

					celdapremio = new PdfPCell(new Paragraph(String.format("%.02f", Vecdatos.get(i).getPremio())));
					celdapremio.setBorderColor(BaseColor.BLACK);
					celdapremio.setPaddingLeft(10);
					celdapremio.setHorizontalAlignment(Element.ALIGN_RIGHT);
					celdapremio.setVerticalAlignment(Element.ALIGN_MIDDLE);


//					tabla.addCell(Vecdatos.get(i).getFechanac());
					tabla.addCell(celdaedad);
					tabla.addCell(celdasepelio);
					tabla.addCell(celdaparcela);
					tabla.addCell(celdaluto);
					tabla.addCell(celdapremio);
				}
				tabla.addCell("");
				tabla.addCell("");
				tabla.addCell("");
				tabla.addCell("");
				tabla.addCell("");

				tabla.addCell("PREMIO MENSUAL");
				tabla.addCell("");
				tabla.addCell("");
				tabla.addCell("");

				PdfPCell celdatotal = new PdfPCell(new Paragraph(this.getIntent().getExtras().getString("total")));
				celdatotal.setBorderColor(BaseColor.BLACK);
				celdatotal.setPaddingLeft(10);
				celdatotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
				celdatotal.setVerticalAlignment(Element.ALIGN_MIDDLE);


				tabla.addCell(celdatotal);
			}

			document.add(tabla);

			//**************************************
			PdfPTable tabla_pie = new PdfPTable(1);
			tabla_pie.setWidthPercentage(100); //Width 100%

			String ptotal = this.getIntent().getExtras().getString("total");

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
			shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Prespuesto Seguro de Sepelio (Android)");
			CharSequence seq = Html.fromHtml("Presupuesto de Seguro de Sepelio. ");
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
