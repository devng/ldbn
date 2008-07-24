<?php
require_once("opendb.php");
require_once("checkinput.php");	
$user_mail;
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
		if ($user_active) {
			die ('<ldbn type="msg"><msg type="warn">User is already active.</msg></ldbn>');
		}
		$sql = "SELECT activation_string FROM activation WHERE user_id=$user_id";
		if (! $sth = @mysql_query($sql)) {
			die(getDBErrorXML());
		}
		if ($row = mysql_fetch_row($sth)) {
			$activation_string = $row[0];
			include("sendactivation.php");
		} else {
			die ('<ldbn type="msg"><msg type="warn">Activation string is not set for user with email:'.$user_mail.'.</msg></ldbn>');
		}
	} else {
		die ('<ldbn type="msg"><msg type="warn">No user with such an email: '.$user_mail.'.</msg></ldbn>');
	}
}

?>