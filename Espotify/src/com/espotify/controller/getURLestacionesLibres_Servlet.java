package com.espotify.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.Sockaddr;

import com.espotify.dao.TransmisionDAO;
import com.espotify.model.Transmision;

/**
 * Servlet implementation class IniciarTransimision_Servlet
 */
@WebServlet("/IniciarTransimision_Servlet")
public class getURLestacionesLibres_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getURLestacionesLibres_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		TransmisionDAO transmisionDAO = new TransmisionDAO();
		List<String> URLs = transmisionDAO.getURLestacionesLibres();
		request.setAttribute("transmision", URLs);
		//request.getRequestDispatcher("control-transmision.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
