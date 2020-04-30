package com.espotify.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.espotify.dao.LikesDAO;

/**
 * Servlet implementation Servlet
 */
public class BotonLikeAudio_LikesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BotonLikeAudio_LikesServlet() {
        super(); 
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
		int idAudio = Integer.parseInt(request.getParameter("idAudio"));
		boolean anyadido, quitado = false;
		String accionRealizada = null;
		
		anyadido = new LikesDAO().anyadirLikeAudio(idUsuario, idAudio);
		if (!anyadido) {
			quitado = new LikesDAO().quitarLikeAudio(idUsuario, idAudio);
			if (quitado) accionRealizada = "quitado";
		}
		else {
			accionRealizada = "anyadido";
		}
		
		// *************************************DESCRIPCIÓN******************************************
		// SI "accionRealizada" CONTIENE "anyadido" INDICA QUE SE HA DADO LIKE, SI CONTIENE "quitado"
		// INDICA QUE SE HA QUITADO EL LIKE, SI CONTIENE "null" HA HABIDO UN ERROR.
		// ******************************************************************************************
		
		HttpSession session = request.getSession();
		session.setAttribute("accionRealizada", accionRealizada);
		
		//request.getRequestDispatcher("obtener_info_likes").forward(request, response);
		//RequestDispatcher dispatcher=request.getRequestDispatcher("audio.jsp");
        //dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
