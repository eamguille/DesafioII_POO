
package modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.conexionModelo;

public class productosModelo {
    
    PreparedStatement ps;
    Connection con;
    
    // Metodo para cargar lista con tipo de productos
        // Creamos un metodo usado para cargar el comboBox de Tipos Productos
    public ResultSet cargarTipoProducto() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT * FROM Tipos_Productos";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la lista" + e.toString());
            return null;
        }
    }
    
    // Creamos un metodo para crear un producto
    public int IngresarProducto(String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_estado) {
        int idProducto = -1;
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO Productos(titulo, precio, unidades_disponibles, id_tipo_producto, estado) VALUES (?,?,?,?,?)";
            ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, p_titulo);
            ps.setFloat(2, p_precio);
            ps.setInt(3, p_unidades);
            ps.setInt(4, p_idTipoP);
            ps.setString(5, p_estado);
            ps.execute();
            
            ResultSet idgenerado = ps.getGeneratedKeys();
            if(idgenerado.next()) {
                idProducto = idgenerado.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al realizar el proceso"+e.toString(), "Proceso imcompleto", JOptionPane.ERROR_MESSAGE);
        }
        return idProducto;
    }
   
    // Definimos un metodo para actualizar productos
    public boolean actualizarProducto(int p_idProducto, String p_titulo, float p_precio, int p_unidades, int p_idTipoP) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE Productos SET titulo = ?, precio = ?, unidades_disponibles = ?, id_tipo_producto = ? WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_titulo);
            ps.setFloat(2, p_precio);
            ps.setInt(3, p_unidades);
            ps.setInt(4, p_idTipoP);
            ps.setInt(5, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar producto"+e.toString());
            return false;
        }
    }
    
    // Definimos metodo para eliminar el producto
    public boolean eliminarProducto(int p_idProducto) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM Productos WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el producto"+e.toString());
            return false;
        }
    }
}
