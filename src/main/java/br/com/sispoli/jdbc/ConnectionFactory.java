package br.com.sispoli.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	public Connection getConnection() {

		try {
			// String de conexão com o MariaDB
			return DriverManager.getConnection("jdbc:mariadb://192.168.18.250/gestao_ginasio", "robson", "1202153120");

		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException(e);

		}

	}

}
