package Controlador;

import static Controlador.FRMPRINCIPAL.deskPricipal;
import Datos.Dapertura;
import Datos.Dcredito;
import Datos.Dhistorial_abono;
import Datos.Dventa;
import Funciones.FcajaApertura;
import Funciones.FcajaCierre;
import Funciones.Fcliente;
import Funciones.Fcredito;
import Funciones.Fdetalle_venta;
import Funciones.Fhistorial_abono;
import Funciones.Fprod_abono;
import Funciones.Fventa;
import Funciones.conexion;
import Reportes.VistaBoleta;
import java.awt.Component;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.scene.input.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public final class FrmGen_Abono extends javax.swing.JInternalFrame {

    private conexion mysql = new conexion();
    private Connection cn = mysql.conectar();

    public FrmGen_Abono() {
        initComponents();    
        
        Calendar c2 = new GregorianCalendar();
        dcFecha_apertura.setCalendar(c2);
        Date ahora = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("hh:mm:ss");
        txtCliente.setText(formateador.format(ahora));
        txtCliente.setVisible(false);
        btnentrega.setEnabled(false);

        lblCodUsuario.setVisible(false);
        txtpago.setVisible(false);
        txtNombreCajero.setEditable(false);
        txtcantidadven.setVisible(false);
        txtStock.setVisible(false);
        txtcodProducto.setVisible(false);
        txtcodVenta.setVisible(false);
        cmbtipo.setVisible(false);
       jLabel11.setVisible(false);

        dcFecha_apertura.setEnabled(false);
        txtSaldo.setEnabled(false);
        txtTotal_venta.setEnabled(false);
        txtNombre_cliente.setEditable(false);
        
        
        jTabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                //l.setBorder(new LineBorder(Color.black, 1));
                l.setBackground(new java.awt.Color(36, 33, 33));
                l.setForeground(new java.awt.Color(25, 118, 210));
                l.setFont(new java.awt.Font("Arial", 1, 12));
                return l;
            }
        });
        

    }
 public void ocultar_columnas() {
        jTabla.getColumnModel().getColumn(0).setMaxWidth(0);
        jTabla.getColumnModel().getColumn(0).setMinWidth(0);
        jTabla.getColumnModel().getColumn(0).setPreferredWidth(0);
    }

    public void mostrar(String buscar) {
        try {
            DefaultTableModel modelo;
            Fprod_abono funcion = new Fprod_abono();
            modelo = funcion.mostrar(buscar);

            jTabla.setModel(modelo);

         //   lblTotalRegistros.setText("N° Registros : " + Integer.toString(funcion.totalRegistros));
            ocultar_columnas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public void selecCod_venta(){
        
         Fdetalle_venta funcion = new Fdetalle_venta();
        long cod_venta = funcion.selec_venta();

        if (cod_venta > 0) {
            //JOptionPane.showMessageDialog(null,"este es cod Venta "+cod_venta);
            txtcodVenta.setText(String.valueOf(cod_venta));
            txtNumfactura.setText(String.valueOf(cod_venta));
        }
        
    }
    public void seleccionProd() {
        mostrar("");

        Fdetalle_venta funcion = new Fdetalle_venta();
        long cod_venta = funcion.selecAbono();

        if (cod_venta > 0) {

            txtNumfactura.setText(String.valueOf(cod_venta));

            
            long total_venta = funcion.selecTotalVenta();
            txtTotal_venta.setText(String.valueOf(total_venta));

//JOptionPane.showMessageDialog(null,"lo logre "+String.valueOf(total_venta));
            long pago = funcion.Pago();
            long saldo = total_venta - pago;
            txtSaldo.setText(String.valueOf(saldo));
            txtpago.setText(String.valueOf(pago));

            long cod_clienteFK = funcion.Cliente();
            txtCliente.setText(String.valueOf(cod_clienteFK));

            String nombre_persona = funcion.Nombre_cliente();
            txtNombre_cliente.setText(String.valueOf(nombre_persona));
            //txtCantidadProducto.setText("");

        } else {
            JOptionPane.showMessageDialog(null, "El valor ingresado no saldos pendientes por favor verifique ");
        }

    }

    public void factura() {
        java.util.Locale locale = new Locale("es", "CL");
        try {
            int codig = Integer.parseInt(txtNumfactura.getText());

            JasperReport jr = (JasperReport) JRLoader.loadObject(VistaBoleta.class.getResource("/Reportes/RptBoleta_Abonos.jasper"));

            Map parametro = new HashMap<String, Integer>();
                                parametro.put("logo",this.getClass().getResourceAsStream("/ImagenesReport/logo1.png"));

            parametro.put("cod_venta", codig);
            parametro.put(JRParameter.REPORT_LOCALE, locale);

            JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
            //JasperViewer jv = new JasperViewer(jp, true);
            //jv.show();

            JasperPrintManager.printReport(jp, false);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(rootPane, "error" + e);
        }
    }
public void facturaCre(){
     java.util.Locale locale = new Locale("es", "CL");

            
                try {
                    int codigo = Integer.parseInt(txtNumfactura.getText());
                    //long pago = Long.parseLong(txtImporte.getText());
                    JasperReport jr = (JasperReport) JRLoader.loadObject(VistaBoleta.class.getResource("/Reportes/RptFactura.jasper"));//aqui

                    Map parametro = new HashMap<String, Integer>();
                    parametro.put(JRParameter.REPORT_LOCALE, locale);
                    parametro.put("logo",this.getClass().getResourceAsStream("/ImagenesReport/logo1.png"));
                    parametro.put("cod_venta", codigo);

                    JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
                    JasperViewer jv = new JasperViewer(jp, true);
                    //jv.show();

                    JasperPrintManager.printReport( jp, false);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(rootPane, "error" + e);
                }
}
    public void guardar_Abono() {
        historial_abono();
        if (txtSaldo.getText().equals(txtAbono_add.getText())){
            btnentrega.setEnabled(true);    
            
            Fdetalle_venta funcion = new Fdetalle_venta();
        Dventa datos = new Dventa();

        int total = Integer.parseInt(txtAbono_add.getText().toString());
        int pago_anterior = Integer.parseInt(txtpago.getText().toString());
        int codigo = Integer.parseInt(txtNumfactura.getText().toString());
        int abono = pago_anterior + total;
        

//datos para guardar el abono en la base de datos;
        Calendar cal;
        int d, m, a;
        cal = dcFecha_apertura.getCalendar();
        d = cal.get(Calendar.DAY_OF_MONTH);
        m = cal.get(Calendar.MONTH);
        a = cal.get(Calendar.YEAR) - 1900;
        datos.setFecha_abono(new java.sql.Date(a, m, d));
        datos.setPago(abono);

        datos.setCod_venta(codigo);
        funcion.guardarPago(datos);
        
        
        JOptionPane.showMessageDialog(rootPane,"ya puede entregar los productos");
        txtAbono_add.setText("0");
        txtSaldo.setText("0");
        return;

        
            
        }else{
            Fdetalle_venta funcion = new Fdetalle_venta();
        Dventa datos = new Dventa();

        int total = Integer.parseInt(txtAbono_add.getText().toString());
        int pago_anterior = Integer.parseInt(txtpago.getText().toString());
        int codigo = Integer.parseInt(txtNumfactura.getText().toString());
        int abono = pago_anterior + total;
        

//datos para guardar el abono en la base de datos;
        Calendar cal;
        int d, m, a;
        cal = dcFecha_apertura.getCalendar();
        d = cal.get(Calendar.DAY_OF_MONTH);
        m = cal.get(Calendar.MONTH);
        a = cal.get(Calendar.YEAR) - 1900;
        datos.setFecha_abono(new java.sql.Date(a, m, d));
        datos.setPago(abono);

        datos.setCod_venta(codigo);
        funcion.guardarPago(datos);
        factura();
        

        txtNumfactura.setText("");
        txtAbono_add.setText("");
        txtNombre_cliente.setText("");
        txtSaldo.setText("0");
        txtTotal_venta.setText("");
        txtNumCedula.setText("");
        }

        

    }
    public void historial_abono(){
        
        Fhistorial_abono fun =new Fhistorial_abono();
        Dhistorial_abono datos = new Dhistorial_abono();
        
        Calendar cal;
        int d, m, a;
        cal = dcFecha_apertura.getCalendar();
        d = cal.get(Calendar.DAY_OF_MONTH);
        m = cal.get(Calendar.MONTH);
        a = cal.get(Calendar.YEAR) - 1900;
        datos.setFecha_abono(new java.sql.Date(a, m, d));
        datos.setCod_ventaFK(Integer.parseInt(txtcodVenta.getText()));
        datos.setValor_abonado(Integer.parseInt(txtAbono_add.getText()));
        datos.setCod_clienteFK(Integer.parseInt(txtCliente.getText()));
        fun.insertar(datos);
    }

    public void modfstock() {
        Fdetalle_venta fun = new Fdetalle_venta();
        Dventa datos = new Dventa();
        int codigo = Integer.parseInt(txtNumfactura.getText().toString());
        datos.setCod_venta(codigo);

        long cod_producto = 1;//long cod_producto = fun.cod_producto();

        while (cod_producto > 0) {
            fun.cod_producto();
            cod_producto = fun.cod_producto();
            txtcodProducto.setText(String.valueOf(cod_producto));

            //buscar cantidad 
            datos.setCompr(cod_producto);
            fun.cantidad();

            long cantidad_detalle = fun.cantidad();
            txtcantidadven.setText(String.valueOf(cantidad_detalle));

            long stock_producto = fun.stockFrmg();
            txtStock.setText(String.valueOf(stock_producto));

            // actualizar producto
            int cantidad = 0;
            cantidad = (Integer.parseInt(txtStock.getText().toString())) - (Integer.parseInt(txtcantidadven.getText().toString()));
            datos.setCedula(cantidad);
            fun.modStock(datos);
            //eliminar venta 
            fun.eliminardetalle(datos);

            //JOptionPane.showMessageDialog(null,"estes es el valor de codigo "+txtcodProducto.getText().toString()+"estes es cantidad "+txtcantidadven.getText().toString());
        }

    }

    public void entregarProducto() {

        if ((Integer.parseInt(txtSaldo.getText().toString()) == Integer.parseInt(txtAbono_add.getText().toString()))) {
             factura();

            /*Fdetalle_venta funcion = new Fdetalle_venta();
            Dventa datos = new Dventa();

            int total = Integer.parseInt(txtAbono_add.getText().toString());
            int pago_anterior = Integer.parseInt(txtpago.getText().toString());
            int codigo = Integer.parseInt(txtNumfactura.getText().toString());
            int abono = pago_anterior + total;

            datos.setPago(abono);
            datos.setCod_venta(codigo);

            funcion.guardarPago(datos);*/
            modfstock();
            
            txtNumfactura.setText("");
            txtAbono_add.setText("");
            txtNombre_cliente.setText("");
            txtSaldo.setText("0");
            txtTotal_venta.setText("");
            txtNumCedula.setText("");
            btnentrega.setEnabled(false);
           

        } else {
            int faltante = (Integer.parseInt(txtSaldo.getText().toString())) - (Integer.parseInt(txtAbono_add.getText().toString()));
            JOptionPane.showMessageDialog(null, "No se Puede Entregar el Producto porque faltan  $" + faltante);

        }

    }
    public void creditos(){
       // JOptionPane.showMessageDialog(null, "este es creditos en construccion ");
        Fcredito funcion = new Fcredito();
        long cod_venta = funcion.selec_venta();

        if (cod_venta > 0) {
            //JOptionPane.showMessageDialog(null,"este es cod Venta "+cod_venta);
            txtcodVenta.setText(String.valueOf(cod_venta));
            txtNumfactura.setText(String.valueOf(cod_venta));
            
               long total_venta = funcion.selecTotalVenta();
            txtTotal_venta.setText(String.valueOf(total_venta));
            
            long pago = funcion.Pago();
            long saldo = total_venta - pago;
            txtSaldo.setText(String.valueOf(saldo));
            txtpago.setText(String.valueOf(pago));
            
             long cod_clienteFK = funcion.Cliente();
            txtCliente.setText(String.valueOf(cod_clienteFK));

            String nombre_persona = funcion.Nombre_cliente();
            txtNombre_cliente.setText(String.valueOf(nombre_persona));
        }else {
            JOptionPane.showMessageDialog(null, "El valor ingresado no saldos pendientes por favor verifique ");
        }
        
    }
    public void guardar_credito(){
        JOptionPane.showMessageDialog(null, "este es creditos en construccion ");
         Fcredito funcion = new Fcredito();
        Dcredito datos = new Dcredito();
        Fventa fun =new Fventa();
        Dventa dat =new Dventa();

        int total = Integer.parseInt(txtAbono_add.getText().toString());
        int pago_anterior = Integer.parseInt(txtpago.getText().toString());
        int codigo = Integer.parseInt(txtNumfactura.getText().toString());
        int abono = pago_anterior + total;
        //datos para actualizar pago en venta 
        dat.setPago(abono);
        dat.setCod_venta(codigo);
        fun.guardarPago(dat);

//datos para guardar el abono en la base de datos;
        Calendar cal;
        int d, m, a;
        cal = dcFecha_apertura.getCalendar();
        d = cal.get(Calendar.DAY_OF_MONTH);
        m = cal.get(Calendar.MONTH);
        a = cal.get(Calendar.YEAR) - 1900;
        datos.setFecha_pago(new java.sql.Date(a, m, d));
        datos.setValor_pagado(total);
        datos.setCod_ventaFK(codigo);
        datos.setCod_personaFK((Integer.parseInt(txtCliente.getText())));
        funcion.insertar(datos);
        facturaCre();
        

        txtNumfactura.setText("");
        txtAbono_add.setText("");
        txtNombre_cliente.setText("");
        txtSaldo.setText("0");
        txtTotal_venta.setText("");
        txtNumCedula.setText("");
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtAbono_add = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreCajero = new javax.swing.JTextField();
        dcFecha_apertura = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNumfactura = new javax.swing.JTextField();
        txtNombre_cliente = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        txtTotal_venta = new javax.swing.JTextField();
        txtCliente = new javax.swing.JLabel();
        btnentrega = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        txtNumCedula = new javax.swing.JTextField();
        cmbtipo = new javax.swing.JComboBox<String>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabla = jTabla = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jLabel4 = new javax.swing.JLabel();
        lblCodUsuario = new javax.swing.JLabel();
        txtpago = new javax.swing.JLabel();
        txtcodVenta = new javax.swing.JLabel();
        txtcantidadven = new javax.swing.JLabel();
        txtStock = new javax.swing.JLabel();
        txtcodProducto = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setToolTipText("");

        jPanel2.setBackground(new java.awt.Color(36, 33, 33));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBackground(new java.awt.Color(36, 33, 33));

        jPanel3.setBackground(new java.awt.Color(36, 33, 33));

        txtAbono_add.setBackground(new java.awt.Color(36, 33, 33));
        txtAbono_add.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        txtAbono_add.setForeground(new java.awt.Color(207, 207, 207));
        txtAbono_add.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAbono_add.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtAbono_add.setCaretColor(new java.awt.Color(255, 255, 255));
        txtAbono_add.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtAbono_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAbono_addActionPerformed(evt);
            }
        });
        txtAbono_add.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAbono_addKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(207, 207, 207));
        jLabel3.setText("Monto  :");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(207, 207, 207));
        jLabel2.setText("Cajero :");

        txtNombreCajero.setBackground(new java.awt.Color(36, 33, 33));
        txtNombreCajero.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNombreCajero.setForeground(new java.awt.Color(207, 207, 207));
        txtNombreCajero.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNombreCajero.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombreCajero.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombreCajero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreCajeroActionPerformed(evt);
            }
        });
        txtNombreCajero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreCajeroKeyTyped(evt);
            }
        });

        dcFecha_apertura.setBackground(new java.awt.Color(36, 33, 33));
        dcFecha_apertura.setForeground(new java.awt.Color(207, 207, 207));
        dcFecha_apertura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(207, 207, 207));
        jLabel5.setText("Fecha :");

        btnGuardar.setBackground(new java.awt.Color(36, 33, 33));
        btnGuardar.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(224, 224, 224));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/guardar.png"))); // NOI18N
        btnGuardar.setToolTipText("");
        btnGuardar.setFocusable(false);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(207, 207, 207));
        jLabel6.setText("Total venta ");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(207, 207, 207));
        jLabel7.setText("saldo a la fecha  ");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(207, 207, 207));
        jLabel8.setText("numerio de factura  :");

        txtNumfactura.setBackground(new java.awt.Color(36, 33, 33));
        txtNumfactura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNumfactura.setForeground(new java.awt.Color(207, 207, 207));
        txtNumfactura.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNumfactura.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNumfactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumfacturaActionPerformed(evt);
            }
        });

        txtNombre_cliente.setBackground(new java.awt.Color(36, 33, 33));
        txtNombre_cliente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNombre_cliente.setForeground(new java.awt.Color(207, 207, 207));
        txtNombre_cliente.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNombre_cliente.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombre_cliente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombre_cliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre_clienteActionPerformed(evt);
            }
        });
        txtNombre_cliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombre_clienteKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(207, 207, 207));
        jLabel9.setText("Nombre del Cliente ");

        txtSaldo.setBackground(new java.awt.Color(36, 33, 33));
        txtSaldo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtSaldo.setForeground(new java.awt.Color(207, 207, 207));
        txtSaldo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtSaldo.setCaretColor(new java.awt.Color(255, 255, 255));
        txtSaldo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSaldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoActionPerformed(evt);
            }
        });
        txtSaldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSaldoKeyTyped(evt);
            }
        });

        txtTotal_venta.setBackground(new java.awt.Color(36, 33, 33));
        txtTotal_venta.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtTotal_venta.setForeground(new java.awt.Color(207, 207, 207));
        txtTotal_venta.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtTotal_venta.setCaretColor(new java.awt.Color(255, 255, 255));
        txtTotal_venta.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotal_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotal_ventaActionPerformed(evt);
            }
        });
        txtTotal_venta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTotal_ventaKeyTyped(evt);
            }
        });

        txtCliente.setText("jLabel1");

        btnentrega.setBackground(new java.awt.Color(36, 33, 33));
        btnentrega.setFont(new java.awt.Font("Arial Black", 0, 11)); // NOI18N
        btnentrega.setText("Entregar Producto");
        btnentrega.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnentregaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(207, 207, 207));
        jLabel10.setText("numerio de Cedula:");

        txtNumCedula.setBackground(new java.awt.Color(36, 33, 33));
        txtNumCedula.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNumCedula.setForeground(new java.awt.Color(207, 207, 207));
        txtNumCedula.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNumCedula.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNumCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumCedulaActionPerformed(evt);
            }
        });

        cmbtipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Abono", "Credito" }));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(207, 207, 207));
        jLabel11.setText("tipo pago ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(txtCliente)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtNumfactura, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtAbono_add, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(dcFecha_apertura, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                            .addComponent(txtNombre_cliente, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtNombreCajero, javax.swing.GroupLayout.Alignment.TRAILING))))))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(jLabel5)
                                .addComponent(jLabel8)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7))
                            .addGap(18, 18, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSaldo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTotal_venta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(221, 221, 221))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtNumCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                                    .addComponent(cmbtipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(0, 29, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(btnentrega)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbtipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtNumCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtNumfactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTotal_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtNombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreCajero, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dcFecha_apertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAbono_add)
                    .addComponent(jLabel3))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnentrega, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtCliente)
                        .addContainerGap())))
        );

        jTabla.setBackground(new java.awt.Color(36, 33, 33));
        jTabla.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabla.setForeground(new java.awt.Color(207, 207, 207));
        jTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jTabla.setRowHeight(20);
        jTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTablaMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTabla);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel4.setBackground(new java.awt.Color(245, 245, 245));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(207, 207, 207));
        jLabel4.setText("MODULO ABONOS");

        lblCodUsuario.setForeground(new java.awt.Color(255, 51, 51));
        lblCodUsuario.setText("jLabel1");

        txtpago.setText("jLabel1");

        txtcodVenta.setText("jLabel1");

        txtcantidadven.setText("jLabel1");

        txtStock.setText("jLabel1");

        txtcodProducto.setText("jLabel1");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblCodUsuario)
                .addGap(102, 102, 102)
                .addComponent(jLabel4)
                .addGap(41, 41, 41)
                .addComponent(txtpago)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtStock)
                .addGap(18, 18, 18)
                .addComponent(txtcantidadven)
                .addGap(36, 36, 36)
                .addComponent(txtcodProducto)
                .addGap(105, 105, 105))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(409, Short.MAX_VALUE)
                    .addComponent(txtcodVenta)
                    .addGap(32, 32, 32)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblCodUsuario)
                    .addComponent(txtpago))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtcodProducto)
                    .addComponent(txtcantidadven)
                    .addComponent(txtStock))
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(txtcodVenta)
                    .addContainerGap(473, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        if(cmbtipo.getSelectedItem().equals("Credito")){
            guardar_credito();
        }else if(cmbtipo.getSelectedItem().equals("Abono")){
        guardar_Abono();
        }
        
      
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void txtNombreCajeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreCajeroActionPerformed

    }//GEN-LAST:event_txtNombreCajeroActionPerformed

    private void txtAbono_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAbono_addActionPerformed
        txtAbono_add.transferFocus();
    }//GEN-LAST:event_txtAbono_addActionPerformed

    private void txtNombreCajeroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreCajeroKeyTyped
        /*char c = evt.getKeyChar();
         if (Character.isDigit(c)) {
         getToolkit().beep();
         evt.consume();
         //JOptionPane.showMessageDialog(null, "Ingrese solo letras");
         }*/
    }//GEN-LAST:event_txtNombreCajeroKeyTyped

    private void txtAbono_addKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAbono_addKeyTyped
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != evt.VK_BACK_SPACE)
                && (c != '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_txtAbono_addKeyTyped

    private void txtNumfacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumfacturaActionPerformed
        txtNumfactura.transferFocus();
        FcajaApertura fun = new FcajaApertura();
        FcajaCierre fun2 = new FcajaCierre();
        if (fun.VerificarFecha() > 0) {
            if (fun2.VerificarFecha() > 0) {
                JOptionPane.showMessageDialog(null, "primero debe abrir caja ");
            } else{
                seleccionProd();
                
            }
        } else {
            JOptionPane.showMessageDialog(null, "primero debe abrir caja ");
        }

    }//GEN-LAST:event_txtNumfacturaActionPerformed

    private void txtNombre_clienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre_clienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombre_clienteActionPerformed

    private void txtNombre_clienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombre_clienteKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombre_clienteKeyTyped

    private void txtSaldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoActionPerformed

    private void txtSaldoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaldoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSaldoKeyTyped

    private void txtTotal_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotal_ventaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_ventaActionPerformed

    private void txtTotal_ventaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotal_ventaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_ventaKeyTyped

    private void btnentregaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnentregaActionPerformed
        FcajaApertura fun = new FcajaApertura();
        FcajaCierre fun2 = new FcajaCierre();
        if (fun.VerificarFecha() > 0) {
            if (fun2.VerificarFecha() > 0) {
                JOptionPane.showMessageDialog(null, "primero debe abrir caja ");
            } else {
                entregarProducto();
                /*prod_abono form =new prod_abono();
                
        
                 deskPricipal.add(form);
                 //   form.setClosable(true);
                 form.setIconifiable(true);
                 form.setMaximizable(false);
                 form.toFront();
                 form.setVisible(true);*/
            }
        } else {
            JOptionPane.showMessageDialog(null, "primero debe abrir caja ");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnentregaActionPerformed

    private void txtNumCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumCedulaActionPerformed
txtNumCedula.transferFocus();
if (cmbtipo.getSelectedItem().equals("Abono")){
        selecCod_venta(); 
}else if (cmbtipo.getSelectedItem().equals("Credito")){
    creditos();
}
// TODO add your handling code here:
    }//GEN-LAST:event_txtNumCedulaActionPerformed

    private void jTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaMouseClicked

    }//GEN-LAST:event_jTablaMouseClicked

    private void jTablaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaMousePressed

        /*int compr=Integer.parseInt(FrmVentaDetalle.txtComprueba.getText().toString());

        if (compr==1){

            if (evt.getClickCount() == 2) {

                int fila = jTabla.getSelectedRow();
                String cod;
                String nombre, cedula;

                cod = jTabla.getValueAt(fila, 0).toString();
                nombre = jTabla.getValueAt(fila, 1).toString();
                cedula= jTabla.getValueAt(fila, 5).toString();

                if (Comprueba == 1) {

                    Abonos.txtCod_cliente.setText(cod);
                    Abonos.txtNombre_cliente.setText(nombre);
                    Abonos.txtcedulaC.setText(cedula);

                }
                if(Comprueba == 2){

                    Abonos.txtCod_cliente.setText(cod);
                    Abonos.txtNombre_cliente.setText(nombre);
                    Abonos.txtcedulaC.setText(cedula);
                }

                this.dispose();

            }

        }else if (compr==0) {
            if (evt.getClickCount() == 2) {

                int fila = jTabla.getSelectedRow();
                String cod;
                String nombre, cedula;

                cod = jTabla.getValueAt(fila, 0).toString();
                nombre = jTabla.getValueAt(fila, 1).toString();
                cedula= jTabla.getValueAt(fila, 5).toString();

                if (Comprueba == 1) {

                    FrmMostrarVentas.txtCod_cliente.setText(cod);
                    FrmMostrarVentas.txtNombre_cliente.setText(nombre);

                }
                if(Comprueba == 2){
                    FrmVentaDetalle.txtCod_cliente.setText(cod);
                    FrmVentaDetalle.txtNombre_cliente.setText(nombre);

                }

                this.dispose();

            }
        }/*

    }//GEN-LAST:event_jTablaMousePressed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmGen_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmGen_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmGen_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmGen_Abono.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmGen_Abono().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnentrega;
    private javax.swing.JComboBox<String> cmbtipo;
    private com.toedter.calendar.JDateChooser dcFecha_apertura;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTabla;
    public static javax.swing.JLabel lblCodUsuario;
    private javax.swing.JTextField txtAbono_add;
    public static javax.swing.JLabel txtCliente;
    public static javax.swing.JTextField txtNombreCajero;
    public static javax.swing.JTextField txtNombre_cliente;
    public static javax.swing.JTextField txtNumCedula;
    public static javax.swing.JTextField txtNumfactura;
    public static javax.swing.JTextField txtSaldo;
    private javax.swing.JLabel txtStock;
    public static javax.swing.JTextField txtTotal_venta;
    private javax.swing.JLabel txtcantidadven;
    public static javax.swing.JLabel txtcodProducto;
    public static javax.swing.JLabel txtcodVenta;
    private javax.swing.JLabel txtpago;
    // End of variables declaration//GEN-END:variables

}
