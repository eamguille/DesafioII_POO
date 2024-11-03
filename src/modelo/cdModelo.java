
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.JOptionPane;
import modelo.conexionModelo;

public class cdModelo {
    
    PreparedStatement ps;
    Connection con;
    
    // Metodo para cargar la tabla de CDs
    public ResultSet cargaTablaCDs() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT p.id_producto, c.codigo_identificacion_cd, p.titulo, c.artista_cd, c.genero_cd, c.duracion, c.numero_canciones, p.precio, p.unidades_disponibles, t.tipo_producto  FROM productos p  INNER JOIN CDs c ON p.id_producto = c.id_producto INNER JOIN Tipos_Productos t ON p.id_tipo_producto = t.id_tipo_producto;";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: "+e.toString());
            return null;
        }
    }
    
    // Metodo para agregar una nuevo CD
    public boolean agregarCD(int p_idProducto, String p_artista, String p_genero, String p_duracion, int p_numeroCanciones) {
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO Cds(id_producto, artista_cd, genero_cd, duracion, numero_canciones) VALUES (?,?,?,?,?);";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.setString(2, p_artista);
            ps.setString(3, p_genero);
            ps.setString(4, p_duracion);
            ps.setInt(5, p_numeroCanciones);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Metodo para actualizar un CD
    public boolean actualizarCD(int p_idProducto, String p_artista, String p_genero, String p_duracion, int p_numeroCanciones) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE CDs SET artista_cd = ?, genero_cd = ?, duracion = ?, numero_canciones = ? WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_artista);
            ps.setString(2, p_genero);
            ps.setString(3, p_duracion);
            ps.setInt(4, p_numeroCanciones);
            ps.setInt(5, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Metodo para eliminar CD
    public boolean eliminarCD(int p_idProducto) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM CDs WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para validar si CD ya existes
    public boolean existeCD(String p_artista, String p_genero, String p_duracion, int p_numeroCanciones) {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT COUNT(*) FROM CDs WHERE artista_cd = ? AND genero_cd = ? AND duracion = ? AND numero_canciones = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_artista);
            ps.setString(2, p_genero);
            ps.setString(3, p_duracion);
            ps.setInt(4, p_numeroCanciones);
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
