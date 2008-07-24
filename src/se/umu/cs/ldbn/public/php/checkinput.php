<?php
	function checkID($id) {
		if(!preg_match("/^[0-9]+$/D", $id)) {
			die ('<ldbn type="msg">' .
			'<msg type="error">Invalid ID.</msg>'.
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
	
	//only hexadecimal chars, length must be 32 char. 
	//(see md5 algorithm)
	function checkMD5($pass) {	  
		if(!preg_match("/^([0-9a-fA-F]){32}$/D", $pass)) {
			die ('<ldbn type="msg">' .
			'<msg type="error">Invalid MD5 hash.</msg>'.
			'</ldbn>');
		}
	}
	
	function checkMail($mail) {
		if(!preg_match("/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/D", $mail)) {
			die ('<ldbn type="msg">' .
			'<msg type="error">Invalid email.</msg>'.
			'</ldbn>');
		}
	}

?>
