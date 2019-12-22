package Funciones;

import Controlador.Abonos;
import Controlador.FrmMostrarVentas;
import Controlador.FrmVentaDetalle;
import Datos.Dproducto;
import Datos.Dventa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Fventa {

    private conexion mysql = new conexion();
    private Connection cn = mysql.conectar();
    private String sSQL = "";
    private String sSQL2 = "";
    public Integer totalRegistros;

    public DefaultTableModel mostrar() {

        DefaultTableModel modelo;

        String[] titulos = {"Numero",
            "Fecha ", "Total", "COD USU",
            "Usuario", "COD CLIE", "Cliente", "Comprobante", "Numero", "Dscto","Metodo Pago"};

        String[] registros = new String[11];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "select cod_venta , fecha_venta ,total_venta , cod_usuarioFK,"
                + "(select nombre_persona from persona where cod_persona = cod_usuarioFK)"
                + "as usuarioNom,cod_clienteFK ,"
                + "(select nombre_persona from persona where cod_persona = cod_clienteFK)"
                + "as clienteNom ,tipo_comprobante,num_factura,descuento,metodo_pago from venta order by cod_venta DESC";

        
        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            
            

            while (rs.next()) {

                registros[0] = rs.getString("cod_venta");
                registros[1] = rs.getString("fecha_venta");
                registros[2] = rs.getString("total_venta");
                registros[3] = rs.getString("cod_usuarioFK");
                registros[4] = rs.getString("usuarioNom");
                registros[5] = rs.getString("cod_clienteFK");
                registros[6] = rs.getString("clienteNom");
                registros[7] = rs.getString("tipo_comprobante");
                registros[8] = rs.getString("num_factura");
                registros[9] = rs.getString("descuento");
                registros[10] = rs.getString("metodo_pago");
                
                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
                
                
            }
            return modelo;

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
        

    }
    
    public DefaultTableModel mostrarabono() {

        DefaultTableModel modelo;

        String[] titulos = {"Numero",
            "Fecha ", "Total", "COD USU",
            "Usuario", "COD CLIE", "Cliente", "Comprobante", "Numero", "Dscto","Metodo Pago"};

        String[] registros = new String[11];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "select cod_venta , fecha_venta ,total_venta , cod_usuarioFK,"
                + "(select nombre_persona from persona where cod_persona = cod_usuarioFK)"
                + "as usuarioNom,cod_clienteFK ,"
                + "(select nombre_persona from persona where cod_persona = cod_clienteFK)"
                + "as clienteNom ,tipo_comprobante,num_factura,descuento,metodo_pago from abono order by cod_venta DESC";

        
        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            
            
            

            while (rs.next()) {

                registros[0] = rs.getString("cod_venta");
                registros[1] = rs.getString("fecha_venta");
                registros[2] = rs.getString("total_venta");
                registros[3] = rs.getString("cod_usuarioFK");
                registros[4] = rs.getString("usuarioNom");
                registros[5] = rs.getString("cod_clienteFK");
                registros[6] = rs.getString("clienteNom");
                registros[7] = rs.getString("tipo_comprobante");
                registros[8] = rs.getString("num_factura");
                registros[9] = rs.getString("descuento");
                registros[10] = rs.getString("metodo_pago");
                
                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
                
                
            }
            return modelo;

        } catch (Exception e) {
            JOptionPane.showConfirmDialog(null, e);
            return null;
        }
        

    }

    public boolean insertar(Dventa datos) {
        
        if(datos.getcompr()==1){
            //JOptionPane.showMessageDialog(null,"LO LOGRE"+datos.getCedula());
            ///////////
              sSQL = "insert into abono "
                + "(fecha_venta,total_venta,cod_usuarioFK,cod_clienteFK,tipo_comprobante,num_factura,descuento,pago,iva)"
                + "values(?,?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);
            
            pst.setDate(1, datos.getFecha_venta());
            pst.setLong(2, datos.getTotal_venta());
            pst.setInt(3, datos.getCod_usuarioFK());
            pst.setInt(4, datos.getCod_clienteFK());
            pst.setString(5, datos.getTipo_comprobante());
            pst.setInt(6, datos.getNum_factura());
             pst.setLong(7,datos.getDescuento());
            pst.setLong(8, datos.getPago());
            pst.setLong(9,datos.getIva());
           
            
           // JOptionPane.showMessageDialog(null, "es te es el mensaje ojo ++++++++"+datos.getPago()+"este es  comprueba"+datos.getcompr());
            
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
            
        }else if(datos.getcompr()==0) {
           sSQL = "insert into venta "
                + "(fecha_venta,total_venta,cod_usuarioFK,cod_clienteFK,tipo_comprobante,num_factura,descuento,pago,iva)"
                + "values(?,?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);
            
            pst.setDate(1, datos.getFecha_venta());
            pst.setLong(2, datos.getTotal_venta());
            pst.setInt(3, datos.getCod_usuarioFK());
            pst.setInt(4, datos.getCod_clienteFK());
            pst.setString(5, datos.getTipo_comprobante());
            pst.setInt(6, datos.getNum_factura());
                        pst.setLong(7,datos.getDescuento());
            pst.setLong(8, datos.getPago());
            pst.setLong(9,datos.getIva());
            
            //JOptionPane.showMessageDialog(null, "es te es el mensaje ojo ++++++++"+datos.getPago()+"este es  comprueba"+datos.getcompr());
            
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
        }else{
            return false;
        }

        

    }//cierre funcion

    public boolean editar(Dventa datos) {
       int comp= Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());
       if (comp==1){
            sSQL = "update abono set fecha_venta = ?, "
                + "total_venta = ? , cod_usuarioFK = ?  , cod_clienteFK = ? ,"
                + " tipo_comprobante =?,num_factura=? , descuento=? where cod_venta = ?, where iva=?";

        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDate(1, datos.getFecha_venta());
            pst.setLong(2, datos.getTotal_venta());
            pst.setInt(3, datos.getCod_usuarioFK());
            pst.setInt(4, datos.getCod_clienteFK());
            pst.setString(5, datos.getTipo_comprobante());
            pst.setInt(6, datos.getNum_factura());
            pst.setLong(7, datos.getDescuento());
            pst.setInt(8, datos.getCod_venta());
            pst.setLong(9,datos.getIva());

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
       }else if(comp==0){
           sSQL = "update venta set fecha_venta = ?, "
                + "total_venta = ? , cod_usuarioFK = ?  , cod_clienteFK = ? ,"
                + " tipo_comprobante =?,num_factura=? , descuento=? where cod_venta = ?, where iva=?";

        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDate(1, datos.getFecha_venta());
            pst.setLong(2, datos.getTotal_venta());
            pst.setInt(3, datos.getCod_usuarioFK());
            pst.setInt(4, datos.getCod_clienteFK());
            pst.setString(5, datos.getTipo_comprobante());
            pst.setInt(6, datos.getNum_factura());
            pst.setLong(7, datos.getDescuento());
            pst.setInt(8, datos.getCod_venta());
            pst.setLong(9,datos.getIva());

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
       }else{
           return false;
       }
       

    }//cierre funcion

    public boolean eliminar(Dventa datos) {
        sSQL = "delete from venta where cod_venta = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setInt(1, datos.getCod_venta());
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

    
    
      public boolean RestaurarProd(Dventa datos) {

        int codigo = Integer.parseInt(FrmMostrarVentas.txtCod_venta.getText());

        
    /*******AL ELIMINAR UNA VENTA VUELVEN LOS PRODUCTOS AL STOCK ANTERIOR***********/
        sSQL2 = "select cod_productoFK,sum(cantidad_detalle) as cantidad_detalle1,"
                + "(select stock_producto from producto where cod_producto = cod_productoFK) "
                + "as stock from detalle_venta where cod_ventaFK =" + codigo + " group"
                + " by cod_productoFK";
        try {
            int codigo_producto = 0;
            int cantidad_detalle = 0;
            int stock = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL2);
            while (rs.next()) {
                codigo_producto = rs.getInt("cod_productoFK");
                cantidad_detalle = rs.getInt("cantidad_detalle1");
                stock = rs.getInt("stock");

                /*
                JOptionPane.showMessageDialog(null, "Codigo Prod " + codigo_producto);
                JOptionPane.showMessageDialog(null, "Cantidad " + cantidad_detalle);
                JOptionPane.showMessageDialog(null, "stock " + stock);*/
                
                stock=stock+cantidad_detalle;
                
                Dproducto datos2 = new Dproducto();
                Fproducto funcion2 = new Fproducto();
                
                datos2.setCod_producto(codigo_producto);
                datos2.setStock_producto(stock);
                funcion2.ModificarStockProductos(datos2);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        /*****************TERMINO FUNCION DE VOLVER PRODUCTOS************************/

        
        sSQL = "delete from venta where cod_venta = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setInt(1, datos.getCod_venta());
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
public boolean eliminarAbono(Dventa datos){
    sSQL = "delete from abono where cod_venta = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setInt(1, datos.getCod_venta());
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
//cierre funcion

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*++++++++++FUNCIONES ADICIONALES+++++++++++++++++*/
    public DefaultTableModel Buscar(String buscar) {

        DefaultTableModel modelo;

        String[] titulos = {"Numero",
            "Fecha ", "Total", "COD USU",
            "usuario", "COD CLIE", "Cliente", "Comprobante", "Dscto"};

        String[] registros = new String[10];
        totalRegistros = 0;
        modelo = new DefaultTableModel(null, titulos);

        sSQL = "select cod_venta , fecha_venta , Replace(Format(total_venta, 0), ',', '.') as total_venta  ,"
                + " cod_usuarioFK, (select nombre_persona from persona "
                + "where cod_persona = cod_usuarioFK) as usuarioNom,"
                + " cod_clienteFK , (select nombre_persona from persona"
                + " where cod_persona = cod_clienteFK) as clienteNom ,tipo_comprobante,num_factura,descuento from venta where"
                + " (select nombre_persona from persona where cod_persona = cod_clienteFK)"
                + " LIKE '%" + buscar + "%' and cod_usuarioFK = "
                + "(select cod_usuario from usuario WHERE cod_usuario = cod_usuarioFK )"
                + "order by cod_venta DESC ";

        try {

            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);

            while (rs.next()) {

                registros[0] = rs.getString("cod_venta");
                registros[1] = rs.getString("fecha_venta");
                registros[2] = rs.getString("total_venta");
                registros[3] = rs.getString("cod_usuarioFK");
                registros[4] = rs.getString("usuarioNom");
                registros[5] = rs.getString("cod_clienteFK");
                registros[6] = rs.getString("clienteNom");
                registros[7] = rs.getString("tipo_comprobante");
                registros[8] = rs.getString("num_factura");
                registros[9] = rs.getString("descuento");
                totalRegistros = totalRegistros + 1;
                modelo.addRow(registros);
            }
            return modelo;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }

    }

    public int BuscarCodigoVenta() {
        int comp= Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());
        
        if(comp==1){
            
          sSQL = "SELECT cod_venta from abono order by cod_venta DESC limit 1 ";

        try {
            int codigo_venta = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while (rs.next()) {
                codigo_venta = rs.getInt("cod_venta");
            }

            return codigo_venta;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }   
        
    }else if(comp==0){
         sSQL = "SELECT cod_venta from venta order by cod_venta DESC limit 1 ";

        try {
            int codigo_venta = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while (rs.next()) {
                codigo_venta = rs.getInt("cod_venta");
            }

            return codigo_venta;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }
    }else{
        return 0;
    }

       

    }
    public int BuscarClienteAbono(){
        sSQL = "SELECT cedula from abonos order by cedula DESC limit 1 ";

        try {
            int cedula = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while (rs.next()) {
                cedula = rs.getInt("cedula");
            }

            return cedula;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }

    }

    public boolean Total(Dventa datos) {
        int comp= Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());
        if (comp== 1){
            
           
             sSQL = "update abono set total_venta = ?, iva = ?"                
                + " where cod_venta = ?";

        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDouble(1, datos.getTotal_venta());            
            pst.setLong(2,datos.getIva());
            pst.setInt(3, datos.getCod_venta());
            
//JOptionPane.showMessageDialog(null,"este es total de Ventas"+datos.getIva()+"estes total VENTAS"+datos.getTotal_venta()+"\n estes codigo venta "+datos.getCod_venta());
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
        }else if (comp==0){
             sSQL = "update venta set total_venta = ?, iva = ?"                
                + " where cod_venta = ?";

        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDouble(1, datos.getTotal_venta());            
            pst.setLong(2,datos.getIva());
            pst.setInt(3, datos.getCod_venta());
            
//JOptionPane.showMessageDialog(null,"este es total de Ventas"+datos.getIva());
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
        }else{
            return false;
        }
        

       

    }

    public boolean Pago(Dventa datos) {
        
         int comp= Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());
        if (comp== 1){
           sSQL = "update abono set pago = ? , metodo_pago = ? , nomCaja= ? where cod_venta = ?";
        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDouble(1, datos.getPago());
            pst.setString(2, datos.getMetodo_pago());
            pst.setString(3, datos.getNomCaja());
            pst.setInt(4, datos.getCod_venta());
            
            
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
        }else if(comp==0){
          sSQL = "update venta set pago = ? , metodo_pago = ? , nomCaja= ? where cod_venta = ?";
        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDouble(1, datos.getPago());
            pst.setString(2, datos.getMetodo_pago());
            pst.setString(3, datos.getNomCaja());
            pst.setInt(4, datos.getCod_venta());
            
            
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
        }else{
            return false;
        }

        

    }

    public int BuscarNfacturas() {

        sSQL = "SELECT COUNT(*) as Nfactura FROM venta WHERE tipo_comprobante ='Factura'";

        try {
            int Nfactura = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while (rs.next()) {
                Nfactura = rs.getInt("Nfactura");
            }

            return Nfactura;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }

    }
    public boolean guardarPago(Dventa datos){
        sSQL = "update venta set pago = ? where cod_venta = ?";
        //JOptionPane.showMessageDialog(null,"estoy rm guaradr pago "+datos.getPago() +"cond venta "+datos.getCod_venta());
        try {

            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setDouble(1, datos.getPago());         
            pst.setLong(2, datos.getCod_venta());
            
            
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
//verificar si existe un abono con el cliente 
 public int BuscarCod_cleinte(){
     int codigo=Integer.parseInt(Abonos.txtCod_cliente.getText().toString());
sSQL = "select cod_clienteFK from abono where cod_clienteFK='"+codigo+"' and total_venta!=pago";

        try {
            int codigo_clienteFK = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL);
            while (rs.next()) {
                codigo_clienteFK = rs.getInt("cod_clienteFK");
            }

            return codigo_clienteFK;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return 0;
        }
}
}

//fin de la verificacion 
/*

 public boolean eliminar(Dventa datos) {

        int codigo = Integer.parseInt(FrmMostrarVentas.txtCod_venta.getText());

        sSQL2 = "select cod_productoFK,sum(cantidad_detalle) as cantidad_detalle1 from detalle_venta where cod_ventaFK ="+codigo+" group by cod_productoFK";
        try {
            int codigo_producto = 0;
            int cantidad_detalle = 0;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sSQL2);
            while (rs.next()) {
                codigo_producto = rs.getInt("cod_productoFK");
                cantidad_detalle = rs.getInt("cantidad_detalle1");
                JOptionPane.showMessageDialog(null,"Codigo Prod "+ codigo_producto);
                JOptionPane.showMessageDialog(null, "Cantidad "+cantidad_detalle);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

        sSQL = "delete from venta where cod_venta = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(sSQL);

            pst.setInt(1, datos.getCod_venta());
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
*/
