package com.espotify.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

import com.espotify.dao.ListaReproduccionDAO;
import com.espotify.dao.UsuarioDAO;
import com.espotify.model.ListaReproduccion;
import com.espotify.model.Usuario;

/**
 * Servlet implementation class AndroidGetAll
 */
@WebServlet("/AndroidGetAll_ListaRepServlet")
public class AndroidGetAll_ListaRepServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String PETICION_LOGIN = "login";
	private static final String PETICION_REGISTRO = "registrar";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AndroidGetAll_ListaRepServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().log("Cliente Android - GetAll-ListaReproduccion"); 
		
		// Parsear JSON!
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = request.getReader().readLine()) != null) {
           sb.append(s);
        }

        getServletContext().log("Cliente Android - GetAll-ListaReproduccion [STRING]: " + sb.toString()); 
        String parseJSON = sb.toString();
       
        JSONObject parametrosPeticion = new JSONObject(parseJSON);
        String usuario = parametrosPeticion.getString("usuario");
        String tipo = parametrosPeticion.getString("tipo");
        
        
        JSONObject respuestaPeticion = new JSONObject();
        List<ListaReproduccion> listas = new ListaReproduccionDAO().showLists(usuario, tipo);
        JSONArray listasJson = new JSONArray();
        
        for(ListaReproduccion lista : listas) {
        	listasJson.put(lista.getNombre());
        }
        
        respuestaPeticion.put("lista", listasJson);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        
        PrintWriter out = response.getWriter();
        getServletContext().log("Cliente Android - GetAll-ListaReproduccion [RESPONSE]: " + sb.toString()); 
        out.print(respuestaPeticion);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
