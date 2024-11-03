
package vistas.secciones;

import controlador.dvdControlador;
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

public class FrmGestionDVDs extends javax.swing.JPanel {

    // Definimos las variables genericas para manejo de datos de la tabla
    DefaultComboBoxModel<String> modelocombo = new DefaultComboBoxModel<>();
    private List myArrayList;
    dvdControlador objDvdControlador = new dvdControlador();
    productosControlador objProductoControlador = new productosControlador();
    private DefaultTableModel modelDvd;
    
    public FrmGestionDVDs() {
        initComponents();
        
        txtIdProducto.setVisible(false);
        // Headers con los que se carga la tabla
        String[] headers = {"ID","Codigo","Titulo","Director","Duracion","Genero","Precio","Unidades disponibles", "Tipo Producto"};
        modelDvd = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbDVDs.setModel(modelDvd);
        
        // Crear el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelDvd);
        tbDVDs.setRowSorter(sorter);
        
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
        modelDvd.setRowCount(0);
        try {
            ResultSet rs = objDvdControlador.cargarTablaController();
            while(rs.next()){
                Object[] campos = {rs.getInt("id_producto"), rs.getString("codigo_identificacion_dvd"), rs.getString("titulo"), rs.getString("director_dvd"), rs.getString("duracion_dvd"), rs.getString("genero_dvd"), rs.getFloat("precio"), rs.getInt("unidades_disponibles"), rs.getString("tipo_producto")};
                
                // Aqui ocultamos los datos que no queremos mostrar en la tabla
                tbDVDs.getColumnModel().getColumn(0).setMinWidth(0);
                tbDVDs.getColumnModel().getColumn(0).setMaxWidth(0);
                tbDVDs.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                tbDVDs.getColumnModel().getColumn(7).setMinWidth(0);
                tbDVDs.getColumnModel().getColumn(7).setMaxWidth(0);
                tbDVDs.getColumnModel().getColumn(7).setPreferredWidth(0);
                
                tbDVDs.getColumnModel().getColumn(8).setMinWidth(0);
                tbDVDs.getColumnModel().getColumn(8).setMaxWidth(0);
                tbDVDs.getColumnModel().getColumn(8).setPreferredWidth(0);
                
                modelDvd.addRow(campos);
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
        for(int i = 0; i < tbDVDs.getColumnCount(); i++) {
            tbDVDs.getColumnModel().getColumn(i).setCellRenderer(centrado);
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
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbDVDs.getRowSorter();
    
        if (busqueda.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
        }
    }
    
    // metodo para agregar revista
    void agregarDVD() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtDirector.getText().trim().isEmpty() || txtGenero.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "" || txtDuracion.getText().trim().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            // Aqui obtenemos los datos de producto
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            String estado = "Disponible";
                        
            // Y aqui obtenemos los datos de revista
            String director = txtDirector.getText();
            String duracion = txtDuracion.getText();
            String genero = txtGenero.getText();
            
            if(objDvdControlador.validarDVDExistenteController(director, duracion, genero)){
                JOptionPane.showMessageDialog(null, "DVD ya existe");
                return;
            }
            
            objDvdControlador.agregarProductoYDVDController(titulo, precio, unidades, id_tipoProducto, estado, director, duracion, genero);
            cargarTabla();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error" + e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Creamos un metodo para cargar los datos de la tabla al formulario
    void cargarDatosFormulario() {
        int filaSeleccionada = tbDVDs.getSelectedRow();
        if(filaSeleccionada != -1) {
            txtIdProducto.setText(modelDvd.getValueAt(filaSeleccionada, 0).toString());
            txtTitulo.setText(modelDvd.getValueAt(filaSeleccionada, 2).toString());
            txtDirector.setText(modelDvd.getValueAt(filaSeleccionada, 3).toString());
            txtDuracion.setText(modelDvd.getValueAt(filaSeleccionada, 4).toString());
            txtGenero.setText(modelDvd.getValueAt(filaSeleccionada, 5).toString());
            txtPrecio.setText(modelDvd.getValueAt(filaSeleccionada, 6).toString());
            txtUnidades.setText(modelDvd.getValueAt(filaSeleccionada, 7).toString());
            cmbTipoProducto.setSelectedItem(modelDvd.getValueAt(filaSeleccionada, 8).toString());
        }
    }
    
    // metodo para actualizar revista    
    void actualizarDVD() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtDirector.getText().trim().isEmpty() || txtGenero.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "" || txtDuracion.getText().trim().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            int id_producto = Integer.parseInt(txtIdProducto.getText());
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            
            String director = txtDirector.getText();
            String duracion = txtDuracion.getText();
            String genero = txtGenero.getText();
            
            objDvdControlador.actualizarProductoYDVDController(id_producto, titulo, precio, unidades, id_tipoProducto, director, duracion, genero);
            cargarTabla();
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: "+e.toString());
        }
    }
    
    // metodo para eliminar revista
    void eliminarDVD() {
        int filaSeleccionada = tbDVDs.getSelectedRow();
        if(filaSeleccionada != -1) {
            int idProducto = (int) modelDvd.getValueAt(filaSeleccionada, 0);
            
            objDvdControlador.eliminarProductoYDVDController(idProducto);
            
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar");
        }
    }
    
    // Finalmente agregamos un metodo para limpiar los campos
    void limpiarCampos() {
        txtIdProducto.setText("");
        txtTitulo.setText("");
        txtDirector.setText("");
        txtDuracion.setText("");
        txtGenero.setText("");
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
        txtDirector = new vistas.componentes.TextFields.TextField();
        txtGenero = new vistas.componentes.TextFields.TextField();
        txtPrecio = new vistas.componentes.TextFields.TextField();
        txtUnidades = new vistas.componentes.TextFields.TextField();
        txtIdProducto = new javax.swing.JLabel();
        txtDuracion = new vistas.componentes.TextFields.TextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDVDs = new vistas.swing.Table();
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

        txtTitulo.setLabelText("Titulo del DVD:");

        txtDirector.setLabelText("Director:");

        txtGenero.setLabelText("Genero:");

        txtPrecio.setLabelText("Precio Unitario:");

        txtUnidades.setLabelText("Unidades disponibles:");

        txtIdProducto.setText("jLabel1");

        txtDuracion.setLabelText("Duracion:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                            .addComponent(txtDirector, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtDuracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUnidades, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                            .addComponent(txtDirector, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));

        tbDVDs.setModel(new javax.swing.table.DefaultTableModel(
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
        tbDVDs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDVDsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbDVDs);

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
        agregarDVD();
        limpiarCampos();
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        actualizarDVD();
        limpiarCampos();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro de que deseas eliminar esta revista?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarDVD();
            limpiarCampos();
        }
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void tbDVDsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDVDsMouseClicked
        cargarDatosFormulario();
    }//GEN-LAST:event_tbDVDsMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.swing.ButtonGradient btnActualizar;
    private vistas.swing.ButtonGradient btnAgregar;
    private vistas.swing.ButtonGradient btnEliminar;
    private vistas.swing.ButtonGradient btnLimpiar;
    private vistas.componentes.ComboBox.ComboBox cmbTipoProducto;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private vistas.swing.Table tbDVDs;
    private vistas.swing.SearchText txtBuscar;
    private vistas.componentes.TextFields.TextField txtDirector;
    private vistas.componentes.TextFields.TextField txtDuracion;
    private vistas.componentes.TextFields.TextField txtGenero;
    private javax.swing.JLabel txtIdProducto;
    private vistas.componentes.TextFields.TextField txtPrecio;
    private vistas.componentes.TextFields.TextField txtTitulo;
    private vistas.componentes.TextFields.TextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
