package com.seguros.presupuestos;

/**
 * Created by ALE77 on 28/03/2017.
 */
public class VendedorD {
    private String Id;
    private String cuenta;
    private String fechaact;
    private String version;
    private String versioninst;
    private String fechau;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getFechaact() {
        return fechaact;
    }

    public void setFechaact(String fechaact) {
        this.fechaact = fechaact;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersioninst() {
        return versioninst;
    }

    public void setVersioninst(String versioninst) {
        this.versioninst = versioninst;
    }

    public String getFechau() {
        return fechau;
    }

    public void setFechau(String fechau) {
        this.fechau = fechau;
    }
}
