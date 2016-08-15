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
		include_once "conexion.php";
		$resultado = @mysqli_query($con,"SELECT * FROM `noticias` ORDER BY `id` DESC LIMIT 1");
		$fila = mysqli_fetch_assoc($resultado);
		$file = $fila['noticia'];
		unlink("uploads/" . $file);
		if (@mysqli_query($con,"DELETE FROM `noticias` ORDER BY `id` DESC LIMIT 1")) {
			echo "Borrado con exito";
		} else {
			echo "error al borrar";
		}
	?>
</body>
</html>
