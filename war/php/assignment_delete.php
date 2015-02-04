<?php
/**
 * Delete assignments from the database. Assignments can be deleted only
 * by the author of the assigment of by super users.
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

function deleteAssignment($dbhandle, $id) {
	$sql = "DELETE FROM assignment WHERE id=" . $id;
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	$sql = "DELETE FROM comment WHERE assignment_id=" . $id;
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	dieWithXMLMessage ( 'The assignment was successfully removed from the DB.', 'ok' );
}

$assignment_id;
if (isset ( $_POST ['assignment_id'] )) {
	$assignment_id = $_POST ['assignment_id'];
	checkID ( $assignment_id );
	// If the user is supper user then delete imedeatelly
	if ($is_su) {
		deleteAssignment ( $dbhandle, $assignment_id );
	}
	// otherwise check if the user is permitted to delete the asssignment
	$sql = "SELECT user_id FROM assignment WHERE id=$assignment_id";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		$submitted_by_id = $row [0];
		$sql = "SELECT user_id, is_admin, is_su FROM user WHERE user_id=$submitted_by_id";
		$is_admin2 = false;
		$is_su2 = false;
		if (! $sth = $dbhandle->query ( $sql )) {
			dieWithXMLMessage ( $msg_db_error );
		}
		if ($row = $sth->fetch ()) {
			$is_admin2 = $row [1] == "1";
			$is_su2 = $row [2] == "1";
		}
		// the user who submitted the assignment and the current user are the same
		if (strcasecmp ( $user_id, $submitted_by_id ) == 0) {
			deleteAssignment ( $dbhandle, $assignment_id );
		}
		// the user who submitted the assignment is an admin => error
		if ($is_admin2) {
			dieWithXMLMessage ( 'You cannot delete assignments submitted by other administrators.', 'warn' );
		}
		// the user who submitted the assignment is not an admin and the current user is
		if ($is_admin) {
			deleteAssignment ( $dbhandle, $assignment_id );
		}
	} else {
		dieWithXMLMessage ( 'The requested assignment is not present in the DB.', 'warn' );
	}
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}
?>
