
package vistas.secciones;

import controlador.cdControlador;
import controlador.productosControlador;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

public class FrmGestionCDs extends javax.swing.JPanel {

    // Definimos las variables genericas para manejo de datos de la tabla
    DefaultComboBoxModel<String> modelocombo = new DefaultComboBoxModel<>();
    private List myArrayList;
    cdControlador objCdControlador = new cdControlador();
    productosControlador objProductoControlador = new productosControlador();
    private DefaultTableModel modelCds;
    
    public FrmGestionCDs() {
        initComponents();
        
        txtIdProducto.setVisible(false);
        // Headers con los que se carga la tabla
        String[] headers = {"ID","Codigo","Titulo","Artista","Genero","Duracion","Numero de canciones","Precio","Unidades disponibles", "Tipo Producto"};
        modelCds = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbCds.setModel(modelCds);
        
        // Crear el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelCds);
        tbCds.setRowSorter(sorter);
        
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
        
        cargarCmbTipoProducto();
    }
    
    // Creamos el metodo que se encargara de cargar los datos en la tabla
    void cargarTabla() {
        modelCds.setRowCount(0);
        try {
            ResultSet rs = objCdControlador.cargarTablaController();
            while(rs.next()){
                Object[] campos = {rs.getInt("id_producto"), rs.getString("codigo_identificacion_cd"), rs.getString("titulo"), rs.getString("artista_cd"), rs.getString("genero_cd"), rs.getString("duracion"), rs.getInt("numero_canciones"), rs.getFloat("precio"), rs.getInt("unidades_disponibles"), rs.getString("tipo_producto")};
                
                // Aqui ocultamos los datos que no queremos mostrar en la tabla
                tbCds.getColumnModel().getColumn(0).setMinWidth(0);
                tbCds.getColumnModel().getColumn(0).setMaxWidth(0);
                tbCds.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                tbCds.getColumnModel().getColumn(8).setMinWidth(0);
                tbCds.getColumnModel().getColumn(8).setMaxWidth(0);
                tbCds.getColumnModel().getColumn(8).setPreferredWidth(0);
                
                tbCds.getColumnModel().getColumn(9).setMinWidth(0);
                tbCds.getColumnModel().getColumn(9).setMaxWidth(0);
                tbCds.getColumnModel().getColumn(9).setPreferredWidth(0);
                
                modelCds.addRow(campos);
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
        for(int i = 0; i < tbCds.getColumnCount(); i++) {
            tbCds.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }
    
    // creamos un metodo para cargar los datos en el comboBox desde el controlador
    final void cargarCmbTipoProducto() {
        myArrayList = new ArrayList();
        try {
            ResultSet rs = objProductoControlador.cargarListaTipoProductos();
            if(rs.next()) {
                modelocombo.addElement("");
                do {
                    myArrayList.add(rs.getInt("id_tipo_producto"));
                    modelocombo.addElement(rs.getString("tipo_producto"));
                    cmbTipoProducto.setModel(modelocombo);
                } while(rs.next());
            } else {
                JOptionPane.showMessageDialog(null, "No existen datos sobre tipos de productos", "Error de carga de datos", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(null, "No se lograron cargar los datos", "Error al cargar datos", JOptionPane.WARNING_MESSAGE);
        }
    }
    
        // Método para filtrar la tabla
    void filtrarTabla() {
        String busqueda = txtBuscar.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbCds.getRowSorter();
    
        if (busqueda.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
        }
    }
    
    // metodo para agregar CD
    void agregarCD() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtArtista.getText().trim().isEmpty() || txtDuracion.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "" || txtNumeroCanciones.getText().trim().isEmpty() || txtGenero.getText().trim().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            // Aqui obtenemos los datos de producto
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            String estado = "Disponible";
            
            // Y aqui obtenemos los datos de CD
            String artista = txtArtista.getText();
            String duracion = txtDuracion.getText();
            String genero = txtGenero.getText();
            int num_canciones = Integer.parseInt(txtNumeroCanciones.getText());
            
            if(objCdControlador.validarCDExistenteController(artista, genero, duracion, num_canciones)) {
                JOptionPane.showMessageDialog(null, "CD ya existe");
                return;
            }
            
            objCdControlador.agregarProductoYCDController(titulo, precio, unidades, id_tipoProducto, estado, artista, genero, duracion, num_canciones);
            cargarTabla();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error" + e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Creamos un metodo para cargar los datos de la tabla al formulario
    void cargarDatosFormulario() {
        int filaSeleccionada = tbCds.getSelectedRow();
        if(filaSeleccionada != -1) {
            txtIdProducto.setText(modelCds.getValueAt(filaSeleccionada, 0).toString());
            txtTitulo.setText(modelCds.getValueAt(filaSeleccionada, 2).toString());
            txtArtista.setText(modelCds.getValueAt(filaSeleccionada, 3).toString());
            txtGenero.setText(modelCds.getValueAt(filaSeleccionada, 4).toString());
            txtDuracion.setText(modelCds.getValueAt(filaSeleccionada, 5).toString());
            txtNumeroCanciones.setText(modelCds.getValueAt(filaSeleccionada, 6).toString());
            txtPrecio.setText(modelCds.getValueAt(filaSeleccionada, 7).toString());
            txtUnidades.setText(modelCds.getValueAt(filaSeleccionada, 8).toString());
            cmbTipoProducto.setSelectedItem(modelCds.getValueAt(filaSeleccionada, 9).toString());
        }
    }
    
    // metodo para actualizar CD    
    void actualizarRevista() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtArtista.getText().trim().isEmpty() || txtDuracion.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "" || txtNumeroCanciones.getText().trim().isEmpty() || txtGenero.getText().trim().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            int id_producto = Integer.parseInt(txtIdProducto.getText());
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            
            String artista = txtArtista.getText();
            String duracion = txtDuracion.getText();
            String genero = txtGenero.getText();
            int num_canciones = Integer.parseInt(txtNumeroCanciones.getText());
            
            objCdControlador.actualizarProductoYCDController(id_producto, titulo, precio, unidades, id_tipoProducto, artista, genero, duracion, num_canciones);
            
            cargarTabla();
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: "+e.toString());
        }
    }
    
    // metodo para eliminar CD
    void eliminarRevista() {
        int filaSeleccionada = tbCds.getSelectedRow();
        if(filaSeleccionada != -1) {
            int idProducto = (int) modelCds.getValueAt(filaSeleccionada, 0);
            
            objCdControlador.eliminarProductoYCDController(idProducto);
            
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar");
        }
    }
    
    // Finalmente agregamos un metodo para limpiar los campos
    void limpiarCampos() {
        txtIdProducto.setText("");
        txtTitulo.setText("");
        txtArtista.setText("");
        txtGenero.setText("");
        txtDuracion.setText("");
        txtNumeroCanciones.setText("");
        txtPrecio.setText("");
        txtUnidades.setText("");
        cmbTipoProducto.setSelectedItem("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnAgregar = new vistas.swing.ButtonGradient();
        btnLimpiar = new vistas.swing.ButtonGradient();
        btnActualizar = new vistas.swing.ButtonGradient();
        btnEliminar = new vistas.swing.ButtonGradient();
        cmbTipoProducto = new vistas.componentes.ComboBox.ComboBox();
        txtTitulo = new vistas.componentes.TextFields.TextField();
        txtArtista = new vistas.componentes.TextFields.TextField();
        txtGenero = new vistas.componentes.TextFields.TextField();
        txtPrecio = new vistas.componentes.TextFields.TextField();
        txtUnidades = new vistas.componentes.TextFields.TextField();
        txtIdProducto = new javax.swing.JLabel();
        txtNumeroCanciones = new vistas.componentes.TextFields.TextField();
        txtDuracion = new vistas.componentes.TextFields.TextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCds = new vistas.swing.Table();
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

        cmbTipoProducto.setLabeText("Tipo Producto");
        cmbTipoProducto.setLineColor(new java.awt.Color(47, 24, 75));

        txtTitulo.setLabelText("Titulo del CD:");

        txtArtista.setLabelText("Artista:");

        txtGenero.setLabelText("Genero:");

        txtPrecio.setLabelText("Precio Unitario:");

        txtUnidades.setLabelText("Unidades disponibles:");

        txtIdProducto.setText("jLabel1");

        txtNumeroCanciones.setLabelText("Numero de canciones:");

        txtDuracion.setLabelText("Duracion:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtGenero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(txtArtista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtDuracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cmbTipoProducto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNumeroCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addComponent(txtIdProducto)
                        .addContainerGap(91, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdProducto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtArtista, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));

        tbCds.setModel(new javax.swing.table.DefaultTableModel(
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
        tbCds.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCdsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbCds);

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked
        agregarCD();
        limpiarCampos();
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        actualizarRevista();
        limpiarCampos();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro de que deseas eliminar esta revista?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarRevista();
            limpiarCampos();
        }
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void tbCdsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCdsMouseClicked
        cargarDatosFormulario();
    }//GEN-LAST:event_tbCdsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.swing.ButtonGradient btnActualizar;
    private vistas.swing.ButtonGradient btnAgregar;
    private vistas.swing.ButtonGradient btnEliminar;
    private vistas.swing.ButtonGradient btnLimpiar;
    private vistas.componentes.ComboBox.ComboBox cmbTipoProducto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private vistas.swing.Table tbCds;
    private vistas.componentes.TextFields.TextField txtArtista;
    private vistas.swing.SearchText txtBuscar;
    private vistas.componentes.TextFields.TextField txtDuracion;
    private vistas.componentes.TextFields.TextField txtGenero;
    private javax.swing.JLabel txtIdProducto;
    private vistas.componentes.TextFields.TextField txtNumeroCanciones;
    private vistas.componentes.TextFields.TextField txtPrecio;
    private vistas.componentes.TextFields.TextField txtTitulo;
    private vistas.componentes.TextFields.TextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
