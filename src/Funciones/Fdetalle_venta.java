package Funciones;

import Controlador.Abonos;
import Controlador.FrmGen_Abono;
import Controlador.FrmVentaDetalle;

import Datos.Ddetalle_venta;
import Datos.Dventa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;

public class Fdetalle_venta {

    private conexion mysql = new conexion();
    private Connection cn = mysql.conectar();
    private String sSQL = "";

    public Integer totalRegistros;

    public DefaultTableModel mostrar(String cod_venta) {
        int comp = Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());

        if (comp == 1) {
            //.showMessageDialog(null, "estes comprueba" + comp);
            DefaultTableModel modelo;
            String[] titulos
                    = {"Codigo detalle", "CODIGO", "NOMBRE ",
                        "PRECIO", "CANTIDAD",
                        "Cod Venta", "STOCK", "SUB TOTAL"};
            String[] registros = new String[8];
            totalRegistros = 0;
            modelo = new DefaultTableModel(null, titulos);

            sSQL = "SELECT cod_detalle ,cod_productoFK,"
                    + "(SELECT nombre_producto FROM producto WHERE cod_productoFK = cod_producto)AS productoNom, "
                    + " precio_producto ,cantidad_detalle, cod_ventaFK ,"
                    + "(SELECT stock_producto FROM producto WHERE cod_productoFK=cod_producto)As "
                    + "stock ,  subtotal FROM detalle_abono WHERE cod_ventaFK = '" + cod_venta + "' ORDER BY cod_detalle ASC ";

            try {

                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);

                while (rs.next()) {

                    registros[0] = rs.getString("cod_detalle");
                    registros[1] = rs.getString("cod_productoFK");
                    registros[2] = rs.getString("productoNom");
                    registros[3] = rs.getString("precio_producto");
                    registros[4] = rs.getString("cantidad_detalle");
                    registros[5] = rs.getString("cod_ventaFK");
                    registros[6] = rs.getString("stock");
                    registros[7] = rs.getString("subtotal");
                    totalRegistros = totalRegistros + 1;
                    modelo.addRow(registros);
                }
                return modelo;

            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e);
                return null;
            }

        } else if (comp == 0) {
            DefaultTableModel modelo;
            String[] titulos
                    = {"Codigo detalle", "CODIGO", "NOMBRE ",
                        "PRECIO", "CANTIDAD",
                        "Cod Venta", "STOCK", "SUB TOTAL"};
            String[] registros = new String[8];
            totalRegistros = 0;
            modelo = new DefaultTableModel(null, titulos);

            sSQL = "SELECT cod_detalle ,cod_productoFK,"
                    + "(SELECT nombre_producto FROM producto WHERE cod_productoFK = cod_producto)AS productoNom, "
                    + " precio_producto ,cantidad_detalle, cod_ventaFK ,"
                    + "(SELECT stock_producto FROM producto WHERE cod_productoFK=cod_producto)As "
                    + "stock ,  subtotal FROM detalle_venta WHERE cod_ventaFK = '" + cod_venta + "' ORDER BY cod_detalle ASC ";

            try {

                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);

                while (rs.next()) {

                    registros[0] = rs.getString("cod_detalle");
                    registros[1] = rs.getString("cod_productoFK");
                    registros[2] = rs.getString("productoNom");
                    registros[3] = rs.getString("precio_producto");
                    registros[4] = rs.getString("cantidad_detalle");
                    registros[5] = rs.getString("cod_ventaFK");
                    registros[6] = rs.getString("stock");
                    registros[7] = rs.getString("subtotal");
                    totalRegistros = totalRegistros + 1;
                    modelo.addRow(registros);
                }
                return modelo;

            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, e);
                return null;
            }
        } else {
            return null;
        }

    }

    public boolean insertar(Ddetalle_venta datos) {
        int comp = Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());
        if (comp == 1) {
            sSQL = "insert into detalle_abono (cantidad_detalle , cod_productoFK,precio_producto"
                    + ", cod_ventaFk ,subtotal,subPrecioCompra,precio_compra) values (?,?,?,?,?,?,?)";
            try {
                PreparedStatement pst = cn.prepareStatement(sSQL);
                pst.setInt(1, datos.getCantidad_detalle());
                pst.setLong(2, datos.getCod_productoFK());
                pst.setLong(3, datos.getPrecio_producto());
                pst.setInt(4, datos.getCod_ventaFK());
                pst.setLong(5, datos.getSubtotal());
                pst.setLong(6, datos.getSubPrecioCompra());
                pst.setLong(7, datos.getPrecio_compra());

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
        } else if (comp == 0) {
            sSQL = "insert into detalle_venta (cantidad_detalle , cod_productoFK,precio_producto"
                    + ", cod_ventaFk ,subtotal,subPrecioCompra,precio_compra) values (?,?,?,?,?,?,?)";
            try {
                PreparedStatement pst = cn.prepareStatement(sSQL);
                pst.setInt(1, datos.getCantidad_detalle());
                pst.setLong(2, datos.getCod_productoFK());
                pst.setLong(3, datos.getPrecio_producto());
                pst.setInt(4, datos.getCod_ventaFK());
                pst.setLong(5, datos.getSubtotal());
                pst.setLong(6, datos.getSubPrecioCompra());
                pst.setLong(7, datos.getPrecio_compra());

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
        } else {
            return false;
        }

    }//cierre funcion

    public boolean eliminar(Ddetalle_venta datos) {

        sSQL = "delete from detalle_venta where cod_detalle = ?";
        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            pst.setInt(1, datos.getCod_detalle());
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
    }//cierre funcion
    //////seccion para abonos 
    
    public long selec_venta(){
        long codigo = Long.parseLong(FrmGen_Abono.txtNumCedula.getText());
        sSQL = "SELECT  cod_venta  FROM cliente INNER JOIN abono ON (cod_cliente=cod_clienteFK) WHERE rut_cliente=" + codigo;
               
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
    public long selecAbono(){
        
        /*String  datos=FrmGen_Abono.cbxTipo.getSelectedItem().toString();
        if (datos.equals("Separados")){
            JOptionPane.showMessageDialog(null,"lo logre ");
        }else if(datos.equals("Creditos Varios")){
            
        }*/
        long codigo = Long.parseLong(FrmGen_Abono.txtcodVenta.getText());
        sSQL = "SELECT cod_venta, pago FROM abono WHERE cod_venta = " + codigo;
            try {
                long cod = 0;
                long pago= 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cod = rs.getLong("cod_venta");
                    pago=rs.getLong("pago");
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
        sSQL ="SELECT total_venta FROM abono WHERE cod_venta = "  + codigo;
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
        sSQL = "SELECT pago FROM abono WHERE cod_venta = " + codigo;
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
        sSQL = "SELECT cod_clienteFK FROM abono WHERE cod_venta = " + codigo;
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
    
    public boolean guardarPago(Dventa datos){
        sSQL = "update abono set pago = ? where cod_venta = ?";
        //JOptionPane.showMessageDialog(null,"estoy rm guaradr pago "+datos.getPago() +"cond venta "+datos.getCod_venta());
        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDouble(1, datos.getPago());         
            pst.setInt(2, datos.getCod_venta());
            
            
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
    
    public long cod_producto(){
         long codigo = Long.parseLong(FrmGen_Abono.txtNumfactura.getText());
        /*"SELECT total_venta FROM abono WHERE cod_venta = "
         SELECT cod_clienteFK FROM abono WHERE cod_venta = " + codigo;*/
        sSQL ="select cod_productoFK from detalle_abono where cod_ventaFK =  "  + codigo;
            try {
                long cod_productoFK = 0;
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cod_productoFK = rs.getLong("cod_productoFK");
                   
                }
                return cod_productoFK ;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
    
    public long cantidad(){
        long codigo = Long.parseLong(FrmGen_Abono.txtNumfactura.getText());
        long codPr= Long.parseLong(FrmGen_Abono.txtcodProducto.getText());
        /*"SELECT total_venta FROM abono WHERE cod_venta = "
         SELECT cod_clienteFK FROM abono WHERE cod_venta = " + codigo;*/
        sSQL ="select cantidad_detalle from detalle_abono where cod_productoFK = '"+codPr+"'and cod_ventaFK=" + codigo ;
            try {
                long cantidad_detalle = 0;
                //JOptionPane.showMessageDialog(null,"codigo:"+codigo+" codPro:  "+codPr);
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cantidad_detalle = rs.getLong("cantidad_detalle");
                   
                }
                return cantidad_detalle;
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
    public long stockFrmg(){
        long codigo = Long.parseLong(FrmGen_Abono.txtcodProducto.getText());
            sSQL = "SELECT stock_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                int stock = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    stock = rs.getInt("stock_producto");
                }
                return stock;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
    }
    public boolean modStock(Dventa datos){
         sSQL = "update producto set stock_producto= ? where cod_producto = ?";
        //JOptionPane.showMessageDialog(null,"es codigo producto: "+datos.getCompr()+"es cantidad productonuevo:"+datos.getCedula());
        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

                 
            pst.setDouble(1, datos.getCedula());
             pst.setDouble(2, datos.getCompr());   
            
            
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
    
    public boolean eliminardetalle(Dventa datos){
        sSQL = "delete from detalle_abono where cod_productoFK = ? and  cod_ventaFK=?";
        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);
            pst.setDouble(1, datos.getCompr());
            pst.setDouble(2, datos.getCod_venta());
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
    
    
    //termina la seccion de abono s 
    
    public long selecProd() {
        long compr = Long.parseLong(FrmVentaDetalle.txtComprueba.getText());

        if (compr == 1) {
            long codigo = Long.parseLong(Abonos.txtCod_producto.getText());
            sSQL = "SELECT cod_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                long cod = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cod = rs.getLong("cod_producto");
                }
                return cod;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else if (compr == 0) {
            long codigo = Long.parseLong(FrmVentaDetalle.txtCod_producto.getText());
            sSQL = "SELECT cod_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                long cod = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    cod = rs.getLong("cod_producto");
                   // JOptionPane.showMessageDialog(null, "estoy en slect prod " + codigo);
                }
                return cod;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else {
            return 0;
        }

    }//cierre funcion */

    public String SelectNombre() {
        long compr = Long.parseLong(FrmVentaDetalle.txtComprueba.getText());

        if (compr == 1) {

            long codigo = Long.parseLong(Abonos.txtCod_producto.getText());
            sSQL = "SELECT nombre_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                String nombre = "";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    nombre = rs.getString("nombre_producto");
                }
                return nombre;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return "";
            }
        } else if (compr == 0) {
            long codigo = Long.parseLong(FrmVentaDetalle.txtCod_producto.getText());
            sSQL = "SELECT nombre_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                String nombre = "";
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    nombre = rs.getString("nombre_producto");
                }
                return nombre;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return "";
            }
        } else {
            return "";
        }

    }//cierre funcion */

    public int selecStock() {
        
       long compr = Long.parseLong(FrmVentaDetalle.txtComprueba.getText());
       

        if (compr == 1) {
            long codigo = Long.parseLong(Abonos.txtCod_producto.getText());
            sSQL = "SELECT stock_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                int stock = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    stock = rs.getInt("stock_producto");
                }
                return stock;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else if (compr == 0) {
            long codigo = Long.parseLong(FrmVentaDetalle.txtCod_producto.getText());
            sSQL = "SELECT stock_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                int stock = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    stock = rs.getInt("stock_producto");
                }
                return stock;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else {
            

        if (compr == 2) {
           // long codigo = Long.parseLong(prod_abono.txtBuscar1.getText());
            sSQL = "SELECT stock_producto FROM producto WHERE cod_producto = "; //+ codigo;
            try {
                int stock = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    stock = rs.getInt("stock_producto");
                }
                return stock;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        }else{
                    return 0;
                    }

    }
    }//cierre funcion 

    public long selectPrecio() {
        long compr = Long.parseLong(FrmVentaDetalle.txtComprueba.getText());

        if (compr == 1) {
            long codigo = Long.parseLong(Abonos.txtCod_producto.getText());
            sSQL = "SELECT precio_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                long precio_producto = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    precio_producto = rs.getLong("precio_producto");
                }

                return precio_producto;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else if (compr == 0) {
            long codigo = Long.parseLong(FrmVentaDetalle.txtCod_producto.getText());
            sSQL = "SELECT precio_producto FROM producto WHERE cod_producto = " + codigo;
            try {
                long precio_producto = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    precio_producto = rs.getLong("precio_producto");
                }

                return precio_producto;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else {
            return 0;
        }

    }//cierre funcion

    public long selectPrecioCompra() {
        long compr = Long.parseLong(FrmVentaDetalle.txtComprueba.getText());

        if (compr == 1) {
            long codigo = Long.parseLong(Abonos.txtCod_producto.getText());
            sSQL = "SELECT precio_compra FROM producto WHERE cod_producto = " + codigo;
            try {
                long precio_productoCompra = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    precio_productoCompra = rs.getLong("precio_compra");
                }
                return precio_productoCompra;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else if (compr == 0) {
            long codigo = Long.parseLong(FrmVentaDetalle.txtCod_producto.getText());
            sSQL = "SELECT precio_compra FROM producto WHERE cod_producto = " + codigo;
            try {
                long precio_productoCompra = 0;
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sSQL);
                while (rs.next()) {
                    precio_productoCompra = rs.getLong("precio_compra");
                }
                return precio_productoCompra;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return 0;
            }
        } else {
            return 0;
        }

    }//cierre funcion

    public boolean insertarDetalle(Ddetalle_venta datos) {
        //JOptionPane.showMessageDialog(null, "estes el get comp" + datos.getcompr());

        if (datos.getcompr() == 1) {
            sSQL = "INSERT INTO detalle_abono (cantidad_detalle , cod_productoFK,precio_producto,"
                    + "cod_ventaFk ,subtotal,subPrecioCompra,precio_compra) VALUES (?,?,?,?,?,?,?)";
            try {
                PreparedStatement pst = cn.prepareStatement(sSQL);
                pst.setInt(1, datos.getCantidad_detalle());
                pst.setLong(2, datos.getCod_productoFK());
                pst.setLong(3, datos.getPrecio_producto());
                pst.setInt(4, datos.getCod_ventaFK());
                pst.setLong(5, datos.getSubtotal());
                pst.setLong(6, datos.getSubPrecioCompra());
                pst.setLong(7, datos.getPrecio_compra());
                
                
                int N = pst.executeUpdate();
                if (N != 0) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "El codigo ingresado no esta en el sistema");
                    return false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return false;
            }
        } else if (datos.getcompr() == 0) {
           // JOptionPane.showMessageDialog(null, "estes el get comp" + datos.getcompr());
            sSQL = "INSERT INTO detalle_venta (cantidad_detalle , cod_productoFK,precio_producto,"
                    + "cod_ventaFk ,subtotal,subPrecioCompra,precio_compra, precio_venta,descuento_detalle) VALUES (?,?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement pst = cn.prepareStatement(sSQL);
                pst.setInt(1, datos.getCantidad_detalle());
                pst.setLong(2, datos.getCod_productoFK());
                pst.setLong(3, datos.getPrecio_producto());
                pst.setInt(4, datos.getCod_ventaFK());
                pst.setLong(5, datos.getSubtotal());
                pst.setLong(6, datos.getSubPrecioCompra());
                pst.setLong(7, datos.getPrecio_compra());
                pst.setLong(8,datos.getTotal_venta());
                pst.setLong(9,datos.getDescuento_detalle());
                int N = pst.executeUpdate();
                if (N != 0) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "El codigo ingresado no esta en el sistema");
                    return false;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                return false;
            }
        } else {
            return false;
        }
    }//cierre funcion

}
