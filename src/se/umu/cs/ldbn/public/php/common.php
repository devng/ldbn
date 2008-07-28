<?php
function generatePassword ($length = 8) {
  // start with a blank password
  $password = "";
  // define possible characters
  $possible = "0123456789bcdfghjkmnpqrstvwxyz"; 
  // set up a counter
  $i = 0; 
  // add random characters to $password until $length is reached
  while ($i < $length) { 

    // pick a random character from the possible ones
    $char = substr($possible, mt_rand(0, strlen($possible)-1), 1);
        
    // we don't want this character if it's already in the password
    if (!strstr($password, $char)) { 
      $password .= $char;
      $i++;
    }
  }
  // done!
  return $password;
}

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


//checks if a string consists only of base64 characters
function checkBase64($str) {	  
	if(!preg_match("/[0-9a-zA-Z\+\/\=]+/", $str)) {
		die ('<ldbn type="msg">' .
		'<msg type="error">Invalid Base64 string.</msg>'.
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

function checkFileType($type) {
	if(!isset($type) || !preg_match("/text.*/", $type)) {
		die ('<ldbn type="msg">' .
		'<msg type="error">Invalid file type.</msg>'.
		'</ldbn>');
	}
}

?>
