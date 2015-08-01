<?php
/**
 * List all (active) users. This function is usable only by administrator. 
 */
require_once ("common.php");

$dbhandle = openDB ();

list ( $id_session, $user_id, $is_su, $is_admin ) = checkSession ( $dbhandle );

if ($is_admin) {
	
	$sql = "SELECT user_id, name, is_admin, is_su
        FROM user
        ORDER BY name";
	
	if ($use_account_email_activation) {
		$sql = "SELECT user_id, name, is_admin, is_su
        FROM user
        WHERE active=1
        ORDER BY name";
	}
	
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	
	echo '<ldbn type="user_list">';
	
	foreach ( $sth as $row ) {
		echo ("<user_entry id=\"$row[0]\" name=\"$row[1]\" is_admin=\"$row[2]\" is_su=\"$row[3]\" />");
	}
	
	echo '</ldbn>';
} else {
	dieWithXMLMessage ( $msg_insufficient_privileges );
}

?>