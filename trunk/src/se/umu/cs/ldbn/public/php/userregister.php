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
	$sql = "SELECT user_id FROM user WHERE name='$user_name'";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		die ('<ldbn type="msg"><msg type="warn">Username is already taken.</msg></ldbn>');
	}
	$sql = "SELECT user_id FROM user WHERE email='$user_mail'";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		die ('<ldbn type="msg"><msg type="warn">Email address already exists.</msg></ldbn>');
	}
	$sql = "INSERT INTO user (name, pass_md5, email) VALUES ('$user_name', '$user_pass', '$user_mail')";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	$sql = "SELECT user_id FROM user WHERE email='$user_mail'";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		$user_id = $row[0];
		include("sendactivation.php");
	}
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
?>