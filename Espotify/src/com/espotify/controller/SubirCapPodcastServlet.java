package com.espotify.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;



import com.espotify.dao.CapituloPodcastDAO;
import com.espotify.dao.GeneroDAO;
import com.espotify.model.Genero;

/**
 * Servlet implementation class SubirCapPodcast
 */
@WebServlet("/SubirCapPodcast")
public class SubirCapPodcastServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubirCapPodcastServlet() {
    	super();
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		System.out.println("---------------------------------------------------------------");
		String nombre = (String) request.getParameter("titulo");
		System.out.println(nombre);
		int autor = Integer.valueOf((String) session.getAttribute("id"));
		System.out.println(autor);
		int genero = Integer.valueOf(request.getParameter("genero"));
		System.out.println(genero);
		String ruta = (String) request.getParameter("ruta");
		System.out.println(ruta);	
		System.out.println("------------------------------------------------------------------------");
		CapituloPodcastDAO capitulo = new CapituloPodcastDAO();
		if (capitulo.subirCapituloPodcast(nombre, autor, genero, ruta) != 0) {

			request.getRequestDispatcher("/obtener_contenido_perfil").forward(request, response);
		}
			
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