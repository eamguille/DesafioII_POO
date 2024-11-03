
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.JOptionPane;
import modelo.conexionModelo;

public class revistaModelo {
    
    PreparedStatement ps;
    Connection con;
    
    // Metodo para cargar la tabla de revistas
    public ResultSet cargaTablaRevistas() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT p.id_producto, r.codigo_identificacion_rev, p.titulo, r.editorial, r.periodicidad, r.fecha_publicacion, p.precio, p.unidades_disponibles, t.tipo_producto FROM productos p INNER JOIN revistas r ON p.id_producto = r.id_producto INNER JOIN Tipos_Productos t ON p.id_tipo_producto = t.id_tipo_producto;";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: "+e.toString());
            return null;
        }
    }
    
    // Metodo para agregar una nueva revista
    public boolean agregarRevista(int p_idProducto, String p_editorial, String p_periodicidad, String p_fechaP) {
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO Revistas(id_producto, editorial, periodicidad, fecha_publicacion) VALUES (?,?,?,?);";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.setString(2, p_editorial);
            ps.setString(3, p_periodicidad);
            ps.setString(4, p_fechaP);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Metodo para actualizar una revista
    public boolean actualizarRevista(int p_idProducto, String p_editorial, String p_periodicidad, String p_fechaP) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE Revistas SET editorial = ?, periodicidad = ?, fecha_publicacion = ? WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_editorial);
            ps.setString(2, p_periodicidad);
            ps.setString(3, p_fechaP);
            ps.setInt(4, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    public boolean eliminarRevista(int p_idProducto) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM Revistas WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para validar si revista ya existe
    public boolean existeRevista(String p_editorial, String p_periodicidad) {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT COUNT(*) FROM Revistas WHERE editorial = ? AND periodicidad = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_editorial);
            ps.setString(2, p_periodicidad);
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
