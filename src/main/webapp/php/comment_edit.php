<?php
/**
 * This script is used to edit and delete comments. Comments may only be 
 * edited/deleted by admins or the user who created them.
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

$id_comment; // comment id
$comment; // comment text
$delete = false; // must the comment be deleted
                 // check user input
if (isset ( $_POST ['id_comment'] ) && $_POST ['id_comment'] != "") {
	$id_comment = $_POST ['id_comment'];
	checkID ( $id_comment );
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}
// the delete argument is optional
if (isset ( $_POST ['delete'] ) && $_POST ['delete'] == "true") {
	$delete = true;
} else if (isset ( $_POST ['comment'] ) && $_POST ['comment'] != "") {
	$comment = $_POST ['comment'];
	checkBase64 ( $comment );
}

if (! $delete && ! isset ( $comment )) {
	// the comment is not set and no delete flag is set, nothing to do
	dieWithXMLMessage ( 'Some arguments are missing.' );
}

$sql;
if ($delete) {
	$sql = "DELETE FROM comment WHERE id=" . $id_comment;
	if (! $is_admin) {
		$sql .= " AND user_id=" . $user_id;
	}
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	} else {
		$rows = 0;
		$rows = sqlite_changes ( $dbhandle );
		if ($rows > 0) {
			dieWithXMLMessage ( 'The comment was successfully deleted.', 'ok' );
		} else {
			dieWithXMLMessage ( 'The comment could not be deleted.' );
		}
	}
} else {
	$sql = "UPDATE comment SET comment_val = '$comment', modified_on = CURRENT_TIMESTAMP WHERE id=" . $id_comment;
	if (! $is_admin) {
		$sql .= " AND user_id=" . $user_id;
	}
	
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	} else {
		$rows = 0;
		$rows = sqlite_changes ( $dbhandle );
		if ($rows > 0) {
			dieWithXMLMessage ( 'The comment was successfully updated.', 'ok' );
		} else {
			dieWithXMLMessage ( 'The comment could not be updated.' );
		}
	}
}

?>