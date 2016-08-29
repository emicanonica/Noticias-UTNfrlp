<!DOCTYPE html>
<html>
	<head>
    		<title>UTN-FRLP Noticias</title>
    		<link href="estilo.css" rel="stylesheet" type="text/css">
            <link rel="shortcut icon" href="rcs/UTN.ico">

				<script type="text/javascript">
				function delete_id(id)
				{
				     if(confirm('¿estás seguro que desea eliminar esta noticia?'))
				     {
				        window.location.href='index.php?delete_id='+id;
				     }
				}

				function show_img(img) {
				  window.open("http://localhost/NoticiasServices/uploads/noticia21.jpg")
				}
				</script>
	</head>
	<body>
        <h1><img src="rcs/UTN.png" class="logo">UTN-FRLP Noticias</h1>

		<?php
			session_start();

			include "conexion.php";

			function verificar_login($con,$user,$password,&$result) {
    				$sql = "SELECT * FROM usuarios WHERE usuario = '$user' and password = '$password'";
    				$rec = mysqli_query($con,$sql);
    				$count = 0;

    				while($row = mysqli_fetch_object($rec))
    				{
        				$count++;
        				$result = $row;
    				}

    				if($count == 1)
    				{
        				return 1;
    				}

    				else
    				{
        				return 0;
    				}
			}

			if(!isset($_SESSION['id']))
			{
    				if(isset($_POST['login']))
    				{
        				if(verificar_login($con,$_POST['user'],$_POST['password'],$result) == 1)
        				{
            					$_SESSION['id'] = $result->id;
            					header("location:index.php");
        				}
        				else
        				{
            					echo '<div class="error">Su usuario es incorrecto, intente nuevamente.</div>';
        				}
    				}

		?>

		<form action="" method="post" class="login">
    			<div><label>Usuario</label><input name="user" type="text" ></div>
    			<div><label>Contraseña</label><input name="password" type="password"></div>
    			<div><input name="login" type="submit" value="login"></div>
		</form>

		<?php
			} else {
    				//header("location:cargar-noticia.php");
    ?>

    <form action="uploader.php" method="POST" enctype="multipart/form-data" class="upload">
        <div><label for="imagen">Seleccionar Imagen o PDF a subir:</label></div>
				<hr>
				<div><input type="file" name="imagen" id="imagen" /></div>
				<div style="margin-right:100px"><input name="subir" type="submit" value="Subir"></div>
    </form>

    <?php
        echo '<p class="logout"><a href="logout.php">Logout</a></p>';
    ?>

		<?php
		if(isset($_GET['delete_id']))
			{
				$resultado = @mysqli_query($con,"SELECT * FROM `noticias` WHERE id=".$_GET['delete_id']);
				$fila = mysqli_fetch_assoc($resultado);
				$file = $fila['noticia'];
				unlink("uploads/" . $file);
			 	$sql_query="DELETE FROM noticias WHERE id=".$_GET['delete_id'];
			 	mysqli_query($con,$sql_query);
			 	header("Location: index.php");
		}

		echo "<form class='rows'>";
			$result = mysqli_query($con,"SELECT * FROM noticias", $link);
			$num_rows = @mysqli_num_rows($result);

			echo "<h2>Listado de Noticias</h2>";

			if ($num_rows > 0) {
				// comienza un bucle que leerá todos los registros existentes
				echo '<table><tr>
				<td><h3><b>ID</b></h3></td>
				<td><h3><b>Noticia</b></h3></td>
				<td><h3><b>Opciones</b></h3></td>
				</tr>';
				while($row = mysqli_fetch_array($result)) {
				// $row es un array con todos los campos existentes en la tabla
				echo '<table><tr>
				<td>'.$row['id'].'</td>
				<td>'.$row["noticia"].'</td>
				<td><a href=javascript:delete_id('.$row[0].')><img src="rcs/trash_can.png" alt="Borrar" /></a>
				<a href=javascript:window.open("http://localhost/NoticiasServices/uploads/'.$row["noticia"].'","imagen","height=792,width=612")><img src="rcs/show_img.png" alt="Ver" /></a></td>
				</tr></table>';
				} // fin del bucle de instrucciones
				mysqli_free_result($result); // Liberamos los registros
				mysqli_close($link); // Cerramos la conexion con la base de datos
				echo "</form>";
			}
			else {
				echo "<p>No hay noticias cargadas</p>";
			}
		 } ?>
	</body>
</html>
