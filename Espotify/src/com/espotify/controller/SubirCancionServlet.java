package com.espotify.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



import com.espotify.dao.CancionDAO;

/**
 * Servlet implementation class SubirCancion
 */
@WebServlet("/SubirCancion")
public class SubirCancionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ALMACEN_PATH = "/var/www/html/almacen-mp3/";
       
	private ServletFileUpload uploader = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubirCancionServlet() {
    	super();
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("---------------------------------------------------------------");
		String nombre = (String) request.getParameter("nombre");
		System.out.println(nombre);
		String autor = (String) request.getParameter("autor");
		System.out.println(autor);
		String genero = (String) request.getParameter("genero");
		System.out.println(genero);			
		String ruta = (String) request.getParameter("ruta");
		System.out.println(ruta);	
		System.out.println("------------------------------------------------------------------------");
		CancionDAO cancion = new CancionDAO();
		cancion.subirCancion(nombre, autor, genero, ruta);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			doGet(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			System.out.println("Servlet Excepción ++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
