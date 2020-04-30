package com.espotify.controller;

import java.io.IOException;

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
public class Parar_TransmisionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Parar_TransmisionServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String idTransmision = request.getParameter("idTransmision");
		String url = request.getParameter("url");
		
		Boolean parada = new TransmisionDAO().parar(Integer.parseInt(idTransmision),url);
		if(parada) {
			
			//RequestDispatcher dispatcher=request.getRequestDispatcher("user.jsp");
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
