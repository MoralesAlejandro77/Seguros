package com.seguros.presupuestos;

public class Integrantes {
	public Integrantes(int id, String fechanac, int edad, float luto, float parcela, float sepelio,
			  float premio) {
		this.id = id;
		this.fechanac = fechanac;
		this.edad = edad;
		this.sepelio = sepelio;
		this.parcela = parcela;
		this.luto = luto;
		this.premio = premio;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFechanac() {
		return fechanac;
	}
	public void setFechanac(String fechanac) {
		this.fechanac = fechanac;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public float getSepelio() {
		return sepelio;
	}
	public void setSepelio(float sepelio) {
		this.sepelio = sepelio;
	}
	public float getParcela() {
		return parcela;
	}
	public void setParcela(float parcela) {
		this.parcela = parcela;
	}
	public float getLuto() {
		return luto;
	}
	public void setLuto(float luto) {
		this.luto = luto;
	}
	public float getPremio() {
		return premio;
	}
	public void setPremio(float premio) {
		this.premio = premio;
	}
	private int id;
	private String fechanac;
	private int edad;
	private float sepelio;
	private float parcela;
	private float luto;
	private float premio;
	

}
