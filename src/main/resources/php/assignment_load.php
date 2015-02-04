<?php
/**
 * Load a single assignment from the backend.
 */
require_once ("common.php");

$dbhandle = openDB ();

$assignment_id;
if (isset ( $_POST ['assignment_id'] )) {
	$assignment_id = $_POST ['assignment_id'];
	checkID ( $assignment_id );
	$sql = "SELECT xml, name FROM assignment WHERE id=$assignment_id";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		echo str_replace ( "type=\"assignment\"", "type=\"assignment\" name=\"$row[1]\"", "$row[0]" );
	} else {
		dieWithXMLMessage ( 'No assignments found.', 'warn' );
	}
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}
?>