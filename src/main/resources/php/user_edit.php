<?php
/**
 * Edit the user settings such as user name, email and password.
 * 
 * Note that the client always sends the password encoded in Base64. This is needed not for security purposes,
 * but rather to ensure the correct transportation of any special characters. 
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

if (isset ( $_POST ['user_name'] )) {
	$user_name = $_POST ['user_name'];
	checkName ( $user_name );
	checkNameUnique ( $dbhandle, $user_name );
	
	$sql = "UPDATE user SET name = '$user_name' WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
}

if (isset ( $_POST ['user_pass'] )) {
	$user_pass = $_POST ['user_pass'];
	checkBase64 ( $user_pass, "Invalid password." );
	
	$user_pass = base64_decode ( $user_pass );
	$pass_salt = generatePassword ();
	changeUserPassword ( $dbhandle, $user_id, $user_pass, $pass_salt );
}

if (isset ( $_POST ['user_mail'] )) {
	$user_mail = $_POST ['user_mail'];
	checkMail ( $user_mail );
	checkMailUnique ( $dbhandle, $user_mail );
	
	$sql = "UPDATE user SET email='$user_mail', is_active=0 WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	
	$sql = "SELECT name FROM user WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		$user_name = $row [0];
		sendEmailActivation ( $dbhandle, $user_id, $user_name, $user_mail );
	}
} else {
	dieWithXMLMessage ( 'User data was changed successfully', 'ok' );
}
?>