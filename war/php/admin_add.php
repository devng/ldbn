<?php
/**
 * Add existing users to the list of administrators.
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

$admin_id;
if (isset ( $_POST ['admin_id'] ) && $_POST ['admin_id'] != "") {
	$admin_id = $_POST ['admin_id'];
	checkID ( $admin_id );
} else {
	dieWithXMLMessage ( "Some arguments are not set." );
}

if ($is_admin) {
	if (strcasecmp ( $user_id, $admin_id ) == 0) {
		dieWithXMLMessage ( 'You cannot change your own rights.' );
	}
	$sql = "UPDATE user SET is_admin=1 WHERE user_id=" . $admin_id;
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	} else {
		dieWithXMLMessage ( 'The user was successfully added to the  list.', 'ok' );
	}
} else {
	dieWithXMLMessage ( $msg_insufficient_privileges );
}

?>