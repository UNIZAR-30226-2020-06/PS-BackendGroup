
package com.espotify.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.espotify.model.ConnectionManager;

@WebListener
public class SubirAudioDAO implements ServletContextListener {
	public static final String GET_LAST_SONG_ID_QUERY = "SELECT id FROM Reproductor_musica.Audio ORDER BY ID DESC LIMIT 1";

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	
    	try {
	    	String rootPath = System.getProperty("catalina.home");
	    	ServletContext ctx = servletContextEvent.getServletContext();
	    	String relativePath = ctx.getInitParameter("directorio_ficheroTmp");
	    	
	    	File file = new File(rootPath + File.separator + relativePath);
	    	
	    	if(!file.exists()) {
	    		file.mkdirs();
	    	}
	    	
	    	servletContextEvent.getServletContext().log("File :" + file.getName());
	    	System.out.println("File Directory created to be used for storing files");
	    	ctx.setAttribute("FILES_DIR_FILE", file);
	    	
    	} catch(Exception e) {
    		servletContextEvent.getServletContext().log("Superfail :" + e);
    	}
    }

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		
	}
	
	public static int obtenerUltimaCancionId() {
		int id = 0;
		
		Connection conn;
		try {
			conn = ConnectionManager.getConnection();
			PreparedStatement ps = conn.prepareStatement(GET_LAST_SONG_ID_QUERY);
			ResultSet rs = ps.executeQuery();
		  
			while (rs.next()) {
			    id = rs.getInt("id");
			}
			
			ConnectionManager.releaseConnection(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
	}
	
}