<?php
require_once("opendb.php");
require_once("checkinput.php");	
$user_name;
$user_pass;
$user_mail;
$user_link;

if (isset($_POST['user_name']) && isset($_POST['user_pass']) && isset($_POST['user_mail'])) {
	$user_name = $_POST['user_name'];
	$user_pass = $_POST['user_pass'];
	$user_mail = $_POST['user_mail'];
	checkName($user_name);
	checkMD5($user_pass);
	checkMail($user_mail);
	$sql = "SELECT id FROM user WHERE name=$user_name";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		die ('<ldbn type="msg"><msg type="warn">Username is already taken.</msg></ldbn>');
	}
	$sql = "SELECT id FROM user WHERE email=$user_mail";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		die ('<ldbn type="msg"><msg type="warn">Email address already exists.</msg></ldbn>');
	}
	$sql = "INSERT INTO user (name, pass_md5, email) VALUES ($user_name, $user_pass, $user_mail)";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	$sql = "SELECT id FROM user WHERE email=$user_mail";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		$user_id = $row[0];
		$tmp1 = time(); $tmp2 = rand();
		$activation_string = md5("$tmp1;$tmp2");
		$sql = "INSERT INTO activation (activation_string, user_id) VALUES ($activation_string, $user_id)";
		$user_link = "http://ldbnonline.com/php/activation.php?a=$activation_string";
		//send mail
		$subject = '{LDBNONLINE.COM} User account activation';
		$message = "Thank You for your registration.\n\n
			Below is your LDBNOnline.com account information:\n\n
			Username  : $user_name\n
			Email     : $user_mail\n
			Password  : <Cannot be shown>\n\n
			In order to verify your account, please  follow the link bellow. If the link does not work, try to copy it and then paste it in the browser.\n\n 
			$user_link";
		$headers = "From: admin@ldbnonline.com\r\nReply-To: admin@ldbnonline.com";
		$mail_sent = @mail( $user_mail, $subject, $message, $headers );
		if($mail_sent){
			echo ('<ldbn type="msg"><msg type="ok">Activation mail has been sent.</msg></ldbn>');
		} else {
			echo ('<ldbn type="msg"><msg type="error">Could not send activation mail.</msg></ldbn>');
		}
	}
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
?>