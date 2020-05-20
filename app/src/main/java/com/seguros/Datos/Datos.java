/***********************************************************************************************************************/	
package com.seguros.Datos;
/***********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Fecha de ultima Actualizacion 10-03-2017
 * Version : 1.4
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/

import android.content.Context;
import android.database.Cursor;

import com.seguros.presupuestos.Integrantes;
import com.seguros.presupuestos.Librerias;
import com.seguros.presupuestos.Servicios;

import java.util.ArrayList;

public class Datos {
	private static final float GASTO_5000 = 5000;
	private static float  capital     = 0;
	private static long   edad        = 0;
	private static int    sexo        = 0;
	private static long   edad_Cony   = 0;
	private static int    sexo_Cony   = 0;
	private static float  seg_muerte  = 0;
	private static float  prima_gasto = 0;
	private static Context contexto;
	private static boolean bparcela, bluto;
	
/***********************************************************************************************************************/	
	public static ArrayList<Servicios> getItems(Context context, float f, Long e, int s,Long e_cony, int s_cony) {
		ArrayList<Servicios> MiLista = new ArrayList<Servicios>();
		capital     = f;
		edad        = e;
		sexo        = s;
		edad_Cony   = e_cony;
		sexo_Cony   = s_cony;
		prima_gasto = 0;
		contexto    = context;
		int codigo  = 0;
		DatosBDTablas db = new DatosBDTablas(context);
		db.open(); 
		
		/**{"codigo", "descripcion", "tipo_base", "porc", "valor_base","lectura","tipocalculo", "tipo_mes"}**/
		Cursor datosServicio  = db.ConsultarServicios();
		int contador  = 0;
		seg_muerte    = 0;
		
		int total    = datosServicio.getCount();
		if (total > 0) { 
			datosServicio.moveToFirst();
		    do {
		    	boolean lectura = false;
		    	if (datosServicio.getInt(5)==1) // Si es solo lectura, No puede modificarse
		    		lectura = true;
		    	
		    	float base = 0;
		    	if (datosServicio.getInt(2) == 1) // Capital
		    		base = capital*datosServicio.getFloat(3)/100;
		    	else
		    		base = datosServicio.getFloat(4); // valor definido fijo (Ultimos Gastos)
		    	
		    	codigo = datosServicio.getInt(0);  // codigo del servicio
		    	
		    	if (codigo == 11 && sexo == 2) // ultimos gastos y femenino
		    		codigo = 12;
		    	
		    	long  ed      = edad;
		    	float prima   = 0;
		    	boolean error = false;
		    	prima_gasto   = 0;		    
		    	float valor_prima = 0;
		    	int   tipo_mes   = 1;
		    	
                if (sexo == 1 && edad > 64 && codigo != 1)
		    		  error = true;

                if (codigo == 9) // Adicional x nacimiento
		    	   {		
                   if (sexo == 1 && sexo_Cony == 0)
   		    		  error = true;

                   if (sexo == 2 && edad > 49)
 		    		  error = true;

                   if (sexo_Cony == 2 && edad_Cony > 49)
 		    		  error = true;
                   
                   if (sexo_Cony == 1)
		    		  error = true;
                    
                   if (sexo_Cony > 0 && !error)
                      ed    = edad_Cony;
		    		}
		    	if (!error)
		    	   {
		    		Cursor datosprimas = null;
		    		int valor = 0;
		    		if ((codigo == 1) && (sexo == 1))
  					    valor = 11;
		    		else
  					    valor = codigo;
					
		    		datosprimas = db.obtenerPrima(valor,ed); // {"codigo", "edad", "edad_max", "porc"}
		    		
			    	if (datosprimas.getCount() > 0)
			    	{
				    	valor_prima = datosprimas.getFloat(3);
				    	tipo_mes    = datosServicio.getInt(7);
			    		
			    	    prima       = calcularPrima(valor_prima,base, datosServicio.getInt(6),tipo_mes);
			    	}
			    	
		    	   }
		    	
				MiLista.add(new Servicios( datosServicio.getInt(0), datosServicio.getString(1), base, prima,lectura,false,prima_gasto,valor_prima,tipo_mes));
				if (++contador==1)
				   seg_muerte = prima;
				
		    } while(datosServicio.moveToNext());
		    datosServicio.close();
		}
		db.close();
		return MiLista;
	}



	//****************************************************************************************************************************************	//
	private static float calcularPrima(float porc, float base, int tipocalculo, int tipomes) {
		float prima  = 0;
		int redondeo = 0;

	
		if (porc > 0) 
		{ 
		  if (tipocalculo==1) // por capital
				prima = capital*porc/tipomes;

		  if (tipocalculo==2) // por seg. muerte
				prima = (seg_muerte*porc/100)/tipomes;
		  
		  if (tipocalculo==3) // Base
				prima = base*porc/tipomes;
		  
		}
//		redondeo = (int) Math.round(prima);
		return prima;
	}
	//****************************************************************************************************************************************	
		
/**   CALCULO DE ADICIONALES
 * 
 	 MUERTE -> BUSCAR_TABLA(EDAD,COL(2)MASC O COL(3) Fem)*CAPITAL / 12
	 INVALIDEZ TOTAL Y PERMANENTE BUSCAR_TABLA(EDAD,COL(4))*CAPITAL / 12
	 DOBLE POR ACCIDENTE -> BUSCAR_TABLA(EDAD,COL(6))*CAPITAL / 12
	 PERDIDAS PARCIALES POR ACCIDENTE -> BUSCAR_TABLA(EDAD,COL(7)0,0014)*CAPITAL / 12
	 ADICIONAL ENFERMED. TERMINALES -> =PRIMA_MUERTE*1%
	 ENFERMEDADES CRITICAS -> =PRIMA_MUERTE*3% 
	 DIAGNOSTICO DE SIDA -> =CAPITAL*0,4% / 12                                      
	 CONTAGIO ACCIDENTAL DE SIDA -> =CAPITAL*0,2% / 12                                           
	 ADICIONAL POR NACIMIENTO -> =
	                              SI (MASC = MASC)
	                                  NO CUBRE
	                              SI(TIT_FEM  Y EDAD > 49) 
                                      NO CUBRE
	 		                      SI(TIT_MASC Y EDAD > 65) 
	 		                          NO CUBRE
	                              SI(CONY_FEM  Y EDAD > 49) 
                                      NO CUBRE
	 		                      SI(CONY_MASC Y EDAD > 65) 
	 		                          NO CUBRE
	 		                      SI CAPITAL VACIO O NO SELECC
	 		                         ""
	 		                      SINO
	 		                      -> BUSCAR_TABLA(EDAD,COL(8))*BASE/ 12   
	 		                             
	 		                          
	 RENTA DIARIA POR INTERNACION -> BUSCAR_TABLA(EDAD,COL(9))*BASE / 12                                        
	 ULTIMOS GASTOS -> SI TITULAR_MASC
	                      BUSCAR_TABLA(EDAD,COL(2)MASC)*BASE/ 12		
	                   SI TITULAR_FEMEN   
	                      BUSCAR_TABLA(EDAD,COL(3)FEM)*BASE/ 12		
	                   
 */

//****************************************************************************************************************************************	//
	public static ArrayList<Integrantes> getItemsTmp(int tarifa,Context context, String p, boolean pa, boolean lu, boolean gf) {
	String plan;	
		ArrayList<Integrantes> MiLista = new ArrayList<Integrantes>();
		plan = p;
		contexto = context;
		bparcela = pa;
		bluto    = lu;
		DatosBDTablas db = new DatosBDTablas(contexto);
		db.open();
		int contador = 0;
		float sepelio = 0;
		float parcela = 0;
		float luto    = 0;
		float premio  = 0;

		Cursor datos  = db.ConsultarTmpPlanes();
		
		int total    = datos.getCount();

		if (total > 0) { 
			datos.moveToFirst();
			contador =1;
			float [] valores = new float[0];
			do {
				int   edad = datos.getInt(2);
				if (gf)
				   {
					if (contador == 1)
					   {
						   valores = Datos.calcular_prima_sep(tarifa, db, 999, plan);
						   sepelio = valores[0];
						   parcela = valores[1];
						   luto    = valores[2];
						   premio  = valores[3];
					   }
					  else
					   {
						   sepelio = 0;
						   parcela = 0;
						   luto    = 0;
						   premio  = 0;
					   }

				   }
				else
				   {
					   valores = Datos.calcular_prima_sep(tarifa, db, edad, plan);
					   sepelio = valores[0];
					   parcela = valores[1];
					   luto    = valores[2];
					   premio  = valores[3];

				   }

		    	MiLista.add(new Integrantes( datos.getInt(0), datos.getString(1), edad, luto,parcela,sepelio,premio));
				contador ++;
		    } 
			while(datos.moveToNext());
            datos.close();
		}
		db.close();
		return MiLista;	
		}
//************************************************************************************************
	public static float []  calcular_prima_sep(int tarifa, DatosBDTablas db, int e, String plan) {
		float [] valores = new float[4];		
		float sepelio = 0;
		float parcela = 0;
		float luto    = 0;
		float premio  = 0;
		Cursor datosprimas = null;
		datosprimas = db.obtenerPlan(tarifa,e, plan,1); // Sepelio
    	if (datosprimas.getCount() > 0)
    		sepelio = datosprimas.getFloat(4);

    	if (bparcela) // Parcela
        {
        	datosprimas = db.obtenerPlan(tarifa,e, plan,2);
        	if (datosprimas.getCount() > 0)
        		parcela = datosprimas.getFloat(4);
        }

        if (bluto) // Luto
        {
        	datosprimas = db.obtenerPlan(tarifa,e, plan,3);
        	if (datosprimas.getCount() > 0)
        		luto = datosprimas.getFloat(4);
        	
        }
    	
    	premio = sepelio + parcela + luto;
		valores[0] = sepelio;
		valores[1] = parcela;
		valores[2] = luto;
		valores[3] = premio;

    	return valores;
	}
	
	//******************************************************************************************************************************* 
	public static float []  calcular_sepelio_completo(int tarifa, DatosBDTablas db, int e, String plan) {
		float [] valores = new float[4];
		float sepelio = 0;
		float parcela = 0;
		float luto    = 0;
		float premio  = 0;
		Cursor datosprimas = null;
		datosprimas = db.obtenerPlan(tarifa,e, plan,1); // Sepelio
		if (datosprimas.getCount() > 0)
			sepelio = datosprimas.getFloat(4);


			datosprimas = db.obtenerPlan(tarifa,e, plan,2);
			if (datosprimas.getCount() > 0)
				parcela = datosprimas.getFloat(4);


			datosprimas = db.obtenerPlan(tarifa,e, plan,3);
			if (datosprimas.getCount() > 0)
				luto = datosprimas.getFloat(4);


		premio = sepelio + parcela + luto;
		valores[0] = sepelio;
		valores[1] = parcela;
		valores[2] = luto;
		valores[3] = premio;

		return valores;
	}
/*******************************************************************************************************************/

	public static boolean Verifica_clave(Context c, String valor) {
		boolean resultado = false;
		
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		Cursor datos  = db.ConsultarClave();
		
		int total    = datos.getCount();
		if (total > 0) 
		{ 
			datos.moveToFirst();
		    if (datos.getString(1).equals(valor))
		    	{
		    		resultado = true;
		    		db.RegistraActivacion();
		    	}
            datos.close();
		}
		db.close();	           
		return resultado;
		}
	//******************************************************************************************************************************* 
	public static boolean Esta_Activadax(Context c) {
		boolean resultado = false;
		int activado = 0;
		
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		Cursor datos  = db.ConsultarClave();
		
		int total    = datos.getCount();
		if (total > 0) 
		{ 
			datos.moveToFirst();
			activado = datos.getInt(2);
		    if (activado == 1)
		        resultado = true;
            datos.close();
		}
		db.close();	           
		return resultado;
		}
//******************************************************************************************************************************* 
	public static boolean Esta_Logueada(Context c) {
		boolean resultado = false;
		int login = 0;
		
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		Cursor datos  = db.ConsultarClave();
		
		int total    = datos.getCount();
		if (total > 0) 
		{ 
			datos.moveToFirst();
			login = datos.getInt(3);
		    if (login == 1)
		        resultado = true;
            datos.close();
		}
		db.close();	           
		return resultado;
		}
//******************************************************************************************************************************* 
public static boolean Esta_Logueadaemp(Context c) {
	boolean resultado = false;
	int perfil = 0;
	perfil = Integer.valueOf(Librerias.Leer_Perfil(c));
    if (perfil > 0)
    	resultado = true;
	return resultado;
}
	//*******************************************************************************************************************************
	public static String Obtener_id_login(Context c) {
		String resultado = "";
		
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		Cursor datos  = db.ConsultarClave();
		
		int total    = datos.getCount();
		if (total > 0) 
		{ 
			datos.moveToFirst();
			resultado = datos.getString(4);
            datos.close();
		}
		db.close();	           
		return resultado;
		}
//******************************************************************************************************************************* 
	public static String[] Obtener_Gastos(Context c) {
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		String[] datos  = db.ConsultarGastos();
		db.close();	           
		return datos;
		}
//******************************************************************************************************************************* 
	public static String[] Obtener_Capital(Context c) {
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		String[] datos  = db.ConsultarCapital();
		db.close();	           
		return datos;
		}
//******************************************************************************************************************************* 
	public static boolean Registro_Log(Context c) {
		DatosBDTablas db = new DatosBDTablas(c);
		db.open(); 		
		boolean resultado  = db.AgregaFecha(c);
		db.close();	           
		return resultado;
		}
//******************************************************************************************************************************* 
public static String[] Obtener_Tarifas(Context c) {
	DatosBDTablas db = new DatosBDTablas(c);
	db.open();
	String[] datos  = db.ConsultarTarifas();
	db.close();
	return datos;
}
//*******************************************************************************************************************************

}
