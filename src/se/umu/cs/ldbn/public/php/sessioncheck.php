<?php
require_once("opendb.php");
require_once("checkinput.php");
$id_session;
$id_user;

if (isset($_POST['id_user']) && isset($_POST['id_session'])) {
	$id_user = $_POST['id_user'];
	checkID($id_user);
	$id_session = $_POST['id_session'];
	checkMD5($id_session);
	$sql = "SELECT user_id, added_on FROM session WHERE session_string='$id_session' AND user_id=$id_user";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		
	} else {
		die ('<ldbn type="msg"><msg type="warn">Session ID is invalid.\nTry to login again.</msg></ldbn>');
	}
} else {
	die ('<ldbn type="msg"><msg type="warn">Session arguments are missing.\nTry to login again.</msg></ldbn>');
}


?>
