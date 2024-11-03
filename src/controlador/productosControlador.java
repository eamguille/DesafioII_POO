
package controlador;

import java.sql.ResultSet;
import modelo.productosModelo;

public class productosControlador {
    
    // Creamos una variable para ProductosModelo
    productosModelo objModelo = new productosModelo();
    
    // Creamos un metodo de retorno para cargar el tipo de producto
    public ResultSet cargarListaTipoProductos() {
        return objModelo.cargarTipoProducto();
    }
}
