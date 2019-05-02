/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viajes.interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Estudiante
 */
public class Autos extends javax.swing.JFrame {

    /**
     * Creates new form Autos
     */
    public Autos() {
        initComponents();
        bloquearBotones();
        bloquearCampos();
        valorSpinnerAño();
        cargarMarca();
        cargarModeloSQL();
        cargarTablaAutos("");
        //Interfaz conjunto de metodos abstractos
        Modificar();

    }

    public void Modificar() {
        tblAuto.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                desbloquearBotonModificar();
                desbloquearCampos();
                if (tblAuto.getSelectedRow() != -1) {
                    Integer fila = tblAuto.getSelectedRow();
                    txtAutPlaca.setText(tblAuto.getValueAt(fila, 0).toString().trim()); //.trim elimina espacios en blanco
                    cbxAutMarca.setSelectedItem(idMarca(tblAuto.getValueAt(fila, 1).toString().trim()) + " " + tblAuto.getValueAt(fila, 1).toString().trim());
                    cbxAutModelo.setSelectedItem(idModelo(tblAuto.getValueAt(fila, 2).toString().trim()) + " " + tblAuto.getValueAt(fila, 2).toString().trim());
                    spnAutAnio.setValue(Integer.valueOf(tblAuto.getValueAt(fila, 3).toString().trim()));
                    cbxAutColor.setSelectedItem(tblAuto.getValueAt(fila, 4).toString().trim());
                    spnAutCapacidad.setValue(Integer.valueOf(tblAuto.getValueAt(fila, 5).toString().trim()));
                    txtAutObservacion.setText(tblAuto.getValueAt(fila, 6).toString().trim());

                }
            }
        });
    }

    public void modificarAuto() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conectar();
            String sql = "";
            sql = "update autos "
                    + "set MOD_CODIGO = '" + cbxAutModelo.getSelectedItem().toString().substring(0, 1).trim()
                    + "', AUT_ANIO = '" + spnAutAnio.getValue() + "',"
                    + " AUT_COLOR = '" + cbxAutColor.getSelectedItem().toString()
                    + "', AUT_CAPACIDAD = '" + spnAutCapacidad.getValue() + "', "
                    + " AUT_OBSERVACION = '" + txtAutObservacion.getText().trim() + "' "
                    + "WHERE AUT_PLACA = '" + txtAutPlaca.getText() + "'";
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Se actualizo correctamente");
                bloquearBotones();
                bloquearCampos();
                cargarTablaAutos("");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Autos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eliminarAutodelaBasedeDatos() {
        if (JOptionPane.showConfirmDialog(new JInternalFrame(), "¿Estas Seguro de Borrar el Registro?",
                "Borrar Registro", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {

        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "DELETE FROM AUTOS WHERE AUT_PLACA = '" + txtAutPlaca.getText() + "'";

                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Auto eliminado Correctamente");
                    bloquearBotones();
                    bloquearCampos();
                    cargarTablaAutos("");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Autos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void eliminarAuto() { //El estado del auto se hace 0 osea inactivo
        if (JOptionPane.showConfirmDialog(new JInternalFrame(), "¿Estas Seguro de Borrar el Registro?",
                "Borrar Registro", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {

        } else {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conectar();
                String sql = "";
                sql = "UPDATE AUTOS SET AUT_ESTADO = '0' WHERE AUT_PLACA = '" + txtAutPlaca.getText() + "'";

                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Auto eliminado Correctamente");
                    bloquearBotones();
                    bloquearCampos();
                    cargarTablaAutos("");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Autos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void bloquearCampos() {
        txtAutPlaca.setEnabled(false);
        cbxAutMarca.setEnabled(false);
        cbxAutModelo.setEnabled(false);
        spnAutAnio.setEnabled(false);
        cbxAutColor.setEnabled(false);
        spnAutCapacidad.setEnabled(false);
        txtAutObservacion.setEnabled(false);
    }

    public void desbloquearCampos() {
        txtAutPlaca.setEnabled(true);
        cbxAutMarca.setEnabled(true);
        cbxAutModelo.setEnabled(true);
        spnAutAnio.setEnabled(true);
        cbxAutColor.setEnabled(true);
        spnAutCapacidad.setEnabled(true);
        txtAutObservacion.setEnabled(true);
    }

    public void bloquearBotones() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnSalir.setEnabled(true);
    }

    public void desbloquearBotonModificar() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
    }

    public void desbloquearBotonCancelar() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
    }

    public void valorSpinnerAño() {
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel();
        modeloSpinner.setMaximum(2020);
        modeloSpinner.setMinimum(0);
        spnAutAnio.setModel(modeloSpinner);
    }

    public void desbloquearBotonGuardar() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
    }

    public void desbloquearBotonGuardar1() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnSalir.setEnabled(false);
        if (txtAutPlaca.getText() == null) {
            desbloquearBotonCancelar();
        } else if (cbxAutModelo.getSelectedItem() == null) {
            desbloquearBotonCancelar();
        } else if (cbxAutMarca.getSelectedItem() == null) {
            desbloquearBotonCancelar();
        } else if (spnAutAnio.getValue() == null) {
            desbloquearBotonCancelar();
        } else {
        }
    }

    public void limpiarTexto() {
        txtAutPlaca.setText("");
        cbxAutMarca.setSelectedItem("Seleccione...");
        cbxAutModelo.removeAllItems();
        cbxAutModelo.setSelectedItem("Seleccione...");
        spnAutAnio.setValue(0);
        cbxAutColor.setSelectedItem(0);
        spnAutCapacidad.setValue(0);
        txtAutObservacion.setText("");
    }

    //Coneccion A LA BASE DE DATOS
    Conexion cc = new Conexion();
    Connection cn = cc.conectar();

    public void guardarAuto() {
        if (txtAutPlaca.getText().isEmpty()) { // MENSAje de ERROR EN CASO QUE LA PLACA NO SE LLENADO
            JOptionPane.showMessageDialog(this, "Ingrese Placa");
            txtAutPlaca.requestFocus();
        } else if (cbxAutMarca.getSelectedItem().equals("Seleccione...")) { // MENSAje de ERROR EN CASO QUE LA PLACA NO SE LLENADO
            JOptionPane.showMessageDialog(this, "Seleccione Marca");
            cbxAutMarca.requestFocus();
        } else if (cbxAutModelo.getSelectedItem().equals("Seleccione...")) { // MENSAje de ERROR EN CASO QUE LA PLACA NO SE LLENADO
            JOptionPane.showMessageDialog(this, "Seleccione Marca");
            cbxAutModelo.requestFocus();
        } else if (Integer.valueOf(spnAutAnio.getValue().toString()) < 1980 || Integer.valueOf(spnAutAnio.getValue().toString()) > 2019) {
            JOptionPane.showMessageDialog(this, "Ingrese Año Válido");
            spnAutAnio.requestFocus();
        } else if (Integer.valueOf(spnAutCapacidad.getValue().toString()) <= 0 || Integer.valueOf(spnAutCapacidad.getValue().toString()) > 20) {
            JOptionPane.showMessageDialog(this, "Ingrese la capacidad Válida");
            spnAutCapacidad.requestFocus();
        } else if (cbxAutColor.getSelectedItem().equals("Seleccione...")) { // MENSAje de ERROR EN CASO QUE LA PLACA NO SE LLENADO
            JOptionPane.showMessageDialog(this, "Seleccione Color");
            cbxAutColor.requestFocus();
        } else {

            try {
                String AUT_PLACA = "", MOD_CODIGO, AUT_COLOR, AUT_OBSERVACION;
                Integer AUT_ANIO, AUT_CAPACIDAD;
                String sql = "";
                try {
                    AUT_PLACA = txtAutPlaca.getText(0, 3) + txtAutPlaca.getText(4, 4);//Placa de un formatedtextfield;
                } catch (BadLocationException ex) {
                    Logger.getLogger(Autos.class.getName()).log(Level.SEVERE, null, ex);
                }
                MOD_CODIGO = cbxAutModelo.getSelectedItem().toString().substring(0, 1);
                AUT_ANIO = Integer.valueOf(spnAutAnio.getValue().toString());
                AUT_COLOR = cbxAutColor.getSelectedItem().toString();
                AUT_CAPACIDAD = Integer.valueOf(spnAutCapacidad.getValue().toString());
                AUT_OBSERVACION = txtAutObservacion.getText();
                sql = "INSERT INTO autos(AUT_PLACA,MOD_CODIGO,AUT_ANIO,AUT_COLOR,AUT_CAPACIDAD,AUT_OBSERVACION,AUT_ESTADO)"
                        + "VALUES(?,?,?,?,?,?,?)";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, AUT_PLACA);
                psd.setString(2, MOD_CODIGO);
                psd.setInt(3, AUT_ANIO);
                psd.setString(4, AUT_COLOR);
                psd.setInt(5, AUT_CAPACIDAD);
                if (txtAutObservacion.getText().isEmpty()) {
                    String obsVacia = "Sin Observaciones";
                    psd.setString(6, obsVacia);

                } else {

                    psd.setString(6, AUT_OBSERVACION);
                }
                char AUT_ESTADO = '1';
                psd.setString(7, String.valueOf(AUT_ESTADO));
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Ok, Insercion Correcta");
                    bloquearCampos();
                    bloquearBotones();
                    limpiarTexto();
                    cargarTablaAutos("");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Insercion Incorrecta");
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public String idMarca(String nombre) {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql, marcaCodigo = "";
        sql = "SELECT MAR_CODIGO FROM MARCAS WHERE MAR_NOMBRE = '" + nombre + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {

                marcaCodigo = rs.getString("MAR_CODIGO");

            }
        } catch (SQLException ex) {
            Logger.getLogger(Autos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return marcaCodigo;
    }

    public String idModelo(String nombre) {
        Conexion cc = new Conexion();
        Connection cn = cc.conectar();
        String sql, modeloCodigo = "";
        sql = "SELECT MOD_CODIGO FROM MODELOS WHERE MOD_NOMBRE = '" + nombre + "'";
        try {
            Statement psd = (Statement) cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {

                modeloCodigo = rs.getString("MOD_CODIGO");

            }
        } catch (SQLException ex) {
            Logger.getLogger(Autos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modeloCodigo;
    }
    /////////////////// CARGAR VALORES A LA TABLA ///////////////////////////////////////////////////////
    DefaultTableModel modelo;

    public void cargarTablaAutos(String dato) {
        String[] titulos = {"PLACA", "MARCA", "MODELO", "AÑO", "COLOR", "CAPACIDAD", "OBSERVACION"};
        String[] registros = new String[7];
        modelo = new DefaultTableModel(null, titulos);
        try {
            String sql = "";
            sql = "SELECT autos.AUT_PLACA,autos.MOD_CODIGO, autos.AUT_ANIO, autos.AUT_COLOR, autos.AUT_CAPACIDAD, autos.AUT_OBSERVACION,"
                    + " modelos.MOD_NOMBRE, marcas.MAR_NOMBRE "
                    + "FROM autos, modelos, marcas "
                    + "WHERE autos.MOD_CODIGO = modelos.MOD_CODIGO and modelos.MAR_CODIGO = marcas.MAR_CODIGO and "
                    + "autos.AUT_PLACA LIKE'%" + dato + "%' "
                    + "AND AUT_ESTADO = '1'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
//            String sql1 = "";
//            sql1 =  "SELECT (SUBSTRING(autos.AUT_PLACA,1,3) + '-' + SUBSTRING(autos.AUT_PLACA,4,7) "
//                    + "FROM autos "
//                    + "WHERE autos.AUT_ESTADO = '1' AND "
//                    + "autos.AUT_PLACA LIKE '%" + dato + "%' "
//                    + "ORDER BY autos.AUT_PLACA "
//                    + "FOR XML PATH('') ) AS PLACA";
//            Statement psd1 = cn.createStatement();
//            ResultSet rs1 = psd1.executeQuery(sql1);
            while (rs.next()) {
                registros[0] = rs.getString("AUT_PLACA");
                registros[1] = rs.getString("MAR_NOMBRE");
                registros[2] = rs.getString("MOD_NOMBRE");
                registros[3] = rs.getString("AUT_ANIO");
                registros[4] = rs.getString("AUT_COLOR");
                registros[5] = rs.getString("AUT_CAPACIDAD");
                registros[6] = rs.getString("AUT_OBSERVACION");
                modelo.addRow(registros);
            }
            tblAuto.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////// 
    //Conectarse a la base de datos
    String id;
    String marca;

    //Conectarse a la base de datos   
    ArrayList listaModelo = new ArrayList();
    ArrayList listaMarca = new ArrayList();

    public void cargarMarca() {
        try {
            String sql = "";
            sql = "select * from marcas";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                String idMarca = rs.getString("MAR_NOMBRE");
                String marcacodigo = rs.getString("MAR_CODIGO");
                listaMarca.add(rs.getString("MAR_CODIGO"));
                cbxAutMarca.addItem(marcacodigo + " " + idMarca);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void cargarModelo() {
        try {
            String sql = "";
            sql = "select * from modelos";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                listaModelo.add(rs.getString("MOD_CODIGO"));
                String idMarca = rs.getString("MAR_CODIGO");
                String nomMod = rs.getString("MOD_NOMBRE");
                int num = cbxAutMarca.getSelectedIndex() - 1;
                if (num != 1) {
                    if (idMarca.equals(listaMarca.get(num).toString())) {
                        cbxAutModelo.addItem(rs.getString("MOD_CODIGO") + " " + nomMod);
                    }
                } else {
                    cbxAutModelo.setEnabled(true);
                }
                //listaMarca.set(num, rs.getString(1));
//                if (cbxAutMarca.getSelectedItem().equals("Seleccione..")) {
//                    cbxAutModelo.setSelectedItem("Seleccione..");
//                } else {
//                   
//                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public void cargarModeloSQL() { //Otra forma de cargar el modelo
        //cbxAutModelo.removeAllItems();
        try {
            String sql = "";
            Integer item = cbxAutMarca.getSelectedIndex();
            sql = "select * from modelos where MAR_CODIGO = " + "'" + item + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) { //next recorre todo hasta encontrar el valor nulo

                String nomMod = rs.getString("MOD_NOMBRE");
                cbxAutModelo.addItem(rs.getString("MOD_CODIGO") + " " + nomMod);
                // listaModelo.add(rs.getString("MOD_CODIGO"));
                // String idMarca = rs.getString("MAR_CODIGO");
                //String nomMod = rs.getString("MAR_NOMBRE");
                //int num = cbxAutMarca.getSelectedIndex()-1;
            }

            if (cbxAutMarca.getSelectedItem().equals("Seleccione...")) {
                cbxAutModelo.setSelectedItem("Seleccione...");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbxAutMarca = new javax.swing.JComboBox<>();
        cbxAutModelo = new javax.swing.JComboBox<>();
        spnAutAnio = new javax.swing.JSpinner();
        cbxAutColor = new javax.swing.JComboBox<>();
        spnAutCapacidad = new javax.swing.JSpinner();
        txtAutObservacion = new javax.swing.JTextField();
        txtAutPlaca = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAuto = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtBuscarxPlaca = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setText("PLACA:");

        jLabel2.setText("MARCA:");

        jLabel3.setText("MODELO:");

        jLabel4.setText("AÑO:");

        jLabel6.setText("OBSERVACIÓN:");

        jLabel7.setText("CAPACIDAD:");

        jLabel8.setText("COLOR:");

        cbxAutMarca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione..." }));
        cbxAutMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAutMarcaActionPerformed(evt);
            }
        });

        cbxAutModelo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione..." }));
        cbxAutModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAutModeloActionPerformed(evt);
            }
        });

        cbxAutColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione...", "Blanco", "Negro", "Gris", "Plateado", "Rojo", "Azul ", "Brown/Beige", "Amarillo ", "Dorado", "Verde " }));

        try {
            txtAutPlaca.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("UUU-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtAutObservacion)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxAutMarca, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxAutModelo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spnAutAnio)
                            .addComponent(cbxAutColor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(spnAutCapacidad)
                            .addComponent(txtAutPlaca))
                        .addGap(0, 10, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtAutPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbxAutMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxAutModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(spnAutAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbxAutColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(spnAutCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(txtAutObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnModificar)
                .addGap(18, 18, 18)
                .addComponent(btnEliminar)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addGap(18, 18, 18)
                .addComponent(btnSalir)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        tblAuto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblAuto);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setText("Buscar: ");

        txtBuscarxPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarxPlacaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtBuscarxPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtBuscarxPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 241, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(307, 307, 307)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardarAuto();
            //cargarTablaAutos("");
            

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        bloquearCampos();
        bloquearBotones();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        limpiarTexto();
        desbloquearCampos();
        desbloquearBotonCancelar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void cbxAutModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxAutModeloActionPerformed
        // TODO add your handling code here:      
    }//GEN-LAST:event_cbxAutModeloActionPerformed

    private void cbxAutMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxAutMarcaActionPerformed
        // TODO add your handling code here:
        cbxAutModelo.removeAllItems();
        cargarModeloSQL();
    }//GEN-LAST:event_cbxAutMarcaActionPerformed

    private void txtBuscarxPlacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarxPlacaKeyReleased
        // TODO add your handling code here:
        cargarTablaAutos(txtBuscarxPlaca.getText());
    }//GEN-LAST:event_txtBuscarxPlacaKeyReleased

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        modificarAuto();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarAuto();
        limpiarTexto();
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Autos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Autos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxAutColor;
    private javax.swing.JComboBox<String> cbxAutMarca;
    private javax.swing.JComboBox<String> cbxAutModelo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner spnAutAnio;
    private javax.swing.JSpinner spnAutCapacidad;
    private javax.swing.JTable tblAuto;
    private javax.swing.JTextField txtAutObservacion;
    private javax.swing.JFormattedTextField txtAutPlaca;
    private javax.swing.JTextField txtBuscarxPlaca;
    // End of variables declaration//GEN-END:variables
}
