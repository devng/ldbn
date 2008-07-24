<?php
require_once("opendb.php");
require_once("checkinput.php");

$user_name;
$user_pass;
$user_id;

//clear old sessions
$sql = "DELETE
		FROM session  
		WHERE DATEDIFF(CURRENT_TIMESTAMP , added_on ) > 0 ";
@mysql_query($sql);

if (isset($_POST['user_name']) && isset($_POST['user_pass'])) {
	$user_name = $_POST['user_name'];
	checkName($user_name);
	$user_pass = $_POST['user_pass'];
	checkMD5($user_pass);
	$sql = "SELECT user_id, active, email FROM user WHERE name='$user_name' AND pass_md5='$user_pass'";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		$user_id =  $row[0];
		$user_active = $row[1];
		$user_email = $row[2];
		if(isset($user_active) && $user_active != 0) {
			$tmp1 = time(); $tmp2 = rand();
			$session_string = md5("$tmp1;$tmp2");
			$sql = "INSERT INTO session (session_string, user_id) VALUES ('$session_string', $user_id)";
			if (! $sth = @mysql_query($sql)) {
				die(getDBErrorXML());
			}
			echo("<ldbn type=\"session\"> 
					<session id=\"$session_string\" /> 
					<user id=\"$user_id\" /> 
					<email val=\"$user_email\" />	
				</ldbn>");
		} else {
			die ('<ldbn type="msg"><msg type="warn">User account is not activated.</msg></ldbn>');
		}
	} else {
		die ('<ldbn type="msg"><msg type="warn">Username or password is incorrect.</msg></ldbn>');
	}
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
?>