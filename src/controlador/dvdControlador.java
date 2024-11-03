
package controlador;

import java.sql.ResultSet;
import javax.swing.JOptionPane;
import modelo.productosModelo;
import modelo.dvdModelo;

public class dvdControlador {
    
    dvdModelo objDvdmodelo = new dvdModelo();
    productosModelo objProductoModelo = new productosModelo();
    
    // Agregamos metodo de retorno para mostrar datos en la tabla
    public ResultSet cargarTablaController() {
        return objDvdmodelo.cargarDVDs();
    }
    
    // Agregamos metodo de retorno para agregar DVD
    public void agregarProductoYDVDController(String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_estado, String p_director, String p_duracion, String p_genero) {
        int id_producto = objProductoModelo.IngresarProducto(p_titulo, p_precio, p_unidades, p_idTipoP, p_estado);
        if(id_producto != -1) {
            boolean dvdAgregado = objDvdmodelo.IngresarDVD(id_producto, p_director, p_duracion, p_genero);
            if(dvdAgregado) {
                JOptionPane.showMessageDialog(null, "DVD agregado con exito", "Confirmacion de proceso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Error al agregar DVD", "Confirmacion de proceso", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error insertando producto");
        }
    }
    
    // Agregamos metodo de retorno para actualizar DVD
    public void actualizarProductoYDVDController(int p_idProducto, String p_titulo, float p_precio, int p_unidades, int p_idTipoP, String p_director, String p_duracion, String p_genero) {
        boolean productoActualizado = objProductoModelo.actualizarProducto(p_idProducto, p_titulo, p_precio, p_unidades, p_idTipoP);
        if(productoActualizado) {
            boolean dvdActualizado = objDvdmodelo.actualizarDVD(p_idProducto, p_director, p_duracion, p_genero);
            if(dvdActualizado){
                JOptionPane.showMessageDialog(null, "DVD actualizado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "DVD no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para eliminar DVD
    public void eliminarProductoYDVDController(int p_idProducto) {
        boolean dvdEliminado = objDvdmodelo.eliminarDVD(p_idProducto);
        if(dvdEliminado) {
            boolean productoEliminado = objProductoModelo.eliminarProducto(p_idProducto);
            if(productoEliminado) {
                JOptionPane.showMessageDialog(null, "DVD eliminado exitosamente", "Proceso completo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "DVD no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Producto no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Agregamos metodo de retorno para validar si DVD ya existe
    public boolean validarDVDExistenteController(String p_director, String p_duracion, String p_genero) {
        return objDvdmodelo.existeDVD(p_director, p_duracion, p_genero);
    }
}
