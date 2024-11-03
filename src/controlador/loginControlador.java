
package controlador;

import modelo.loginModelo;

public class loginControlador {
    
    public static String usuario;
    private String clave;

    public static String getUsuario() {
        return usuario;
    }

    public static void setUsuario(String usuario) {
        loginControlador.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
    
    public int ValidarLoginController() {
        return loginModelo.validarLogin(usuario, clave);
    }
}
