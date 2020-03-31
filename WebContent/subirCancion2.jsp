<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head></head>
<body>
<p>Cancion: <%= request.getAttribute("titulo") %></p>
<p>Autor: <%= request.getAttribute("autor") %></p>
<form action="subir_cancion_audio" method="post">
Select File to Upload:<input type="file" name="fileName">
<input type="submit" value="Subir canción">
<br>
</body>
</html>