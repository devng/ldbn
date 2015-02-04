<?php
/**
 * This is used only for RE-SENDING the email account activation.
 * This can be triggered by the user in the front end.
 */
require_once ("common.php");

$dbhandle = openDB ();

$user_mail;

if ($use_account_email_activation) {
	if (isset ( $_POST ['user_mail'] )) {
		$user_mail = $_POST ['user_mail'];
		checkMail ( $user_mail );
		$sql = "SELECT user_id, name, active FROM user WHERE email='$user_mail'";
		if (! $sth = $dbhandle->query ( $sql )) {
			dieWithXMLMessage ( $msg_db_error );
		}
		if ($row = $sth->fetch ()) {
			$user_id = $row [0];
			$user_name = $row [1];
			$user_active = $row [2];
			if ($user_active) {
				dieWithXMLMessage ( 'User is already active.', 'warn' );
			}
			$sql = "SELECT activation_string FROM activation WHERE user_id=$user_id";
			if (! $sth = $dbhandle->query ( $sql )) {
				dieWithXMLMessage ( $msg_db_error );
			}
			if ($row = $sth->fetch ()) {
				$activation_string = $row [0];
				sendEmailActivation ( $dbhandle, $user_id, $user_name, $user_mail, $activation_string );
			} else {
				dieWithXMLMessage ( 'Activation string is not set for user with email:' . $user_mail, 'warn' );
			}
		} else {
			dieWithXMLMessage ( 'No user with such an email: ' . $user_mail, 'warn' );
		}
	}
} else {
	dieWithXMLMessage ( $msg_email_disabled );
}

?>