/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Funciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conexion {

    public String db = "bdcontroldeventas";
    public String url = "jdbc:mysql://localhost:3307/" + db;
    public String user = "root";
    public String pass = "uriel";

    public conexion() {
    }

    public Connection conectar() {
        Connection link = null;

        try {
            Class.forName("org.gjt.mm.mysql.Driver");
            link = DriverManager.getConnection(this.url, this.user, this.pass);

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showConfirmDialog(null, e+"error aca 12123");

        }

        return link;
    }/*
        Connection con=null;
        String sDriver="com.mysql.jdbc.Driver";
        String Url="jdbc:mysql://localhost:3307/bdcontroldeventas";
        try {
            Class.forName(sDriver).newInstance();
            con = DriverManager.getConnection(Url,"root","uriel");
            System.out.println("conexion exitosa");
        } catch (Exception e) {
            System.err.println("error al conectar ");
        }
            return con;
    }*/
    

}
