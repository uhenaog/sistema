/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import Controlador.FrmGen_Abono;
import java.sql.Connection;
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
        sSQL = "select cod_venta from cliente inner join venta on(cod_cliente=cod_clienteFK) where rut_cliente='"+codigo+"' and metodo_pago='credito'";

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
}
