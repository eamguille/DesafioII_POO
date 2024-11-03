
package vistas;

import com.sun.awt.AWTUtilities;
import controlador.loginControlador;
import javax.swing.JOptionPane;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;

public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
        
        txtClave.setEchoChar('•');
        
        // Codigo para redondear los bordes
        Shape forma = new RoundRectangle2D.Double(0,0,this.getBounds().width, this.getBounds().height, 20, 20);
        AWTUtilities.setWindowShape(this, forma);
        
        initMoving(this);
    }
    
    // Metodo para validar el Login
    void validarLogin() {
        if (txtUsuario.getText().equals("") || txtClave.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Existen campos vacios, favor llenar los campos", "Campos vacios", JOptionPane.WARNING_MESSAGE);
        } else {
            loginControlador objControlador = new loginControlador();
            
            objControlador.setUsuario(txtUsuario.getText());
            objControlador.setClave(txtClave.getText());
            
            int resultado = objControlador.ValidarLoginController();
            if (resultado == 1) {
                Principal frmPrincipal = new Principal();
                frmPrincipal.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showConfirmDialog(null, "Credenciales incorrectas", "Validacion de credenciales", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private int x;
    private int y;
    
    public void initMoving(JFrame frame) {
        panelMoving.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }
        });
        panelMoving.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                frame.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }
    
    
    
    
    
    
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnIngresar = new javax.swing.JButton();
        txtClave = new javax.swing.JPasswordField();
        btnCerrar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("FrmLogin"); // NOI18N
        setUndecorated(true);
        setSize(new java.awt.Dimension(790, 390));

        panelMoving.setBackground(new java.awt.Color(83, 43, 136));
        panelMoving.setMinimumSize(new java.awt.Dimension(260, 390));
        panelMoving.setPreferredSize(new java.awt.Dimension(260, 390));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgs/Login_converted.png"))); // NOI18N

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(79, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(155, 114, 207));
        jPanel2.setMinimumSize(new java.awt.Dimension(430, 390));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(430, 390));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(200, 200, 200));
        jLabel1.setText("INICIA SESIÓN");

        txtUsuario.setBackground(new java.awt.Color(126, 96, 191));
        txtUsuario.setForeground(new java.awt.Color(200, 200, 200));
        txtUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtUsuario.setBorder(null);
        txtUsuario.setName("txtUsuario"); // NOI18N
        txtUsuario.setSelectionColor(new java.awt.Color(187, 187, 187));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(200, 200, 200));
        jLabel2.setLabelFor(txtUsuario);
        jLabel2.setText("Usuario");

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(200, 200, 200));
        jLabel3.setLabelFor(txtClave);
        jLabel3.setText("Contraseña");

        jLabel4.setForeground(new java.awt.Color(200, 200, 200));
        jLabel4.setText("Olvidaste tu contraseña ?");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnIngresar.setBackground(new java.awt.Color(83, 43, 136));
        btnIngresar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnIngresar.setForeground(new java.awt.Color(200, 200, 200));
        btnIngresar.setText("Ingresar");
        btnIngresar.setBorder(null);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnIngresar.setFocusPainted(false);
        btnIngresar.setName("btnIngresar"); // NOI18N
        btnIngresar.setPreferredSize(new java.awt.Dimension(170, 32));
        btnIngresar.setSelected(true);
        btnIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnIngresarMouseClicked(evt);
            }
        });

        txtClave.setBackground(new java.awt.Color(126, 96, 191));
        txtClave.setForeground(new java.awt.Color(200, 200, 200));
        txtClave.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtClave.setToolTipText("");
        txtClave.setBorder(null);
        txtClave.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtClave.setDropMode(javax.swing.DropMode.INSERT);
        txtClave.setEchoChar('\u0012');
        txtClave.setName("txtClave"); // NOI18N
        txtClave.setSelectionColor(new java.awt.Color(187, 187, 187));

        btnCerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/icons/close_converted.png"))); // NOI18N
        btnCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCerrarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btnCerrar)
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)))
                        .addComponent(jLabel2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(114, 114, 114))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(btnIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCerrarMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnCerrarMouseClicked

    private void btnIngresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIngresarMouseClicked
        validarLogin();
    }//GEN-LAST:event_btnIngresarMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnCerrar;
    private javax.swing.JButton btnIngresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panelMoving;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
