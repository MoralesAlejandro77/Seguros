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
public class Servicios {
	private int    id;
	private String nombre;
	private float base;
	private float valor;
	private float valor2;
	private boolean lectura;
	private boolean seleccion;
	private float prima;
	private int tipo_mes;
	
	
	public Servicios(int id, String nombre, float base, float valor, boolean lectura, boolean seleccion, float prima_gasto, float prima, int tipo_mes) {
		super();
		this.id        = id;
		this.nombre    = nombre;
		this.base      = base;
		this.valor     = valor;
		this.valor2    = prima_gasto;
		this.lectura   = lectura;
		this.seleccion = seleccion;
		this.prima     = prima;
		this.tipo_mes  = tipo_mes;
	}
	public boolean isLectura() {
		return lectura;
	}
	public void setLectura(boolean lectura) {
		this.lectura = lectura;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public float getBase() {
		return base;
	}
	public void setBase(float base) {
		this.base = base;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}

	public float getValor2() {
		return valor2;
	}
	public void setValor2(float valor) {
		this.valor2 = valor;
	}
	
	public float getPrima() {
		return prima;
	}
	public void setPrima(float prima) {
		this.prima = prima;
	}
	public int getTipo_mes() {
		return tipo_mes;
	}
	
	public void setTipo_mes(int tipo_mes) {
		this.tipo_mes = tipo_mes;
	}
	
	public void RecalcularValor(float base){ 
	float resultado =  base*this.prima/this.tipo_mes;
	this.valor = resultado;
	}
}
