<?php
require_once("sessioncheck.php");

$admin_id;
if (isset($_POST['admin_id']) && $_POST['admin_id'] != "") {
	$admin_id = $_POST['admin_id'];
	checkID($admin_id);
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}

if ($is_su) {
	if(strcasecmp($user_id, $admin_id) == 0) {
		die ('<ldbn type="msg">' .
			'<msg type="error">You cannot change your own rights.</msg>' .
			'</ldbn>');
	}
	$sql = "UPDATE user SET is_admin=0, is_su=0 WHERE user_id=".$admin_id;
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	} else {
		$rows = 0;
		$rows = sqlite_changes($dbhandle);
		if ($rows > 0) {
			echo ('<ldbn type="msg">' .
			'<msg type="ok">The user was successfully removed from the administrator list.</msg>' .
			'</ldbn>');
		} else {
			echo ('<ldbn type="msg">' .
			'<msg type="error">The administrator list could not be updated.</msg>' .
			'</ldbn>');
		}
	}
} else {
	die ('<ldbn type="msg"><msg type="error">Insufficient rights! You are not a super user.</msg></ldbn>');
}

?>