<?php
/* 
 * This script is used to edit and delete comments. Comments may only be 
 * edited/deleted by admins or the user who created them.
 */
require_once("sessioncheck.php");

$id_comment; //comment id
$comment; //comment text
$delete = false; //must the comment be deleted
//check user input
if (isset($_POST['id_comment']) && $_POST['id_comment'] != "") {
	$id_comment = $_POST['id_comment'];
	checkID($id_comment);
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
//the delete argument is optional
if (isset($_POST['delete']) && $_POST['delete'] == "true") {
	$delete = true;
} else if (isset($_POST['comment']) && $_POST['comment'] != "") {
	$comment = $_POST['comment'];
	checkBase64($comment);
} 

if (!$delete && !isset($comment)) { 
	// the comment is not set and no delete flag is set, nothing to do
	die('<ldbn type="msg">' .
			'<msg type="error">Some arguments are missing.</msg>' .
			'</ldbn>');
}

$sql;
if ($delete) {
	$sql = "DELETE FROM comment WHERE id=".$id_comment;
	if(!$is_admin) {
		$sql .= " AND user_id=".$user_id;
	}
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	} else {
		$rows = 0;
		$rows = @mysql_affected_rows();
		if ($rows > 0) {
			echo ('<ldbn type="msg">' .
			'<msg type="ok">The comment was successfully deleted.</msg>' .
			'</ldbn>');
		} else {
			echo ('<ldbn type="msg">' .
			'<msg type="error">The comment could not be deleted.</msg>' .
			'</ldbn>');
		}
		
	}
} else {
	$sql = "UPDATE comment SET comment_val = '$comment' WHERE id=".$id_comment;
	if(!$is_admin) {
		$sql .= " AND user_id=".$user_id;
	}
	
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	} else {
		$rows = 0;
		$rows = @mysql_affected_rows();
		if ($rows > 0) {
			echo ('<ldbn type="msg">' .
			'<msg type="ok">The comment was successfully updated.</msg>' .
			'</ldbn>');
		} else {
			echo ('<ldbn type="msg">' .
			'<msg type="error">The comment could not be updated.</msg>' .
			'</ldbn>');
		}
	}
}
	
?>