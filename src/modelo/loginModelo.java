
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.conexionModelo;

public class loginModelo {
    
    public static int validarLogin(String usuario, String clave) {
        Connection con;
        int i = 0;
        PreparedStatement ps;
        
        try {
            con = conexionModelo.getConnection();
            ps = con.prepareStatement("SELECT * FROM Usuarios WHERE nombre_usuario = ? AND clave = ?");
            
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                i = 1;
            } else {
                i = 0;
            }
            return i;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error durante la verificacion de credenciales" + e.toString(), "ERROR CRITICO", JOptionPane.WARNING_MESSAGE);
            return 0;
        }
    }
    
}
