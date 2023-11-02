package DAO;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.DriverManager;

public class FabricaConexao {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DATABASE = "agenda";
    private static final String IP = "127.0.0.1";
    private static final String PORTA = "3306";
    private static final String URL = "jdbc:mysql://" + IP + ":" + PORTA + "/" + DATABASE;

    private static Connection conexao = null;

    private static Connection abrirConexao() {
        Connection objConexao = null;
        try {
            Class.forName(DRIVER);
            objConexao = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado com sucesso!");
            
        } catch (ClassNotFoundException | SQLException ex) {
            
            Logger.getLogger(DAOContato.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Ocorreu um erro ao tentar abrir conexão: " + ex.getMessage());
        }

        return objConexao;
    }

    public static Connection getConexao() {
        if (conexao == null) {
            conexao = abrirConexao();
        }

        return conexao;
    }

}
