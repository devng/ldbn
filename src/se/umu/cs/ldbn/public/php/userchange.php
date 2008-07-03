<?php
require_once("opendb.php");
require_once("checkinput.php");
$user_id;
if(isset($_POST['user_id'])) {
	$user_id = $_POST['user_id'];
	checkID($user_id);
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}

if (isset($_POST['user_name'])) {
	$user_name = $_POST['user_name'];
	checkName($user_name);
	$sql = "UPDATA user SET name=$user_name WHERE user_id=$user_id";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
}

if (isset($_POST['user_pass'])) {
	$user_pass = $_POST['user_pass'];
	checkMD5($user_pass);
	$sql = "UPDATA user SET pass=$user_pass WHERE user_id=$user_id";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
}

if (isset($_POST['user_mail'])) {
	$user_mail = $_POST['user_mail'];
	checkMail($user_mail);
	$sql = "UPDATA user SET email=$user_mail WHERE user_id=$user_id";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
}
?>