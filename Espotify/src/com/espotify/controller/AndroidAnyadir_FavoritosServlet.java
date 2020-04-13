package com.espotify.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.espotify.dao.FavoritosDAO;
import com.espotify.dao.JSONAdapter;
import com.espotify.dao.ListaReproduccionDAO;
import com.espotify.dao.UsuarioDAO;
import com.espotify.model.Audio;
import com.espotify.model.ListaReproduccion;
import com.espotify.model.Usuario;

/**
 * Servlet implementation class AndroidVal
 */
@WebServlet("/AndroidGet_ProfileServlet")
public class AndroidAnyadir_FavoritosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PETICION_LOGIN = "login";
	private static final String PETICION_REGISTRO = "registrar";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidAnyadir_FavoritosServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        JSONObject parametrosPeticion = JSONAdapter.parsarJSON(request);
        getServletContext().log("PETICION RECIBIDA [GETPROFILE]: " + parametrosPeticion); 
        
        String email = parametrosPeticion.getString("email");
        JSONObject respuestaPeticion = new JSONObject();
        
        
        String idUsuario = UsuarioDAO.obtenerId(email);
        List<Audio> audios = new FavoritosDAO().getAudios(idUsuario);
        JSONArray audiosJSON = new JSONArray();
        JSONObject datosAudio;
        
        for(Audio audio : audios) {
        	datosAudio = new JSONObject();
        	datosAudio.put("titulo", audio.getTitulo());
        	datosAudio.put("usuario", audio.getUsuario());
        	datosAudio.put("url", audio.getUrl());
        	datosAudio.put("genero", audio.getGenero());
        	audiosJSON.put(datosAudio);
        }
        
        getServletContext().log("ENVIADO [GETPROFILE]: " + respuestaPeticion.toString()); 
        
        // Lanzar JSON
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        out.print(respuestaPeticion.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
