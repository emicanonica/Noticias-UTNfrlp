<?php

$con = mysqli_connect('localhost', 'root', '37559721v','noticias');
mysql_query("SET CHARACTER SET utf8");
mysql_query("SET NAMES utf8");


if( $con )
{
    //mysql_select_db('noticias');

	$sql = "SELECT `id`, `noticia` FROM `noticias`";
  $result = mysqli_query($con,$sql);
	$json = array();

	if(mysqli_num_rows($result)){
    		while( $row = mysqli_fetch_object($result)){
        		$json[]=$row;
    	}	}
    	mysqli_close($con);
	echo json_encode($json);
}

?>
