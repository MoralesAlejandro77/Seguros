package Seguridad;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.seguros.Actualizacion.JSONParser;

public class  Encriptar {
	private List<NameValuePair> paramametros;
	private static MCrypt mcryptador;
	private static String TAG_APPLICATIONS  = "3P197792S";
	
	// constructor
	public Encriptar(){
		paramametros = new ArrayList<NameValuePair>();
		paramametros.add(new BasicNameValuePair("tag",encripta(TAG_APPLICATIONS)));
		mcryptador   = new MCrypt();
	}	
	
/****************************************************************************/	
	public static String encriptar_Valor(String valor){
		String valor_encriptado     = "";
		
		MCrypt mcrypt = new MCrypt();
		try {
			valor_encriptado  = MCrypt.bytesToHex( mcrypt.encrypt(valor));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return valor_encriptado;	
	}
	/****************************************************************************/	
public static String encripta(String valor){
	String valor_encriptado     = "";
	
	try {
		valor_encriptado  = MCrypt.bytesToHex( mcryptador.encrypt(valor));
	} catch (Exception e) {
		e.printStackTrace();
	}	
	
	return valor_encriptado;	
}
/****************************************************************************/	
public void agrega_Lista(String nombre, String valor){
	paramametros.add(new BasicNameValuePair(nombre,encripta(valor)));
}
/****************************************************************************/	
public List<NameValuePair> get_Lista(){
	return paramametros;	
}
/****************************************************************************/	
}
