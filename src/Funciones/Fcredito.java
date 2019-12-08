/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import Controlador.FrmGen_Abono;
import Datos.Dcredito;
import Datos.Dventa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Uriel Henao
 */
public class Fcredito {
     private conexion mysql = new conexion();
    private Connection cn = mysql.conectar();
    private String sSQL = "";

    public Integer totalRegistros;
    
    //select cod_venta from cliente inner join venta on(cod_cliente=cod_clienteFK) where rut_cliente='10889345432' and metodo_pago="credito"
public long selec_venta(){
        long codigo = Long.parseLong(FrmGen_Abono.txtNumCedula.getText());
        sSQL = "select cod_venta from cliente inner join venta on(cod_cliente=cod_clienteFK) where rut_cliente='"+codigo+"' and metodo_pago='Credito'";

            try {
                long cod = 0;
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cod = rs.getLong("cod_venta");
                   
                }
                return cod ;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
public long selecTotalVenta (){
        long codigo = Long.parseLong(FrmGen_Abono.txtNumfactura.getText());
        /*"SELECT total_venta FROM abono WHERE cod_venta = "*/
        sSQL ="SELECT total_venta FROM venta WHERE cod_venta = "  + codigo;
            try {
                long total_venta = 0;
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    total_venta = rs.getLong("total_venta");
                   
                }
                return total_venta ;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
public long Pago(){
        long codigo = Long.parseLong(FrmGen_Abono.txtNumfactura.getText());
        sSQL = "SELECT pago FROM venta WHERE cod_venta = " + codigo;
            try {
                long pago = 0;
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    pago = rs.getLong("pago");
                    
                }
                return pago;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
public long Cliente(){
        long codigo = Long.parseLong(FrmGen_Abono.txtNumfactura.getText());
        sSQL = "SELECT cod_clienteFK FROM venta WHERE cod_venta = " + codigo;
            try {
                long cod_clienteFK = 0;
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cod_clienteFK = rs.getLong("cod_clienteFK");
                    
                }
                return cod_clienteFK;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
public String Nombre_cliente(){
        long codigo = Long.parseLong(FrmGen_Abono.txtCliente.getText());
            sSQL = "select nombre_persona from persona where cod_persona = " + codigo;
            try {
                String nombre = "";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    nombre = rs.getString("nombre_persona");
                }
                return nombre;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return "";
            }
    }
  
   public boolean insertar(Dcredito datos) {
       
            sSQL = "insert into creditos (cod_ventaFK, cod_personaFK, valor_pagado, fecha_pago) values (?,?,?,?)";
            try {
                PreparedStatement pst = cn.prepareStatement(sSQL);
                pst.setLong(1, datos.getCod_ventaFK());
                pst.setLong(2, datos.getCod_personaFK());
                pst.setLong(3, datos.getValor_pagado());
                pst.setDate(4,datos.getFecha_pago());
                
                
                
                

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
