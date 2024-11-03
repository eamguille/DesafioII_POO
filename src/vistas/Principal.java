
package vistas;

import vistas.eventos.EventMenuSelected;
import vistas.secciones.FrmDashBoard;
import vistas.secciones.FrmGestionLibros;
import vistas.secciones.FrmGestionRevistas;
import vistas.secciones.FrmGestionDVDs;
import vistas.secciones.FrmGestionCDs;
import vistas.secciones.FrmGestionClientes;
import vistas.secciones.FrmGestionUsuarios;
import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class Principal extends javax.swing.JFrame {

    private FrmDashBoard frmDashBoard;
    private FrmGestionLibros frmLibros;
    private FrmGestionRevistas frmRevistas;
    private FrmGestionDVDs frmDVDs;
    private FrmGestionCDs frmCDs;
    private FrmGestionClientes frmClientes;
    private FrmGestionUsuarios frmUsuarios;
    
    
    public Principal() {
        initComponents();
        
        // Codigo para redondear los bordes
        Shape forma = new RoundRectangle2D.Double(0,0,this.getBounds().width, this.getBounds().height, 20, 20);
        AWTUtilities.setWindowShape(this, forma);
        
        setBackground(new Color(0, 0, 0, 0));
        frmDashBoard = new FrmDashBoard();
        frmLibros = new FrmGestionLibros();
        frmRevistas = new FrmGestionRevistas();
        frmDVDs = new FrmGestionDVDs();
        frmCDs = new FrmGestionCDs();
        frmClientes = new FrmGestionClientes();
        frmUsuarios = new FrmGestionUsuarios();
        menu.initMoving(Principal.this);
        menu.addEventMenuSelected(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                if(index == 0) {
                    setForm(frmDashBoard);
                } else if(index == 3) {
                    setForm(frmLibros);
                } else if(index == 4) {
                    setForm(frmRevistas);
                } else if(index == 5) {
                    setForm(frmDVDs);
                } else if(index == 6) {
                    setForm(frmCDs);
                } else if(index == 9) {
                    setForm(frmClientes);
                } else if(index == 10) {
                    setForm(frmUsuarios);
                } else if(index == 13) {
                    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas salir?", "Confirmacion de salida", JOptionPane.YES_NO_OPTION);
                    if(confirmacion == JOptionPane.YES_OPTION) {
                        Principal.this.dispose();
                        Login lg = new Login();
                        lg.setVisible(true);
                    }
                }
            }});
        
        // Configuramos se abra el panel dashboard al ejecutar el programa
        setForm(new FrmDashBoard());
    }
    
    private void setForm(JComponent comp) {
        mainPanel.removeAll();
        mainPanel.add(comp);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        menu = new vistas.componentes.menu();
        jPanel2 = new javax.swing.JPanel();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(155, 114, 207));
        jPanel1.setPreferredSize(new java.awt.Dimension(280, 600));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1);

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));
        jPanel2.setPreferredSize(new java.awt.Dimension(820, 600));

        mainPanel.setBackground(new java.awt.Color(244, 239, 250));
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 588, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel mainPanel;
    private vistas.componentes.menu menu;
    // End of variables declaration//GEN-END:variables
}
