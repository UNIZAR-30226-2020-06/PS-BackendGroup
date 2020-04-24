package com.espotify.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.espotify.dao.TransmisionDAO;
import com.espotify.model.Transmision;

/**
 * Servlet implementation Servlet
 */
public class Iniciar_TransmisionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Iniciar_TransmisionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombre = request.getParameter("nombre");
		String usuario = request.getParameter("usuario");
		String descripcion = request.getParameter("descripcion");
		
		Transmision transmision = new TransmisionDAO().iniciar(nombre, descripcion, Integer.parseInt(usuario));
		if(transmision != null) {
			HttpSession session = request.getSession();
			session.setAttribute("transmision", transmision);
			
			//RequestDispatcher dispatcher=request.getRequestDispatcher("transmision.jsp");
	        //dispatcher.forward(request, response);
		}else {
			//response.sendRedirect("iniciarTransmision.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
