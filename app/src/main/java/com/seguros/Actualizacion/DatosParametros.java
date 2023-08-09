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
 public class DatosParametros {
	private String tope1;
	private String status;


public DatosParametros(String valor)
   {
		this.tope1 = valor;
	}

  public DatosParametros() {
	// TODO Auto-generated constructor stub
}

public String getTope1() {
		return tope1;
	}



	public void setTope1(String p) {
		this.tope1   = p;

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}