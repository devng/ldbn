<?php
require_once("opendb.php");
require_once("checkinput.php");
$id_session;

if (isset($_POST['id_session'])) {
	$id_session = $_POST['id_session'];
	checkMD5($id_session);
	//kills the session, but also all session older than one day
	$sql = "DELETE
			FROM session  
			WHERE session_string = '$id_session' 
			OR DATEDIFF(CURRENT_TIMESTAMP , added_on ) > 0 ";
	@mysql_query($sql) or die (getDBErrorXML());
	echo ('<ldbn type="msg">' .
		'<msg type="ok">Logout successful.</msg>' .
	  '</ldbn>');
}

?>