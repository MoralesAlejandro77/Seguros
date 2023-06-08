/***********************************************************************************************************************/	
package com.seguros.Datos;
/***********************************************************************************************************************/	
/*
 * Programador: Ing. Alejandro Morales
 * Producciones Ale77
 * Fecha de ultima Actualizacion 06-03-2015
 * Version : 1.4
 * Modulo: Presupuestador Seguro de Vida y Sepelio
 * Empresa : TRES PROVINCIAS SEGUROS - MENDOZA
 * 
 */
/***********************************************************************************************************************/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.seguros.presupuestos.Librerias;
/***********************************************************************************************************************/	
/**
 *
 * @author ALE77
 */
/***********************************************************************************************************************/	
public class DatosBDTablas {
private BaseDatos dbHelper;
private SQLiteDatabase db;
private final Context contexto;
public static final String TAG = "DATOS";   

//**************************************************************************************
public DatosBDTablas(Context contexto) 
{
this.contexto = contexto;
}
//**************************************************************************************
public SQLiteDatabase open() throws SQLException 
{
dbHelper = new BaseDatos(contexto);
db = dbHelper.getWritableDatabase();
return db;
}
//**************************************************************************************
public void close() 
{
dbHelper.close();
}
//**************************************************************************************
public long AgregarServicio(int codigo, String descripcion, int tipo_base, float porc, float valor_base, int edad_max, boolean lectura, int tipocalculo, int tipomes) 
{
	ContentValues registro = new ContentValues();
	registro.put("codigo"     , codigo);
	registro.put("descripcion", descripcion);
	registro.put("tipo_base"  , tipo_base);
	registro.put("porc"       , porc);
	registro.put("valor_base" , valor_base);
	registro.put("edad_max"   , edad_max);
	registro.put("tipocalculo", tipocalculo);
	registro.put("tipo_mes"   , tipomes);
	
	int lect = 0;
	if (lectura)
		lect = 1;
	 
	registro.put("lectura", lect);
	
	return db.insert(BaseDatos.TABLA_SERVICIOS, null, registro);
}
//**************************************************************************************
public Cursor ConsultarServicios() {
	if (obtenerCantidadServicios()==0)
	   {
		CargarServicios();
		CargarPrimas();	
		CargarPlanes();
		CargarGastos();
		CargarCapital();
		CargarTarifas();
		AgregarClave("");
	//	CargarComplejidad();

	//	AgregarLog();
	   }
	Cursor registros = db.query(BaseDatos.TABLA_SERVICIOS, new String[] {
	"codigo", "descripcion", "tipo_base", "porc", "valor_base","lectura","tipocalculo", "tipo_mes"}, null, null, null, null, "codigo asc");
	return registros;
} 
private void AgregarLog() {
String fechaSistema = Librerias.CargarFechaSistema();
ContentValues registro = new ContentValues();
registro.put("fecha"    , fechaSistema);
db.insert(BaseDatos.TABLA_LOG, null, registro);
}
//**************************************************************************************
public Cursor obtenerServicio(long id) throws SQLException {
Cursor registro = db.query(true, BaseDatos.TABLA_SERVICIOS,
new String[] {"codigo", "descripcion", "tipo_base", "porc", "valor_base","tipocalculo", "tipo_mes"}, "codigo = " + id , null, null, null, null, null);
 if (registro != null) 
    {
    registro.moveToFirst();
    }
return registro;
}
//**************************************************************************************
public int obtenerCantidadServicios() throws SQLException {
	int resultado = 0;
	Cursor registro = db.query(true, BaseDatos.TABLA_SERVICIOS,
			new String[] {"codigo"}, null, null, null, null, null, null);
			 if (registro != null) 
			    {
				 resultado = registro.getCount();
			    }
			return resultado;

}
//**************************************************************************************
public long ultimo_Servicio() {
	long i = 0, resultado;   
	Cursor cursor = null;
	boolean error;
	
	error = false;       
	String query = "SELECT MAX(codigo) AS maximo FROM Servicios";
	
    try 
    {   
    	cursor = db.rawQuery(query, null);
    }    
    catch (Exception e)
    {   
      Log.i("Sistema", "Error al ejecutar consulta en la base de datos" + e);   
      error = true;
    }   
	
	
	if (!error)
	{	
	cursor.moveToFirst();
	i = cursor.getInt(0);
	if (i>0)
	   {
	   resultado = (i + 1);
	   cursor.close();
	   }            
	   else
	   resultado = 1;
	}
	   else
	   resultado = 1;
	

	return(resultado);
	}

//**************************************************************************************
public void CargarServicios() 
{
		 
/*	AgregarServicio(codigo, descripcion              ,tipo_base,porc,valor_base,edad_max, solo_lectura,tipocalculo, tipomes);	*/
	AgregarServicio( 1, "Muerte"                           , 1 , 100,     0, 300, true,  1, 12);
	AgregarServicio( 2, "Invalidez Total y Permanente"     , 1 , 100,     0, 300, false, 1, 12);
	AgregarServicio( 3, "Doble por Accidente"              , 1 , 100,     0, 300, false, 1, 12);
	AgregarServicio( 4, "Perdidas parciales por Accidente" , 1 , 100,     0, 300, false, 1, 12);
	AgregarServicio( 5, "Adicional Enfermedades Terminales", 1 ,  50,     0,  64, false, 2,  1);
	AgregarServicio( 6, "Enfermedades Criticas"            , 1 ,  50,     0,  64, false, 2,  1);
	AgregarServicio( 7, "Diagnostico de Sida"              , 1 ,  50,     0,  64, false, 1, 12);
	AgregarServicio( 8, "Contagio Accidental de Sida"      , 1 ,  50,     0,  64, false, 1, 12);
	AgregarServicio( 9, "Adicional por Nacimiento"         , 1 ,  50,     0,  64, false, 3, 12);
	AgregarServicio(10, "Renta diaria por Internacion"     , 1 ,   1,     0,  64, false, 3, 12);
	AgregarServicio(11, "Ultimos Gastos"                   , 2 , 100, 10000, 300, false, 3, 12);
   // AgregarServicio(13, "Alta Complejidad Medica"          , 1 , 100,     0, 65, false, 1, 1000);

	
}
//**************************************************************************************
public int EliminarServicios() throws SQLException {
int registro = db.delete(BaseDatos.TABLA_SERVICIOS, null, null);
return registro;
}
//**************************************************************************************

public long AgregarPrima(int codigo, int edad, int edad_max, double porc) 
{
	ContentValues registro = new ContentValues();
	registro.put("codigo"    , codigo);
	registro.put("edad"      , edad);
	registro.put("edad_max"  , edad_max);
	registro.put("porc"      , porc);
	return db.insert(BaseDatos.TABLA_PRIMAS, null, registro);
}
//**************************************************************************************
public Cursor ConsultarPrimas() {
	Cursor registros = db.query(BaseDatos.TABLA_PRIMAS, new String[] {
	"codigo", "edad", "edad_max", "porc"}, null, null, null, null, null);
	return registros;
}
//**************************************************************************************
public Cursor obtenerPrima(int id, long edad) throws SQLException {
Cursor registro = db.query(BaseDatos.TABLA_PRIMAS,
new String[] {"codigo", "edad", "edad_max", "porc"}, "codigo = " + id + " and edad <= " + edad + " and " + edad + " <= edad_max ", null, null, null, null, null);
 if (registro != null) 
    {
    registro.moveToFirst();
    }
return registro;
}
//**************************************************************************************

public int Borrar_Prima(int codigo)
{
    return db.delete(BaseDatos.TABLA_PRIMAS, "codigo = " + codigo , null);
}
//**************************************************************************************

// /**** Carga de Tablas predefinidas de Calculo *****/

public void CargarPrimas() 
{
/*	AgregarPrima(codigo,    edad,edad_max, porc ) */
/** MUERTE (Mujeres)**/	
	AgregarPrima(	1	,	18 	,	18 	,	0.0066127769769682500	);
	AgregarPrima(	1	,	19 	,	19 	,	0.0068215332303716000	);
	AgregarPrima(	1	,	20 	,	20 	,	0.0070390080067269800	);
	AgregarPrima(	1	,	21 	,	21 	,	0.0072661903629131400	);
	AgregarPrima(	1	,	22 	,	22 	,	0.0075041384606030400	);
	AgregarPrima(	1	,	23 	,	23 	,	0.0077535126730258800	);
	AgregarPrima(	1	,	24 	,	24 	,	0.0080150238512535100	);
	AgregarPrima(	1	,	25 	,	25 	,	0.0082889588268070600	);
	AgregarPrima(	1	,	26 	,	26 	,	0.0085765941833520800	);
	AgregarPrima(	1	,	27 	,	27 	,	0.0088783356674671200	);
	AgregarPrima(	1	,	28 	,	28 	,	0.0091951137698363300	);
	AgregarPrima(	1	,	29 	,	29 	,	0.0095274449660731700	);
	AgregarPrima(	1	,	30 	,	30 	,	0.0098763879923089200	);
	AgregarPrima(	1	,	31 	,	31 	,	0.0102425939018194000	);
	AgregarPrima(	1	,	32 	,	32 	,	0.0106272770295330000	);
	AgregarPrima(	1	,	33 	,	33 	,	0.0110317600819794000	);
	AgregarPrima(	1	,	34 	,	34 	,	0.0114574863954286000	);
	AgregarPrima(	1	,	35 	,	35 	,	0.0119044874258082000	);
	AgregarPrima(	1	,	36 	,	36 	,	0.0123748891039483000	);
	AgregarPrima(	1	,	37 	,	37 	,	0.0128683927956110000	);
	AgregarPrima(	1	,	38 	,	38 	,	0.0133857441739119000	);
	AgregarPrima(	1	,	39 	,	39 	,	0.0139277681507352000	);
	AgregarPrima(	1	,	40 	,	40 	,	0.0144948356545821000	);
	AgregarPrima(	1	,	41 	,	41 	,	0.0150879094048395000	);
	AgregarPrima(	1	,	42 	,	42 	,	0.0157080551148902000	);
	AgregarPrima(	1	,	43 	,	43 	,	0.0163570147611306000	);
	AgregarPrima(	1	,	44 	,	44 	,	0.0170378547159459000	);
	AgregarPrima(	1	,	45 	,	45 	,	0.0177528272719658000	);
	AgregarPrima(	1	,	46 	,	46 	,	0.0185044405243682000	);
	AgregarPrima(	1	,	47 	,	47 	,	0.0192960854688158000	);
	AgregarPrima(	1	,	48 	,	48 	,	0.0201309558662818000	);
	AgregarPrima(	1	,	49 	,	49 	,	0.0210114183027153000	);
	AgregarPrima(	1	,	50 	,	50 	,	0.0219407561992541000	);
	AgregarPrima(	1	,	51 	,	51 	,	0.0229220453198470000	);
	AgregarPrima(	1	,	52 	,	52 	,	0.0239594041854044000	);
	AgregarPrima(	1	,	53 	,	53 	,	0.0250562030634801000	);
	AgregarPrima(	1	,	54 	,	54 	,	0.0262149487492381000	);
	AgregarPrima(	1	,	55 	,	55 	,	0.0274418708593308000	);
	AgregarPrima(	1	,	56 	,	56 	,	0.0287434088623137000	);
	AgregarPrima(	1	,	57 	,	57 	,	0.0301283654692805000	);
	AgregarPrima(	1	,	58 	,	58 	,	0.0316083384593504000	);
	AgregarPrima(	1	,	59 	,	59 	,	0.0331967877138926000	);
	AgregarPrima(	1	,	60 	,	60 	,	0.0349056613269718000	);
	AgregarPrima(	1	,	61 	,	61 	,	0.0367466625335185000	);
	AgregarPrima(	1	,	62 	,	62 	,	0.0387280394159101000	);
	AgregarPrima(	1	,	63 	,	63 	,	0.0408562583106365000	);
	AgregarPrima(	1	,	64 	,	64 	,	0.0431338727918870000	);
	AgregarPrima(	1	,	65 	,	65 	,	0.0455689046279980000	);
	AgregarPrima(	1	,	66 	,	66 	,	0.0481761331013350000	);
	AgregarPrima(	1	,	67 	,	67 	,	0.0509769388773510000	);
	AgregarPrima(	1	,	68 	,	68 	,	0.0540019840721953000	);
	AgregarPrima(	1	,	69 	,	69 	,	0.0572927832540423000	);
	AgregarPrima(	1	,	70 	,	70 	,	0.0608873788880337000	);
	AgregarPrima(	1	,	71 	,	71 	,	0.0648202860105261000	);
	AgregarPrima(	1	,	72 	,	72 	,	0.0691189472448647000	);
	AgregarPrima(	1	,	73 	,	73 	,	0.0737998310870723000	);
	AgregarPrima(	1	,	74 	,	74 	,	0.0788726188049528000	);
	AgregarPrima(	1	,	75 	,	75 	,	0.0843500643440493000	);
	AgregarPrima(	1	,	76 	,	76 	,	0.0902583371483659000	);
	AgregarPrima(	1	,	77 	,	77 	,	0.0966393698551372000	);
	AgregarPrima(	1	,	78 	,	78 	,	0.1035587141607380000	);
	AgregarPrima(	1	,	79 	,	79 	,	0.1111027108437770000	);
	AgregarPrima(	1	,	80 	,	80 	,	0.1193617777944740000	);
	
/** Invalidez Total y Permanente **/	
	AgregarPrima(	2	,	18 	,	18 	,	0.00303809770710446	);
	AgregarPrima(	2	,	19 	,	19 	,	0.00312555701640856	);
	AgregarPrima(	2	,	20 	,	20 	,	0.00321723335713523	);
	AgregarPrima(	2	,	21 	,	21 	,	0.00331344666545751	);
	AgregarPrima(	2	,	22 	,	22 	,	0.00341508220821117	);
	AgregarPrima(	2	,	23 	,	23 	,	0.00352150234652544	);
	AgregarPrima(	2	,	24 	,	24 	,	0.00363364872506163	);
	AgregarPrima(	2	,	25 	,	25 	,	0.00375091389016777	);
	AgregarPrima(	2	,	26 	,	26 	,	0.00387430684904407	);
	AgregarPrima(	2	,	27 	,	27 	,	0.00400382126288813	);
	AgregarPrima(	2	,	28 	,	28 	,	0.00414002848404103	);
	AgregarPrima(	2	,	29 	,	29 	,	0.00428356678031223	);
	AgregarPrima(	2	,	30 	,	30 	,	0.00443515263739498	);
	AgregarPrima(	2	,	31 	,	31 	,	0.00459499780657516	);
	AgregarPrima(	2	,	32 	,	32 	,	0.00476455929846693	);
	AgregarPrima(	2	,	33 	,	33 	,	0.00494301900874257	);
	AgregarPrima(	2	,	34 	,	34 	,	0.00513198216984629	);
	AgregarPrima(	2	,	35 	,	35 	,	0.00533071835329306	);
	AgregarPrima(	2	,	36 	,	36 	,	0.00554102592455908	);
	AgregarPrima(	2	,	37 	,	37 	,	0.00576296993034326	);
	AgregarPrima(	2	,	38 	,	38 	,	0.00599732759531412	);
	AgregarPrima(	2	,	39 	,	39 	,	0.00624501127039895	);
	AgregarPrima(	2	,	40 	,	40 	,	0.00650781168415947	);
	AgregarPrima(	2	,	41 	,	41 	,	0.00678563320090443	);
	AgregarPrima(	2	,	42 	,	42 	,	0.00707990908895576	);
	AgregarPrima(	2	,	43 	,	43 	,	0.00739080274317544	);
	AgregarPrima(	2	,	44 	,	44 	,	0.0077201627229023	);
	AgregarPrima(	2	,	45 	,	45 	,	0.00807023465256238	);
	AgregarPrima(	2	,	46 	,	46 	,	0.00844120459674299	);
	AgregarPrima(	2	,	47 	,	47 	,	0.00883426320721968	);
	AgregarPrima(	2	,	48 	,	48 	,	0.0092472444894617	);
	AgregarPrima(	2	,	49 	,	49 	,	0.00968145944396384	);
	AgregarPrima(	2	,	50 	,	50 	,	0.0101386646415675	);
	AgregarPrima(	2	,	51 	,	51 	,	0.0106180007684012	);
	AgregarPrima(	2	,	52 	,	52 	,	0.0111173896228372	);
	AgregarPrima(	2	,	53 	,	53 	,	0.0116282270515638	);
	AgregarPrima(	2	,	54 	,	54 	,	0.0121365717405115	);
	AgregarPrima(	2	,	55 	,	55 	,	0.0126206295370323	);
	AgregarPrima(	2	,	56 	,	56 	,	0.0130663221985835	);
	AgregarPrima(	2	,	57 	,	57 	,	0.0134696589697118	);
	AgregarPrima(	2	,	58 	,	58 	,	0.0138354884443672	);
	AgregarPrima(	2	,	59 	,	59 	,	0.0142052119980267	);
	AgregarPrima(	2	,	60 	,	60 	,	0.0145816405865933	);
	AgregarPrima(	2	,	61 	,	61 	,	0.0149677824243078	);
	AgregarPrima(	2	,	62 	,	62 	,	0.0153871419656547	);
	AgregarPrima(	2	,	63 	,	63 	,	0.0159113181711684	);
	AgregarPrima(	2	,	64 	,	64 	,	0.016741452991453	);
	
	/** Doble por Accidente **/	
	AgregarPrima(	3	,	18 	,	64 	,	0.0007	);

	/** Perdidas parciales por Accidente **/	
	AgregarPrima(	4	,	18 	,	64 	,	0.0014	);

	/** Adicional Enfermedades Terminales **/	
	AgregarPrima(	5	,	18 	,	64 	,	1.00	);

	/** Enfermedades Criticas **/	
	AgregarPrima(	6	,	18 	,	64 	,	3.00	);

	/** Diagnostico de Sida **/	
	AgregarPrima(	7	,	18 	,	64 	,	0.004	);

	/** Contagio Accidental de Sida **/	
	AgregarPrima(	8	,	18 	,	64 	,	0.002	);

	/** Adicional por Nacimiento **/	
	AgregarPrima(	9	,	18 	,	19 	,	0.0616	);
	AgregarPrima(	9	,	20 	,	24 	,	0.1207	);
	AgregarPrima(	9	,	25 	,	29 	,	0.1288	);
	AgregarPrima(	9	,	30 	,	34 	,	0.109	);
	AgregarPrima(	9	,	35 	,	39 	,	0.06	);
	AgregarPrima(	9	,	40 	,	44 	,	0.0177	);
	AgregarPrima(	9	,	45 	,	49 	,	0.0014	);
	
	/** Renta diaria por Internacion **/
	AgregarPrima(	10	,	18 	,	25 	,	0.48	);
	AgregarPrima(	10	,	26 	,	30 	,	0.53	);
	AgregarPrima(	10	,	31 	,	35 	,	0.58	);
	AgregarPrima(	10	,	36 	,	40 	,	0.64	);
	AgregarPrima(	10	,	41 	,	45 	,	0.7	);
	AgregarPrima(	10	,	46 	,	50 	,	0.8	);
	AgregarPrima(	10	,	51 	,	55 	,	1	);
	AgregarPrima(	10	,	56 	,	60 	,	1.4	);
	AgregarPrima(	10	,	61 	,	65 	,	2	);

	/** Ultimos Gastos Y Muerte (Hombres)**/	
	AgregarPrima(	11	,	18 	,	18 	,	0.00792073787037021	);
	AgregarPrima(	11	,	19 	,	19 	,	0.00815907582449437	);
	AgregarPrima(	11	,	20 	,	20 	,	0.0084061112159169	);
	AgregarPrima(	11	,	21 	,	21 	,	0.00866431748439022	);
	AgregarPrima(	11	,	22 	,	22 	,	0.00893585947599886	);
	AgregarPrima(	11	,	23 	,	23 	,	0.00922306206599769	);
	AgregarPrima(	11	,	24 	,	24 	,	0.00952744449083504	);
	AgregarPrima(	11	,	25 	,	25 	,	0.00985064967806547	);
	AgregarPrima(	11	,	26 	,	26 	,	0.0101944584971792	);
	AgregarPrima(	11	,	27 	,	27 	,	0.0105598013492483	);
	AgregarPrima(	11	,	28 	,	28 	,	0.010947193570384	);
	AgregarPrima(	11	,	29 	,	29 	,	0.0113577188685063	);
	AgregarPrima(	11	,	30 	,	30 	,	0.0117920540845766	);
	AgregarPrima(	11	,	31 	,	31 	,	0.0122514697136761	);
	AgregarPrima(	11	,	32 	,	32 	,	0.0127363171687416	);
	AgregarPrima(	11	,	33 	,	33 	,	0.0132485829388654	);
	AgregarPrima(	11	,	34 	,	34 	,	0.0137888507759724	);
	AgregarPrima(	11	,	35 	,	35 	,	0.0143588567552021	);
	AgregarPrima(	11	,	36 	,	36 	,	0.0149599764094902	);
	AgregarPrima(	11	,	37 	,	37 	,	0.0155937391348171	);
	AgregarPrima(	11	,	38 	,	38 	,	0.0162612874725953	);
	AgregarPrima(	11	,	39 	,	39 	,	0.0169644659282591	);
	AgregarPrima(	11	,	40 	,	40 	,	0.0177047578004049	);
	AgregarPrima(	11	,	41 	,	41 	,	0.0184844058853405	);
	AgregarPrima(	11	,	42 	,	42 	,	0.0193047385622174	);
	AgregarPrima(	11	,	43 	,	43 	,	0.0201696477280246	);
	AgregarPrima(	11	,	44 	,	44 	,	0.021081067424053	);
	AgregarPrima(	11	,	45 	,	45 	,	0.0220430354213425	);
	AgregarPrima(	11	,	46 	,	46 	,	0.0230582222273643	);
	AgregarPrima(	11	,	47 	,	47 	,	0.0241315732997881	);
	AgregarPrima(	11	,	48 	,	48 	,	0.0252674010025582	);
	AgregarPrima(	11	,	49 	,	49 	,	0.0264712826609896	);
	AgregarPrima(	11	,	50 	,	50 	,	0.0277475602602118	);
	AgregarPrima(	11	,	51 	,	51 	,	0.0291025930148316	);
	AgregarPrima(	11	,	52 	,	52 	,	0.0305394554841198	);
	AgregarPrima(	11	,	53 	,	53 	,	0.0320631183948919	);
	AgregarPrima(	11	,	54 	,	54 	,	0.0336778344145577	);
	AgregarPrima(	11	,	55 	,	55 	,	0.035387743789928	);
	AgregarPrima(	11	,	56 	,	56 	,	0.0372007216553256	);
	AgregarPrima(	11	,	57 	,	57 	,	0.0391243560184007	);
	AgregarPrima(	11	,	58 	,	58 	,	0.0411707879859574	);
	AgregarPrima(	11	,	59 	,	59 	,	0.0433518169918996	);
	AgregarPrima(	11	,	60 	,	60 	,	0.0456805402893153	);
	AgregarPrima(	11	,	61 	,	61 	,	0.0481680639859611	);
	AgregarPrima(	11	,	62 	,	62 	,	0.0508257355715733	);
	AgregarPrima(	11	,	63 	,	63 	,	0.0536632449187243	);
	AgregarPrima(	11	,	64 	,	64 	,	0.0566890941940101	);
	AgregarPrima(	11	,	65 	,	65 	,	0.0599142697400716	);
	AgregarPrima(	11	,	66 	,	66 	,	0.0633528754700076	);
	AgregarPrima(	11	,	67 	,	67 	,	0.067027374755843	);
	AgregarPrima(	11	,	68 	,	68 	,	0.0709642191558375	);
	AgregarPrima(	11	,	69 	,	69 	,	0.075196182340707	);
	AgregarPrima(	11	,	70 	,	70 	,	0.0797556042820721	);
	AgregarPrima(	11	,	71 	,	71 	,	0.0846661933611524	);
	AgregarPrima(	11	,	72 	,	72 	,	0.0899449808679463	);
	AgregarPrima(	11	,	73 	,	73 	,	0.0955965723394414	);
	AgregarPrima(	11	,	74 	,	74 	,	0.101612370389838	);
	AgregarPrima(	11	,	75 	,	75 	,	0.107990186234983	);
	AgregarPrima(	11	,	76 	,	76 	,	0.114742084043352	);
	AgregarPrima(	11	,	77 	,	77 	,	0.121900783224179	);
	AgregarPrima(	11	,	78 	,	78 	,	0.129525267500996	);
	AgregarPrima(	11	,	79 	,	79 	,	0.137706848091258	);
	AgregarPrima(	11	,	80 	,	80 	,	0.146534587087105	);

	/** Ultimos Gastos (Mujeres)**/	
	AgregarPrima(	12	,	18 	,	18 	,	0.00661277697696825	);
	AgregarPrima(	12	,	19 	,	19 	,	0.0068215332303716	);
	AgregarPrima(	12	,	20 	,	20 	,	0.00703900800672698	);
	AgregarPrima(	12	,	21 	,	21 	,	0.00726619036291314	);
	AgregarPrima(	12	,	22 	,	22 	,	0.00750413846060304	);
	AgregarPrima(	12	,	23 	,	23 	,	0.00775351267302588	);
	AgregarPrima(	12	,	24 	,	24 	,	0.00801502385125351	);
	AgregarPrima(	12	,	25 	,	25 	,	0.00828895882680706	);
	AgregarPrima(	12	,	26 	,	26 	,	0.00857659418335208	);
	AgregarPrima(	12	,	27 	,	27 	,	0.00887833566746712	);
	AgregarPrima(	12	,	28 	,	28 	,	0.00919511376983633	);
	AgregarPrima(	12	,	29 	,	29 	,	0.00952744496607317	);
	AgregarPrima(	12	,	30 	,	30 	,	0.00987638799230892	);
	AgregarPrima(	12	,	31 	,	31 	,	0.0102425939018194	);
	AgregarPrima(	12	,	32 	,	32 	,	0.010627277029533	);
	AgregarPrima(	12	,	33 	,	33 	,	0.0110317600819794	);
	AgregarPrima(	12	,	34 	,	34 	,	0.0114574863954286	);
	AgregarPrima(	12	,	35 	,	35 	,	0.0119044874258082	);
	AgregarPrima(	12	,	36 	,	36 	,	0.0123748891039483	);
	AgregarPrima(	12	,	37 	,	37 	,	0.012868392795611	);
	AgregarPrima(	12	,	38 	,	38 	,	0.0133857441739119	);
	AgregarPrima(	12	,	39 	,	39 	,	0.0139277681507352	);
	AgregarPrima(	12	,	40 	,	40 	,	0.0144948356545821	);
	AgregarPrima(	12	,	41 	,	41 	,	0.0150879094048395	);
	AgregarPrima(	12	,	42 	,	42 	,	0.0157080551148902	);
	AgregarPrima(	12	,	43 	,	43 	,	0.0163570147611306	);
	AgregarPrima(	12	,	44 	,	44 	,	0.0170378547159459	);
	AgregarPrima(	12	,	45 	,	45 	,	0.0177528272719658	);
	AgregarPrima(	12	,	46 	,	46 	,	0.0185044405243682	);
	AgregarPrima(	12	,	47 	,	47 	,	0.0192960854688158	);
	AgregarPrima(	12	,	48 	,	48 	,	0.0201309558662818	);
	AgregarPrima(	12	,	49 	,	49 	,	0.0210114183027153	);
	AgregarPrima(	12	,	50 	,	50 	,	0.0219407561992541	);
	AgregarPrima(	12	,	51 	,	51 	,	0.022922045319847	);
	AgregarPrima(	12	,	52 	,	52 	,	0.0239594041854044	);
	AgregarPrima(	12	,	53 	,	53 	,	0.0250562030634801	);
	AgregarPrima(	12	,	54 	,	54 	,	0.0262149487492381	);
	AgregarPrima(	12	,	55 	,	55 	,	0.0274418708593308	);
	AgregarPrima(	12	,	56 	,	56 	,	0.0287434088623137	);
	AgregarPrima(	12	,	57 	,	57 	,	0.0301283654692805	);
	AgregarPrima(	12	,	58 	,	58 	,	0.0316083384593504	);
	AgregarPrima(	12	,	59 	,	59 	,	0.0331967877138926	);
	AgregarPrima(	12	,	60 	,	60 	,	0.0349056613269718	);
	AgregarPrima(	12	,	61 	,	61 	,	0.0367466625335185	);
	AgregarPrima(	12	,	62 	,	62 	,	0.0387280394159101	);
	AgregarPrima(	12	,	63 	,	63 	,	0.0408562583106365	);
	AgregarPrima(	12	,	64 	,	64 	,	0.043133872791887	);
	AgregarPrima(	12	,	65 	,	65 	,	0.045568904627998	);
	AgregarPrima(	12	,	66 	,	66 	,	0.048176133101335	);
	AgregarPrima(	12	,	67 	,	67 	,	0.050976938877351	);
	AgregarPrima(	12	,	68 	,	68 	,	0.0540019840721953	);
	AgregarPrima(	12	,	69 	,	69 	,	0.0572927832540423	);
	AgregarPrima(	12	,	70 	,	79 	,	0.0608873788880337	);
	AgregarPrima(	12	,	71 	,	71 	,	0.0648202860105261	);
	AgregarPrima(	12	,	72 	,	72 	,	0.0691189472448647	);
	AgregarPrima(	12	,	73 	,	73 	,	0.0737998310870723	);
	AgregarPrima(	12	,	74 	,	74 	,	0.0788726188049528	);
	AgregarPrima(	12	,	75 	,	75 	,	0.0843500643440493	);
	AgregarPrima(	12	,	76 	,	76 	,	0.0902583371483659	);
	AgregarPrima(	12	,	77 	,	77 	,	0.0966393698551372	);
	AgregarPrima(	12	,	78 	,	78 	,	0.103558714160738	);
	AgregarPrima(	12	,	79 	,	79 	,	0.111102710843777	);
	AgregarPrima(	12	,	80 	,	80 	,	0.119361777794474	);
/*

    AgregarPrima(13,	1,	  25	,	 12  );
    AgregarPrima(13,	26,	  30	,	 14  );
    AgregarPrima(13,	31,	  35	,	 16  );
    AgregarPrima(13,	36,	  40	,	 18  );
    AgregarPrima(13,	41,	  45	,	 21  );
    AgregarPrima(13,	46,	  50	,	 24  );
    AgregarPrima(13,	51,	  55	,	 28  );
    AgregarPrima(13,	56,	  60	,	 33  );
    AgregarPrima(13,	61,	  65	,	 40  );
*/
}
//**************************************************************************************
public int obtenerCantidadPrimas() throws SQLException {
	int resultado = 0;
	Cursor registro = db.query(true, BaseDatos.TABLA_PRIMAS,
			new String[] {"codigo","edad"}, null, null, null, null, null, null);
			 if (registro != null) 
			    {
				 resultado = registro.getCount();
			    }
			return resultado;

}
//**************************************************************************************
public long AgregarPlanes(int tarifa, String plan, int servicio, int edadi, int edadf, float valor)
{
	ContentValues registro = new ContentValues();
	registro.put("tarifa", tarifa);
	registro.put("plan", plan);
	registro.put("servicio", servicio);
	registro.put("edadi", edadi);
	registro.put("edadf", edadf);
	registro.put("valor", valor);
	registro.put("lectura", 1);
	
	return db.insert(BaseDatos.TABLA_PLANES, null, registro);
}
//**************************************************************************************
public Cursor ConsultarPlanes() {
	Cursor registros = db.query(BaseDatos.TABLA_PLANES, new String[] {
	"codigo", "plan", "servicio", "edadi", "edadf","valor","lectura","tarifa"}, null, null, null, null, "codigo asc");
	return registros;
}
//**************************************************************************************
public Cursor obtenerPlan(int tarifa, int edad, String plan, int servicio) throws SQLException {
Cursor registro = db.query(true, BaseDatos.TABLA_PLANES,
new String[] {"plan", "servicio", "edadi", "edadf","valor","lectura","tarifa"}, "tarifa = " + tarifa +" and plan = '" + plan + "' and edadi <= " + edad + " and " + edad + " <= edadf and servicio = " + servicio, null, null, null, null, null);
 if (registro != null) 
    {
    registro.moveToFirst();
    }
return registro;
}
//**************************************************************************************
public int EliminarPlanes() throws SQLException {
int registro = db.delete(BaseDatos.TABLA_PLANES, null, null);
return registro;
}
//**************************************************************************************
public int obtenerCantidadPlanes() throws SQLException {
	int resultado = 0;
	Cursor registro = db.query(true, BaseDatos.TABLA_PLANES,
			new String[] {"plan", "servicio", "edadi","tarifa"}, null, null, null, null, null, null);
			 if (registro != null) 
			    {
				 resultado = registro.getCount();
			    }
			return resultado;

}
//**************************************************************************************
public long AgregarControl(int tipo, int codigo, String fechainst, String fechavigencia, int activado) 
{
if (tipo == 1)	
 	db.delete(BaseDatos.TABLA_CONTROL, "codigo = " + codigo, null);

	ContentValues registro = new ContentValues();
	registro.put("codigo"         , codigo);
	registro.put("fechainst"      , fechainst);
	registro.put("fechavigencia"  , fechavigencia);
	registro.put("activado"       , activado);
	long resultado = 0;
	if (tipo == 1)	
	    resultado = db.insert(BaseDatos.TABLA_CONTROL, null, registro);
	if (tipo == 2)	
	    resultado = db.update(BaseDatos.TABLA_CONTROL, registro, "codigo = " + codigo, null);
	
	return resultado;
}
//**************************************************************************************
public Cursor obtenerControl(long id) throws SQLException {
Cursor registro = db.query(true, BaseDatos.TABLA_CONTROL,
new String[] {"codigo", "fechainst", "fechavigencia", "activado"}, "codigo = " + id , null, null, null, null, null);
 if (registro != null) 
    {
    registro.moveToFirst();
    }
return registro;
}
//**************************************************************************************
public boolean EstActivo(long id) throws SQLException {
boolean resultado = false;	
Cursor registro = db.query(true, BaseDatos.TABLA_CONTROL,
new String[] {"codigo", "fechainst", "fechavigencia", "activado"}, "codigo = " + id , null, null, null, null, null);
 if (registro != null) 
    {
    registro.moveToFirst();
    String fechaSistema = Librerias.CargarFechaSistema();
    resultado = Librerias.EstaVigente(fechaSistema,registro.getString(2));
    }
return resultado;
}
//**************************************************************************************
public void CargarPlanes()
{
/*	AgregarPlanes(String plan, int servicio, int edadi, int edadf, float valor)  */
/** PLANES SEPELIO **/	
/*
 1 SEPELIO
 2 PARCELA
 3 LUTO	
 */
/*** Precios al 18/11/2014 ***************************//*
	AgregarPlanes("A",	1,	  1	,	 9 ,  4);
	AgregarPlanes("A",	1,	 10	,	19 , 10);
	AgregarPlanes("A",	1,	 20	,	29 , 15);
	AgregarPlanes("A",	1,	 30	,	39 , 19);
	AgregarPlanes("A",	1,	 40	,	49 , 23);
	AgregarPlanes("A",	1,	 50	,	59 , 36);
	AgregarPlanes("A",	1,	 60	,	69 , 45);
	AgregarPlanes("A",	1,	 70	,	74 , 62);

	AgregarPlanes("G",	1,	  1 ,	 9 ,  8);
	AgregarPlanes("G",	1,	 10	,	19 , 14);
	AgregarPlanes("G",	1,	 20	,	29 , 24);
	AgregarPlanes("G",	1,	 30	,	39 , 29);
	AgregarPlanes("G",	1,	 40	,	49 , 33);
	AgregarPlanes("G",	1,	 50	,	59 , 63);
	AgregarPlanes("G",	1,	 60	,	69 , 69);
	AgregarPlanes("G",	1,	 70	,	74 , 87);
	
	AgregarPlanes(3," ",	2,	  1 ,	 9 ,  9);
	AgregarPlanes(3," ",	2,	 10	,	19 , 12);
	AgregarPlanes(3," ",	2,	 20	,	29 , 22);
	AgregarPlanes(3," ",	2,	 30	,	39 , 26);
	AgregarPlanes(3," ",	2,	 40	,	49 , 34);
	AgregarPlanes(3," ",	2,	 50	,	59 , 50);
	AgregarPlanes(3," ",	2,	 60	,	69 , 73);
	AgregarPlanes(3," ",	2,	 70	,	74 , 97);
	
	AgregarPlanes(3," ",	3,	  1 ,	 9 ,  4);
	AgregarPlanes(3," ",	3,	 10	,	19 ,  7);
	AgregarPlanes(3," ",	3,	 20	,	29 , 12);
	AgregarPlanes(3," ",	3,	 30	,	39 , 13);
	AgregarPlanes(3," ",	3,	 40	,	49 , 15);
	AgregarPlanes(3," ",	3,	 50	,	59 , 22);
	AgregarPlanes(3," ",	3,	 60	,	69 , 29);
	AgregarPlanes(3," ",	3,	 70	,	74 , 44);*/
	
	/*** Precios a partir del 11/01/2015 ***************************/
	/*	AgregarPlanes(String plan, int servicio, int edadi, int edadf, float valor)  */
	/** PLANES SEPELIO **/	
	/*
	 1 SEPELIO
	 2 PARCELA
	 3 LUTO	*/

	//1-sepelio
	AgregarPlanes(3,"A",	1,	  0	,	 9  ,  4);
	AgregarPlanes(3,"A",	1,	 10	,	19  , 10);
	AgregarPlanes(3,"A",	1,	 20	,	29  , 15);
	AgregarPlanes(3,"A",	1,	 30	,	39  , 19);
	AgregarPlanes(3,"A",	1,	 40	,	49  , 23);
	AgregarPlanes(3,"A",	1,	 50	,	59  , 36);
	AgregarPlanes(3,"A",	1,	 60	,	69  , 45);
	AgregarPlanes(3,"A",	1,	 70	,	79  , 74);
	AgregarPlanes(3,"A",	1,	 80	,	110 , 110);

	
	AgregarPlanes(3,"G",	1,	  0 ,	 9 ,  8);
	AgregarPlanes(3,"G",	1,	 10	,	19 , 14);
	AgregarPlanes(3,"G",	1,	 20	,	29 , 24);
	AgregarPlanes(3,"G",	1,	 30	,	39 , 29);
	AgregarPlanes(3,"G",	1,	 40	,	49 , 33);
	AgregarPlanes(3,"G",	1,	 50	,	59 , 63);
	AgregarPlanes(3,"G",	1,	 60	,	69 , 82);
	AgregarPlanes(3,"G",	1,	 70	,	79 , 104);
	AgregarPlanes(3,"G",	1,	 80	,  110 , 171);
	
	//2-parcela
	AgregarPlanes(3," ",	2,	  0 ,	 9 ,  8);
	AgregarPlanes(3," ",	2,	 10	,	19 , 14);
	AgregarPlanes(3," ",	2,	 20	,	29 , 24);
	AgregarPlanes(3," ",	2,	 30	,	39 , 29);
	AgregarPlanes(3," ",	2,	 40	,	49 , 33);
	AgregarPlanes(3," ",	2,	 50	,	59 , 63);
	AgregarPlanes(3," ",	2,	 60	,	69 , 82);
	AgregarPlanes(3," ",	2,	 70	,	74 , 104);
	AgregarPlanes(3," ",	2,	 75	,	79 , 136);
	AgregarPlanes(3," ",	2,	 80	,  110 , 171);
	
	//3-luto
	AgregarPlanes(3," ",	3,	  0 ,	 9 ,  5);
	AgregarPlanes(3," ",	3,	 10	,	19 ,  9);
	AgregarPlanes(3," ",	3,	 20	,	29 , 15);
	AgregarPlanes(3," ",	3,	 30	,	39 , 16);
	AgregarPlanes(3," ",	3,	 40	,	49 , 19);
	AgregarPlanes(3," ",	3,	 50	,	59 , 28);
	AgregarPlanes(3," ",	3,	 60	,	69 , 37);
	AgregarPlanes(3," ",	3,	 70	,	79 , 57);
	AgregarPlanes(3," ",	3,	 80	,  110 , 74);

    String fechaSistema = Librerias.CargarFechaSistema();
	AgregarControl(1,1, fechaSistema, fechaSistema, 0);
	AgregarControl(1,2, fechaSistema, fechaSistema, 0);
	
}
//**************************************************************************************
public long AgregarTmpPlanes(int tarifa,int codigo,String fechanac, int edad, float luto, float parcela, float sepelio, float prima)
{
	ContentValues registro = new ContentValues();

	registro.put("tarifa", tarifa);
	registro.put("codigo", codigo);
	registro.put("fechanac", fechanac);
	registro.put("edad", edad);
	registro.put("luto", luto);
	registro.put("parcela", parcela);
	registro.put("sepelio", sepelio);
	registro.put("prima", prima);
	return db.insert(BaseDatos.TABLA_PLANES_TMP, null, registro);
	
	
}
//**************************************************************************************
public Cursor ConsultarTmpPlanes() {
	Cursor registros = db.query(BaseDatos.TABLA_PLANES_TMP, new String[] {
	"codigo", "fechanac", "edad", "luto", "parcela","sepelio","prima","tarifa"}, null, null, null, null, "codigo asc");
	return registros;
}
//**************************************************************************************
public int borrarTmpPlan(int tarifa, long idRegistro)
{
	return db.delete(BaseDatos.TABLA_PLANES_TMP, "tarifa = " + tarifa + " and codigo = " + idRegistro, null);
}
//**************************************************************************************
public long actualizarTmpPlan(int tarifa, long idRegistro, String fechanac, int edad, float luto, float parcela, float sepelio, float prima)
{
	ContentValues registro = new ContentValues();
	registro.put("tarifa", tarifa);
	registro.put("fechanac", fechanac);
	registro.put("edad", edad);
	return db.update(BaseDatos.TABLA_PLANES_TMP, registro, "tarifa = " + tarifa + " and codigo = " + idRegistro, null);
}
//**************************************************************************************

public long ultimo_tmp_Plan(int tarifa) {
	long i = 0, resultado;   
	Cursor cursor = null;
	boolean error;
	
	error = false;       
	String query = "SELECT MAX(codigo) AS maximo FROM tmpPlanes where tarifa = " + tarifa;
	
    try 
    {   
    	cursor = db.rawQuery(query, null);
    }    
    catch (Exception e)
    {   
      Log.i("Sistema", "Error al ejecutar consulta en la base de datos" + e);   
      error = true;
    }   
	
	
	if (!error)
	{	
	cursor.moveToFirst();
	i = cursor.getInt(0);
	if (i>0)
	   {
	   resultado = (i + 1);
	   cursor.close();
	   }            
	   else
	   resultado = 1;
	}
	   else
	   resultado = 1;
	

	return(resultado);
	}

//**************************************************************************************
public int LimpiarTmpPlanes() 
{
	return db.delete(BaseDatos.TABLA_PLANES_TMP, "", null);
}
//**************************************************************************************
public long AgregarClave(String clave) 
{
	ContentValues registro = new ContentValues();
	registro.put("codigo"  , 1);
	registro.put("clave"   , clave);
	registro.put("activado", 0);
	registro.put("login"   , 0);
	return db.insert(BaseDatos.TABLA_PARAMETROS, null, registro);
}
//**************************************************************************************
public Cursor ConsultarClave() {
	Cursor registros = db.query(BaseDatos.TABLA_PARAMETROS, new String[] {
	"codigo", "clave", "activado","login", "id", "fecha_act","fecha_login"}, null, null, null, null, null);
	return registros;
}
//**************************************************************************************
public long RegistraActivacion() 
{
	ContentValues registro = new ContentValues();
	registro.put("activado", 1);
	registro.put("fecha_act", Librerias.CargarFechaSistema());
	return db.update(BaseDatos.TABLA_PARAMETROS, registro,  "codigo = 1", null);
}
//**************************************************************************************
public long RegistraLogin(Integer id) 
{
	ContentValues registro = new ContentValues();
	registro.put("login", 1);
	registro.put("id", id);
	registro.put("fecha_login", Librerias.CargarFechaSistema());
	return db.update(BaseDatos.TABLA_PARAMETROS, registro,  "codigo = 1", null);
}
//**************************************************************************************
public long LimpiarLogin()
{
	ContentValues registro = new ContentValues();
	registro.put("login", 1);
	registro.put("id", 0);
	registro.put("fecha_login", "");
	return db.update(BaseDatos.TABLA_PARAMETROS, registro,  "codigo = 1", null);
}
//**************************************************************************************

public long RegistraEmpresa(Integer id)
{
	ContentValues registro = new ContentValues();
	registro.put("login", 0);
	registro.put("id", id);
	registro.put("fecha_login", Librerias.CargarFechaSistema());
	return db.update(BaseDatos.TABLA_PARAMETROS, registro,  "codigo = 1", null);
}
//**************************************************************************************

public long BlanquearLogin() 
{
	ContentValues registro = new ContentValues();
	registro.put("login", 0);
	registro.put("id", 0);
	registro.put("fecha_login", "");
	return db.update(BaseDatos.TABLA_PARAMETROS, registro,  "codigo = 1", null);
}
//**************************************************************************************
public void CargarGastos() 
{
	int i = 0;
	AgregarGastos(++i, 5000);
	AgregarGastos(++i,10000);
}
	//**************************************************************************************
	public void CargarTarifas()
	{

		AgregarTarifa(3 ,"PARTICULARES");
		AgregarTarifa(29,"CONVENIOS");

	}


//**************************************************************************************
public void CargarCapital() 
{
	int i = 0;
	AgregarCapital(++i, 10000);
	AgregarCapital(++i, 15000);
	AgregarCapital(++i, 20000);
	AgregarCapital(++i, 25000);
	AgregarCapital(++i, 30000);
	AgregarCapital(++i, 35000);
	AgregarCapital(++i, 40000);
	AgregarCapital(++i, 45000);
	AgregarCapital(++i, 50000);
	AgregarCapital(++i, 55000);
	AgregarCapital(++i, 60000);
	AgregarCapital(++i, 65000);
	AgregarCapital(++i, 70000);
	AgregarCapital(++i, 75000);
	AgregarCapital(++i, 80000);
	AgregarCapital(++i, 85000);
	AgregarCapital(++i, 90000);
	AgregarCapital(++i, 95000);
	AgregarCapital(++i,100000);
}
//**************************************************************************************
public long AgregarGastos(int codigo, float importe) 
{
	ContentValues registro = new ContentValues();
	registro.put("codigo"  , codigo);
	registro.put("importe" , importe);
	return db.insert(BaseDatos.TABLA_GASTOS, null, registro);
}
//**************************************************************************************
public long AgregarCapital(int codigo, double importe) 
{
	ContentValues registro = new ContentValues();
	registro.put("codigo"  , codigo);
	registro.put("importe" , (float) importe);
	return db.insert(BaseDatos.TABLA_CAPITAL, null, registro);
}
//**************************************************************************************
public String[] ConsultarGastos() {
	Cursor registros = db.query(BaseDatos.TABLA_GASTOS, new String[] {
	"codigo", "importe"}, null, null, null, null, null);
	String[] gastos = new String[registros.getCount()];
    registros.moveToFirst();
		for (int i = 0; i < registros.getCount(); i++) {
			gastos[i] = String.format("%.00f",registros.getFloat(1)); 
			registros.moveToNext();
	}
	return gastos;
}
//**************************************************************************************
public int EliminarGastos() throws SQLException {
int registro = db.delete(BaseDatos.TABLA_GASTOS, null, null);
return registro;
}
//**************************************************************************************
public String[] ConsultarCapital() {
	Cursor registros = db.query(BaseDatos.TABLA_CAPITAL, new String[] {
	"codigo", "importe"}, null, null, null, null, null);
	String[] gastos = new String[registros.getCount()];
    registros.moveToFirst();
		for (int i = 0; i < registros.getCount(); i++) {
			gastos[i] = String.format("%.00f",registros.getFloat(1)); 
			registros.moveToNext();
	}
	return gastos;
}
//**************************************************************************************
public boolean AgregaFecha(Context c)
{
boolean resultado = false;
Cursor valores = null;

String fechaSistema = Librerias.CargarFechaSistema();
boolean esta = false;
try {
	valores = db.query(true, BaseDatos.TABLA_LOG,
			new String[] {"fecha"}, "fecha = '" + fechaSistema + "'", null, null, null, null, null);
	if (valores.getCount()>0) 
	{
//		 valores.moveToFirst();
//		 Toast.makeText(c,"fecha: " + valores.getString(0) ,Toast.LENGTH_LONG).show();
		esta = true;
	}
	
} catch (Exception e) {
	// TODO: handle exception
}
if (!esta)
{
	ContentValues registro = new ContentValues();
	registro.put("fecha"    , fechaSistema);
	db.insert(BaseDatos.TABLA_LOG, null, registro);
	resultado = true;
	
}
	
return resultado;
}
//**************************************************************************************
public long AgregarTarifa(int codigo, String descripcion)
{
	ContentValues registro = new ContentValues();
	registro.put("codigo"     , codigo);
	registro.put("descripcion", descripcion);
	return db.insert(BaseDatos.TABLA_TARIFAS, null, registro);
}
//**************************************************************************************
public String[] ConsultarTarifas() {
	boolean error = false;
	Cursor registros = null;
	registros = db.query(BaseDatos.TABLA_TARIFAS, new String[] {
			"codigo", "descripcion"}, null, null, null, null, null);

	String[] arreglotarifas = new String[registros.getCount()];
	registros.moveToFirst();
	for (int i = 0; i < registros.getCount(); i++) {
		arreglotarifas[i] = String.valueOf(registros.getInt(0));
		registros.moveToNext();
	}
	return arreglotarifas;
}
	//**************************************************************************************
	public int EliminarCapital() throws SQLException {
		int registro = db.delete(BaseDatos.TABLA_CAPITAL, null, null);
		return registro;
	}
//**************************************************************************************
public int EliminarTartifas() throws SQLException {
	int registro = db.delete(BaseDatos.TABLA_TARIFAS, null, null);
	return registro;
}
//**************************************************************************************
public int Consultar_Capital_indice200000() throws SQLException {
	float importe = 0;
	int indice = 0;
	Cursor registros = db.query(BaseDatos.TABLA_CAPITAL, new String[] {
			"codigo", "importe"}, null, null, null, null, null);
	String[] gastos = new String[registros.getCount()];
	registros.moveToFirst();
	for (int i = 0; i < registros.getCount(); i++) {
		importe = registros.getFloat(1);
		registros.moveToNext();
		if (importe == 200000)
			indice = i;
	}
	return indice;
}
//**************************************************************************************
	/*
public long AgregarComplejidad(int codigo, int sexo, int edadi, int edadf, double importe)
	{
		ContentValues registro = new ContentValues();
		registro.put("codigo"    , codigo);
		registro.put("sexo"      , sexo);
		registro.put("edadi"     , edadi);
		registro.put("edadf"     , edadf);
		registro.put("valor"     , importe);
		return db.insert(BaseDatos.TABLA_Complejidad, null, registro);
	}
	//**************************************************************************************
	public Cursor ConsultarComplejidad() {
		Cursor registros = db.query(BaseDatos.TABLA_PRIMAS, new String[] {
				"codigo", "sexo", "edadi", "edadf", "valor"}, null, null, null, null, null);
		return registros;
	}
	//**************************************************************************************
public Cursor obtenerComplejidad(int codigo, int edad, int sexo) throws SQLException {
	Cursor registro = db.query(true, BaseDatos.TABLA_PLANES,
			new String[] {"codigo", "sexo", "edadi", "edadf","valor"}, "codigo = " + codigo +" and sexo = " + sexo + " and edadi <= " + edad + " and " + edad + " <= edadf  ", null, null, null, null, null);
	if (registro != null)
	{
		registro.moveToFirst();
	}
	return registro;
}
//**************************************************************************************
public void CargarComplejidad()
{
	AgregarComplejidad(13,0,	1,	  25	,	 12  );
	AgregarComplejidad(13,0,	26,	  30	,	 14  );
	AgregarComplejidad(13,0,	31,	  35	,	 16  );
	AgregarComplejidad(13,0,	36,	  40	,	 18  );
	AgregarComplejidad(13,0,	41,	  45	,	 21  );
	AgregarComplejidad(13,0,	46,	  50	,	 24  );
	AgregarComplejidad(13,0,	51,	  55	,	 28  );
	AgregarComplejidad(13,0,	56,	  60	,	 33  );
	AgregarComplejidad(13,0,	61,	  65	,	 40  );

    AgregarPrima(13,	1,	  25	,	 12  );
    AgregarPrima(13,	26,	  30	,	 14  );
    AgregarPrima(13,	31,	  35	,	 16  );
    AgregarPrima(13,	36,	  40	,	 18  );
    AgregarPrima(13,	41,	  45	,	 21  );
    AgregarPrima(13,	46,	  50	,	 24  );
    AgregarPrima(13,	51,	  55	,	 28  );
    AgregarPrima(13,	56,	  60	,	 33  );
    AgregarPrima(13,	61,	  65	,	 40  );

}*/
//**************************************************************************************




}
    