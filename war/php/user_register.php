<?php
/**
 * Used for registering new users. The user mus provide an user name, email and password.
 * Then if the $use_account_email_activation variable is set to true, an email with an a link containing an unique string is sent to the user.
 * This way we can verify the user's email. IF the variable, however, is set to false, the user can login immediately.
 */
require_once ("common.php");

$dbhandle = openDB ();

$user_name;
$user_pass;
$user_mail;
$user_link;

if (isset ( $_POST ['user_name'] ) && isset ( $_POST ['user_pass'] ) && isset ( $_POST ['user_mail'] )) {
	$user_name = $_POST ['user_name'];
	$user_pass = $_POST ['user_pass'];
	$user_mail = $_POST ['user_mail'];
	
	checkName ( $user_name );
	checkBase64 ( $user_pass );
	checkMail ( $user_mail );
	
	checkNameUnique ( $dbhandle, $user_name );
	checkMailUnique ( $dbhandle, $user_mail );
	
	$user_pass = base64_decode ( $user_pass );
	$pass_salt = generatePassword ();
	$pass_hash = generatePasswordHash ( $user_pass, $pass_salt );
	
	$sql = "INSERT INTO user (name, pass_hash, pass_salt, email) VALUES ('$user_name', '$pass_hash', '$pass_salt', '$user_mail')";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	
	$sql = "SELECT user_id FROM user WHERE email='$user_mail'";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		$user_id = $row [0];
		sendEmailActivation ( $dbhandle, $user_id, $user_name, $user_mail );
	}
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}
?>