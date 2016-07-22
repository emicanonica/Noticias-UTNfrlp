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
	mysql_select_db("noticias") or die(mysql_error()) ;
	if ($_FILES["imagen"]["error"] > 0){
		echo "ha ocurrido un error";
	} else {
		//ahora vamos a verificar si el tipo de archivo es un tipo de imagen permitido.
		//y que el tamano del archivo no exceda los 1024kb
		$permitidos = array("image/jpg", "image/jpeg");
		$limite_kb = 1024;

		if (in_array($_FILES['imagen']['type'], $permitidos) && $_FILES['imagen']['size'] <= $limite_kb * 1024){
			//esta es la ruta donde copiaremos la imagen
			//recuerden que deben crear un directorio con este mismo nombre
			//en el mismo lugar donde se encuentra el archivo subir.php
			$ruta = "uploads/" . $_FILES['imagen']['name'];
			//comprovamos si este archivo existe para no volverlo a copiar.
			//pero si quieren pueden obviar esto si no es necesario.
			//o pueden darle otro nombre para que no sobreescriba el actual.
			if (!file_exists($ruta)){
				//aqui movemos el archivo desde la ruta temporal a nuestra ruta
				//usamos la variable $resultado para almacenar el resultado del proceso de mover el archivo
				//almacenara true o false
				$resultado = @move_uploaded_file($_FILES["imagen"]["tmp_name"], $ruta);
				if ($resultado){
					//calcula el numero de filas de la tabla. No puede haber mas de 10
					$resultado = @mysql_query("SELECT * FROM `noticias`");
					$rows = @mysql_num_rows($resultado);
					if ($rows < 10){
						echo "el archivo ha sido movido exitosamente";
						$nombre = $_FILES['imagen']['name'];
						@mysql_query("INSERT INTO `noticias`(`photo`) VALUES ('$nombre')");
					} else {
						//aqui borramos de la carpeta upload y de la base de datos la noticia más antigua
						echo "el archivo ha sido movido exitosamente";
						$resultado = @mysql_query("SELECT photo FROM `noticias` LIMIT 1");
						$fila = mysql_fetch_assoc($resultado);
						$file = $fila['photo'];
						unlink("uploads/" . $file);
						//if (unlink("uploads/" . $file)){
						//	echo "archivo borrado";
						//} else {
						//	echo "error al borrar";
						//}
						@mysql_query("DELETE FROM noticias LIMIT 1");
						$nombre = $_FILES['imagen']['name'];
                                                @mysql_query("INSERT INTO `noticias`(`photo`) VALUES ('$nombre')");
					}
				} else {
					echo "ocurrio un error al mover el archivo.";
				}
			} else {
				echo $_FILES['imagen']['name'] . ", este archivo existe";
			}
		} else {
			echo "archivo no permitido, es tipo de archivo prohibido o excede el tamano de $limite_kb Kilobytes";
		}
	}
?>
</body>
</html>

