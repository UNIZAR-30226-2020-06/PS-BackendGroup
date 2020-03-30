package com.espotify.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.espotify.dao.SubirFicheroDAO;

@WebServlet("/UploadDownloadFileServlet")
public class UploadDownloadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ALMACEN_PATH = "/var/www/html/almacen-mp3/";
	
	
    private ServletFileUpload uploader = null;
	@Override
	public void init() throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
	}
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		getServletContext().log("new NEW!");
		getServletContext().log("MP3 Upload starts...");
		
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.write("<html><head></head><body>");
		
		try {
			List<FileItem> fileItemsList = uploader.parseRequest(request);
			Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			while(fileItemsIterator.hasNext()){
				FileItem fileItem = fileItemsIterator.next();
				getServletContext().log("FieldName="+fileItem.getFieldName());
				getServletContext().log("FileName="+fileItem.getName());
				getServletContext().log("ContentType="+fileItem.getContentType());
				getServletContext().log("Size in bytes="+fileItem.getSize());
				getServletContext().log("FILES_DIR: " + request.getServletContext().getAttribute("FILES_DIR"));
				getServletContext().log("FILE SEPARATOR: " + File.separator);
				getServletContext().log("FILE NAME: " + fileItem.getName());
				
				int lastId = SubirFicheroDAO.obtenerUltimaCancionId();
				
				getServletContext().log("LAST ID: "+ lastId);
				
				File file = new File(ALMACEN_PATH + lastId + ".mp3");
				getServletContext().log("Absolute Path at server="+file.getAbsolutePath());
				
				fileItem.write(file);
				
				file.setReadable(true, false);
				file.setExecutable(true, false);
				file.setWritable(true, false);
				
				out.write("File "+fileItem.getName()+ " uploaded successfully.");
				out.write("<br>");
				out.write("<a href=\"UploadDownloadFileServlet?fileName="+fileItem.getName()+"\">Download "+fileItem.getName()+"</a>");
			}
		} catch (FileUploadException e) {
			getServletContext().log("FAIL: " + e.toString());
			out.write("Exception in uploading file. NEW!");
		} catch (Exception e) {
			getServletContext().log("FAIL: " + e.toString());
			out.write("Exception in uploading file. NEW!");
		}
		out.write("</body></html>");
	}

}
