package com.espotify.controller;

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
@WebServlet("/Reg_UsuarioServlet")
public class Reg_UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reg_UsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombre = request.getParameter("nombre");
		String email = request.getParameter("email");
		String contrasena = request.getParameter("contrasena");
		String password = request.getParameter("password"); // confirmaci�n de la contrase�a
		String description = request.getParameter("descripcion");
		String imagen = request.getParameter("imagen");
		
		if(!contrasena.equals(password)){
			response.sendRedirect("registrar.jsp?error1=password_distinto");
		}
		else{
			boolean reg = new UsuarioDAO().register(nombre,email,contrasena,description,imagen);
			if(reg) {
				// No se guarda ninguna sesi�n, para ello despu�s de registrarse necesita iniciar sesi�n
				//HttpSession session = request.getSession();
				//session.setAttribute("nombre", nombre);
				//session.setAttribute("email", email);
				//session.setAttribute("descripcion", description);
				
				//request.getRequestDispatcher("index_in.jsp").forward(request, response);
				response.sendRedirect("validacion.jsp");
			}else {
				response.sendRedirect("registrar.jsp?error2=usuario_existente");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
