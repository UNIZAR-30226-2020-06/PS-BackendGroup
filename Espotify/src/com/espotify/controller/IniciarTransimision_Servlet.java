package com.espotify.controller;

import java.io.IOException;
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
public class IniciarTransimision_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IniciarTransimision_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int idUsuario = Integer.valueOf((String) session.getAttribute("id"));
		System.out.println(idUsuario);
		String nombre = (String) request.getParameter("nombre");
		System.out.println(nombre);
		String descripcion = (String) request.getParameter("descripcion");
		System.out.println(descripcion);
		String URL = (String) request.getParameter("URL");
		System.out.println(URL);
		TransmisionDAO transmisionDAO = new TransmisionDAO();
		Transmision transmision = transmisionDAO.iniciar(nombre, descripcion, idUsuario, URL);
		request.setAttribute("transmision", transmision);
		request.getRequestDispatcher("control-transmision.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
