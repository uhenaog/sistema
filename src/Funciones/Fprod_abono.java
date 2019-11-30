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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Uriel Henao
 */
public class Fprod_abono {
    
    private conexion mysql = new conexion(); //Instanciando la clase conexion
    private Connection cn = mysql.conectar();
    private String sSQL = ""; //Sentencia SQL
    private String sSQL2 = "";
    public Integer totalRegistros; // Obtener los registros
    
    
     public DefaultTableModel mostrar(String buscar ) {
         
        DefaultTableModel modelo;
        
        buscar =FrmGen_Abono.txtNumfactura.getText();

        String[] titulos
                = {"Codigo","Codigo", "Nombre del producto ",
                    "cantidad separada", "nombre del cliente "
                    };

        String[] registros = new String[5];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL ="select cod_ventaFK,cod_productoFK, nombre_producto,cantidad_detalle,nombre_persona "
                + " from  detalle_abono inner join producto on (cod_productoFK=cod_producto)"
                + " inner join abono on (cod_ventaFK= cod_venta) inner join cliente on (cod_clienteFK=cod_cliente) "
                + "inner join persona on (cod_cliente=cod_persona)"
                + " where cod_ventaFK like '%" + buscar + "%' order by cod_persona desc";
                
                /*"select p.cod_persona , p.nombre_persona,p.direccion,p.telefono"
                + " , p.email, c.rut_cliente from "
                + " persona p inner join cliente c on p.cod_persona = c.cod_cliente "
                + " where nombre_persona like '%" + buscar + "%' order by cod_persona desc";*/

        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);

            while (rs.next()) {

                registros[0] = rs.getString("cod_ventaFK");
                registros[1] = rs.getString("cod_productoFK");
                registros[2] = rs.getString("nombre_producto");
                registros[3] = rs.getString("cantidad_detalle");
                registros[4] = rs.getString("nombre_persona");
                
               

                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }

    }
}
