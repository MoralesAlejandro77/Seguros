package com.seguros.presupuestos;
/***********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Fecha 02-12-2014
 * Version : 1.1
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seguros.Cuentas.Cuentas;
import com.seguros.Datos.DatosBDTablas;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Librerias {
static public String Nro_Whatsapp = "+5492616738000";
static public String Nro_0800     = "08102223778";


//******************************************************************************************************************************* 
	
public static int calcular_edad(String fecha) {
	int anios = 0;
	
	String[] datos = fecha.split("/");
	
	if (datos.length == 3)
	{
		int dia = Integer.valueOf(datos[0]);
		int mes = Integer.valueOf(datos[1]);
		int ano = Integer.valueOf(datos[2]);
		
		final Calendar calendar = Calendar.getInstance();
		int ano_actual = calendar.get(Calendar.YEAR);
		int mes_actual = calendar.get(Calendar.MONTH) + 1;
		int dia_actual = calendar.get(Calendar.DAY_OF_MONTH);
	try {
		anios = ano_actual - ano;
		if (mes > mes_actual)
			anios -=1;
		if (mes == mes_actual && dia > dia_actual)
			anios -=1;
		
	   } 
	catch (Exception e) 
	   {
		anios  = 0;
	   }
	}
	return anios;

	}

//******************************************************************************************************************************* 
protected static boolean hay_error_fecha(String fecha) {
	boolean error = false;
	
	final Calendar calendar = Calendar.getInstance();
	int ano_actual = calendar.get(Calendar.YEAR);
	
	String[] datos = fecha.split("/");
	
	if (datos.length == 3)
	{
		int dd = Integer.valueOf(datos[0]);
		int mm = Integer.valueOf(datos[1]);
		int yy = Integer.valueOf(datos[2]);
	
	if (yy > ano_actual)	
		error = true;
	
	
	if (yy < 1900 || yy > 2100)
		error = true;

	if (mm < 1 || mm > 12)
		error = true;

	if (dd < 1 || dd > 31)
		error = true;

	if ((mm == 4 || mm == 6 || mm == 9 || mm == 11) && (dd > 30))
		error = true;

  boolean bisiesto = es_biciesto(yy);

	if (mm == 2)
	    if (bisiesto) 
	    {
	       if (dd > 29)
		       error = true;
	    }   
	    else
	    {
		   if (dd > 28)
			   error = true;
		}   	
	}
	else
     error = true;
	
return error;
}

//******************************************************************************************************************************* 
private static boolean es_biciesto(int year) {
	boolean resultado = false;
	if ((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)))
		resultado = true;
	return resultado;
}
//******************************************************************************************************************************* 
public static String CargarFechaSistema() {
	Date fechaActual = new Date();
    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
    String fechaSistema=formateador.format(fechaActual);	
    return fechaSistema;
}
//**************************************************************************************
public static boolean EstaVigente(String fechaSistema, String fechavigencia) {
	boolean resultado = true;
	SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy"); 
	Date fecha_sistema;
	try {
		fecha_sistema = formateador.parse(fechaSistema);
		Date fecha_vigencia = formateador.parse(fechavigencia);
			
		if (fecha_vigencia.before(fecha_sistema))
			resultado = false;
		} catch (ParseException e) {
	     e.printStackTrace();
		}
	 return resultado;	
	}
//**************************************************************************************
public static Cuentas LeerCuentas(Context c) {
int cantidad = 0;
Cuentas   cuentas     = new Cuentas();
cantidad = 0;
try {
	Account[] accounts = AccountManager.get(c).getAccounts();

	  for (Account account : accounts)
	  { 
		if (account.type.equalsIgnoreCase("com.google")) {
			  cuentas.setNombre(account.name);
			  cuentas.setTipo(account.type);
			  cantidad++;
		}
	  }  
	  
	  if (cantidad ==0){
	  	  for (Account account : accounts)
	  	  { 
		  	  if (!account.type.equalsIgnoreCase("com.motorola.ServiceDialingNumbers"))
		  	      {
				  cuentas.setNombre(account.name);
				  cuentas.setTipo(account.type);
				  break;
		  	      }
	  	  } 
	  	  
		  
	  }		  	  
		  	
		  } catch (Exception e) {
			  Log.d("login", "Error!!");
		  }
		  return cuentas;
		  }
/*******************************************************************************************************/
public static String unico_ID(Context c) {
	String idunico = "";
	try {
		final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(c.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

		idunico = deviceUuid.toString();
	} catch (Exception e) {
		Log.d("unico_ID", "Error!!");
	}
  return idunico;
}
/*******************************************************************************************************/
public static String getAndroidVersion() {
    String release = Build.VERSION.RELEASE;
    int sdkVersion = Build.VERSION.SDK_INT;
    return "Android SDK: " + sdkVersion + " (" + release +")";
}

/*******************************************************************************************************/
public static String getAndroidVersion_new(Context context) {
	String deviceID = "";
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		int permissionResult = context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE);
		if (permissionResult == PackageManager.PERMISSION_DENIED) {
			permissionResult = context.checkCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE");
		}
		boolean isPermissionGranted = permissionResult == PackageManager.PERMISSION_GRANTED;
		if (isPermissionGranted) {
			deviceID = getAndroidVersion();
		}
	} else {
		deviceID = getAndroidVersion();
	}

	Log.i("id","getAndroidVersion : " + deviceID);
	return deviceID;
}
	/*******************************************************************************************************/

	public static void ForzarVersion() {
	try {
		 if( Build.VERSION.SDK_INT >= 9){
	         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	         StrictMode.setThreadPolicy(policy); 
	  }
		
	} catch (Exception e) {
		// TODO: handle exception
	}
}
/*******************************************************************************************************/
public static boolean verificaConexion(Context ctx){
    boolean bConectado = false;
    ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo[] redes = connec.getAllNetworkInfo();
    for (int i = 0; i < 2; i++) {
        if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
            bConectado = true;
        }
    }
    return bConectado;
}
/*******************************************************************************************************/
static public void Grabar_act_vida(Context c) {
	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
	sharedPreferences.edit().putBoolean("act_vida"     , true).apply();

}
/***************************************************************************************************************************/
static public void Grabar_act_sep(Context c) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
		sharedPreferences.edit().putBoolean("act_sep"     , true).apply();

}
/***************************************************************************************************************************/
static public boolean Esta_act_vida(Context c) {
		SharedPreferences Preferences =
				PreferenceManager.getDefaultSharedPreferences(c);
		return  Preferences.getBoolean("act_vida", false);
	}
	//********************************************************************************
	static public boolean Esta_act_sep(Context c) {
		SharedPreferences Preferences =
				PreferenceManager.getDefaultSharedPreferences(c);
		return  Preferences.getBoolean("act_sep", false);
	}
	//********************************************************************************
	static public boolean Esta_Tarifa(Context c) {
		SharedPreferences Preferences =
				PreferenceManager.getDefaultSharedPreferences(c);
		boolean esta = Preferences.getBoolean("tarifa", false);
		return  esta;
	}
	//********************************************************************************
	static public void Grabar_Tarifa(Context c) {
		DatosBDTablas db = new DatosBDTablas(c);
		db.open();
		db.CargarTarifas();
		db.close();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
		sharedPreferences.edit().putBoolean("tarifa"     , true).apply();

	}
/***************************************************************************************************************************/
static public void Grabar_Perfil(Context c, String perfil) {
SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        try {
		sharedPreferences.edit().putString("perfil"     , perfil).apply();
	}catch (Exception e) {
		Log.i("Librerias","Error perfil " + e.toString());
	}
}
//********************************************************************************
static public String Leer_Perfil(Context c) {
	SharedPreferences Preferences;
	Preferences = PreferenceManager.getDefaultSharedPreferences(c);
	return Preferences.getString("perfil", "0");
}
//********************************************************************************
static public void Registrar_asegurado(Context c) {
	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
	try {
		sharedPreferences.edit().putBoolean("reg_asegurado"     , true).apply();
	}catch (Exception e) {
		Log.i("Librerias","Error Registro Asegurado " + e.toString());
	}
}
	//********************************************************************************
	static public void Cerrar_sesion(Context c) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
		try {
			sharedPreferences.edit().putBoolean("reg_asegurado"     , false).apply();
			sharedPreferences.edit().putInt("tipodoc"    , 0).apply();
			sharedPreferences.edit().putString("dni"     , "0").apply();

		}catch (Exception e) {
			Log.i("Librerias","Error Registro Asegurado " + e.toString());
		}
	}
	//********************************************************************************

	static public void Registrar_asegurado_id(Context c, int tipodoc, String dni) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
		try {
			sharedPreferences.edit().putInt("tipodoc"    , tipodoc).apply();
			sharedPreferences.edit().putString("dni"     , dni).apply();
		}catch (Exception e) {
			Log.i("Librerias","Error Registro Asegurado " + e.toString());
		}
	}
	//********************************************************************************
	static public String Leer_dni(Context c) {
		SharedPreferences Preferences;
		Preferences = PreferenceManager.getDefaultSharedPreferences(c);
		return Preferences.getString("dni", "0");
	}
	//********************************************************************************
	static public int Leer_tipodoc(Context c) {
		SharedPreferences Preferences;
		Preferences = PreferenceManager.getDefaultSharedPreferences(c);
		return Preferences.getInt("tipodoc", 0);
	}

	//********************************************************************************



	static public boolean Esta_registrado_asegurado(Context c) {
		SharedPreferences Preferences =
				PreferenceManager.getDefaultSharedPreferences(c);
		return  Preferences.getBoolean("reg_asegurado", false);
	}
	//********************************************************************************
	static public boolean es_entero_valido(String n){
		String regex = "\\d+";
		return (n.matches(regex));

	}
//****************************************************************************************
public static boolean isEmailValid(String email) {
	String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(email);
	return matcher.matches();
}
//****************************************************************************************
	static public void mostrar_error(Activity c, int tipo, String mensaje) {

		LayoutInflater inflater = c.getLayoutInflater();
		View layout = inflater.inflate(R.layout.customtoast, (ViewGroup) c.findViewById(R.id.toast_layout_root));

		TextView text     = (TextView) layout.findViewById(R.id.toastText);
		ImageView iconimag = (ImageView) layout.findViewById(R.id.toastImage);
		text.setText(mensaje);
		iconimag.setImageResource(0);

		if (tipo==1) //info
			iconimag.setImageResource(R.mipmap.ic_info_1);


		if (tipo==2) //error
			iconimag.setImageResource(R.mipmap.ic_error_1);

		Toast t = new Toast(c);
		t.setDuration(Toast.LENGTH_LONG);
		t.setView(layout);
		t.show();
	}
/*************************************************************************************************************/

static public void grabar_tokenlocal(Context c, String token) {
	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
	sharedPreferences.edit().putString("token"        , token).apply();
}

	//****************************************************************************************
	static public void mostrar_error2(Activity c, String titulo, String mensaje) {


	}
/*************************************************************************************************************/
static public String UTF8(String palabras) {
	String resultado = palabras;
	try {
		resultado = URLEncoder.encode(palabras, "UTF-8");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	return resultado;
}
//****************************************************************************************
static public String ISO(String palabras) {
	String resultado = palabras;
	try {
		resultado = URLEncoder.encode(palabras, "ISO-8859-1");
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
	return resultado;
}
//****************************************************************************************

	static public void enviarWhatsapp(Context c) {
		String text       = "*MENSAJE DESDE LA APP*: ";
		String toNumber   = Librerias.Nro_Whatsapp;
		toNumber          = toNumber.replace("+", "").replace(" ", "");
		Intent sendIntent = new Intent("android.intent.action.MAIN");
//        sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
		sendIntent.putExtra("jid", toNumber + "@s.whatsapp.net");
		sendIntent.putExtra(Intent.EXTRA_TEXT, text);
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setPackage("com.whatsapp");
		sendIntent.setType("text/plain");
		c.startActivity(sendIntent);
	}
	/**************************************************************************************/

	static public void openWhatsApp(Context c){
		try {
			String text       = "*MENSAJE DESDE LA APP* " ;
			String toNumber = Librerias.Nro_Whatsapp;
			toNumber = toNumber.replace("+", "").replace(" " , "");


			String url = "https://api.whatsapp.com/send?phone="+ toNumber + "&text=" + URLEncoder.encode(text, "UTF-8");

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setPackage("com.whatsapp");
			intent.setData(Uri.parse(url));
			c.startActivity(intent);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	/**************************************************************************************/
	public static File getRuta(Context contexto, String NOMBRE_DIRECTORIO) {
		File ruta = null;
		String path = "";

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            path = contexto.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
		}
		else
		{
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
			}
		}

        ruta = new File(path,NOMBRE_DIRECTORIO);

		if (ruta != null) {
			if (!ruta.mkdirs()) {
				if (!ruta.exists()) {
					return null;
				}
			}
		}


		return ruta;
	}
	/**************************************************************************************/

}

