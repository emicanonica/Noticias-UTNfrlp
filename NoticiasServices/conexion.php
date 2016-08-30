<?php
// datos para la coneccion a mysql para el servidor en "free hosting area" pagina:http://utn-frlp-noticias.ueuo.com/index.php 
define('DB_SERVER','localhost');
define('DB_NAME','1019447');
define('DB_USER','1019447');
define('DB_PASS','37559721v');

//include_once "BD_credentials";

//function conectar($DB_SERVER,$DB_USER,$DB_PASS,$DB_NAME){
  $con = mysqli_connect(DB_SERVER,DB_USER,DB_PASS,DB_NAME);

// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }
//}
//conectar(DB_SERVER,DB_USER,DB_PASS,DB_NAME);

?>
