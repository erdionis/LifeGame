package ru.erdi.game.db.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;

import ru.erdi.game.db.SqliteLifeService;
import ru.erdi.game.facade.types.Entity;
import ru.erdi.game.facade.types.InfoEntity;

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
        	String tableMnpKody="CREATE TABLE IF NOT EXISTS ENTITY (\r\n" + 
        			"    KEY VARCHAR2(50) NOT NULL,\r\n" +
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
	public synchronized void setEntity(InfoEntity world) {
		try (Connection connection = connect()){
			String insert = "INSERT INTO ENTITY VALUES (?, ?, ?, ?)";
			if (world.getEntitys().size()>0) {
	            try (PreparedStatement pstmt = connection.prepareStatement(insert)){
	            	
	            	connection.setAutoCommit(false);
	            	world.getEntitys().forEach(entity ->{
	            		try {
	            			pstmt.setString(1, world.getKey());
							pstmt.setLong(2, world.getAge());
		                	pstmt.setInt(3, entity.getX());
		                	pstmt.setInt(4, entity.getY());
		                	
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
	public InfoEntity getEntity(String key, Long age) {
		List<Entity> prevWorld = new ArrayList<Entity>();
		try (Connection connection = connect()){
			String sql="SELECT X,Y FROM ENTITY WHERE KEY=? and AGE=?";
			
	        try (PreparedStatement pstmt = connection.prepareStatement(sql)){
	        	pstmt.setString(1, key);
	        	pstmt.setLong(2, age);
	        	
	        	ResultSet rs  = pstmt.executeQuery();
	        	if (rs!=null) {
	        		while (rs.next()) {
		        		prevWorld.add(new Entity(rs.getInt("X"), rs.getInt("Y")));
		        	}
		        	InfoEntity infoEntity=new InfoEntity();
		        	infoEntity.setKey(key);
		        	infoEntity.setAge(age);
		        	infoEntity.setEntitys(prevWorld);
		        	
		        	return infoEntity;
	        	} else {
	        		return null;
	        	}
	        	
	        }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return null; // пока пустота, потом переделать на обработчик ошибок, который также вернет пустоту, но даст событие ошибки для анализа
        }
	}
}
