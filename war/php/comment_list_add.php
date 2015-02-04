<?php
/**
 * This script lists all comments for a given assignment. 
 * 
 * However, if the parameter comments is set, then a comment is added to the 
 * database for that assignment and then only that comment is returned to the client
 */
require_once ("common.php");

$dbhandle = openDB ();

$assignment_id;
if (isset ( $_POST ['aid'] ) && $_POST ['aid'] != "") {
	$assignment_id = $_POST ['aid'];
	checkID ( $assignment_id );
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}
// create new comment return only a list with the new comment
if (isset ( $_POST ['comment'] ) && $_POST ['comment'] != "") {
	list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );
	$comment = $_POST ['comment'];
	checkBase64 ( $comment, "Invalid comment." );
	$sql = "INSERT INTO comment(assignment_id, user_id, comment_val) VALUES ('$assignment_id', '$user_id', '$comment');";
	$dbhandle->query ( $sql ) or dieWithXMLMessage ( $msg_db_error );
	$sql = "SELECT c.id, c.user_id, u.name, c.modified_on, c.comment_val
        FROM comment AS c, user AS u
        WHERE c.user_id=u.user_id AND assignment_id=$assignment_id AND c.user_id=$user_id
        ORDER BY c.id DESC
        LIMIT 1";
} else { // return all comments
	$sql = "SELECT c.id, c.user_id, u.name, c.modified_on, c.comment_val
        FROM comment AS c, user AS u
        WHERE c.user_id=u.user_id AND assignment_id=$assignment_id
        ORDER BY c.id";
}

if (! $sth = $dbhandle->query ( $sql )) {
	dieWithXMLMessage ( $msg_db_error );
}

echo '<ldbn type="comments_list" assignment_id="' . $assignment_id . '">';
// NB this could be empty !!!
foreach ( $sth as $row ) {
	echo ("<comment id=\"$row[0]\" author_id=\"$row[1]\" author=\"$row[2]\" last_modified=\"$row[3]\">$row[4]</comment>");
}

echo '</ldbn>';

?>