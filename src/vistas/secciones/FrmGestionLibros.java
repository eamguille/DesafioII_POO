
package vistas.secciones;

import controlador.libroControlador;
import controlador.productosControlador;
import java.awt.HeadlessException;
import javax.swing.DefaultComboBoxModel;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class FrmGestionLibros extends javax.swing.JPanel {

    // Definimos las variables genericas para manejo de datos de la tabla
    DefaultComboBoxModel<String> modelocombo = new DefaultComboBoxModel<>();
    private List myArrayList;
    libroControlador libroControlador = new libroControlador();
    productosControlador productoControlador = new productosControlador();
    private DefaultTableModel modelLibros;

    
    public FrmGestionLibros() {
        initComponents();
        
        txtIdProducto.setVisible(false);
        // Headers con los que se carga la tabla
        String[] headers = {"ID","Codigo","Titulo","Autor","Editorial","Numero de Paginas","ISBN","Precio","Fecha de Publicacion", "Unidades disponibles", "Tipo Producto"};
        modelLibros = new DefaultTableModel(null, headers) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbLibros.setModel(modelLibros);
        
        // Crear el TableRowSorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelLibros);
        tbLibros.setRowSorter(sorter);
        
        // Llamamos el metodo cargarTabla al constructor
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
        
        // Mandamos a llamar el metodo el comboBox
        cargarCmbTipoProducto();
    }
    
    // Creamos el metodo que se encargara de cargar los datos en la tabla
    void cargarTabla() {
        modelLibros.setRowCount(0);
        try {
            ResultSet rs = libroControlador.obtenerDatosController();
            while(rs.next()){
                Object[] campos = {rs.getInt("id_producto"), rs.getString("codigo_identificacion_lib"), rs.getString("titulo"), rs.getString("autor_libro"), rs.getString("editorial_libro"), rs.getInt("numero_paginas"), rs.getString("ISBN"), rs.getFloat("precio"), rs.getString("fecha_publicacion"), rs.getInt("unidades_disponibles"), rs.getString("tipo_producto")};
                
                // Aqui ocultamos los datos que no queremos mostrar en la tabla
                tbLibros.getColumnModel().getColumn(0).setMinWidth(0);
                tbLibros.getColumnModel().getColumn(0).setMaxWidth(0);
                tbLibros.getColumnModel().getColumn(0).setPreferredWidth(0);
                
                tbLibros.getColumnModel().getColumn(8).setMinWidth(0);
                tbLibros.getColumnModel().getColumn(8).setMaxWidth(0);
                tbLibros.getColumnModel().getColumn(8).setPreferredWidth(0);
                
                tbLibros.getColumnModel().getColumn(9).setMinWidth(0);
                tbLibros.getColumnModel().getColumn(9).setMaxWidth(0);
                tbLibros.getColumnModel().getColumn(9).setPreferredWidth(0);
                
                tbLibros.getColumnModel().getColumn(10).setMinWidth(0);
                tbLibros.getColumnModel().getColumn(10).setMaxWidth(0);
                tbLibros.getColumnModel().getColumn(10).setPreferredWidth(0);
                
                modelLibros.addRow(campos);
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
        for(int i = 0; i < tbLibros.getColumnCount(); i++) {
            tbLibros.getColumnModel().getColumn(i).setCellRenderer(centrado);
        }
    }

    // creamos un metodo para cargar los datos en el comboBox desde el controlador
    final void cargarCmbTipoProducto() {
        myArrayList = new ArrayList();
        try {
            ResultSet rs = productoControlador.cargarListaTipoProductos();
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
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) tbLibros.getRowSorter();
    
        if (busqueda.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + busqueda));
        }
    }
    
    
    // Creamos el metodo para ingrear los datos
    void agregarLibro() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtAutor.getText().trim().isEmpty() || txtEditorial.getText().trim().isEmpty() || txtNumPags.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "") {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            // Aqui obtenemos los datos de producto
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            String estado = "Disponible";
            
            // Y aqui obtenemos los datos de libro
            String autor = txtAutor.getText();
            int numeroPags = Integer.parseInt(txtNumPags.getText());
            String editorial = txtEditorial.getText();
            String ISBN = txtIsbn.getText();
            
            if(libroControlador.validarLibroExisteController(ISBN)) {
                JOptionPane.showMessageDialog(null, "El libro ya existe");
                return;
            }
            
            Date fechaP = dtFecha.getDate();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatoFecha.format(fechaP);
            
            libroControlador.insertarProductoYLibroController(titulo, precio, unidades, id_tipoProducto, estado, autor, numeroPags, editorial, ISBN, fechaString);
            cargarTabla();
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error" + e.toString(), "Proceso incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Creamos un metodo para cargar los datos de la tabla al formulario
    void cargarDatosFormulario() {
        int filaSeleccionada = tbLibros.getSelectedRow();
        if(filaSeleccionada != -1) {
            txtIdProducto.setText(modelLibros.getValueAt(filaSeleccionada, 0).toString());
            txtTitulo.setText(modelLibros.getValueAt(filaSeleccionada, 2).toString());
            txtAutor.setText(modelLibros.getValueAt(filaSeleccionada, 3).toString());
            txtEditorial.setText(modelLibros.getValueAt(filaSeleccionada, 4).toString());
            txtNumPags.setText(modelLibros.getValueAt(filaSeleccionada, 5).toString());
            txtIsbn.setText(modelLibros.getValueAt(filaSeleccionada, 6).toString());
            txtPrecio.setText(modelLibros.getValueAt(filaSeleccionada, 7).toString());
            txtUnidades.setText(modelLibros.getValueAt(filaSeleccionada, 9).toString());
            
            cmbTipoProducto.setSelectedItem(modelLibros.getValueAt(filaSeleccionada, 10).toString());
            
            try {
                Date fechaP = new SimpleDateFormat("yyyy-MM-dd").parse(modelLibros.getValueAt(filaSeleccionada, 8).toString());
                dtFecha.setDate(fechaP);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar la fecha de publicacion: " + e.toString());
            }
        }
    }
    
    // Creamos metodo para actualizar el libro
    void actualizarLibro() {
        try {
            if(txtTitulo.getText().trim().isEmpty() || txtAutor.getText().trim().isEmpty() || txtEditorial.getText().trim().isEmpty() || txtNumPags.getText().trim().isEmpty() || txtIsbn.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty() || txtUnidades.getText().trim().isEmpty() || cmbTipoProducto.getSelectedItem() == "") {
            JOptionPane.showConfirmDialog(null, "Existen campos vacios");
        } else {
            int id_producto = Integer.parseInt(txtIdProducto.getText());
            String titulo = txtTitulo.getText();
            float precio = Float.parseFloat(txtPrecio.getText());
            int unidades = Integer.parseInt(txtUnidades.getText());
            int id_tipoProducto = cmbTipoProducto.getSelectedIndex();
            
            String autor = txtAutor.getText();
            int numeroPags = Integer.parseInt(txtNumPags.getText());
            String editorial = txtEditorial.getText();
            String ISBN = txtIsbn.getText();
            Date fechaP = dtFecha.getDate();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            String fechaString = formatoFecha.format(fechaP);
            
            libroControlador.actualizarProductoYLibroController(id_producto, titulo, precio, unidades, id_tipoProducto, autor, numeroPags, editorial, ISBN, fechaString);
            
            cargarTabla();
            
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar: "+e.toString());
        }
    }
   
    // Creamos metodo para eliminar el libro
    void eliminarLibro() {
        int filaSeleccionada = tbLibros.getSelectedRow();
        if(filaSeleccionada != -1) {
            int idProducto = (int) modelLibros.getValueAt(filaSeleccionada, 0);
            
            libroControlador.eliminarProductoYLibroController(idProducto);
            
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecciona una fila para eliminar");
        }
    }
    
    // Finalmente agregamos un metodo para limpiar los campos
    void limpiarCampos() {
        txtIdProducto.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtNumPags.setText("");
        txtIsbn.setText("");
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
        dtFecha = new com.toedter.calendar.JDateChooser();
        cmbTipoProducto = new vistas.componentes.ComboBox.ComboBox();
        txtTitulo = new vistas.componentes.TextFields.TextField();
        txtAutor = new vistas.componentes.TextFields.TextField();
        txtEditorial = new vistas.componentes.TextFields.TextField();
        txtNumPags = new vistas.componentes.TextFields.TextField();
        txtIsbn = new vistas.componentes.TextFields.TextField();
        txtPrecio = new vistas.componentes.TextFields.TextField();
        txtUnidades = new vistas.componentes.TextFields.TextField();
        txtIdProducto = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbLibros = new vistas.swing.Table();
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

        dtFecha.setBackground(new java.awt.Color(244, 239, 250));
        dtFecha.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(47, 24, 75)));
        dtFecha.setForeground(new java.awt.Color(47, 24, 75));
        dtFecha.setDateFormatString("yyyy-MM-dd");

        cmbTipoProducto.setLabeText("Tipo Producto");
        cmbTipoProducto.setLineColor(new java.awt.Color(47, 24, 75));

        txtTitulo.setLabelText("Titulo del libro:");

        txtAutor.setLabelText("Autor del libro:");

        txtEditorial.setLabelText("Editorial:");

        txtNumPags.setLabelText("Numero de paginas:");

        txtIsbn.setLabelText("ISBN:");

        txtPrecio.setLabelText("Precio Unitario:");

        txtUnidades.setLabelText("Unidades disponibles:");

        txtIdProducto.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAutor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEditorial, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6)
                    .addComponent(dtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(txtNumPags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtIsbn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbTipoProducto, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(txtUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGap(86, 86, 86)
                        .addComponent(txtIdProducto)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                                .addComponent(txtNumPags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAutor, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(txtIsbn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtUnidades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cmbTipoProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(7, 7, 7)
                            .addComponent(dtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txtEditorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtIdProducto)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel2.setBackground(new java.awt.Color(244, 239, 250));

        tbLibros.setModel(new javax.swing.table.DefaultTableModel(
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
        tbLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbLibrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbLibros);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 790, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAgregarMouseClicked
        agregarLibro();
        limpiarCampos();
    }//GEN-LAST:event_btnAgregarMouseClicked

    private void tbLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbLibrosMouseClicked
        cargarDatosFormulario();
    }//GEN-LAST:event_tbLibrosMouseClicked

    private void btnActualizarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnActualizarMouseClicked
        actualizarLibro();
        limpiarCampos();
    }//GEN-LAST:event_btnActualizarMouseClicked

    private void btnEliminarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEliminarMouseClicked
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Seguro de que deseas eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarLibro();
            limpiarCampos();
        }
    }//GEN-LAST:event_btnEliminarMouseClicked

    private void btnLimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLimpiarMouseClicked
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private vistas.swing.ButtonGradient btnActualizar;
    private vistas.swing.ButtonGradient btnAgregar;
    private vistas.swing.ButtonGradient btnEliminar;
    private vistas.swing.ButtonGradient btnLimpiar;
    private vistas.componentes.ComboBox.ComboBox cmbTipoProducto;
    private com.toedter.calendar.JDateChooser dtFecha;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private vistas.swing.Table tbLibros;
    private vistas.componentes.TextFields.TextField txtAutor;
    private vistas.swing.SearchText txtBuscar;
    private vistas.componentes.TextFields.TextField txtEditorial;
    private javax.swing.JLabel txtIdProducto;
    private vistas.componentes.TextFields.TextField txtIsbn;
    private vistas.componentes.TextFields.TextField txtNumPags;
    private vistas.componentes.TextFields.TextField txtPrecio;
    private vistas.componentes.TextFields.TextField txtTitulo;
    private vistas.componentes.TextFields.TextField txtUnidades;
    // End of variables declaration//GEN-END:variables
}
