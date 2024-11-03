
package controlador;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.productosModelo;
import modelo.cdModelo;

public class cdControlador {
    
    cdModelo objmodelo = new cdModelo();
    productosModelo objProductoModelo = new productosModelo();
    
    // Agregamos metodo de retorno para mostrar datos en la tabla
    public ResultSet cargarTablaController() {
        return objmodelo.cargaTablaCDs();
    }
    
    // Agregamos metodo de retorno para agregar un CD nuevo
    public void agregarProductoYCDController(String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_estado, String p_artista, String p_genero, String p_duracion, int p_numeroCanciones) {
        int id_producto = objProductoModelo.IngresarProducto(p_titulo, p_precio, p_unidades, p_idTipoP, p_estado);
        if(id_producto != -1) {
            boolean cdAgregado = objmodelo.agregarCD(id_producto, p_artista, p_genero, p_duracion, p_numeroCanciones);
            if(cdAgregado) {
                JOptionPane.showMessageDialog(null, "CD agregado con exito", "Confirmacion de proceso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar CD", "Confirmacion de proceso", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error insertando producto");
        }
    }
    
    // Agregamos metodo de retorno para actualizar CD
    public void actualizarProductoYCDController(int p_idProducto, String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_artista, String p_genero, String p_duracion, int p_numeroCanciones) {
        boolean productoActualizado = objProductoModelo.actualizarProducto(p_idProducto, p_titulo, p_precio, p_unidades, p_idTipoP);
        if(productoActualizado) {
            boolean cdActualizado = objmodelo.actualizarCD(p_idProducto, p_artista, p_genero, p_duracion, p_numeroCanciones);
            if(cdActualizado){
                JOptionPane.showMessageDialog(null, "CD actualizado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "CD no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para eliminar CD
    public void eliminarProductoYCDController(int p_idProducto) {
        boolean cdEliminado = objmodelo.eliminarCD(p_idProducto);
        if(cdEliminado) {
            boolean productoEliminado = objProductoModelo.eliminarProducto(p_idProducto);
            if(productoEliminado) {
                JOptionPane.showMessageDialog(null, "CD eliminado exitosamente", "Proceso completo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "CD no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para validar si CD ya existe
    public boolean validarCDExistenteController(String p_artista, String p_genero, String p_duracion, int p_numeroCanciones) {
        return objmodelo.existeCD(p_artista, p_genero, p_duracion, p_numeroCanciones);
    }
}
