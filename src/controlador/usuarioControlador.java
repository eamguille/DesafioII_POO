
package controlador;

import java.sql.ResultSet;
import modelo.usuarioModelo;


public class usuarioControlador {
    
    usuarioModelo objmodelo = new usuarioModelo();
    
    // AGREGAMOS LOS METODO DE RETORNO
    public ResultSet cargarTablaController() {
        return objmodelo.cargarDatosTabla();
    }
    
    public ResultSet cargarTiposUsuarioController() {
        return objmodelo.cargarListaTipoUsuario();
    }
    
    public boolean agregarUsuarioController(String p_nombreUsuario, String p_clave, String p_tipoUsuario, String p_telefono, String p_email) {
        return objmodelo.agregarUsuario(p_nombreUsuario, p_clave, p_tipoUsuario, p_telefono, p_email);
    }
    
    public boolean actualizarUsuarioController(int p_idUsuario, String p_nombreUsuario, String p_clave, String p_tipoUsuario, String p_telefono, String p_email) {
        return objmodelo.actualizarUsuario(p_idUsuario, p_nombreUsuario, p_clave, p_tipoUsuario, p_telefono, p_email);
    }
    
    public boolean eliminarUsuarioController(int p_idUsuario) {
        return objmodelo.eliminarUsuario(p_idUsuario);
    }
    
    public boolean validarUsuarioExistenteController(String p_email, String p_telefono) {
        return objmodelo.existeUsuario(p_email, p_telefono);
    }
}
