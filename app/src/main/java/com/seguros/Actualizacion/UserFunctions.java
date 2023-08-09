package com.seguros.Actualizacion;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.seguros.Cuentas.Cuentas;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFunctions {

	private JSONParser jsonParser;
//	private static String URL_SERVER = "http://www.sistemas77.com.ar/Seguros/";

//    public static String URL_SERVER = "https://tresprovinciassa.com.ar/Seguros/ws/";
	public static String URL_SERVER = "http://seguros.tresprovinciassa.com.ar/";



//	public static String URL_SERVER = "http://190.210.98.74/Seguros/ws/";
//	public static String URL_SERVER = "http://192.168.1.77:5680/Seguros/";
    public static String loginURL     = URL_SERVER + "PreciosSepelio.php";
    public static String loginURL1    = URL_SERVER + "RegistroVend.php";
    public static String loginURL2    = URL_SERVER + "ActivaApp.php";
    public static String loginURL3    = URL_SERVER + "LoginApp.php";
    public static String loginURL4    = URL_SERVER + "RegisterUser.php";
    public static String loginURL5    = URL_SERVER + "TablaControl.php";
    public static String loginURL6    = URL_SERVER + "PreciosVida.php";
    public static String loginURL7    = URL_SERVER + "PreciosGastos.php";
    public static String loginURL8    = URL_SERVER + "PreciosCapital.php";
    public static String loginURL9    = URL_SERVER + "VerificaUser.php";
    public static String loginURL10   = URL_SERVER + "ListVendedores.php";
    public static String loginURL11   = URL_SERVER + "ListTarifas.php";
    public static String loginURL12   = URL_SERVER + "ListVendedoresD.php";
    public static String loginURL13   = URL_SERVER + "BuscaDatosCocheria.php";
    public static String loginURL14   = URL_SERVER + "verifemp.php";
    public static String loginURL15   = URL_SERVER + "LoginAsegurado.php";
	public static String loginURL16   = URL_SERVER + "RegAsegurado.php";
    public static String loginURL17   = URL_SERVER + "LeerAsegurado.php";
    public static String loginURL18   = URL_SERVER + "ModAsegurado.php";
    public static String loginURL19   = URL_SERVER + "ModAsegurado2.php";
    public static String loginURL20   = URL_SERVER + "Solicitud.php";
    public static String loginURL21   = URL_SERVER + "Blanqueo.php";
    public static String loginURL22   = URL_SERVER + "LoginAseguradoP.php";
	public static String loginURL23   = URL_SERVER + "ConsultaPremio.php";
	public static String loginURL24   = URL_SERVER + "ListAsegurados.php";
	public static String loginURL25   = URL_SERVER + "LeerAsegurado2.php";
	public static String loginURL26   = URL_SERVER + "ModAsegurado3.php";
    public static String URL_TOKEN    = URL_SERVER + "token.php";
	public static String loginURL27   = URL_SERVER + "PreciosItems.php";
	public static String loginURL28   = URL_SERVER + "ListPlanes.php";

	public static String loginURL29   = URL_SERVER + "ModPrecios.php";
	public static String loginURL30   = URL_SERVER + "ModPrecios2.php";
	public static String loginURL31   = URL_SERVER + "AltaPrecios.php";
	public static String loginURL32   = URL_SERVER + "EliPrecios.php";
    public static String loginURL33   = URL_SERVER + "Blanqueov.php";
    public static String loginURL34   = URL_SERVER + "consultaTopes.php";

	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}

	/**
	 * @param idunico ***************************************************************************/
	public JSONArray ListadePreciosSepelio(String tags, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray ListadePreciosVida(String tags, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL6, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray consultavendedores(String tags, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL10, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray ConsultaDispositivosD(String tags, String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL12, params);
		return json;
	}



	/*****************************************************************************/
	public JSONArray ListadePreciosGastos(String tags, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL7, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray ListadePreciosCapital(String tags, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL8, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray ActivarAplicacion(String clave, String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("clave", clave));
		params.add(new BasicNameValuePair("id"   , id));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL2, params);
		return json;
	}
	/**
	 * @param idunico 
	 * @param cuentas 
	 * @param version ***************************************************************************/
	public JSONArray LoginAplicacion(String clave, String id, Cuentas cuentas, String idunico, String version, String app){
/*		Encriptar parametros = new Encriptar();
		parametros.agrega_Lista("clave"     , clave);
		parametros.agrega_Lista("id"        , id);
		parametros.agrega_Lista("idunico"   , idunico);
		parametros.agrega_Lista("cuenta"    , cuentas.getNombre());
		parametros.agrega_Lista("version"   , version);
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL3, parametros.get_Lista());
*/
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("clave", clave));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		params.add(new BasicNameValuePair("cuenta"   , cuentas.getNombre()));
		params.add(new BasicNameValuePair("version"   , version));
		params.add(new BasicNameValuePair("tag","3P197792S"));
		params.add(new BasicNameValuePair("app"   , app));

		
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL3, params);
		return json;
	}
//*******************************************************************************************************************************
	public JSONArray AltaVendedores(String nombre, String clave, String id, Cuentas cuentas, String idunico, String version, int accion, int estado, int perfil){

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("clave", clave));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("nombre", nombre));
		params.add(new BasicNameValuePair("idunico", idunico));
		params.add(new BasicNameValuePair("cuenta"   , cuentas.getNombre()));
		params.add(new BasicNameValuePair("version"   , version));
		params.add(new BasicNameValuePair("tag","3P197792S"));
		params.add(new BasicNameValuePair("accion",String.valueOf(accion)));
		params.add(new BasicNameValuePair("perfil",String.valueOf(perfil)));
		int est = estado;
		if (accion == 1)
				est = 1;
		params.add(new BasicNameValuePair("estado",String.valueOf(est)));


		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL1, params);
		return json;
	}


	/**
	 * @param version ***************************************************************************/
	public JSONArray RegistrarUser(String id, String idunico, Cuentas cuentas, String version) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		params.add(new BasicNameValuePair("cuenta"   , cuentas.getNombre()));
		params.add(new BasicNameValuePair("version"   , version));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL4, params);
		return json;
	}

	/**
	 * @param idunico 
	 * @param id ***************************************************************************/
	public JSONArray ListaControl(String tags, String producto, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("producto", producto));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL5, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray VerificaUserActivo(String id, Cuentas cuentas, String idunico, String version, String app){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id"   , id));
		params.add(new BasicNameValuePair("idunico", idunico));
		params.add(new BasicNameValuePair("cuenta"   , cuentas.getNombre()));
		params.add(new BasicNameValuePair("version"   , version));
		params.add(new BasicNameValuePair("app"   , app));

		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL9, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray ListadeTarifas(String tags, String id, String idunico){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("idunico", idunico));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL11, params);
		return json;
	}
	/*****************************************************************************/
	public JSONArray BuscarDatosCocheria(String tags, String sexo, String dni, String id){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tags", tags));
		params.add(new BasicNameValuePair("sexo", sexo));
		params.add(new BasicNameValuePair("dni", dni));
		params.add(new BasicNameValuePair("id", id));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL13, params);
		return json;
	}
	/*****************************************************************************/
    public JSONArray verifemp(String id, String tags){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tags", tags));
        params.add(new BasicNameValuePair("id", id));
        JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL14, params);
        return json;
    }
    /*****************************************************************************/
	public JSONArray LoginAsegurado(Context c, String tipodoc, String dni, String clave,String version, String app){
/*		Encriptar parametros = new Encriptar();
		parametros.agrega_Lista("clave"     , clave);
		parametros.agrega_Lista("id"        , id);
		parametros.agrega_Lista("idunico"   , idunico);
		parametros.agrega_Lista("cuenta"    , cuentas.getNombre());
		parametros.agrega_Lista("version"   , version);
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL3, parametros.get_Lista());
*/
		SharedPreferences Preferences;
		Preferences = PreferenceManager.getDefaultSharedPreferences(c);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("clave", clave));
		params.add(new BasicNameValuePair("dni", dni));
		params.add(new BasicNameValuePair("tipodoc", tipodoc));
		params.add(new BasicNameValuePair("version"   , version));
		params.add(new BasicNameValuePair("tag","3P197792S"));
		params.add(new BasicNameValuePair("app"   , app));
		params.add(new BasicNameValuePair("token"        ,  Preferences.getString("token", "")));




		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL15, params);
		return json;
	}
//*******************************************************************************************************************************
public JSONArray RegistrarAsegurado(String tipodoc, String dni, String sexo, String apellido, String nombre,String fechanac,String caract,String celular,String compania,String password, String mail, String version, String app){
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("tipodoc", tipodoc));
	params.add(new BasicNameValuePair("dni", dni));
	params.add(new BasicNameValuePair("sexo", sexo));

	params.add(new BasicNameValuePair("apellido", apellido));
	params.add(new BasicNameValuePair("nombre", nombre));
	params.add(new BasicNameValuePair("fechanac", fechanac));
	params.add(new BasicNameValuePair("caract", caract));
	params.add(new BasicNameValuePair("celular", celular));
	params.add(new BasicNameValuePair("compania", compania));
	params.add(new BasicNameValuePair("clave", password));
	params.add(new BasicNameValuePair("mail", mail));


	params.add(new BasicNameValuePair("version"   , version));
	params.add(new BasicNameValuePair("tag","3P197792S"));
	params.add(new BasicNameValuePair("app"   , app));


	JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL16, params);
	return json;
}
//*******************************************************************************************************************************
    public JSONArray LeerDatosAsegurado(String dni, int tipodoc) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tipodoc", String.valueOf(tipodoc)));
		params.add(new BasicNameValuePair("dni", dni));
		params.add(new BasicNameValuePair("tag","3P197792S"));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL17, params);
		return json;
    }
//*******************************************************************************************************************************
public JSONArray ModAsegurado(String tipodoc, String dni, String car, String cel, String carp, String telp, String compania,
							  String provincia,String departament, String cp, String loc, String call , String pis , String dept){
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("tag","3P197792S"));
	params.add(new BasicNameValuePair("tipodoc", tipodoc));
	params.add(new BasicNameValuePair("dni", dni));
	params.add(new BasicNameValuePair("caract", car));
	params.add(new BasicNameValuePair("celular", cel));
	params.add(new BasicNameValuePair("caractp", carp));
	params.add(new BasicNameValuePair("telefono", telp));
	params.add(new BasicNameValuePair("compania", compania));
	params.add(new BasicNameValuePair("provincia", provincia));
	params.add(new BasicNameValuePair("departamento", departament));
	params.add(new BasicNameValuePair("cp", cp));
	params.add(new BasicNameValuePair("localidad", loc));
	params.add(new BasicNameValuePair("calle", call));
	params.add(new BasicNameValuePair("piso", pis));
	params.add(new BasicNameValuePair("depto", dept));

	JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL18, params);
	return json;
}
//*******************************************************************************************************************************
public JSONArray ModAsegurado2(String tipodoc, String dni, String cbu, String banco, String dias){
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("tag","3P197792S"));
	params.add(new BasicNameValuePair("tipodoc", tipodoc));
	params.add(new BasicNameValuePair("dni", dni));
	params.add(new BasicNameValuePair("cbu", cbu));
	params.add(new BasicNameValuePair("banco", banco));
	params.add(new BasicNameValuePair("dias", dias));

	JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL19, params);
	return json;
}
//*******************************************************************************************************************************
public JSONArray SolicitudPoliza(String tipodoc, String dni, String comentario, String titulo){
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("tag","3P197792S"));
	params.add(new BasicNameValuePair("tipodoc", tipodoc));
	params.add(new BasicNameValuePair("dni", dni));
	params.add(new BasicNameValuePair("comentario", comentario));
	params.add(new BasicNameValuePair("titulo", titulo));

	JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL20, params);
	return json;
}
//*******************************************************************************************************************************
public static String getUrlToken() {
	return URL_TOKEN;
}
//********************************************************************************
public static String Registrar_token(Context c,String tipodoc, String dni) {
		SharedPreferences Preferences;
		HttpClient httpclient = new DefaultHttpClient();
		Preferences = PreferenceManager.getDefaultSharedPreferences(c);

		HttpPost httppost = new HttpPost(URL_SERVER+"token.php");
		String text = null;
		try {


			List<NameValuePair> params = new ArrayList<NameValuePair>();

			params.add(new BasicNameValuePair("tag","3P197792S"));
			params.add(new BasicNameValuePair("tipodoc", tipodoc));
			params.add(new BasicNameValuePair("dni", dni));
			params.add(new BasicNameValuePair("token"        ,  Preferences.getString("token", "")));

			httppost.setEntity(new UrlEncodedFormEntity(params));

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity ent = response.getEntity();
			text = EntityUtils.toString(ent);
			Log.e("rutinas",text);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return text;

	}

/*********************************************************************************************************************************/

	public JSONArray BlanqueoClave(String tipodoc, String dni){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag","3P197792S"));
		params.add(new BasicNameValuePair("tipodoc", tipodoc));
		params.add(new BasicNameValuePair("dni", dni));
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL21, params);
		return json;
	}
//*******************************************************************************************************************************
	public JSONArray LoginAsegurado_p(Context c, String tipodoc, String dni, String clave,String version, String app, String clavep){
/*		Encriptar parametros = new Encriptar();
		parametros.agrega_Lista("clave"     , clave);
		parametros.agrega_Lista("id"        , id);
		parametros.agrega_Lista("idunico"   , idunico);
		parametros.agrega_Lista("cuenta"    , cuentas.getNombre());
		parametros.agrega_Lista("version"   , version);
		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL3, parametros.get_Lista());
*/
		SharedPreferences Preferences;
		Preferences = PreferenceManager.getDefaultSharedPreferences(c);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("dni"    , dni));
		params.add(new BasicNameValuePair("tipodoc", tipodoc));
		params.add(new BasicNameValuePair("version", version));
		params.add(new BasicNameValuePair("tag"    ,"3P197792S"));
		params.add(new BasicNameValuePair("app"    , app));
		params.add(new BasicNameValuePair("token"  ,  Preferences.getString("token", "")));
		params.add(new BasicNameValuePair("clavep" , clavep));
		params.add(new BasicNameValuePair("clave"  , clave));

		JSONArray json = jsonParser.getJSONArrayFromUrl(loginURL22, params);
		return json;
	}
//*******************************************************************************************************************************

}




