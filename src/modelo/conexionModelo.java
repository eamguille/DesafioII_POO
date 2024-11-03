
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class conexionModelo {
    public static final String nombre_usuario = "root";
    public static final String clave_usuario = "";
    public static final String url = "jdbc:mysql://localhost:3306/dbGestionMediateca";
    public static Connection con = null;
    
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, nombre_usuario, clave_usuario);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "" + ex, "", JOptionPane.WARNING_MESSAGE);
        }
        
        return con;
    }
}
