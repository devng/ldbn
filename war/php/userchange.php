<?php
require_once("sessioncheck.php");

if (isset($_POST['user_name'])) {
	$user_name = $_POST['user_name'];
	checkName($user_name);
	$sql = "UPDATE user SET name = '$user_name' WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
}

if (isset($_POST['user_pass'])) {
	$user_pass = $_POST['user_pass'];
	checkMD5($user_pass);
	$sql = "UPDATE user SET pass_md5 ='$user_pass' WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
}

if (isset($_POST['user_mail'])) {
	$user_mail = $_POST['user_mail'];
	checkMail($user_mail);
	$sql = "SELECT user_id FROM user WHERE email='$user_mail'";
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
	if ($row = $sth->fetch()) {
		die ('<ldbn type="msg"><msg type="warn">Email address already exists.</msg></ldbn>');
	}
	$sql = "UPDATE user SET email='$user_mail', active=0 WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
	$sql = "SELECT name FROM user WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
	if ($row = $sth->fetch()) {
		$user_name = $row[0];
		include("sendactivation.php");
	}
} else {
?>
<ldbn type="msg">
	<msg type="ok">User data was changed successfully.</msg>
</ldbn>
<?php
}
?>