<?php
//$user_id, $user_name and $user_mail must be set before invoking this script.
//The mail function of php must be enabled.
//NB:
//Define the variable $activation_string in the caller script in order 
//not to generate nor store a new activation string in the database.

if ($use_account_email_activation) {
	if(!isset($activation_string)) {
		$tmp1 = time(); $tmp2 = rand();
		$activation_string = md5("$tmp1;$tmp2");
		$sql = "INSERT INTO activation (activation_string, user_id) VALUES ('$activation_string', $user_id)";
		if (! $sth = $dbhandle->query($sql)) {
			die($db_error_xml);
		}
	}

	$user_link = "http://$ldbnhost/php/activation.php?a=$activation_string";
	//send mail
	$subject = "{$ldbnhost} User account activation";
	$message = "Thank You for your registration.\n\n
		Below is your $ldbnhost account information:\n\n
		Username  : $user_name\n
		Email     : $user_mail\n
		Password  : <Cannot be shown>\n\n
		In order to verify your account, please  follow the link bellow. If the link does not work, try to copy it and then paste it in the browser.\n\n 
		$user_link";
	$headers = "From: $ldbnemail\r\nReply-To: $ldbnemail";
	$mail_sent = mail( $user_mail, $subject, $message, $headers );
	if($mail_sent){
		echo ('<ldbn type="msg"><msg type="ok">Activation mail has been sent.</msg></ldbn>');
	} else {
		echo ('<ldbn type="msg"><msg type="error">Could not send activation mail.</msg></ldbn>');
	}
} else {
	echo ('<ldbn type="msg"><msg type="ok">Registration successful. You can now login with your username and password.</msg></ldbn>');
}

?>