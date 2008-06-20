<?php
	// include this file whenever you want to use the database.
	include("config.php");
	//header('Content-Type: text/plain;');
	function getDBErrorXML() {
		return '<ldbn type="msg">' .
			'<msg type="error">'.mysql_error().'</msg>'.
			'</ldbn>';
	}
	
	function checkID($id) {
		if(!preg_match("/^[0-9]+$/D", $id)) {
			die ('<ldbn type="msg">' .
			'<msg type="error">SQL injection attempt.</msg>'.
			'</ldbn>');
		}
	}
	
	function checkName($name) {
		if(!preg_match("/^([\w]|\-){1,20}$/D", $name)) {
			die ('<ldbn type="msg">' .
			'<msg type="error">Invalid name.</msg>'.
			'</ldbn>');
		}
	}
	
	$conn = @mysql_connect($dbhost, $dbuser, $dbpass) or die (
			getDBErrorXML());
	@mysql_select_db($dbname) or die (getDBErrorXML());
?>