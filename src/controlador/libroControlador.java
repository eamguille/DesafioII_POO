
package controlador;

import modelo.libroModelo;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.productosModelo;

public class libroControlador extends productosControlador{
    
    libroModelo objmodelo = new libroModelo();
    productosModelo objProductoModelo = new productosModelo();
    
    // Agregamos un metodo de retorno para cargar la tabla con los datos de la base
    public ResultSet obtenerDatosController() {
        return objmodelo.cargarLibros();
    }
    
    // Agregamos un metodo de retorno desde el modelo a nuestro controlador
    public ResultSet cargarTipoProductoController() {
        return objProductoModelo.cargarTipoProducto();
    }
    
    // Agregamos un metodo de retorno desde el modelo producto para imgresar el registro
    public void insertarProductoYLibroController(String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_estado, String p_autor, int p_numeroPags, String p_editorial, String p_ISBN, String p_fechaP) {
        int ID_PRODUCTO = objProductoModelo.IngresarProducto(p_titulo, p_precio, p_unidades, p_idTipoP, p_estado);
        
        if(ID_PRODUCTO != -1) {
            boolean libroAgregado = objmodelo.IngresarLibro(ID_PRODUCTO, p_autor, p_numeroPags, p_editorial, p_ISBN, p_fechaP);
            if(libroAgregado) {
                JOptionPane.showMessageDialog(null, "Libro agregado con exito", "Confirmacion de proceso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar libro", "Confirmacion de proceso", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error insertando producto");
        }
    }
    
    // Agregamos un metodo de retorno para actualizar el libro
    public void actualizarProductoYLibroController(int p_idProducto, String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_autor, int p_numeroPags, String p_editorial, String p_ISBN, String p_fechaIngreso) {
        boolean productoActualizado = objModelo.actualizarProducto(p_idProducto, p_titulo, p_precio, p_unidades, p_idTipoP);
        if(productoActualizado) {
            boolean libroActualizado = objmodelo.actualizarLibro(p_idProducto, p_autor, p_numeroPags, p_editorial, p_ISBN, p_fechaIngreso);
            if(libroActualizado){
                JOptionPane.showMessageDialog(null, "Libro actualizado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Libro no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos un metodo de retorno para eliminar el libro
    public void eliminarProductoYLibroController(int p_idProducto) {
        boolean libroEliminado = objmodelo.eliminarLibro(p_idProducto);
        if(libroEliminado){
            boolean productoEliminado = objModelo.eliminarProducto(p_idProducto);
            if(productoEliminado) {
                JOptionPane.showMessageDialog(null, "Libro eliminado exitosamente", "Proceso completo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Libro no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para validar libro existente
    public boolean validarLibroExisteController(String p_ISBN) {
        return objmodelo.existeLibro(p_ISBN);
    }
}
