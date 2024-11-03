
package controlador;

import modelo.conexionModelo;
import java.sql.Connection;

public class conexionControlador {
    
    // Definimos metodo de retorno para la conexion a la base
    public Connection getConnectionController() {
        return conexionModelo.getConnection();
    }
}
