// Classe para fazer conexao com banco de dados
package com.sample.trabalho.dao;


import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    public static Connection con;
    public void setConnection(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String ip = "";
            // ip +  nome usuario + senha e nome database
            String userName = "";
            String password = "";
            String dbName = "";
            String connectURL = "jdbc:jtds:sqlserver://"+ip+":1433;instance=SQLEXPRESS;user="+userName+";password="+password+";databasename="+dbName+";";

            //Driver JDBC
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            //fazer conexao
            con = DriverManager.getConnection(connectURL);

            Log.e("ASK","|Connection Called");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}