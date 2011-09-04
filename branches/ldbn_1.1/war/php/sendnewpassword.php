<?php
require_once("opendb.php");
require_once("common.php");
$user_mail;
$user_id;
$user_name;
$user_active;
if(isset($_POST['user_mail'])) {
	$user_mail = $_POST['user_mail'];
	checkMail($user_mail);
	$sql = "SELECT user_id, name, active FROM user WHERE email='$user_mail'";
		if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		$user_id = $row[0];
		$user_name = $row[1];
		$user_active = $row[2];
		if (!$user_active) {
			die ('<ldbn type="msg"><msg type="warn">User is not active.</msg></ldbn>');
		}	
		$user_pass = generatePassword();
		$user_pass_md5 = md5($user_pass);
		$sql = "UPDATE user SET pass_md5 ='$user_pass_md5' WHERE user_id=$user_id;";
		if (! $sth = @mysql_query($sql)) {
			die(getDBErrorXML());
		}
		//send mail
		$subject = "{$ldbnhost} New password";
		$message = "Below is Your new $ldbnhost account password:\n\n
			Username      : $user_name\n
			NEW Password  : $user_pass\n\n
			We advise You to change Your password at http://$ldbnhost imidiatelly.\n\n";
		$headers = "From: $ldbnemail\r\nReply-To: $ldbnemail";
		$mail_sent = @mail( $user_mail, $subject, $message, $headers );
		if($mail_sent){
			echo ('<ldbn type="msg"><msg type="ok">A new password has been sent to Your email.</msg></ldbn>');
		} else {
			echo ('<ldbn type="msg"><msg type="error">Could not send email with a new password.</msg></ldbn>');
		}
	} else {
		die ('<ldbn type="msg"><msg type="warn">No user with such an email: '.$user_mail.'.</msg></ldbn>');
	}
}	
?>