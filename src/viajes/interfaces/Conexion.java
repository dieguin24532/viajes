/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viajes.interfaces;

import java.sql.Connection; //Libreria del sql 
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Estudiante
 */
public class Conexion {
    Connection conect = null;
    public Connection conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");///Permite consumir el midleware
            conect = DriverManager.getConnection("jdbc:mysql://localhost/viajes","root","");
            //JOptionPane.showMessageDialog(null, "Correcto");
            //jdbc Medio de coneccion entre el usuario y la base de datos
            //root usuario de la base de datos mysql
            //"" contraseña
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Sin conexión intentalo mas tarde, Ponte en contacto con el administrador");
        }
        
        return conect;
    }
}
