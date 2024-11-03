
package vistas.secciones;

import controlador.clienteControlador;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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



public class FrmGestionClientes extends javax.swing.JPanel {

    // Definimos las variables genericas para manejo de datos de la tabla
    DefaultComboBoxModel<String> modelocombo = new DefaultComboBoxModel<>();
    private List myArrayList;
    clienteControlador objControlador = new clienteControlador();
    private DefaultTableModel modelClientes;
    
    public FrmGestionClientes() {
        initComponents();
        
        txtIdCliente.setVisible(false);
        // Headers con los que se carga la tabla
        String[] headers = {"ID","Nombres","Apellidos","Correo","Contraseña","Telefono","Fecha de registro"};
        modelClientes = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbClientes.setModel(modelClientes);
        
        // Crear el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelClientes);
        tbClientes.setRowSorter(sorter);
        
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
    }

    // Creamos el metodo que se encargara de cargar los datos en la tabla
    void cargarTabla() {
        modelClientes.setRowCount(0);
        try {
            ResultSet rs = objControlador.cargarTablaController();
            while(rs.next()){
                Object[] campos = {rs.getInt("id_cliente"), rs.getString("nombres"), rs.getString("apellidos"), rs.getString("email"), rs.getString("clave"), rs.getString("telefono"), rs.getString("fecha_registro")};
                
                // Aqui ocultamos los datos que no queremos mostrar en la tabla
                tbClientes.getColumnModel().getColumn(0).setMinWidth(0);
                tbClientes.getColumnModel().getColumn(0).setMaxWidth(0);
                tbClientes.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                modelClientes.addRow(campos);
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
        for(int i = 0; i < tbClientes.getColumnCount(); i++) {
            tbClientes.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
        // Método para filtrar la tabla
    void filtrarTabla() {
        String busqueda = txtBuscar.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbClientes.getRowSorter();
    
        if (busqueda.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
        }
    }
    
    // metodo para agregar cliente
    void agregarCliente() {
        try {
            if(txtNombres.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtClave.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty() || dtFecha.getCalendar() == null) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            // Aqui obtenemos los datos de cliente
            String nombres = txtNombres.getText();
            String apellidos = txtApellido.getText();
            String correo = txtEmail.getText();
            String clave = txtClave.getText();
            String telefono = txtTelefono.getText();
            Date fechaP = dtFecha.getDate();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatoFecha.format(fechaP);
            
            if(objControlador.validarClienteExistenteController(correo, telefono)){
                JOptionPane.showMessageDialog(null, "Cliente ya existe");
                return;
            }
            
            boolean resultado = objControlador.agregarClienteController(nombres, apellidos, correo, clave, telefono, fechaString);
            if(resultado){
                JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no pudo ser agregado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
            cargarTabla();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error" + e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Creamos un metodo para cargar los datos de la tabla al formulario
    void cargarDatosFormulario() {
        int filaSeleccionada = tbClientes.getSelectedRow();
        if(filaSeleccionada != -1) {
            txtIdCliente.setText(modelClientes.getValueAt(filaSeleccionada, 0).toString());
            txtNombres.setText(modelClientes.getValueAt(filaSeleccionada, 1).toString());
            txtApellido.setText(modelClientes.getValueAt(filaSeleccionada, 2).toString());
            txtEmail.setText(modelClientes.getValueAt(filaSeleccionada, 3).toString());
            txtClave.setText(modelClientes.getValueAt(filaSeleccionada, 4).toString());
            txtTelefono.setText(modelClientes.getValueAt(filaSeleccionada, 5).toString());
            
            try {
                Date fechaP = new SimpleDateFormat("yyyy-MM-dd").parse(modelClientes.getValueAt(filaSeleccionada, 6).toString());
                dtFecha.setDate(fechaP);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar la fecha de publicacion: " + e.toString());
            }
        }
    }
    
    // metodo para actualizar cliente    
    void actualizarCliente() {
        try {
            if(txtNombres.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty() || txtEmail.getText().trim().isEmpty() || txtClave.getText().trim().isEmpty() || txtTelefono.getText().trim().isEmpty() || dtFecha.getCalendar() == null) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            int id = Integer.parseInt(txtIdCliente.getText());
            String nombres = txtNombres.getText();
            String apellidos = txtApellido.getText();
            String correo = txtEmail.getText();
            String clave = txtClave.getText();
            String telefono = txtTelefono.getText();
            Date fechaP = dtFecha.getDate();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatoFecha.format(fechaP);
            
            boolean resultado = objControlador.actualizarClienteController(id, nombres, apellidos, correo, clave, telefono, fechaString);
            if(resultado){
                JOptionPane.showMessageDialog(null, "Cliente actualizado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no pudo ser actualizado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
            cargarTabla();
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: "+e.toString());
        }
    }
    
    // metodo para eliminar cliente
    void eliminarCliente() {
        int filaSeleccionada = tbClientes.getSelectedRow();
        if(filaSeleccionada != -1) {
            int id = (int) modelClientes.getValueAt(filaSeleccionada, 0);
            
            boolean resultado = objControlador.eliminarClienteController(id);
            if(resultado){
                JOptionPane.showMessageDialog(null, "Cliente eliminado exitosamente", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente no pudo ser eliminado", "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
            }
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar");
        }
    }
    
    // metodo para limpiar campos
    void limpiarCampos() {
        txtIdCliente.setText("");
        txtNombres.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtClave.setText("");
        txtTelefono.setText("");
        dtFecha.setCalendar(null);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btnAgregar = new vistas.swing.ButtonGradient();
        btnLimpiar = new vistas.swing.ButtonGradient();
        btnActualizar = new vistas.swing.ButtonGradient();
        btnEliminar = new vistas.swing.ButtonGradient();
        dtFecha = new com.toedter.calendar.JDateChooser();
        txtNombres = new vistas.componentes.TextFields.TextField();
        txtApellido = new vistas.componentes.TextFields.TextField();
        txtEmail = new vistas.componentes.TextFields.TextField();
        txtIdCliente = new javax.swing.JLabel();
        txtTelefono = new vistas.componentes.TextFields.TextField();
        txtClave = new vistas.componentes.TextFields.PasswordField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbClientes = new vistas.swing.Table();
        txtBuscar = new vistas.swing.SearchText();

        setBackground(new java.awt.Color(244, 239, 250));

        jPanel1.setBackground(new java.awt.Color(244, 239, 250));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(47, 24, 75));
        jLabel6.setText("Fecha Ingreso:");

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

        dtFecha.setBackground(new java.awt.Color(244, 239, 250));
        dtFecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(47, 24, 75)));
        dtFecha.setForeground(new java.awt.Color(47, 24, 75));
        dtFecha.setDateFormatString("yyyy-MM-dd");

        txtNombres.setLabelText("Nombres:");

        txtApellido.setLabelText("Apellidos:");

        txtEmail.setLabelText("Email:");

        txtIdCliente.setText("jLabel1");

        txtTelefono.setLabelText("Telefono:");

        txtClave.setLabelText("Contraseña:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtApellido, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtNombres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(dtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addComponent(txtIdCliente)
                        .addContainerGap(117, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(txtClave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(7, 7, 7)
                                .addComponent(dtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 6, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));

        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        tbClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbClientes);

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
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked
        agregarCliente();
        limpiarCampos();
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        actualizarCliente();
        limpiarCampos();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro de que deseas eliminar este cliente?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarCliente();
            limpiarCampos();
        }
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void tbClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbClientesMouseClicked
        cargarDatosFormulario();
    }//GEN-LAST:event_tbClientesMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.swing.ButtonGradient btnActualizar;
    private vistas.swing.ButtonGradient btnAgregar;
    private vistas.swing.ButtonGradient btnEliminar;
    private vistas.swing.ButtonGradient btnLimpiar;
    private com.toedter.calendar.JDateChooser dtFecha;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private vistas.swing.Table tbClientes;
    private vistas.componentes.TextFields.TextField txtApellido;
    private vistas.swing.SearchText txtBuscar;
    private vistas.componentes.TextFields.PasswordField txtClave;
    private vistas.componentes.TextFields.TextField txtEmail;
    private javax.swing.JLabel txtIdCliente;
    private vistas.componentes.TextFields.TextField txtNombres;
    private vistas.componentes.TextFields.TextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
