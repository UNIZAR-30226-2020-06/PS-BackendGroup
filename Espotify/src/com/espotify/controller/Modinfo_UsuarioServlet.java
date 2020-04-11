package com.espotify.controller;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.espotify.dao.UsuarioDAO;

/**
 * Servlet implementation class UsuarioServlet
 */
@WebServlet("/Modinfo_UsuarioServlet")
public class Modinfo_UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Modinfo_UsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//String id = (String) session.getAttribute("id");
		String id = (String) request.getParameter("id"); // PRUEBA - BORRAR
		
		// valores de los campos del jsp
		String nom = (String) request.getParameter("nombre");
		String descripcion =(String) request.getParameter("descripcion");
		String email =(String) request.getParameter("email");
		String imagen =(String) request.getParameter("imagen");

		boolean ok = UsuarioDAO.cambiar_info(nom,descripcion,email,id,imagen);
		
		if(ok) {
			// Actualizamos datos de la sesi�n
			if(nom != null && !nom.equals("")) session.setAttribute("usuario", nom);
			if(email != null && !email.equals("")) session.setAttribute("email", email);
			if(descripcion != null) session.setAttribute("descripcion", descripcion);
			if(imagen != null && !imagen.equals("")) {
				FileInputStream imagenBinaria = new FileInputStream(imagen);
				session.setAttribute("imagen", imagenBinaria); // Se pasa la imagen como un Blob
				imagenBinaria.close();
			}
			
			//request.getRequestDispatcher("usuario.jsp").forward(request, response);
			//response.sendRedirect("usuario.jsp?ok=");
		}
		else {
			//response.sendRedirect("perfil.jsp?error1=");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
