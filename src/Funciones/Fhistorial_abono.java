/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import Controlador.FrmVentaDetalle;
import Datos.Ddetalle_venta;
import Datos.Dhistorial_abono;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author Uriel Henao
 */
public class Fhistorial_abono {
    
    private conexion mysql = new conexion();
    private Connection cn = mysql.conectar();
    private String sSQL = "";

    public Integer totalRegistros;
    
    public boolean insertar(Dhistorial_abono datos) {
       
            sSQL = "insert into historial_abono (cod_ventaFK, cod_clienteFK, valor_abonado, fecha_abono) values (?,?,?,?)";
            try {
                PreparedStatement pst = cn.prepareStatement(sSQL);
                pst.setLong(1, datos.getCod_ventaFK());
                pst.setLong(2, datos.getCod_clienteFK());
                pst.setLong(3, datos.getValor_abonado());
                pst.setDate(4,datos.getFecha_abono());
                
                
                
                

                int N = pst.executeUpdate();
                if (N != 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return false;
            }
        
    }
}

    

