<?php
/**
 * Remove existing users from the list of administrators. The user is not deleted.
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

$admin_id;
if (isset ( $_POST ['admin_id'] ) && $_POST ['admin_id'] != "") {
	$admin_id = $_POST ['admin_id'];
	checkID ( $admin_id );
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}

if ($is_su) {
	if (strcasecmp ( $user_id, $admin_id ) == 0) {
		dieWithXMLMessage ( 'You cannot change your own rights.', 'warn' );
	}
	$sql = "UPDATE user SET is_admin=0, is_su=0 WHERE user_id=" . $admin_id;
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	} else {
		$rows = 0;
		$rows = sqlite_changes ( $dbhandle );
		if ($rows > 0) {
			dieWithXMLMessage ( 'The user was successfully removed from the administrator list.', 'ok' );
		} else {
			dieWithXMLMessage ( 'The administrator list could not be updated.' );
		}
	}
} else {
	dieWithXMLMessage ( $msg_insufficient_privileges );
}

?>