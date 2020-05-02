package com.espotify.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.espotify.dao.ComentariosDAO;
import com.espotify.model.Comentario;

/**
 * Servlet implementation Servlet
 */
public class Anyadir_ComentarioAudioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Anyadir_ComentarioAudioServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String descripcion = request.getParameter("descripcion");
		int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
		int idAudio = Integer.parseInt(request.getParameter("idAudio"));
		
		Boolean anyadido = new ComentariosDAO().anyadirComentarioAudio(descripcion, idUsuario, idAudio);
		if(anyadido) {
			Comentario comentario = new ComentariosDAO().getUltimoComentarioAudio(idAudio);
			HttpSession session = request.getSession();
			session.setAttribute("comentario", comentario);

			//RequestDispatcher dispatcher=request.getRequestDispatcher("audio.jsp");
	        //dispatcher.forward(request, response);
		}else {
			//response.sendRedirect("audio.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
