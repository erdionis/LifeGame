package ru.erdi.game.db.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import ru.erdi.game.db.SqliteLifeService;
import ru.erdi.game.facade.types.Entity;

public class SqliteLifeServiceImpl implements SqliteLifeService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SqliteLifeServiceImpl.class);

    private final String folder = "sqlite";
    private String dbName;
    private String absolutePath;
	
    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
	
	private Connection connect() {
        SQLiteDataSource ds = new SQLiteDataSource();
        String url = "jdbc:sqlite:" + this.absolutePath + "/" + dbName;
        ds.setUrl(url);
        Connection connection = null;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
        return connection;
    }
	
    private synchronized void createNewTable() {
        try (Connection connection = connect()){
        	String tableMnpKody="CREATE TABLE ENTITY (\r\n" + 
        			"    KEY VARCHAR2(50) NOT NULL,\r\n" +
        			"    UID VARCHAR2(50) NOT NULL,\r\n" + 
        			"    AGE BIGINT NOT NULL,\r\n" + 
        			"    X INTEGER NOT NULL,\r\n" + 
        			"    Y INTEGER  NOT NULL\r\n" + 
        			")";
        	
        	String age_idx="CREATE INDEX AGE_IDX ON ENTITY (AGE ASC)";
        	String key_idx="CREATE INDEX KEY_IDX ON ENTITY (KEY ASC)";
        	String select_age_idx="SELECT count(*) FROM sqlite_master WHERE type = 'index' and name='AGE_IDX'";
        	String select_key_idx="SELECT count(*) FROM sqlite_master WHERE type = 'index' and name='KEY_IDX'";
        	
            try (Statement stmt = connection.createStatement()){
            	connection.setAutoCommit(false);
            	
            	stmt.execute(tableMnpKody);
    			
            	createInd(stmt, select_key_idx, key_idx);
            	createInd(stmt, select_age_idx, age_idx);
            	connection.commit();
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
    
    private synchronized void createInd(Statement stmt, String selidx, String index) throws SQLException {
    	 try (ResultSet rs = stmt.executeQuery(selidx)){
             int countidx = 0;
             while (rs.next()) {
                 countidx = rs.getInt(1);
             }
             if (countidx == 0) {
                 stmt.execute(index);
             }
         }
    }
	
	
	@Override
	public synchronized void init() {
		File files = new File(folder);
        if (!files.exists()) {
            if (files.mkdirs()) {
                LOGGER.info("Sqlite directories are created!");
            } else {
                LOGGER.info("Failed to create Sqlite directories!");
            }
        }

        this.setAbsolutePath(files.getAbsolutePath());
        createNewTable();
	}

	@Override
	public void setEntity(String key, HashSet<Entity> world) {
		try (Connection connection = connect()){
			String insert = "INSERT INTO ENTITY VALUES (?, ?, ?, ?, ?)";
			if (world.size()>0) {
	            try (PreparedStatement pstmt = connection.prepareStatement(insert)){
	            	
	            	connection.setAutoCommit(false);
	            	world.forEach(entity ->{
	            		try {
	            			pstmt.setString(1, key);
	            			pstmt.setString(2, entity.getUid());
							pstmt.setLong(3, entity.getAge());
		                	pstmt.setInt(4, entity.getX());
		                	pstmt.setInt(5, entity.getY());
		                	
		                	pstmt.executeUpdate();
						} catch (SQLException e) {
							 LOGGER.error(e.getMessage());
						}
	            	});
	            	connection.commit();
	            }
			}
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
        }
	}

	@Override
	public HashSet<Entity> getEntity(String key, Long age) {
		HashSet<Entity> prevWorld = new HashSet<Entity>();
		try (Connection connection = connect()){
			String sql="SELECT * FROM ENTITY WHERE KEY=? and AGE=?";
			
	        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
	        	pstmt.setString(1, key);
	        	pstmt.setLong(2, age);
	        	
	        	ResultSet rs  = pstmt.executeQuery();
	        	while (rs.next()) {
	        		prevWorld.add(new Entity(rs.getString("UID"), rs.getLong("AGE"), rs.getInt("X"), rs.getInt("Y")));
	        	}
	        	return prevWorld;
	        }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return prevWorld; // пока пустота, потом переделать на обработчик ошибок, который также вернет пустоту, но даст событие ошибки для анализа
        }
	}
}
