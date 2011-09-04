<?php
require_once("sessioncheck.php");

if ($is_admin) {
	$sql = "SELECT user_id, name, is_admin, is_su
        FROM user 
        WHERE active=1
        ORDER BY name";
        
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	
	echo '<ldbn type="user_list">';
	
	while ($row = mysql_fetch_row($sth)) {
		echo ("<user_entry id=\"$row[0]\" name=\"$row[1]\" is_admin=\"$row[2]\" is_su=\"$row[3]\" />");
	}

	echo '</ldbn>';
	
} else {
	die ('<ldbn type="msg"><msg type="error">You are not an administrator.</msg></ldbn>');
}


?>