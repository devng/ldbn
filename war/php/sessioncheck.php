<?php
require_once("opendb.php");
require_once("common.php");
$id_session;
$user_id;
$is_su = false; // does the user have super user (su) rights
$is_admin = false; // does the user have administrator rights, is true if the user also has su rights

if (isset($_POST['id_user']) && isset($_POST['id_session'])) {
	$user_id = $_POST['id_user'];
	checkID($user_id);
	$id_session = $_POST['id_session'];
	checkMD5($id_session);
	$sql = "SELECT user_id, added_on FROM session WHERE session_string='$id_session' AND user_id=$user_id";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		//checks if the user is admin and/or super user
	 	$sql = "SELECT user_id, is_admin, is_su FROM user WHERE user_id=$user_id";
	 	if (! $sth = @mysql_query($sql)) {
			die(getDBErrorXML());
		}
		if ($row = mysql_fetch_row($sth)) {
			$is_admin    = $row[1] == "1";
			$is_su       = $row[2] == "1";
		}
	} else {
		die ('<ldbn type="msg"><msg type="warn">Session ID is invalid.\n Try to login again.</msg></ldbn>');
	}
} else {
	die ('<ldbn type="msg"><msg type="warn">Session arguments are missing.\n Try to login again.</msg></ldbn>');
}


?>
