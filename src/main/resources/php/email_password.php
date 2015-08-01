<?php
/**
 * This is used for requesting a new password by the user.
 */
require_once ("common.php");

$dbhandle = openDB ();

$user_mail;
$user_id;
$user_name;
$user_active;
if ($use_account_email_activation) {
	if (isset ( $_POST ['user_mail'] )) {
		$user_mail = $_POST ['user_mail'];
		checkMail ( $user_mail );
		$sql = "SELECT user_id, name, is_active FROM user WHERE email='$user_mail'";
		if (! $sth = $dbhandle->query ( $sql )) {
			dieWithXMLMessage ( $msg_db_error );
		}
		if ($row = $sth->fetch ()) {
			$user_id = $row [0];
			$user_name = $row [1];
			$user_active = $row [2];
			if (! $user_active) {
				dieWithXMLMessage ( 'User is not active.', 'warn' );
			}
			$user_pass = generatePassword ();
			$pass_salt = generatePassword ();
			changeUserPassword ( $dbhandle, $user_id, $user_pass, $pass_salt );
			// send mail
			$subject = "{$ldbnhost} New password";
			$message = "Below is Your new $ldbnhost account password:\n\n
				Username      : $user_name\n
				NEW Password  : $user_pass\n\n
				We advise You to change Your password at http://$ldbnhost imidiatelly.\n\n";
			$headers = "From: $ldbnemail\r\nReply-To: $ldbnemail";
			$mail_sent = mail ( $user_mail, $subject, $message, $headers );
			if ($mail_sent) {
				dieWithXMLMessage ( 'A new password has been sent to your email.', 'ok' );
			} else {
				dieWithXMLMessage ( 'Could not send email with a new password.' );
			}
		} else {
			dieWithXMLMessage ( 'No user with such an email: ' . $user_mail . '.', 'warn' );
		}
	}
} else {
	dieWithXMLMessage ( $msg_email_disabled );
}
?>