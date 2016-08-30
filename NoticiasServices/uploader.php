<!DOCTYPE html>
<html>
<head>
	<title>UTN-FRLP Noticias</title>
	<link href="estilo.css" rel="stylesheet" type="text/css">
	<link rel="shortcut icon" href="rcs/UTN.ico">
	<META HTTP-EQUIV="Refresh" CONTENT="1.5; URL=index.php">
</head>
<body>
	<?php
	//conexion a la base de datos
	include "conexion.php";


	if ($_FILES["imagen"]["error"] > 0){
		echo "<h1>ha ocurrido un error</h1>";
	} else {
		$imagick = new Imagick();
		$imagick->readImage($_FILES["imagen"]["tmp_name"]."[0]");

		$rs = mysqli_query($con,"SELECT MAX(id) AS id FROM noticias");
		if ($row = mysqli_fetch_row($rs)) {
			$id = trim($row[0]);
		}

		$filename = "noticia" . ($id + 1) . ".jpg";
		$imagick->writeImage('uploads/' . $filename);
		$resultado = @mysqli_query($con,"SELECT * FROM `noticias`");
		$rows = @mysqli_num_rows($resultado);

		if ($rows < 100){
			@mysqli_query($con,"INSERT INTO `noticias`(`noticia`) VALUES ('$filename')");
			echo "<h1>el archivo ha sido cargado exitosamente</h1>";
		} else {
			//aqui borramos de la carpeta upload y de la base de datos la noticia más antigua
			$resultado = @mysqli_query("SELECT noticia FROM `noticias` LIMIT 1");
			$fila = mysqli_fetch_assoc($resultado);
			$file = $fila['noticia'];
			unlink("uploads/" . $file);
			echo "<h1>el archivo ha sido cargado exitosamente y se ha borrado el archivo más antiguo</h1>";

			@mysqli_query($con,"DELETE FROM noticias LIMIT 1");
			$nombre = $_FILES['imagen']['name'];
                @mysqli_query("INSERT INTO `noticias`(`noticia`) VALUES ('$filename')");
		}
	}
?>
</body>
</html>
