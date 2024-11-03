
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class dvdModelo {
    
    PreparedStatement ps;
    Connection con;
    
    // Creamos un metodo para cargar la tabla con los datos del DVD
    public ResultSet cargarDVDs() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT p.id_producto, d.codigo_identificacion_dvd, p.titulo, d.director_dvd, d.duracion_dvd, d.genero_dvd, p.precio, p.unidades_disponibles, t.tipo_producto  FROM productos p  INNER JOIN DVDs d ON p.id_producto = d.id_producto  INNER JOIN Tipos_Productos t ON p.id_tipo_producto = t.id_tipo_producto;";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos" + e.toString());
            return null;
        }
    }
    
    // Creamos un metodo para ingresar un nuevo DVD
    public boolean IngresarDVD(int p_idProducto, String p_director, String p_duracion, String p_genero) {
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO DVDs(id_producto, director_dvd, duracion_dvd, genero_dvd) VALUES (?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.setString(2, p_director);
            ps.setString(3, p_duracion);
            ps.setString(4, p_genero);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, "Error al realizar el proceso"+e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Metodo para actualizar un DVD
    public boolean actualizarDVD(int p_idProducto, String p_director, String p_duracion, String p_genero) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE DVDs SET director_dvd = ?, duracion_dvd = ?, genero_dvd = ? WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_director);
            ps.setString(2, p_duracion);
            ps.setString(3, p_genero);
            ps.setInt(4, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Metodo para eliminar DVD
    public boolean eliminarDVD(int p_idProducto) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM DVDs WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para validar si DVD ya existe
    public boolean existeDVD(String p_director, String p_duracion, String p_genero) {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT COUNT(*) FROM DVDs WHERE director_dvd = ? AND duracion_dvd = ? AND genero_dvd = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_director);
            ps.setString(2, p_duracion);
            ps.setString(3, p_genero);
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
