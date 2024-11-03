
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import javax.swing.JOptionPane;
import modelo.conexionModelo;

public class libroModelo {
    
    PreparedStatement ps;
    Connection con;
    
    // Creamos un metodo para cargar la tabla con los datos del Libro
    public ResultSet cargarLibros() {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT p.id_producto, l.codigo_identificacion_lib, p.titulo, l.autor_libro, l.editorial_libro, l.numero_paginas, l.ISBN, p.precio, l.fecha_publicacion, p.unidades_disponibles, t.tipo_producto FROM productos p INNER JOIN libros l ON p.id_producto = l.id_producto INNER JOIN Tipos_Productos t ON p.id_tipo_producto = t.id_tipo_producto;";
            ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos" + e.toString());
            return null;
        }
    }
    
    // Creamos un metodo para ingresar un nuevo libro
    public boolean IngresarLibro(int p_idProducto, String p_autor, int p_numeroPags, String p_editorial, String p_ISBN, String p_fechaIngreso) {
        try {
            con = conexionModelo.getConnection();
            String query = "INSERT INTO Libros(id_producto, autor_libro, numero_paginas, editorial_libro, ISBN, fecha_publicacion) VALUES (?,?,?,?,?,?)";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.setString(2, p_autor);
            ps.setInt(3, p_numeroPags);
            ps.setString(4, p_editorial);
            ps.setString(5, p_ISBN);
            ps.setString(6, p_fechaIngreso);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, "Error al realizar el proceso"+e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Definimos un metodo para actualizar los libros
    public boolean actualizarLibro(int p_idProducto, String p_autor, int p_numeroPags, String p_editorial, String p_ISBN, String p_fechaIngreso) {
        try {
            con = conexionModelo.getConnection();
            String query = "UPDATE Libros SET autor_libro = ?, numero_paginas = ?, editorial_libro = ?, ISBN = ?, fecha_publicacion = ? WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_autor);
            ps.setInt(2, p_numeroPags);
            ps.setString(3, p_editorial);
            ps.setString(4, p_ISBN);
            ps.setString(5, p_fechaIngreso);
            ps.setInt(6, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el libro: "+e.toString());
            return false;
        }
    }
    
    // Definimos un metodo para eliminar el libro
    public boolean eliminarLibro(int p_idProducto) {
        try {
            con = conexionModelo.getConnection();
            String query = "DELETE FROM Libros WHERE id_producto = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, p_idProducto);
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el libro: "+e.toString());
            return false;
        }
    }
    
    // Agregamos metodo para validar si libro ya existe
    public boolean existeLibro(String p_ISBN) {
        try {
            con = conexionModelo.getConnection();
            String query = "SELECT COUNT(*) FROM Libros WHERE ISBN = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, p_ISBN);
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
