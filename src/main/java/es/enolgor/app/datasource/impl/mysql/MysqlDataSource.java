package es.enolgor.app.datasource.impl.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import es.enolgor.app.datasource.DataSource;
import es.enolgor.app.datasource.dao.providers.PetProvider;
import es.enolgor.configuration.Configuration;
import es.enolgor.configuration.Configuration.DatabaseConfiguration;

public class MysqlDataSource implements DataSource{

	private Connection conn;
	private MysqlPetProvider mysqlPetProvider;
	
	@Override
	public PetProvider getPetProvider() {
		return mysqlPetProvider;
	}

	@Override
	public void onInit(Configuration configuration) throws Exception{
		// Class.forName("com.mysql.jdbc.Driver").newInstance();
		DatabaseConfiguration dbconf = configuration.getDatabaseConfiguration();
		this.conn = DriverManager.getConnection(dbconf.getUrl(), dbconf.getUsername(), dbconf.getPassword());
		this.mysqlPetProvider = new MysqlPetProvider(conn);
		this.initTables();
	}

	@Override
	public void onClose() {
		// TODO Auto-generated method stub
		
	}
	
	public void initTables() throws SQLException {
		PreparedStatement stm = conn.prepareStatement(Statements.CREATE_PET_TABLE);
		stm.execute();
	}

}
