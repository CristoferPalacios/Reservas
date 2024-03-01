package com.hotel.reservas.Conexion;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    public static Connection conectar() {
        Connection cnn =null;
        try {
            StrictMode.ThreadPolicy politica= new  StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(politica);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://Hoteles.mssql.somee.com;user=PauloYas_SQLLogin_1;password=wuqyiy3npx;databaseName=Hoteles");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return cnn;
    }
}
