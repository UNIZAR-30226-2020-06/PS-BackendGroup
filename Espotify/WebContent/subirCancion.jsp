<html>
<head></head>
<body>
<form action="subir_cancion" method="post">
<br>
<%= request.getAttribute("ruta") %>
<input type="text" name="ruta" text="">
<input type="text" name="nombre" placeholder="Nombre Canci�n" required="">
<input type="text" name="autor" placeholder="Autor" required="">
<input type="text" name="genero" placeholder="G�nero" required=""> 
<input type="submit" value="Meter datos canci�n">
</form>
</body>
</html>