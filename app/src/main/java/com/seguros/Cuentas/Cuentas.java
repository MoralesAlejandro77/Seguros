package com.seguros.Cuentas;

public class Cuentas {
	private String nombre;
	private String tipo;
	
	public Cuentas(String nombre,String tipo){
		this.nombre = nombre;
		this.tipo = tipo;
		
	}
	
	public Cuentas() {
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
