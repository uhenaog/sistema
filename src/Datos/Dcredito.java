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
public class Dcredito {
    
    private long cod_ventaFK;
    private long cod_personaFK;
    private long valor_pagado;
    private Date fecha_pago;

    public Dcredito() {
    }

    public Dcredito(long cod_ventaFK, long cod_personaFK, long valor_pagado, Date fecha_pago) {
        this.cod_ventaFK = cod_ventaFK;
        this.cod_personaFK = cod_personaFK;
        this.valor_pagado = valor_pagado;
        this.fecha_pago = fecha_pago;
    }

    public long getCod_ventaFK() {
        return cod_ventaFK;
    }

    public void setCod_ventaFK(long cod_ventaFK) {
        this.cod_ventaFK = cod_ventaFK;
    }

    public long getCod_personaFK() {
        return cod_personaFK;
    }

    public void setCod_personaFK(long cod_personaFK) {
        this.cod_personaFK = cod_personaFK;
    }

    public long getValor_pagado() {
        return valor_pagado;
    }

    public void setValor_pagado(long valor_pagado) {
        this.valor_pagado = valor_pagado;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }
    
    
    
}
