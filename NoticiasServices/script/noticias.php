<?php

//$con = mysqli_connect('localhost', 'root', '37559721v','noticias');
include "../conexion.php";

mysqli_query($con,"SET CHARACTER SET utf8");
mysqli_query($con,"SET NAMES utf8");


	$sql = "SELECT `id`, `noticia` FROM `noticias`";
  $result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)){
    		while( $row = mysqli_fetch_object($result)){
        		$json[]=$row;
    	}	}
    	mysqli_close($con);
	echo json_encode($json);

?>
