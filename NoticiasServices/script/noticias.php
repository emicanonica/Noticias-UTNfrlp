<?php

$con = mysql_connect('localhost', 'root', '37559721v');
mysql_query("SET CHARACTER SET utf8");
mysql_query("SET NAMES utf8");


if( $con )
{
    mysql_select_db('noticias');

	$sql = "SELECT `id`, `photo` FROM `noticias`";
   	$result = mysql_query($sql, $con);
	$json = array();

	if(mysql_num_rows($result)){
    		while( $row = mysql_fetch_object($result)){
        		$json[]=$row;
    	}	}
    	mysql_close($con);
	echo json_encode($json);
}

?>
