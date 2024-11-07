
package vistas.secciones;

import controlador.revistaControlador;
import controlador.productosControlador;
import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


public class FrmGestionRevistas extends javax.swing.JPanel {

    // Definimos las variables genericas para manejo de datos de la tabla
    DefaultComboBoxModel<String> modelocombo = new DefaultComboBoxModel<>();
    private List myArrayList;
    revistaControlador objRevistaControlador = new revistaControlador();
    productosControlador objProductoControlador = new productosControlador();
    private DefaultTableModel modelRevistas;
    
    public FrmGestionRevistas() {
        initComponents();
        
        txtIdProducto.setVisible(false);
        // Headers con los que se carga la tabla
        String[] headers = {"ID","Codigo","Titulo","Editorial","Periodicidad","Fecha de publicacion","Precio","Unidades disponibles", "Tipo Producto"};
        modelRevistas = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbRevistas.setModel(modelRevistas);
        
        // Crear el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelRevistas);
        tbRevistas.setRowSorter(sorter);
        
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
        modelRevistas.setRowCount(0);
        try {
            ResultSet rs = objRevistaControlador.cargarTablaController();
            while(rs.next()){
                Object[] campos = {rs.getInt("id_producto"), rs.getString("codigo_identificacion_rev"), rs.getString("titulo"), rs.getString("editorial"), rs.getString("periodicidad"), rs.getString("fecha_publicacion"), rs.getFloat("precio"), rs.getInt("unidades_disponibles"), rs.getString("tipo_producto")};
                
                // Aqui ocultamos los datos que no queremos mostrar en la tabla
                tbRevistas.getColumnModel().getColumn(0).setMinWidth(0);
                tbRevistas.getColumnModel().getColumn(0).setMaxWidth(0);
                tbRevistas.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                tbRevistas.getColumnModel().getColumn(4).setMinWidth(0);
                tbRevistas.getColumnModel().getColumn(4).setMaxWidth(0);
                tbRevistas.getColumnModel().getColumn(4).setPreferredWidth(0);
                
                tbRevistas.getColumnModel().getColumn(7).setMinWidth(0);
                tbRevistas.getColumnModel().getColumn(7).setMaxWidth(0);
                tbRevistas.getColumnModel().getColumn(7).setPreferredWidth(0);
                
                tbRevistas.getColumnModel().getColumn(8).setMinWidth(0);
                tbRevistas.getColumnModel().getColumn(8).setMaxWidth(0);
                tbRevistas.getColumnModel().getColumn(8).setPreferredWidth(0);
                
                modelRevistas.addRow(campos);
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
        for(int i = 0; i < tbRevistas.getColumnCount(); i++) {
            tbRevistas.getColumnModel().getColumn(i).setCellRenderer(centrado);
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
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbRevistas.getRowSorter();
    
        if (busqueda.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
        }
    }
    
    // metodo para agregar revista
    void agregarRevista() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtPeriodicidad.getText().trim().isEmpty() || txtEditorial.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "" || dtFecha.getCalendar() == null) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            // Aqui obtenemos los datos de producto
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            String estado = "Disponible";
            
            // Y aqui obtenemos los datos de revista
            String editorial = txtEditorial.getText();
            String periodicidad = txtPeriodicidad.getText();
            Date fechaP = dtFecha.getDate();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatoFecha.format(fechaP);
            
            if(objRevistaControlador.validarRevistaExistenteController(editorial, periodicidad)) {
                JOptionPane.showMessageDialog(null, "Revista ya existe");
                return;
            }
            
            objRevistaControlador.agregarProductoYRevistaController(titulo, precio, unidades, id_tipoProducto, estado, editorial, periodicidad, fechaString);
            cargarTabla();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error" + e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Creamos un metodo para cargar los datos de la tabla al formulario
    void cargarDatosFormulario() {
        int filaSeleccionada = tbRevistas.getSelectedRow();
        if(filaSeleccionada != -1) {
            txtIdProducto.setText(modelRevistas.getValueAt(filaSeleccionada, 0).toString());
            txtTitulo.setText(modelRevistas.getValueAt(filaSeleccionada, 2).toString());
            txtEditorial.setText(modelRevistas.getValueAt(filaSeleccionada, 3).toString());
            txtPeriodicidad.setText(modelRevistas.getValueAt(filaSeleccionada, 4).toString());
            txtPrecio.setText(modelRevistas.getValueAt(filaSeleccionada, 6).toString());
            txtUnidades.setText(modelRevistas.getValueAt(filaSeleccionada, 7).toString());
            
            cmbTipoProducto.setSelectedItem(modelRevistas.getValueAt(filaSeleccionada, 8).toString());
            
            try {
                Date fechaP = new SimpleDateFormat("yyyy-MM-dd").parse(modelRevistas.getValueAt(filaSeleccionada, 5).toString());
                dtFecha.setDate(fechaP);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar la fecha de publicacion: " + e.toString());
            }
        }
    }
    
    // metodo para actualizar revista    
    void actualizarRevista() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtPeriodicidad.getText().trim().isEmpty() || txtEditorial.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "" || dtFecha.getCalendar() == null) {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            int id_producto = Integer.parseInt(txtIdProducto.getText());
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            
            String editorial = txtEditorial.getText();
            String periodicidad = txtPeriodicidad.getText();
            Date fechaP = dtFecha.getDate();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatoFecha.format(fechaP);
            
            objRevistaControlador.actualizarProductoYRevistaController(id_producto, titulo, precio, unidades, id_tipoProducto, editorial, periodicidad, fechaString);
            
            cargarTabla();
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: "+e.toString());
        }
    }
    
    // metodo para eliminar revista
    void eliminarRevista() {
        int filaSeleccionada = tbRevistas.getSelectedRow();
        if(filaSeleccionada != -1) {
            int idProducto = (int) modelRevistas.getValueAt(filaSeleccionada, 0);
            
            objRevistaControlador.eliminarProductoYRevistaController(idProducto);
            
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar");
        }
    }
    
    // Finalmente agregamos un metodo para limpiar los campos
    void limpiarCampos() {
        txtIdProducto.setText("");
        txtTitulo.setText("");
        txtPeriodicidad.setText("");
        txtEditorial.setText("");
        txtPrecio.setText("");
        txtUnidades.setText("");
        cmbTipoProducto.setSelectedItem("");
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
        cmbTipoProducto = new vistas.componentes.ComboBox.ComboBox();
        txtTitulo = new vistas.componentes.TextFields.TextField();
        txtPeriodicidad = new vistas.componentes.TextFields.TextField();
        txtEditorial = new vistas.componentes.TextFields.TextField();
        txtPrecio = new vistas.componentes.TextFields.TextField();
        txtUnidades = new vistas.componentes.TextFields.TextField();
        txtIdProducto = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbRevistas = new vistas.swing.Table();
        txtBuscar = new vistas.swing.SearchText();

        setBackground(new java.awt.Color(244, 239, 250));

        jPanel1.setBackground(new java.awt.Color(244, 239, 250));

        jLabel6.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(47, 24, 75));
        jLabel6.setText("Fecha publicacion:");

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

        txtTitulo.setLabelText("Titulo de la revista:");

        txtPeriodicidad.setLabelText("Periodicidad:");

        txtEditorial.setLabelText("Editorial:");

        txtPrecio.setLabelText("Precio Unitario:");

        txtUnidades.setLabelText("Unidades disponibles:");

        txtIdProducto.setText("jLabel1");

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
                            .addComponent(txtPeriodicidad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbTipoProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                            .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPeriodicidad, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbTipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(13, 13, 13)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(42, 42, 42)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));

        tbRevistas.setModel(new javax.swing.table.DefaultTableModel(
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
        tbRevistas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbRevistasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbRevistas);

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
                    .addComponent(txtBuscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        agregarRevista();
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

    private void tbRevistasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbRevistasMouseClicked
        cargarDatosFormulario();
    }//GEN-LAST:event_tbRevistasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.swing.ButtonGradient btnActualizar;
    private vistas.swing.ButtonGradient btnAgregar;
    private vistas.swing.ButtonGradient btnEliminar;
    private vistas.swing.ButtonGradient btnLimpiar;
    private vistas.componentes.ComboBox.ComboBox cmbTipoProducto;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private vistas.swing.Table tbRevistas;
    private vistas.swing.SearchText txtBuscar;
    private vistas.componentes.TextFields.TextField txtEditorial;
    private javax.swing.JLabel txtIdProducto;
    private vistas.componentes.TextFields.TextField txtPeriodicidad;
    private vistas.componentes.TextFields.TextField txtPrecio;
    private vistas.componentes.TextFields.TextField txtTitulo;
    private vistas.componentes.TextFields.TextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
