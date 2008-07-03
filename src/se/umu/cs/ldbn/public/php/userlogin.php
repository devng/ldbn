<?php
require_once("opendb.php");
require_once("checkinput.php");

	$user_name;
	$user_pass;
	$user_id;
	if (isset($_POST['user_name']) && isset($_POST['user_pass'])) {
		$user_name = $_POST['user_name'];
		checkName($user_name);
		$user_pass = $_POST['user_pass'];
		checkMD5($user_pass);
		$sql = "SELECT id, active FROM user WHERE name=$user_name AND pass=$user_pass";
		if (! $sth = @mysql_query($sql)) {
			die(getDBErrorXML());
		}
		if ($row = mysql_fetch_row($sth)) {
			$user_id =  $row[0];
			$user_active = $row[1];
			if(isset($user_active) && $user_active != 0) {
				die ('<ldbn type="msg"><msg type="ok">'.$user_id.'</msg></ldbn>');
			} else {
				die ('<ldbn type="msg"><msg type="warn">User account is not activated.</msg></ldbn>');
			}
		} else {
			die ('<ldbn type="msg"><msg type="warn">Username or password is incorrect.</msg></ldbn>');
		}
	} else {
		die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
	}
?>