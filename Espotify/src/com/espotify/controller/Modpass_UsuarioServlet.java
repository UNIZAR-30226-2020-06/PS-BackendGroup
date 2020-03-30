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
@WebServlet("/Modpass_UsuarioServlet")
public class Modpass_UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Modpass_UsuarioServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//String id = (String) session.getAttribute("ID");
		String id = (String) request.getParameter("id"); // PRUEBA - BORRAR
		
		// valores de los campos del jsp
		String pass1 = (String) request.getParameter("contrasena1");
		String pass2 =(String) request.getParameter("contrasena2");
		String pass3 =(String) request.getParameter("contrasena3");

		if(pass2.equals(pass3)){
			boolean ok = UsuarioDAO.cambiar_pass(pass1,pass2,id);
			if(ok) {
				//response.sendRedirect("usuario.jsp?ok=");
			}
			else {
				response.sendRedirect("modinfo.jsp?error=pass_incorrecto");
			}
		}
		else{
			response.sendRedirect("modinfo.jsp?error=pass_diferentes");
		}
				
		//request.getRequestDispatcher("usuario.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
