
package vistas.componentes;

import vistas.eventos.EventMenuSelected;
import modelo.MenuModelo;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;

public class menu extends javax.swing.JPanel {
    
    private EventMenuSelected event;
    
    public void addEventMenuSelected(EventMenuSelected event) {
        this.event = event;
        listMenu1.addEventMenuSelected(event);
    }

    public menu() {
        initComponents();
        
        setOpaque(false);
        listMenu1.setOpaque(false);
        panelMoving.setOpaque(false);
        init();
    }
    
    private void init() {
        listMenu1.addItem(new MenuModelo("dashboard", "Dashboard", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("", " ", MenuModelo.MenuType.EMPTY));
        listMenu1.addItem(new MenuModelo("", "Productos", MenuModelo.MenuType.TITLE));
        listMenu1.addItem(new MenuModelo("icolibro", "Gestion de Libros", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("magazine", "Gestion de Revistas", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("dvd", "Gestion de DVDs", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("cd", "Gestion de CDs", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("", " ", MenuModelo.MenuType.EMPTY));
        
        listMenu1.addItem(new MenuModelo("", "Personal", MenuModelo.MenuType.TITLE));
        listMenu1.addItem(new MenuModelo("clients", "Clientes", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("users", "Usuarios", MenuModelo.MenuType.MENU));
        listMenu1.addItem(new MenuModelo("", " ", MenuModelo.MenuType.EMPTY));
        listMenu1.addItem(new MenuModelo("", " ", MenuModelo.MenuType.EMPTY));

        listMenu1.addItem(new MenuModelo("salir", "Salir", MenuModelo.MenuType.MENU));
    }
    
    @Override
    protected void paintChildren(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#9B72CF"), 0, getHeight(), Color.decode("#2F184B"));
        g2.setPaint(g);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.fillRect(getWidth() - 10, 0, getWidth(), getHeight());
        super.paintChildren(graphics);
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

        listMenu1 = new vistas.swing.ListMenu<>();
        panelMoving = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/imgs/logo_converted.png"))); // NOI18N

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(listMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private vistas.swing.ListMenu<String> listMenu1;
    private javax.swing.JPanel panelMoving;
    // End of variables declaration//GEN-END:variables

}

