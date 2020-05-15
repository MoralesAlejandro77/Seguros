/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.seguros.Actualizacion;

/**
 *
 * @author ALE77
 */
//________________________________________________________________    
 public class DatosVida {
	  private int id = 0;
	  private String nombre = "";
	  private int tipo_base = 0;
	  private float porc = 0;
	  private float valor_base = 0;
	  private int edad_max;
	  private int sololectura;
	  private int tipo_calculo;
	  private int edad_mes;
	  
	  

  public DatosVida() {
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



public int getTipo_base() {
	return tipo_base;
}



public void setTipo_base(int tipo_base) {
	this.tipo_base = tipo_base;
}



public float getPorc() {
	return porc;
}



public void setPorc(float porc) {
	this.porc = porc;
}



public int getEdad_max() {
	return edad_max;
}



public void setEdad_max(int edad_max) {
	this.edad_max = edad_max;
}



public int getSololectura() {
	return sololectura;
}



public void setSololectura(int sololectura) {
	this.sololectura = sololectura;
}



public int getTipo_calculo() {
	return tipo_calculo;
}



public void setTipo_calculo(int tipo_calculo) {
	this.tipo_calculo = tipo_calculo;
}



public int getEdad_mes() {
	return edad_mes;
}



public void setEdad_mes(int edad_mes) {
	this.edad_mes = edad_mes;
}



public float getValor_base() {
	return valor_base;
}



public void setValor_base(float valor_base) {
	this.valor_base = valor_base;
}

 
  	
}