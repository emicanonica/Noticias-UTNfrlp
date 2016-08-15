<!DOCTYPE html>
<html>
	<head>
    		<title>UTN-FRLP Noticias</title>
    		<link href="estilo.css" rel="stylesheet" type="text/css">
            <link rel="shortcut icon" href="rcs/UTN.ico">
	</head>
	<body>
        <h1>UTN-FRLP Noticias</h1>

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
								<div><input name="subir" type="submit" value="Subir"></div>
            </form>
            <form action="borrar.php" method="POST" class="borrar">
                <div><input type="submit" name="borrar" value="Borrar ultima noticia"/></div>
            </form>
            <p> Su usuario ingreso correctamente.</p>
            <?php
                echo '<p class="logout"><a href="logout.php">Logout</a></p>';
            ?>

		<?php } ?>
	</body>
</html>
