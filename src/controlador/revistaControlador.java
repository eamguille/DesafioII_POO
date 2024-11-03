
package controlador;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.productosModelo;
import modelo.revistaModelo;

public class revistaControlador {
    
    revistaModelo objmodelo = new revistaModelo();
    productosModelo objProductoModelo = new productosModelo();
    
    // Agregamos metodo de retorno para mostrar datos en la tabla
    public ResultSet cargarTablaController() {
        return objmodelo.cargaTablaRevistas();
    }
    
    // Agregamos metodo de retorno para agregar revista
    public void agregarProductoYRevistaController(String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_estado, String p_editorial, String p_periodicidad, String p_fechaP) {
        int id_producto = objProductoModelo.IngresarProducto(p_titulo, p_precio, p_unidades, p_idTipoP, p_estado);
        if(id_producto != -1) {
            boolean libroAgregado = objmodelo.agregarRevista(id_producto, p_editorial, p_periodicidad, p_fechaP);
            if(libroAgregado) {
                JOptionPane.showMessageDialog(null, "Revista agregada con exito", "Confirmacion de proceso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar revista", "Confirmacion de proceso", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error insertando producto");
        }
    }
    
    // Agregamos metodo de retorno para actualizar revista
    public void actualizarProductoYRevistaController(int p_idProducto, String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_editorial, String p_periodicidad, String p_fechaP) {
        boolean productoActualizado = objProductoModelo.actualizarProducto(p_idProducto, p_titulo, p_precio, p_unidades, p_idTipoP);
        if(productoActualizado) {
            boolean revistaActualizada = objmodelo.actualizarRevista(p_idProducto, p_editorial, p_periodicidad, p_fechaP);
            if(revistaActualizada){
                JOptionPane.showMessageDialog(null, "Revista actualizada exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Revista no pudo ser actualizada", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para eliminar revista
    public void eliminarProductoYRevistaController(int p_idProducto) {
        boolean revistaEliminado = objmodelo.eliminarRevista(p_idProducto);
        if(revistaEliminado) {
            boolean productoEliminado = objProductoModelo.eliminarProducto(p_idProducto);
            if(productoEliminado) {
                JOptionPane.showMessageDialog(null, "Revista eliminada exitosamente", "Proceso completo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Revista no pudo ser eliminada", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para validar si revista ya existe
    public boolean validarRevistaExistenteController(String p_editorial, String p_periodicidad) {
        return objmodelo.existeRevista(p_editorial, p_periodicidad);
    }
}
