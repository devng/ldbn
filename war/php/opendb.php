<?php
// include this file whenever you want to use the database.
require_once("config.php");

// Generic DB error message for the front end
$db_error_xml = '<ldbn type="msg"><msg type="error">Request failed due to a database error.</msg></ldbn>';


// open the database
$dbhandle = new PDO($db_url) or die ('<msg type="error">Cannot open the database file.</msg></ldbn>');
$dbhandle->setAttribute( PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING );

?>