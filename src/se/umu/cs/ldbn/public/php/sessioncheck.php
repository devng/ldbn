<?php
require_once("opendb.php");
require_once("common.php");
$id_session;
$user_id;

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
		
	} else {
		die ('<ldbn type="msg"><msg type="warn">Session ID is invalid.\n Try to login again.</msg></ldbn>');
	}
} else {
	die ('<ldbn type="msg"><msg type="warn">Session arguments are missing.\n Try to login again.</msg></ldbn>');
}


?>
