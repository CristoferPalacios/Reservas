package com.hotel.reservas.mvc.BLL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.hotel.reservas.Conexion.Conexion;
import com.hotel.reservas.mvc.Entidad.Usuario;
import com.hotel.reservas.mvc.Recurso.recurso;


public class UsuarioBll {

    public  Boolean Login (String usuario , String contrasena) {
        ResultSet resultado;
        Connection conexion = null;
        try {
            Conexion conectionBD = new Conexion();
            conexion = conectionBD.conectar();
            if (conexion != null) {
                String consulta = "SELECT * FROM Usuarios  WHERE usuario  = ? AND contraseña  = ?";

                try (PreparedStatement pstmt = conexion.prepareStatement(consulta)) {
                    pstmt.setString(1, usuario);
                    pstmt.setString(2, contrasena);

                    resultado = pstmt.executeQuery();
                    return resultado.next();
                }
            } else {
                return false;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        } finally {
            cerrarConexion(conexion);
        }
    }


    public boolean Registrar(Usuario user, String htmlCorreo) {
        try {
            Connection conexion = Conexion.conectar();
            if (conexion != null) {
                String procedimiento = "{call RegistrarUsuario(?, ?, ?, ?, ?, ?, ?)}";

                try (CallableStatement cs = conexion.prepareCall(procedimiento)) {
                    cs.setString(1, user.getNombre());
                    cs.setString(2, user.getUsuario());
                    cs.setString(3, recurso.sha256(user.getClave()));
                    cs.setString(4, user.getFecha()); // Convertir LocalDate a java.sql.Date
                    cs.setString(5, user.getCorreo());
                    cs.setString(6, user.getNumero());
                    cs.setInt(7, user.getGenero());

                    boolean resultado = cs.executeUpdate() > 0; // Comprobar si se realizó una actualización
                    return resultado;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }




    private void cerrarConexion(Connection conexion) {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
