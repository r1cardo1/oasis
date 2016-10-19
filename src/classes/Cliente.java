/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.Serializable;

/**
 *
 * @author Ricardo Marcano
 */
public class Cliente implements Serializable{
    private String cedula;
    private String nombre;
    private String contrato;
    private String plan;
    private String banco;
    private String restringido;

    public void setRestringido(String restringido) {
        this.restringido = restringido;
    }

    public String getRestringido() {
        return restringido;
    }

    public Cliente(String cedula, String nombre, String contrato, String plan, String banco, String restringido) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.contrato = contrato;
        this.plan = plan;
        this.banco = banco;
        this.restringido = restringido;
    }

    public Cliente(String cedula, String nombre, String contrato, String plan, String banco) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.contrato = contrato;
        this.plan = plan;
        this.banco = banco;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContrato() {
        return contrato;
    }

    public String getPlan() {
        return plan;
    }

    public String getBanco() {
        return banco;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
    
    
}
