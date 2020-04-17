package com.espotify.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.espotify.dao.TransmisionDAO;

/**
 * Servlet implementation Servlet
 */
public class ModInfo_TransmisionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModInfo_TransmisionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombre = request.getParameter("nombre");
		String descripcion = request.getParameter("descripcion");
		String idTransmision = request.getParameter("idTransmision");
		
		Boolean cambiada = new TransmisionDAO().cambiar_info(nombre,descripcion,Integer.parseInt(idTransmision));
		if(cambiada) {
			HttpSession session = request.getSession();
			if (nombre != "" && nombre != null) session.setAttribute("nombre", nombre);
			if (descripcion != null) session.setAttribute("descripcion", descripcion);
			
			//RequestDispatcher dispatcher=request.getRequestDispatcher("transmision.jsp");
	        //dispatcher.forward(request, response);
		}else {
			//response.sendRedirect("error.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
