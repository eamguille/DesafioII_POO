
package controlador;

import java.sql.ResultSet;
import modelo.clienteModelo;

public class clienteControlador {
    
    clienteModelo objModelo = new clienteModelo();
    
    // METODOS DE RETORNO
    
    public ResultSet cargarTablaController() {
        return objModelo.cargarTablaClientes();
    }
    
    public boolean agregarClienteController(String p_nombres, String p_apellidos, String p_email, String p_clave, String p_telefono, String p_fechaR) {
        return objModelo.agregarCliente(p_nombres, p_apellidos, p_email, p_clave, p_telefono, p_fechaR);
    }
    
    public boolean actualizarClienteController(int p_idCliente, String p_nombres, String p_apellidos, String p_email, String p_clave, String p_telefono, String p_fechaR) {
        return objModelo.actualizarCliente(p_idCliente, p_nombres, p_apellidos, p_email, p_clave, p_telefono, p_fechaR);
    }
    
    public boolean eliminarClienteController(int p_idCliente) {
        return objModelo.eliminarCliente(p_idCliente);
    }
    
    public boolean validarClienteExistenteController(String p_email, String p_telefono) {
        return objModelo.existeCliente(p_email, p_telefono);
    }
}
