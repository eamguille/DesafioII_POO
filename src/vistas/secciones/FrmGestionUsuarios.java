
package vistas.secciones;

import controlador.usuarioControlador;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FrmGestionUsuarios extends javax.swing.JPanel {

    // Definimos las variables genericas para manejo de datos de la tabla
    DefaultComboBoxModel<String> modelocombo = new DefaultComboBoxModel<>();
    private List myArrayList;
    usuarioControlador objControlador = new usuarioControlador();
    private DefaultTableModel modelUsuarios;
    
    public FrmGestionUsuarios() {
        initComponents();
        
        txtIdUsuario.setVisible(false);
        // Headers con los que se carga la tabla
        String[] headers = {"ID","Nombre de usuario","Contraseña","Tipo de usuario","Telefono","Email"};
        modelUsuarios = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbUsuarios.setModel(modelUsuarios);
        
        // Crear el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelUsuarios);
        tbUsuarios.setRowSorter(sorter);
        
        cargarTabla();
        centrarContenido();
        
        // Añadir el DocumentListener para el JTextField de búsqueda
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            filtrarTabla();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            filtrarTabla();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            filtrarTabla();
        }
        });
        
        cargarTipoUsuarios();
    }

    // Creamos el metodo que se encargara de cargar los datos en la tabla
    void cargarTabla() {
        modelUsuarios.setRowCount(0);
        try {
            ResultSet rs = objControlador.cargarTablaController();
            while(rs.next()){
                Object[] campos = {rs.getInt("id_usuario"), rs.getString("nombre_usuario"), rs.getString("clave"), rs.getString("tipo_usuario"), rs.getString("telefono_usuario"), rs.getString("email")};
                
                // Aqui ocultamos los datos que no queremos mostrar en la tabla
                tbUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
                tbUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
                tbUsuarios.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                modelUsuarios.addRow(campos);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }
    
    // metodo para centrar contenido de la tabla
    private void centrarContenido() {
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment( SwingConstants.CENTER );
        
        // aplicamos el centrado a todas las columnas
        for(int i = 0; i < tbUsuarios.getColumnCount(); i++) {
            tbUsuarios.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
    // metodo para cargar ComboBox
    void cargarTipoUsuarios() {
        ResultSet rs = objControlador.cargarTiposUsuarioController();
        try {
            if(rs != null && rs.next()) {
                String campo = rs.getString("Type");
                campo = campo.substring(5, campo.length() - 1);
                String[] valores = campo.replace("'", "").split(",");
                
                cmbTipoUsuario.removeAllItems();
                
                for (String valor : valores) {
                    cmbTipoUsuario.addItem(valor);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar comboBox", "Error de carga", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Método para filtrar la tabla
    void filtrarTabla() {
        String busqueda = txtBuscar.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbUsuarios.getRowSorter();
    
        if (busqueda.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
        }
    }
    
    // metodo para agregar usuario
    void agregarUsuario() {
        try {
            if(txtNombreUsuario.getText().trim().isEmpty() || txtClave.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty() || cmbTipoUsuario.getSelectedItem() == "") {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            // Aqui obtenemos los datos de usuario
            String nombre_usuario = txtNombreUsuario.getText();
            String clave = txtClave.getText();
            String correo = txtEmail.getText();
            String telefono = txtTelefono.getText();
            String tipo_usuario = cmbTipoUsuario.getSelectedItem().toString();
            
            if(objControlador.validarUsuarioExistenteController(correo, telefono)){
                JOptionPane.showMessageDialog(null, "Usuario ya existe");
                return;
            }
            
            boolean resultado = objControlador.agregarUsuarioController(nombre_usuario, clave, tipo_usuario, telefono, correo);
            if(resultado){
                JOptionPane.showMessageDialog(null, "Usuario agregado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no pudo ser agregado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
            cargarTabla();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error" + e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Creamos un metodo para cargar los datos de la tabla al formulario
    void cargarDatosFormulario() {
        int filaSeleccionada = tbUsuarios.getSelectedRow();
        if(filaSeleccionada != -1) {
            txtIdUsuario.setText(modelUsuarios.getValueAt(filaSeleccionada, 0).toString());
            txtNombreUsuario.setText(modelUsuarios.getValueAt(filaSeleccionada, 1).toString());
            txtClave.setText(modelUsuarios.getValueAt(filaSeleccionada, 2).toString());
            cmbTipoUsuario.setSelectedItem(modelUsuarios.getValueAt(filaSeleccionada, 3).toString());
            txtTelefono.setText(modelUsuarios.getValueAt(filaSeleccionada, 4).toString());
            txtEmail.setText(modelUsuarios.getValueAt(filaSeleccionada, 5).toString());
        }
    }
    
    // metodo para actualizar cliente    
    void actualizarUsuario() {
        try {
            if(txtNombreUsuario.getText().trim().isEmpty() || txtClave.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty() || cmbTipoUsuario.getSelectedItem() == "") {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            int id = Integer.parseInt(txtIdUsuario.getText());
            String nombre_usuario = txtNombreUsuario.getText();
            String clave = txtClave.getText();
            String correo = txtEmail.getText();
            String telefono = txtClave.getText();
            String tipo_usuario = cmbTipoUsuario.getSelectedItem().toString();
            
            boolean resultado = objControlador.actualizarUsuarioController(id, nombre_usuario, clave, tipo_usuario, telefono, correo);
            if(resultado){
                JOptionPane.showMessageDialog(null, "Usuario actualizado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
            cargarTabla();
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: "+e.toString());
        }
    }
    
    // metodo para eliminar usuario
    void eliminarUsuario() {
        int filaSeleccionada = tbUsuarios.getSelectedRow();
        if(filaSeleccionada != -1) {
            int id = (int) modelUsuarios.getValueAt(filaSeleccionada, 0);
            
            boolean resultado = objControlador.eliminarUsuarioController(id);
            if(resultado){
                JOptionPane.showMessageDialog(null, "Usuario eliminado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar");
        }
    }
    
    // metodo para limpiar campos
    void limpiarCampos() {
        txtIdUsuario.setText("");
        txtNombreUsuario.setText("");
        txtEmail.setText("");
        txtClave.setText("");
        txtTelefono.setText("");
        cmbTipoUsuario.setSelectedItem("");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnAgregar = new vistas.swing.ButtonGradient();
        btnLimpiar = new vistas.swing.ButtonGradient();
        btnActualizar = new vistas.swing.ButtonGradient();
        btnEliminar = new vistas.swing.ButtonGradient();
        txtNombreUsuario = new vistas.componentes.TextFields.TextField();
        txtEmail = new vistas.componentes.TextFields.TextField();
        txtIdUsuario = new javax.swing.JLabel();
        txtTelefono = new vistas.componentes.TextFields.TextField();
        txtClave = new vistas.componentes.TextFields.PasswordField();
        cmbTipoUsuario = new vistas.componentes.ComboBox.ComboBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbUsuarios = new vistas.swing.Table();
        txtBuscar = new vistas.swing.SearchText();

        setBackground(new java.awt.Color(244, 239, 250));

        jPanel1.setBackground(new java.awt.Color(244, 239, 250));

        btnAgregar.setText("Agregar");
        btnAgregar.setColor1(new java.awt.Color(200, 177, 228));
        btnAgregar.setColor2(new java.awt.Color(155, 114, 207));
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAgregarMouseClicked(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.setColor1(new java.awt.Color(200, 177, 228));
        btnLimpiar.setColor2(new java.awt.Color(155, 114, 207));
        btnLimpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLimpiarMouseClicked(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.setColor1(new java.awt.Color(200, 177, 228));
        btnActualizar.setColor2(new java.awt.Color(155, 114, 207));
        btnActualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnActualizarMouseClicked(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.setColor1(new java.awt.Color(200, 177, 228));
        btnEliminar.setColor2(new java.awt.Color(155, 114, 207));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEliminarMouseClicked(evt);
            }
        });

        txtNombreUsuario.setLabelText("Nombre de usuario:");

        txtEmail.setLabelText("Email:");

        txtIdUsuario.setText("jLabel1");

        txtTelefono.setLabelText("Telefono:");

        txtClave.setLabelText("Contraseña:");

        cmbTipoUsuario.setLabeText("Tipo de usuario:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(txtNombreUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbTipoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtClave, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(txtIdUsuario)
                        .addContainerGap(117, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(txtClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(cmbTipoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 1, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));

        tbUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbUsuarios);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked
        agregarUsuario();
        limpiarCampos();
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        actualizarUsuario();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro de que deseas eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarUsuario();
            limpiarCampos();
        }
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void tbUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuariosMouseClicked
        cargarDatosFormulario();
    }//GEN-LAST:event_tbUsuariosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.swing.ButtonGradient btnActualizar;
    private vistas.swing.ButtonGradient btnAgregar;
    private vistas.swing.ButtonGradient btnEliminar;
    private vistas.swing.ButtonGradient btnLimpiar;
    private vistas.componentes.ComboBox.ComboBox cmbTipoUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private vistas.swing.Table tbUsuarios;
    private vistas.swing.SearchText txtBuscar;
    private vistas.componentes.TextFields.PasswordField txtClave;
    private vistas.componentes.TextFields.TextField txtEmail;
    private javax.swing.JLabel txtIdUsuario;
    private vistas.componentes.TextFields.TextField txtNombreUsuario;
    private vistas.componentes.TextFields.TextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
