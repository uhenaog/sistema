package Controlador;

import static Controlador.FRMPRINCIPAL.deskPricipal;
import static Controlador.FrmVistaProducto.comprobarProducto;
import static Controlador.FrmVistacliente.Comprueba;
import Datos.Ddetalle_venta;
import Datos.Dproducto;
import Datos.Dventa;
import Funciones.FcajaApertura;
import Funciones.FcajaCierre;
import Funciones.Fdetalle_venta;
import Funciones.Fproducto;

import Funciones.Fventa;
import Funciones.conexion;
import Reportes.VistaBoleta;
import java.awt.Component;
import java.net.InetAddress;

import java.sql.Connection;
import java.sql.Date;
import java.text.DecimalFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
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

public final class Abonos extends javax.swing.JInternalFrame {

    static int Compr;
    int foco;
    private conexion mysql = new conexion();
    private Connection cn = mysql.conectar();

    public Abonos() {
        initComponents();
        //btnGuardar.setMnemonic(KeyEvent.VK_X);
        FrmVentaDetalle.txtComprueba.setText("1");
        btnNuevo.setEnabled(false);
        btnCalcular.setEnabled(false);
        DetallesFormVenta();
        lblModo.setLabelFor(cboModoIngreso);
        lblModo.setDisplayedMnemonic('y');
        txtSaldo.setFocusAccelerator('u');
        txtImporte.setFocusAccelerator('i');

        txtCantidadProducto.setFocusAccelerator('o');
        txtCod_producto.setFocusAccelerator('p');

        OcultaBotones();
        Calendar c2 = new GregorianCalendar();
        dcFecha_venta.setCalendar(c2);
        txtStockDetalle.setVisible(false);
        txtCantidadProducto.setEditable(false);

        txtImporte.setEditable(true);
        txtSaldo.setEditable(true);

        btnClick.setVisible(false);

        txtNumFactura.setEditable(false);
        txtNumFactura.setText("0");
        btnBuscarCliente.requestFocus();

        txtSubPrecioCompra.setVisible(false);

        txtNombre_cliente.setText("Cliente General");
        txtCod_cliente.setText("2");
        mostrar("0");
        //    BasicInternalFrameUI bi = (BasicInternalFrameUI) this.getUI();
        //   bi.setNorthPane(null);
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

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
         btnCalcular.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent e) {
                btnCalcularActionPerformed(null);
            }
        });
    }

    public void ocultar_columnas() {

        jTabla.getColumnModel().getColumn(0).setMaxWidth(0);
        jTabla.getColumnModel().getColumn(0).setMinWidth(0);
        jTabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        jTabla.getColumnModel().getColumn(5).setMaxWidth(0);
        jTabla.getColumnModel().getColumn(5).setMinWidth(0);
        jTabla.getColumnModel().getColumn(5).setPreferredWidth(0);
        jTabla.getColumnModel().getColumn(6).setMaxWidth(0);
        jTabla.getColumnModel().getColumn(6).setMinWidth(0);
        jTabla.getColumnModel().getColumn(6).setPreferredWidth(0);
    }

    public void limpiarProductosDetalle() {
        txtCod_producto.setText("");
        txtNombre_producto.setText("");
        txtCantidadProducto.setText("");
        txtPrecio_producto.setText("");
    }

    public void BuscarCodigoVenta() {

        Fventa funcion = new Fventa();
        int codigo = funcion.BuscarCodigoVenta();
        //ACA PODRIA PONER PARA EN UN LBL MOSTRAR EL NUMERO DE VENTA
        txtCod_venta.setText(String.valueOf(codigo));
        txtCod_ventaFK.setText(String.valueOf(codigo));
    }

    public void NfacturaAtxt() {
        DecimalFormat formateador = new DecimalFormat("000000");
        Fventa funcion = new Fventa();
        int Nfactura = funcion.BuscarNfacturas();

        Nfactura = Nfactura + 1;

        String format = formateador.format(Nfactura);

        txtNumFactura.setText(String.valueOf(format));

    }

    /**
     * ****BUSQUEDA SI EL CODIGO DEL PRODUCTO EXISTE**
     */
    public void seleccionProd() {

        Fdetalle_venta funcion = new Fdetalle_venta();
        long cod_producto = funcion.selecProd();

        if (cod_producto > 0) {
            txtCod_producto.setText(String.valueOf(cod_producto));

            String nombre_producto = funcion.SelectNombre();
            txtNombre_producto.setText(String.valueOf(nombre_producto));

            int stock_producto = funcion.selecStock();
            txtStockDetalle.setText(String.valueOf(stock_producto));

            long precio_producto = funcion.selectPrecio();
            txtPrecio_producto.setText(String.valueOf(precio_producto));

            long precio_compra = funcion.selectPrecioCompra();
            txtSubPrecioCompra.setText(String.valueOf(precio_compra));

            if (cboModoIngreso.getSelectedItem() == "x Mayor") {
                txtCantidadProducto.setEditable(true);
            } else if (txtCantidadProducto.getText().equals("")) {

                //txtCantidadProducto.setText("");
                txtCod_producto.setEditable(false);
                foco = 0;
                insertarDetalle();
            } else if (cboModoIngreso.getSelectedItem() == "x Unidad") {

                foco = 0;
                txtCod_producto.setEditable(true);
                insertarDetalle();
            }

        } else {
            JOptionPane.showMessageDialog(null, "No existe el codigo en el sistema");
            txtCod_producto.requestFocus();
            txtCod_producto.setText("");
        }

    }

    public void insertarDetalle() {

        int cantidad = Integer.parseInt(txtCantidadProducto.getText());
        long Stock = Long.valueOf(txtStockDetalle.getText());
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad");
            return;
        }
        if (cantidad > Stock) {
            JOptionPane.showMessageDialog(null, "La cantidad a vender supera el Stock del producto.");
            txtCod_producto.setText("");
            txtNombre_producto.setText("");
            txtPrecio_producto.setText("");
            txtStockDetalle.setText("");
            txtCod_producto.requestFocus();

            return;
        }

        long PrecioP = Long.parseLong(txtPrecio_producto.getText());
        long CantProd = Long.parseLong(txtCantidadProducto.getText());
        long subPreC = Long.parseLong(txtSubPrecioCompra.getText());

        long resultadoDetalle = PrecioP * CantProd;
        long resultadoDetalle2 = CantProd * subPreC;

        Ddetalle_venta datos = new Ddetalle_venta();
        Fdetalle_venta funcion = new Fdetalle_venta();

        datos.setCantidad_detalle(Integer.parseInt(txtCantidadProducto.getText()));
        datos.setCod_productoFK(Long.valueOf(txtCod_producto.getText()));
        datos.setCod_ventaFK(Integer.parseInt(txtCod_ventaFK.getText()));
        datos.setPrecio_producto(PrecioP);
        datos.setSubtotal(resultadoDetalle);
        datos.setSubPrecioCompra(resultadoDetalle2);
        datos.setPrecio_compra(subPreC);
        datos.setCompr(1);
        if (funcion.insertarDetalle(datos)) {
            long precio_producto = Long.parseLong(txtPrecio_producto.getText());
            long sub_total = Long.parseLong(txtTotal_venta.getText());
            long descuento = Long.parseLong(txtSaldo.getText());
            long total = precio_producto * cantidad;
            long resultado = total + sub_total;
            long descuento2 = Math.round(resultado * (descuento * 0.01));
            long resultadoDescuento = resultado - descuento2;
            long iva = ((resultadoDescuento * 19) / 100);

            //JOptionPane.showMessageDialog(null, "estes el el total " + resultado + "este es resultado descuento " + resultadoDescuento + "      este es iva" + iva);

            txtTotal_venta.setText(String.valueOf(resultadoDescuento));
            txtTotal_venta.setText(String.valueOf(resultado));
            Dventa datos1 = new Dventa();
            Fventa funcion1 = new Fventa();
            datos1.setCod_venta(Integer.parseInt(txtCod_ventaFK.getText()));
            datos1.setTotal_venta(Long.valueOf(txtTotal_venta.getText()));
            datos1.setIva(iva);

            funcion1.Total(datos1);

            Dproducto datos2 = new Dproducto();
            Fproducto funcion2 = new Fproducto();
            int stock = 0;
            datos2.setCod_producto(Long.valueOf(txtCod_producto.getText()));
            stock = Integer.parseInt(txtStockDetalle.getText());
            stock = stock - (cantidad - cantidad);
            //JOptionPane.showMessageDialog(null,"estes el el stock: "+stock+"    estes es la cantidad "+cantidad);
            datos2.setStock_producto(stock);
            funcion2.ModificarStockProductos(datos2);
        } else {
            JOptionPane.showMessageDialog(null, "El codigo ingresado no esta en el sistema");
        }
        txtCod_producto.setText("");
        txtPrecio_producto.setText("");
        txtCantidadProducto.setText("1");
        txtNombre_producto.setText("");
        txtCantidadProducto.setEditable(false);
        txtSubPrecioCompra.setText("");

    }

    public void mostrar(String cod_venta) {
        try {
            DefaultTableModel modelo;
            Fdetalle_venta func = new Fdetalle_venta();
            modelo = func.mostrar(cod_venta);
            jTabla.setModel(modelo);
            ocultar_columnas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void DetallesFormVenta() {
        txtCod_usuario.setVisible(false);
        txtCod_cliente.setVisible(false);
        txtCod_venta.setVisible(false);
        txtNombre_cliente.setEditable(false);
        txtCod_detalle.setVisible(false);
        txtCod_ventaFK.setVisible(false);
        txtNombre_producto.setEditable(false);
        txtCantidadProducto.setEditable(false);
        txtPrecio_producto.setEditable(false);
        txtCod_producto.setEditable(false);

    }

    public void DetallesFormVentaProd() {
        txtCantidadProducto.setEditable(true);
        txtPrecio_producto.setEditable(false);
    }

    public void OcultaBotones() {
        btnbuscarProducto.setEnabled(false);
        btnAgregarProducto.setEnabled(false);
        btnQuitarProducto.setEnabled(false);
    }

    public void activaBotones() {
        btnbuscarProducto.setEnabled(true);
        btnAgregarProducto.setEnabled(true);
        btnQuitarProducto.setEnabled(true);
    }

    public void nuevaVenta() {
        {

            Dventa datos = new Dventa();
            Fventa funcion = new Fventa();
            Fdetalle_venta funcion2 = new Fdetalle_venta();

            datos.setCod_venta(Integer.parseInt(txtCod_venta.getText()));
            String importe0 = txtImporte.getText();

            if ("".equals(importe0)) {
                JOptionPane.showMessageDialog(null, "Ingrese un valor en el IMPORTE");
                txtImporte.requestFocus();
                return;
            }

            long importe = Long.parseLong(txtImporte.getText());

            funcion2.mostrar(txtCod_ventaFK.getText());

            if (funcion2.totalRegistros == 0) {
                funcion.eliminar(datos);
                JOptionPane.showMessageDialog(null, "Venta eliminada ya que no posee registros");
            } else if (importe == 0) {
                JOptionPane.showMessageDialog(null, "Realizo una venta sin INGRESAR IMPORTE");
                txtImporte.requestFocus();
                return;
            }

            this.setClosable(true);
            txtImporte.setText("0");
            txtImporte.setEditable(true);

            txtSaldo.setText("0");
            txtSaldo.setEditable(true);
            txtNumFactura.setText("");
            txtStockDetalle.setText("");
            txtCod_venta.setText("");
            txtCod_ventaFK.setText("");
            txtCod_detalle.setText("");

            txtTotal_venta.setText("0");

            txtCod_producto.setText("");
            txtCod_producto.setEditable(false);
            txtNombre_producto.setText("");
            txtPrecio_producto.setText("");
            txtCantidadProducto.setText("");
            btnGuardar.setEnabled(true);
            txtNumFactura.setText("");
            btnNuevo.setEnabled(false);

            txtNombre_cliente.setText("Cliente General");
            txtCod_cliente.setText("2");

            cboComprobante.setSelectedIndex(0);
            mostrar("0");

            btnQuitarProducto.setEnabled(false);
            btnbuscarProducto.setEnabled(false);
            btnAgregarProducto.setEnabled(false);
            btnCalcular.setEnabled(false);

        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        dcFecha_venta = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        cboComprobante = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtNombre_cliente = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtNumFactura = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnBuscarCliente = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtcedulaC = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabla = jTabla = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false; //Disallow the editing of any cell
            }
        };
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCod_producto = new javax.swing.JTextField();
        txtNombre_producto = new javax.swing.JTextField();
        txtPrecio_producto = new javax.swing.JTextField();
        txtCantidadProducto = new javax.swing.JTextField();
        btnAgregarProducto = new javax.swing.JButton();
        btnbuscarProducto = new javax.swing.JButton();
        btnQuitarProducto = new javax.swing.JButton();
        cboModoIngreso = new javax.swing.JComboBox<>();
        lblModo = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtImporte = new javax.swing.JTextField();
        btnCalcular = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtTotal_venta = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtSaldo = new javax.swing.JTextField();
        txtCod_cliente = new javax.swing.JTextField();
        txtCod_usuario = new javax.swing.JTextField();
        txtCod_ventaFK = new javax.swing.JTextField();
        txtCod_detalle = new javax.swing.JTextField();
        txtCod_venta = new javax.swing.JTextField();
        txtStockDetalle = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNombre_usuario = new javax.swing.JLabel();
        btnClick = new javax.swing.JButton();
        txtSubPrecioCompra = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jPanel2.setBackground(new java.awt.Color(36, 33, 33));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel1.setBackground(new java.awt.Color(36, 33, 33));

        jPanel3.setBackground(new java.awt.Color(36, 33, 33));

        dcFecha_venta.setBackground(new java.awt.Color(36, 33, 33));
        dcFecha_venta.setForeground(new java.awt.Color(207, 207, 207));
        dcFecha_venta.setEnabled(false);
        dcFecha_venta.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(207, 207, 207));
        jLabel2.setText("  Fecha :");

        cboComprobante.setBackground(new java.awt.Color(36, 33, 33));
        cboComprobante.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cboComprobante.setForeground(new java.awt.Color(207, 207, 207));
        cboComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boleta" }));
        cboComprobante.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboComprobanteItemStateChanged(evt);
            }
        });
        cboComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboComprobanteActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(207, 207, 207));
        jLabel4.setText("N° :");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(207, 207, 207));
        jLabel6.setText(" Cliente :");

        txtNombre_cliente.setBackground(new java.awt.Color(36, 33, 33));
        txtNombre_cliente.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNombre_cliente.setForeground(new java.awt.Color(207, 207, 207));
        txtNombre_cliente.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNombre_cliente.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombre_cliente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombre_cliente.setEnabled(false);

        btnGuardar.setBackground(new java.awt.Color(36, 33, 33));
        btnGuardar.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnGuardar.setForeground(new java.awt.Color(207, 207, 207));
        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/guardar.png"))); // NOI18N
        btnGuardar.setMnemonic('x');
        btnGuardar.setText("Iniciar ");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(207, 207, 207));
        jLabel1.setText("    Tipo :");

        txtNumFactura.setBackground(new java.awt.Color(36, 33, 33));
        txtNumFactura.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNumFactura.setForeground(new java.awt.Color(207, 207, 207));
        txtNumFactura.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNumFactura.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNumFactura.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNumFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumFacturaKeyTyped(evt);
            }
        });

        btnNuevo.setBackground(new java.awt.Color(36, 33, 33));
        btnNuevo.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnNuevo.setForeground(new java.awt.Color(207, 207, 207));
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/nuevo.png"))); // NOI18N
        btnNuevo.setMnemonic('n');
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnBuscarCliente.setBackground(new java.awt.Color(36, 33, 33));
        btnBuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/buscar.png"))); // NOI18N
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(207, 207, 207));
        jLabel13.setText("cedula de cliente ");

        txtcedulaC.setBackground(new java.awt.Color(36, 33, 33));
        txtcedulaC.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtcedulaC.setForeground(new java.awt.Color(207, 207, 207));
        txtcedulaC.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtcedulaC.setCaretColor(new java.awt.Color(255, 255, 255));
        txtcedulaC.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtcedulaC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcedulaCActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dcFecha_venta, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                            .addComponent(cboComprobante, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel13)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtNombre_cliente, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNumFactura)
                    .addComponent(txtcedulaC))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuardar))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNombre_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dcFecha_venta, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(5, 5, 5))))
                            .addComponent(btnBuscarCliente))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1)
                            .addComponent(txtNumFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtcedulaC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabla.setBackground(new java.awt.Color(36, 33, 33));
        jTabla.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabla.setForeground(new java.awt.Color(207, 207, 207));
        jTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cod producto", "Nombre", "Precio", "Cantidad"
            }
        ));
        jTabla.setRowHeight(20);
        jTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTabla);

        jPanel4.setBackground(new java.awt.Color(36, 33, 33));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(207, 207, 207));
        jLabel11.setText("Cantidad :");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(207, 207, 207));
        jLabel12.setText("Precio :");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(207, 207, 207));
        jLabel7.setText("Producto :");

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(207, 207, 207));
        jLabel10.setText("Codigo :");

        txtCod_producto.setBackground(new java.awt.Color(36, 33, 33));
        txtCod_producto.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtCod_producto.setForeground(new java.awt.Color(207, 207, 207));
        txtCod_producto.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtCod_producto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCod_producto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCod_producto.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtCod_productoCaretUpdate(evt);
            }
        });
        txtCod_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCod_productoActionPerformed(evt);
            }
        });
        txtCod_producto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCod_productoKeyTyped(evt);
            }
        });

        txtNombre_producto.setBackground(new java.awt.Color(36, 33, 33));
        txtNombre_producto.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNombre_producto.setForeground(new java.awt.Color(207, 207, 207));
        txtNombre_producto.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtNombre_producto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtNombre_producto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombre_producto.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                txtNombre_productoMouseWheelMoved(evt);
            }
        });
        txtNombre_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombre_productoActionPerformed(evt);
            }
        });

        txtPrecio_producto.setBackground(new java.awt.Color(36, 33, 33));
        txtPrecio_producto.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtPrecio_producto.setForeground(new java.awt.Color(207, 207, 207));
        txtPrecio_producto.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtPrecio_producto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtPrecio_producto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtPrecio_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecio_productoActionPerformed(evt);
            }
        });

        txtCantidadProducto.setBackground(new java.awt.Color(36, 33, 33));
        txtCantidadProducto.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtCantidadProducto.setForeground(new java.awt.Color(207, 207, 207));
        txtCantidadProducto.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(207, 207, 207)));
        txtCantidadProducto.setCaretColor(new java.awt.Color(255, 255, 255));
        txtCantidadProducto.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadProductoActionPerformed(evt);
            }
        });
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });

        btnAgregarProducto.setBackground(new java.awt.Color(36, 33, 33));
        btnAgregarProducto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnAgregarProducto.setForeground(new java.awt.Color(207, 207, 207));
        btnAgregarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/Agregarr.png"))); // NOI18N
        btnAgregarProducto.setMnemonic('a');
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnbuscarProducto.setBackground(new java.awt.Color(36, 33, 33));
        btnbuscarProducto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnbuscarProducto.setForeground(new java.awt.Color(207, 207, 207));
        btnbuscarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/buscar.png"))); // NOI18N
        btnbuscarProducto.setText(" ");
        btnbuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuscarProductoActionPerformed(evt);
            }
        });

        btnQuitarProducto.setBackground(new java.awt.Color(36, 33, 33));
        btnQuitarProducto.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        btnQuitarProducto.setForeground(new java.awt.Color(207, 207, 207));
        btnQuitarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImagenesForm/Quitar.png"))); // NOI18N
        btnQuitarProducto.setMnemonic('s');
        btnQuitarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarProductoActionPerformed(evt);
            }
        });

        cboModoIngreso.setBackground(new java.awt.Color(36, 33, 33));
        cboModoIngreso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        cboModoIngreso.setForeground(new java.awt.Color(207, 207, 207));
        cboModoIngreso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "x Unidad", "x Mayor" }));
        cboModoIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboModoIngresoActionPerformed(evt);
            }
        });

        lblModo.setBackground(new java.awt.Color(255, 255, 255));
        lblModo.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblModo.setForeground(new java.awt.Color(207, 207, 207));
        lblModo.setText("Modo :");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblModo, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cboModoIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 357, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtCod_producto)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel11)
                                .addGap(6, 6, 6)
                                .addComponent(txtCantidadProducto))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrecio_producto)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNombre_producto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnbuscarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                    .addComponent(btnQuitarProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboModoIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCod_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnQuitarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPrecio_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtNombre_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanel6.setBackground(new java.awt.Color(238, 238, 238));

        jLabel14.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel14.setText("Cantidad a Abonar ");

        txtImporte.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtImporte.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtImporte.setText("0");
        txtImporte.setSelectionColor(new java.awt.Color(0, 0, 0));
        txtImporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtImporteActionPerformed(evt);
            }
        });
        txtImporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtImporteKeyTyped(evt);
            }
        });

        btnCalcular.setBackground(new java.awt.Color(36, 33, 33));
        btnCalcular.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        btnCalcular.setForeground(new java.awt.Color(207, 207, 207));
        btnCalcular.setMnemonic('c');
        btnCalcular.setText("COBRAR");
        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcularActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCalcular, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtImporte)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(0, 33, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(btnCalcular, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        jPanel7.setBackground(new java.awt.Color(238, 238, 238));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setText("total Abonado ");

        txtTotal_venta.setEditable(false);
        txtTotal_venta.setBackground(new java.awt.Color(255, 255, 255));
        txtTotal_venta.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtTotal_venta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotal_venta.setText("0");
        txtTotal_venta.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotal_venta.setDragEnabled(true);
        txtTotal_venta.setEnabled(false);
        txtTotal_venta.setFocusable(false);
        txtTotal_venta.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        txtTotal_venta.setVerifyInputWhenFocusTarget(false);
        txtTotal_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotal_ventaActionPerformed(evt);
            }
        });

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/menu/coins17.png"))); // NOI18N

        jLabel20.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel20.setText("saldo ");

        txtSaldo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtSaldo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSaldo.setText("0");
        txtSaldo.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSaldo.setDoubleBuffered(true);
        txtSaldo.setEnabled(false);
        txtSaldo.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        txtSaldo.setVerifyInputWhenFocusTarget(false);
        txtSaldo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSaldoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTotal_venta)
                    .addComponent(txtSaldo)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addComponent(jLabel20))
                        .addGap(0, 8, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotal_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(108, 108, 108))
        );

        txtCod_ventaFK.setText(" ");

        txtCod_detalle.setText(" ");

        txtCod_venta.setText(" ");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(207, 207, 207));
        jLabel5.setText("USUARIO :");

        txtNombre_usuario.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtNombre_usuario.setForeground(new java.awt.Color(207, 207, 207));
        txtNombre_usuario.setText("Vendedor");

        btnClick.setText("prod");
        btnClick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClickActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(207, 207, 207));
        jLabel16.setText("FORMULARIO DE ABONOS ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNombre_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSubPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClick)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtStockDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCod_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCod_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCod_ventaFK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtCod_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCod_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 70, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCod_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCod_detalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCod_ventaFK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCod_cliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCod_usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStockDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombre_usuario)
                    .addComponent(jLabel5)
                    .addComponent(btnClick)
                    .addComponent(txtSubPrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(11, 11, 11)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        if (txtNombre_cliente.getText().toString().equals("Cliente General")) {
            JOptionPane.showMessageDialog(null, "Primero debe Seleccinar un Cliente ");
        } else {

              FcajaApertura fun = new FcajaApertura();
        FcajaCierre fun2 = new FcajaCierre();
        if (fun.VerificarFecha() > 0) {
            if (fun2.VerificarFecha() > 0) {
                JOptionPane.showMessageDialog(null, "primero debe abrir caja ");
            } else {

               // JOptionPane.showMessageDialog(null, "este es el valor de caja");

                /*PARTE VALIDACION DE CAMPOS*/
                if (txtCod_cliente.getText().length() == 0 || txtNombre_cliente.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Seleccione un Cliente");
                    btnBuscarCliente.requestFocus();
                    return;
                }
                if (txtSaldo.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Este campo debe llevar un valor");
                    txtSaldo.requestFocus();
                    return;
                }
                if (txtImporte.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null, "Este campo debe llevar un valor");
                    txtImporte.requestFocus();
                    return;
                }
                Dventa datos = new Dventa();
                Fventa funcion = new Fventa();
                
                int codigo=funcion.BuscarCod_cleinte();
                
                if (codigo>0){
                    JOptionPane.showMessageDialog(null,"este cliente tiene saldos pendientes ");
                    return;
                 }
                

                this.setClosable(false);
                btnNuevo.setEnabled(true);
                txtImporte.setEditable(true);
                txtSaldo.setEditable(false);
                
                Calendar cal;
                int d, m, a;
                cal = dcFecha_venta.getCalendar();
                d = cal.get(Calendar.DAY_OF_MONTH);
                m = cal.get(Calendar.MONTH);
                a = cal.get(Calendar.YEAR) - 1900;
                datos.setFecha_venta(new Date(a, m, d));

                datos.setTotal_venta(Long.valueOf(txtTotal_venta.getText()));

                datos.setCod_usuarioFK(Integer.parseInt(txtCod_usuario.getText()));
                datos.setCod_clienteFK(Integer.parseInt(txtCod_cliente.getText()));

                int comprobante = cboComprobante.getSelectedIndex();
                datos.setTipo_comprobante((String) cboComprobante.getItemAt(comprobante));
                datos.setNum_factura(Integer.parseInt(txtNumFactura.getText()));
                datos.setDescuento(Long.parseLong(txtSaldo.getText()));
                datos.setPago(Long.parseLong(txtImporte.getText()));
                datos.setcompr(Long.parseLong(FrmVentaDetalle.txtComprueba.getText()));
                datos.setCedula(Long.parseLong(txtcedulaC.getText()));

                if (funcion.insertar(datos)) {
                    DetallesFormVentaProd();
                    BuscarCodigoVenta();
                    activaBotones();
                    btnGuardar.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "No se Ingresaron Datos");
                }

                txtCod_producto.setEditable(true);
                txtCod_producto.requestFocus();
                txtCantidadProducto.setEditable(false);
                txtCantidadProducto.setText("1");
                btnCalcular.setEnabled(true);

            } 
        }
        }


    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnbuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuscarProductoActionPerformed
        comprobarProducto = 3;
        FrmVistaProducto form = new FrmVistaProducto();
        deskPricipal.add(form);
        //   form.setIconifiable(true);
        // form.setMaximizable(false);
        form.toFront();
        form.setVisible(true);

    }//GEN-LAST:event_btnbuscarProductoActionPerformed

    private void jTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablaMouseClicked

        txtCod_producto.setEditable(false);
        txtNombre_producto.setEditable(false);
        txtCantidadProducto.setEditable(false);
        txtPrecio_producto.setEditable(false);
        btnQuitarProducto.setEnabled(true);
        int fila = jTabla.rowAtPoint(evt.getPoint());
        txtCod_detalle.setText(jTabla.getValueAt(fila, 0).toString());
        txtCod_producto.setText(jTabla.getValueAt(fila, 1).toString());
        txtNombre_producto.setText(jTabla.getValueAt(fila, 2).toString());
        txtPrecio_producto.setText(jTabla.getValueAt(fila, 3).toString());
        txtCantidadProducto.setText(jTabla.getValueAt(fila, 4).toString());
        txtCod_ventaFK.setText(jTabla.getValueAt(fila, 5).toString());
        txtStockDetalle.setText(jTabla.getValueAt(fila, 6).toString());
    }//GEN-LAST:event_jTablaMouseClicked

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        seleccionProd();

        if (foco == 1) {
            txtCantidadProducto.requestFocus();
        } else if (foco == 0) {
            txtCod_producto.requestFocus();
            mostrar(txtCod_ventaFK.getText());
        }
        /* if (txtCod_producto.getText().length() == 0 || txtNombre_producto.getText().length() == 0) {
         JOptionPane.showMessageDialog(null, "Seleccione un Producto.");
         btnbuscarProducto.requestFocus();
         return;
         }
         if (txtCantidadProducto.getText().length() == 0) {
         JOptionPane.showMessageDialog(null, "Ingrese una Cantidad.");
         txtCantidadProducto.requestFocus();
         return;
         }
         if (txtPrecio_producto.getText().length() == 0) {
         JOptionPane.showMessageDialog(null, "Ingrese un precio.");
         txtPrecio_producto.requestFocus();
         return;
         }

         if (txtNombre_producto.getText().length() == 0) {
         JOptionPane.showMessageDialog(null, "Ingrese un nombre.");
         txtNombre_producto.requestFocus();
         return;
         }

         if (txtSubPrecioCompra.getText().length() == 0) {
         JOptionPane.showMessageDialog(null, "Ingrese un  sub precio.");
         txtSubPrecioCompra.requestFocus();
         return;
         }

         int Cantidad = Integer.parseInt(txtCantidadProducto.getText());
         long Stock = Long.valueOf(txtStockDetalle.getText());
         if (Cantidad > Stock) {
         JOptionPane.showMessageDialog(null, "La cantidad a vender supera el Stock del producto.");
         txtCantidadProducto.setText("");
         txtCantidadProducto.requestFocus();
         return;
         }
         ;

         long precio_prod = Long.parseLong(txtPrecio_producto.getText());
         int cantidad_prod = Integer.parseInt(txtCantidadProducto.getText());
         long sub_precioC = Long.parseLong(txtSubPrecioCompra.getText());

         long resultado1 = precio_prod * cantidad_prod;
         long resultado2 = cantidad_prod * sub_precioC;

         Ddetalle_venta datos = new Ddetalle_venta();
         Fdetalle_venta funcion = new Fdetalle_venta();

         datos.setCantidad_detalle(Integer.parseInt(txtCantidadProducto.getText()));
         datos.setCod_productoFK(Long.valueOf(txtCod_producto.getText()));
         datos.setPrecio_producto(precio_prod);
         datos.setCod_ventaFK(Integer.parseInt(txtCod_ventaFK.getText()));
         datos.setSubtotal(resultado1);
         datos.setSubPrecioCompra(resultado2);
         datos.setPrecio_compra(Long.parseLong(txtSubPrecioCompra.getText()));

         if (funcion.insertar(datos)) {

         long valorProdP = Long.parseLong(txtPrecio_producto.getText());
         long valorSub = Long.parseLong(txtSubTotal.getText());

         long descuento = Long.parseLong(txtDescuento.getText());

         long total = valorProdP * Cantidad;
         long resultado = total + valorSub;

         long descuento2 = Math.round(resultado * (descuento * 0.01));

         long resultadoDescuento = resultado - descuento2;

         txtSubTotal.setText(String.valueOf(resultado));
         txtTotal_venta.setText(String.valueOf(resultadoDescuento));

         Dventa datos1 = new Dventa();
         Fventa funcion1 = new Fventa();
         datos1.setCod_venta(Integer.parseInt(txtCod_ventaFK.getText()));
         datos1.setTotal_venta(Long.valueOf(txtTotal_venta.getText()));
         funcion1.Total(datos1);

         /**
         * ****Quitar Stock*+++++++++
         */
        /*            Dproducto datos2 = new Dproducto();
         Fproducto funcion2 = new Fproducto();
         int stock = 0;
         int cantidad = 0;
         datos2.setCod_producto(Long.valueOf(txtCod_producto.getText()));
         stock = Integer.parseInt(txtStockDetalle.getText());
         cantidad = Integer.parseInt(txtCantidadProducto.getText());
         stock = stock - cantidad;
         datos2.setStock_producto(stock);
         funcion2.ModificarStockProductos(datos2);
         } else {
         JOptionPane.showMessageDialog(null, "Error Ingreso Producto Form");
         }
         mostrar(txtCod_ventaFK.getText());
         limpiarProductosDetalle();
         txtPrecio_producto.setEditable(false);
         txtCantidadProducto.setText("1");
         cboModoIngreso.setSelectedIndex(0);
         txtCod_producto.setEditable(true);
         txtCod_producto.requestFocus();
         txtCantidadProducto.setEditable(false);*/
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnQuitarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarProductoActionPerformed

        if (!txtCod_detalle.getText().equals("")) {
            Ddetalle_venta datos = new Ddetalle_venta();
            Fdetalle_venta funcion = new Fdetalle_venta();
            datos.setCod_detalle(Integer.parseInt(txtCod_detalle.getText()));
            funcion.eliminar(datos);
            mostrar(txtCod_ventaFK.getText());

            int cantidadProd = Integer.parseInt(txtCantidadProducto.getText());
            long PrecioProd = Long.valueOf(txtPrecio_producto.getText());
            long SubTotal = Long.valueOf(txtTotal_venta.getText());

            long descuento = Long.parseLong(txtSaldo.getText());

            long total = PrecioProd * cantidadProd;
            long resultado = SubTotal - total;

            long descuento2 = Math.round(resultado * (descuento * 0.01));
            long resultadoDescuento = resultado - descuento2;

            txtTotal_venta.setText(String.valueOf(resultado));
            //txtTotal_venta.setText(String.valueOf(resultadoDescuento));

            //txtTotal_venta.setText(String.valueOf(resultadoDescuento));
            Dventa datos1 = new Dventa();
            Fventa funcion1 = new Fventa();
            datos1.setCod_venta(Integer.parseInt(txtCod_ventaFK.getText()));

//            datos1.setTotal_venta(Long.valueOf(txtTotal_venta.getText()));
            funcion1.Total(datos1);

            /**
             * ****Aumentar Stock*+++++++++
             */
            Dproducto datos2 = new Dproducto();
            Fproducto funcion2 = new Fproducto();
            int stock2 = 0;
            int cantidad2 = 0;
            datos2.setCod_producto(Long.valueOf(txtCod_producto.getText()));
            stock2 = Integer.parseInt(txtStockDetalle.getText());
            cantidad2 = Integer.parseInt(txtCantidadProducto.getText());
            int totalRestar = stock2 + cantidad2;
            datos2.setStock_producto(totalRestar);
            funcion2.ModificarStockProductos(datos2);

        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        }
        mostrar(txtCod_ventaFK.getText());
        limpiarProductosDetalle();
        txtPrecio_producto.setEditable(false);
        txtCantidadProducto.setEditable(false);
        txtCod_producto.setEditable(true);
        txtCantidadProducto.setText("1");
        txtCod_producto.setText("");
        txtCod_producto.requestFocus();
    }//GEN-LAST:event_btnQuitarProductoActionPerformed

    private void txtNombre_productoMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_txtNombre_productoMouseWheelMoved

    }//GEN-LAST:event_txtNombre_productoMouseWheelMoved

    private void btnClickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClickActionPerformed

        seleccionProd();

        if (foco == 1) {
            txtCantidadProducto.requestFocus();
        } else if (foco == 0) {
            txtCod_producto.requestFocus();
            mostrar(txtCod_ventaFK.getText());
        }


    }//GEN-LAST:event_btnClickActionPerformed

    private void txtCod_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCod_productoActionPerformed

        btnClickActionPerformed(evt);
    }//GEN-LAST:event_txtCod_productoActionPerformed

    private void txtCod_productoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtCod_productoCaretUpdate

    }//GEN-LAST:event_txtCod_productoCaretUpdate


    private void txtCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadProductoActionPerformed

    }//GEN-LAST:event_txtCantidadProductoActionPerformed

    private void txtPrecio_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecio_productoActionPerformed

    }//GEN-LAST:event_txtPrecio_productoActionPerformed

    private void txtCod_productoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCod_productoKeyTyped

        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != evt.VK_BACK_SPACE)) {
            evt.consume();
        }

    }//GEN-LAST:event_txtCod_productoKeyTyped

    private void cboComprobanteItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboComprobanteItemStateChanged

    }//GEN-LAST:event_cboComprobanteItemStateChanged

    private void cboComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboComprobanteActionPerformed
        if (cboComprobante.getSelectedItem() == "Factura") {
            NfacturaAtxt();
            txtNumFactura.setEditable(true);

        } else {

            txtNumFactura.setEditable(false);
            txtNumFactura.setText("0");
        }
    }//GEN-LAST:event_cboComprobanteActionPerformed

    private void btnCalcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcularActionPerformed

        if (txtImporte.getText().length() == 0) {
            JOptionPane.showMessageDialog(null, "Ingrese un Monto ");
            txtImporte.setText("");
            txtImporte.requestFocus();
            return;
        }

        long importe = Long.valueOf(txtImporte.getText());
        long total = Long.valueOf(txtTotal_venta.getText());
        long iva;

        if (importe < total) {
            Fventa funcion = new Fventa();
            Dventa datos = new Dventa();

            datos.setPago(importe);

            datos.setCod_venta(Integer.parseInt(txtCod_venta.getText()));

            //int unidad = cboMetodoPago.getSelectedIndex();
            datos.setMetodo_pago("Abono");

            try {
                InetAddress addr = InetAddress.getLocalHost();
                String hostname = addr.getHostName();
                datos.setNomCaja(hostname);

            } catch (Exception e) {
                // TODO: Add catch code
                e.printStackTrace();
            }

            funcion.Pago(datos);

            java.util.Locale locale = new Locale("es", "CL");

            if (cboComprobante.getSelectedItem().equals("Factura")) {
                try {
                    int codigo = Integer.parseInt(txtCod_venta.getText());
                    //long pago = Long.parseLong(txtImporte.getText());
                    JasperReport jr = (JasperReport) JRLoader.loadObject(VistaBoleta.class.getResource("/Reportes/RptBoleta.jasper"));//aqui

                    Map parametro = new HashMap<String, Integer>();
                    parametro.put(JRParameter.REPORT_LOCALE, locale);

                    parametro.put("cod_venta", codigo);

                    JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
                    JasperViewer jv = new JasperViewer(jp, true);
                    //jv.show(closable);

                    JasperPrintManager.printReport( jp, false);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(rootPane, "error" + e);
                }
            } else if (cboComprobante.getSelectedItem().equals("Boleta")) {
                try {
                    int codigo = Integer.parseInt(txtCod_venta.getText());

                    JasperReport jr = (JasperReport) JRLoader.loadObject(VistaBoleta.class.getResource("/Reportes/RptBoleta_Abonos.jasper"));

                    Map parametro = new HashMap<String, Integer>();
                    parametro.put("logo",this.getClass().getResourceAsStream("/ImagenesReport/logo1.png"));

                    parametro.put("cod_venta", codigo);
                    parametro.put(JRParameter.REPORT_LOCALE, locale);

                    JasperPrint jp = JasperFillManager.fillReport(jr, parametro, cn);
                    //JasperViewer jv = new JasperViewer(jp, true);
                    //jv.show();

                    JasperPrintManager.printReport( jp, false);
                    btnNuevo.setEnabled(true);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(rootPane, "error" + e);
                }

            }

        } else {

            JOptionPane.showMessageDialog(rootPane, "esta ingresando mas Dinero del Necesario");
            txtImporte.setText("");
            txtImporte.requestFocus();

        }
        this.setClosable(true);
        nuevaVenta();

    }//GEN-LAST:event_btnCalcularActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed

        nuevaVenta();

    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        FrmVistacliente form = new FrmVistacliente();
        Comprueba = 2;
        deskPricipal.add(form);
        //   form.setClosable(true);
        form.setIconifiable(true);
        form.setMaximizable(false);
        form.toFront();
        form.setVisible(true);

    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void cboModoIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboModoIngresoActionPerformed

        if (cboModoIngreso.getSelectedItem() == "x Mayor") {
            txtCantidadProducto.setText("0");
            txtCantidadProducto.setEditable(false);
            txtNombre_producto.setEditable(false);
            txtStockDetalle.setEditable(false);
            txtPrecio_producto.setEditable(false);
            txtCod_producto.setEditable(true);
            txtCod_producto.requestFocus();
        } else if (cboModoIngreso.getSelectedItem() == "x Unidad") {
            txtCantidadProducto.setText("1");
            txtCod_producto.setEditable(true);
            txtCantidadProducto.setEditable(false);
            txtNombre_producto.setEditable(false);
            txtStockDetalle.setEditable(false);
            txtPrecio_producto.setEditable(false);
            txtCod_producto.requestFocus();
        }
    }//GEN-LAST:event_cboModoIngresoActionPerformed

    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyTyped
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != evt.VK_BACK_SPACE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtCantidadProductoKeyTyped

    private void txtNumFacturaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumFacturaKeyTyped
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != evt.VK_BACK_SPACE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtNumFacturaKeyTyped

    private void txtSaldoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSaldoKeyTyped
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != evt.VK_BACK_SPACE)) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSaldoKeyTyped

    private void txtImporteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtImporteKeyTyped
        char c = evt.getKeyChar();
        if (((c < '0') || (c > '9')) && (c != evt.VK_BACK_SPACE)
                && (c != '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_txtImporteKeyTyped

    private void txtNombre_productoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombre_productoActionPerformed

    }//GEN-LAST:event_txtNombre_productoActionPerformed

    private void txtImporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtImporteActionPerformed

txtImporte.transferFocus();         // TODO add your handling code here:
    }//GEN-LAST:event_txtImporteActionPerformed

    private void txtTotal_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotal_ventaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotal_ventaActionPerformed

    private void txtcedulaCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcedulaCActionPerformed
        btnClickActionPerformed(evt);
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcedulaCActionPerformed

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
            java.util.logging.Logger.getLogger(Abonos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Abonos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Abonos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Abonos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new Abonos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnCalcular;
    private javax.swing.JButton btnClick;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnQuitarProducto;
    private javax.swing.JButton btnbuscarProducto;
    private javax.swing.JComboBox<String> cboComprobante;
    private javax.swing.JComboBox<String> cboModoIngreso;
    private com.toedter.calendar.JDateChooser dcFecha_venta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTabla;
    private javax.swing.JLabel lblModo;
    public static javax.swing.JTextField txtCantidadProducto;
    public static javax.swing.JTextField txtCod_cliente;
    private javax.swing.JTextField txtCod_detalle;
    public static javax.swing.JTextField txtCod_producto;
    public static javax.swing.JTextField txtCod_usuario;
    private javax.swing.JTextField txtCod_venta;
    private javax.swing.JTextField txtCod_ventaFK;
    private javax.swing.JTextField txtImporte;
    public static javax.swing.JTextField txtNombre_cliente;
    public static javax.swing.JTextField txtNombre_producto;
    public static javax.swing.JLabel txtNombre_usuario;
    private javax.swing.JTextField txtNumFactura;
    public static javax.swing.JTextField txtPrecio_producto;
    private javax.swing.JTextField txtSaldo;
    public static javax.swing.JTextField txtStockDetalle;
    public static javax.swing.JTextField txtSubPrecioCompra;
    private javax.swing.JTextField txtTotal_venta;
    public static javax.swing.JTextField txtcedulaC;
    // End of variables declaration//GEN-END:variables

}
