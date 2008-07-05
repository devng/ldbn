<?php
require_once("sessioncheck.php");
$sql = "DELETE FROM session WHERE session_string='$id_session'";
@mysql_query($sql) or die (getDBErrorXML());
echo ('<ldbn type="msg">' .
		'<msg type="ok">Logout successful.</msg>' .
	  '</ldbn>');

?>