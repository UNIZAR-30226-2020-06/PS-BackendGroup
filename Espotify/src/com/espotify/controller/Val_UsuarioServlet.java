package com.espotify.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.espotify.dao.UsuarioDAO;
import com.espotify.model.Usuario;

/**
 * Servlet implementation class UsuarioServlet
 */
@WebServlet("/Val_UsuarioServlet")
public class Val_UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Val_UsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String usuario = request.getParameter("email");
		String password = request.getParameter("contrasena");
		
		Usuario u = new UsuarioDAO().login(usuario, password);

		if(u != null) {
			HttpSession session = request.getSession();
			session.setAttribute("id", u.getId());
			session.setAttribute("nombre", u.getNombre());
			session.setAttribute("email", u.getCorreo());
			session.setAttribute("descripcion", u.getDescripcion());
			
			//request.getRequestDispatcher( "index_in.jsp" ).forward( request, response );
			//response.sendRedirect("index.jsp");
		}else {
			//response.getWriter().println("<div class='popup' id='popup'><a href='#' id='btn-cerrar-popup' class='btn-cerrar-popup'><i class='fas fa-times'></i></a><h3>Datos incorrectos</h3><h4>Email o contraseña incorrectos, inténtelo de nuevo.</h4><form action=''><div class='contenedor-inputs'></div><input type='submit' class='btn-cerrar-popup' value='Entendido'></form></div>");
			response.sendRedirect("validacion.jsp?p=error");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
