<?php
/**
 * Save an assignment to the database.
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

$xml;
$name;
$id;
$sql;

if (isset ( $_POST ['name'] )) {
	$name = $_POST ['name'];
}

if (isset ( $_POST ['xml'] )) {
	$xml = $_POST ['xml'];
}

if (isset ( $_POST ['id'] ) && $_POST ['id'] != "") {
	$id = $_POST ['id'];
}

if (isset ( $id ) && $id != "") {
	if (isset ( $xml ) && isset ( $name )) {
		checkID ( $id );
		checkName ( $name );
		$sql = 'UPDATE assignment SET name=\'' . $name . '\', xml=\'' . $xml . '\', modified_on = CURRENT_TIMESTAMP WHERE id=' . $id . ';';
	} else {
		dieWithXMLMessage ( $msg_missing_arguments );
	}
} else {
	if (isset ( $xml ) && isset ( $name )) {
		checkName ( $name );
		$sql = "INSERT INTO assignment(user_id, name, xml) VALUES ('$user_id', '$name', '$xml');";
	} else {
		dieWithXMLMessage ( $msg_missing_arguments );
	}
	;
}

$dbhandle->query ( $sql ) or dieWithXMLMessage ( $msg_db_error );

dieWithXMLMessage ( 'Assignment is stored in the DB.', 'ok' );
?>