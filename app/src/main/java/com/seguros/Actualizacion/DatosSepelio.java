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
 public class DatosSepelio {
	private int tarifa;
	  private String plan = "";
	  private int servicio = 0;
	  private int edadi = 0;
	  private int edadf = 0;
	  private float valor;
	  
	  
public DatosSepelio(int tarifa, String plan, int servicio, int edadi, int edadf, float valor)
   {
	    this.tarifa = tarifa;
 	    this.plan   = plan;
		this.servicio = servicio;
		this.edadi = edadi;
		this.edadf = edadf;
		this.valor = valor;
	}
	  
  public DatosSepelio() {
	// TODO Auto-generated constructor stub
}

public String getPlan() {
		return plan;
	}
	public void setPlan(String plan) {
		this.plan = plan;
	}
	public int getServicio() {
		return servicio;
	}
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}
	public int getEdadi() {
		return edadi;
	}
	public void setEdadi(int edadi) {
		this.edadi = edadi;
	}
	public int getEdadf() {
		return edadf;
	}
	public void setEdadf(int edadf) {
		this.edadf = edadf;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}

	public int getTarifa() {
		return tarifa;
	}

	public void setTarifa(int tarifa) {
		this.tarifa = tarifa;
	}
}