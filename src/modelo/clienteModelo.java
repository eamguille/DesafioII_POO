
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class clienteModelo {
    
    PreparedStatement ps;
    Connection con;
    
    // Metodo para cargar tabla de clientes
    public ResultSet cargarTablaClientes() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT * FROM Clientes";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error cargando datos: "+e.toString());
            return null;
        }
    }
    
    // Metodo para agregar cliente
    public boolean agregarCliente(String p_nombres, String p_apellidos, String p_email, String p_clave, String p_telefono, String p_fechaR) {
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO Clientes(nombres, apellidos, email, clave, telefono, fecha_registro) VALUES (?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setString(1, p_nombres);
            ps.setString(2, p_apellidos);
            ps.setString(3, p_email);
            ps.setString(4, p_clave);
            ps.setString(5, p_telefono);
            ps.setString(6, p_fechaR);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al ingresar datos: "+e.toString());
            return false;
        }
    }
    
    // Metodo para actualizar cliente
    public boolean actualizarCliente(int p_idCliente, String p_nombres, String p_apellidos, String p_email, String p_clave, String p_telefono, String p_fechaR) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE Clientes SET nombres = ?, apellidos = ?, email = ?, clave = ?, telefono = ?, fecha_registro = ? WHERE id_cliente = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_nombres);
            ps.setString(2, p_apellidos);
            ps.setString(3, p_email);
            ps.setString(4, p_clave);
            ps.setString(5, p_telefono);
            ps.setString(6, p_fechaR);
            ps.setInt(7, p_idCliente);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar datos: "+e.toString());
            return false;
        }
    }
    
    // Metodo para eliminar cliente
    public boolean eliminarCliente(int p_idCliente) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM Clientes WHERE id_cliente = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idCliente);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar datos: "+e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para validar si Cliente ya existe
    public boolean existeCliente(String p_email, String p_telefono) {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT COUNT(*) FROM Clientes WHERE email = ? AND telefono = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_email);
            ps.setString(2, p_telefono);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return false;
    }
}
