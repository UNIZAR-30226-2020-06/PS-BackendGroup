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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Servlet implementation Servlet
 */
public class GetName_TransmisionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetName_TransmisionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String nombre = request.getParameter("nombre");
		try{
			List<Transmision> directos = new TransmisionDAO().getTransmisionPorNombre(nombre);
			HttpSession session = request.getSession();
			session.setAttribute("directos", directos);
				
			//RequestDispatcher dispatcher=request.getRequestDispatcher("buscar.jsp");
			//dispatcher.forward(request, response);

		}catch(Throwable theException) {
			//response.sendRedirect("buscar.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
