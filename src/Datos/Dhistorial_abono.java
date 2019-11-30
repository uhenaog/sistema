/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datos;

import java.sql.Date;

/**
 *
 * @author Uriel Henao
 */
public class Dhistorial_abono {

    private long cod_ventaFK;
    private long cod_clienteFK;
    private long valor_abonado;
    private Date fecha_abono;
    private long historial_abonocol;

    public Dhistorial_abono(long cod_ventaFK, long cod_clienteFK, long valor_abonado, Date fecha_abono, long historial_abonocol) {
        this.cod_ventaFK = cod_ventaFK;
        this.cod_clienteFK = cod_clienteFK;
        this.valor_abonado = valor_abonado;
        this.fecha_abono = fecha_abono;
        this.historial_abonocol = historial_abonocol;
    }

    public Dhistorial_abono() {
       
    }

    public long getCod_ventaFK() {
        return cod_ventaFK;
    }

    public void setCod_ventaFK(long cod_ventaFK) {
        this.cod_ventaFK = cod_ventaFK;
    }

    public long getCod_clienteFK() {
        return cod_clienteFK;
    }

    public void setCod_clienteFK(long cod_clienteFK) {
        this.cod_clienteFK = cod_clienteFK;
    }

    public long getValor_abonado() {
        return valor_abonado;
    }

    public void setValor_abonado(long valor_abonado) {
        this.valor_abonado = valor_abonado;
    }

    public Date getFecha_abono() {
        return fecha_abono;
    }

    public void setFecha_abono(Date fecha_abono) {
        this.fecha_abono = fecha_abono;
    }

    public long getHistorial_abonocol() {
        return historial_abonocol;
    }

    public void setHistorial_abonocol(long historial_abonocol) {
        this.historial_abonocol = historial_abonocol;
    }
    

}

