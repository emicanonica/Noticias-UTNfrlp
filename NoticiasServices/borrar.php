<!DOCTYPE html>
<html>
<head>
	<title>UTN-FRLP Noticias</title>
	<link href="estilo.css" rel="stylesheet" type="text/css">
	<link rel="shortcut icon" href="rcs/UTN.ico">
</head>
<body>
	<?php
		//conexion a la base de datos
		mysql_connect("localhost", "root", "corleone23") or die(mysql_error()) ;
		mysql_select_db("noticias") or die(mysql_error());
		$resultado = @mysql_query("SELECT * FROM `noticias` ORDER BY `id` DESC LIMIT 1");
		$fila = mysql_fetch_assoc($resultado);
		$file = $fila['photo'];
		unlink("uploads/" . $file);
		if (@mysql_query("DELETE FROM `noticias` ORDER BY `id` DESC LIMIT 1")) {
			echo "Borrado con exito";
		} else {
			echo "error al borrar";
		}
	?>
</body>
</html>