<?php
require_once("sessioncheck.php");

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
        
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
	
	echo '<ldbn type="user_list">';
	
	foreach ($sth as $row) {
		echo ("<user_entry id=\"$row[0]\" name=\"$row[1]\" is_admin=\"$row[2]\" is_su=\"$row[3]\" />");
	}

	echo '</ldbn>';
	
} else {
	die ('<ldbn type="msg"><msg type="error">You are not an administrator.</msg></ldbn>');
}


?>