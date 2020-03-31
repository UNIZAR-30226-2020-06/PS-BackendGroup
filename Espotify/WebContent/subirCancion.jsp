<html>
<head></head>
<body>
<form action="subir_cancion" method="post">
<br>
<%= request.getAttribute("ruta") %>
<input type="text" name="ruta" text="">
<input type="text" name="nombre" placeholder="Nombre Canción" required="">
<input type="text" name="autor" placeholder="Autor" required="">
<input type="text" name="genero" placeholder="Género" required=""> 
<input type="submit" value="Meter datos canción">
</form>
</body>
</html>