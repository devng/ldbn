<?php
/**
 * Logs out the user by deleting his session. Any sessions older than one day are also removed.
 */
require_once ("common.php");

$dbhandle = openDB ();

$id_session;

if (isset ( $_POST ['id_session'] )) {
	$id_session = $_POST ['id_session'];
	checkSha256 ( $id_session );
	
	// delete the session
	$sql = "DELETE FROM session WHERE session_string = '$id_session'";
	$dbhandle->query ( $sql ) or dieWithXMLMessage ( $msg_db_error );
	
	// but also delete all session older than one day
	deleteOldSessions( $dbhandle );
	
	dieWithXMLMessage ( 'Logout successful.', 'ok' );
}

?>