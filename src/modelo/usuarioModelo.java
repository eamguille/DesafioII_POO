
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class usuarioModelo {
    
    Connection con;
    PreparedStatement ps;
    
    // Agregamos metodo para cargar los datos de la tabla
    public ResultSet cargarDatosTabla() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT * FROM Usuarios";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    // Agregamos metodo para cargar la lista de los tipos de usuarios
    public ResultSet cargarListaTipoUsuario() {
        try {
            con = conexionModelo.getConnection();
            String query = "SHOW COLUMNS FROM Usuarios LIKE 'tipo_usuario'";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    // Agregamos metodo para agregar un nuevo usuario
    public boolean agregarUsuario(String p_nombreUsuario, String p_clave, String p_tipoUsuario, String p_telefono, String p_email) {
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO Usuarios(nombre_usuario, clave, tipo_usuario, telefono_usuario, email) VALUES (?,?,?,?,?);";
            ps = con.prepareStatement(query);
            ps.setString(1, p_nombreUsuario);
            ps.setString(2, p_clave);
            ps.setString(3, p_tipoUsuario);
            ps.setString(4, p_telefono);
            ps.setString(5, p_email);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para actualizar usuario
    public boolean actualizarUsuario(int p_idUsuario, String p_nombreUsuario, String p_clave, String p_tipoUsuario, String p_telefono, String p_email) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE Usuarios SET nombre_usuario = ?, clave = ?, tipo_usuario = ?, telefono_usuario = ?, email = ? WHERE id_usuario = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_nombreUsuario);
            ps.setString(2, p_clave);
            ps.setString(3, p_tipoUsuario);
            ps.setString(4, p_telefono);
            ps.setString(5, p_email);
            ps.setInt(6, p_idUsuario);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para eliminar usuario
    public boolean eliminarUsuario(int p_idUsuario) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM Usuarios WHERE id_usuario = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idUsuario);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para validar si Usuario ya existe
    public boolean existeUsuario(String p_email, String p_telefono) {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT COUNT(*) FROM Clientes WHERE email = ? AND telefono_usuario = ?";
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
