<?php
	// include this file whenever you want to use the database.
	include("config.php");
	//header('Content-Type: text/plain;');
	function getDBErrorXML() {
		return '<ldbn type="msg">' .
			'<msg type="error">'.mysql_error().'</msg>'.
			'</ldbn>';
	}
	
	$conn = @mysql_connect($dbhost, $dbuser, $dbpass) or die (
			getDBErrorXML());
	@mysql_select_db($dbname) or die (getDBErrorXML());
?>