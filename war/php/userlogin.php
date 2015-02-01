<?php
require_once("opendb.php");
require_once("common.php");

$user_name;
$user_pass;
$user_id;
$is_admin = 0; //administrator
$is_su = 0;    //super_user

//clear old sessions
$sql = "DELETE
		FROM session  
		WHERE JULIANDAY('now') - JULIANDAY(added_on) > 0 ";
$dbhandle->query($sql);

if (isset($_POST['user_name']) && isset($_POST['user_pass'])) {
	$user_name = $_POST['user_name'];
	checkName($user_name);
	$user_pass = $_POST['user_pass'];
	checkMD5($user_pass);
	$sql = "SELECT user_id, active, email, is_admin, is_su FROM user WHERE name='$user_name' AND pass_md5='$user_pass'";
	if (! $sth = $dbhandle->query($sql)) {
		die($db_error_xml);
	}
	if ($row = $sth->fetch()) {
		$user_id     = $row[0];
		$user_active = $row[1];
		$user_email  = $row[2];
		$is_admin    = $row[3];
		$is_su       = $row[4];

		if ($use_account_email_activation == false) {
			// if we do not use email activation mark all users as active
			$user_active = 1;
		}
		
		if(isset($user_active) && $user_active != 0) {
			$tmp1 = time(); $tmp2 = rand();
			$session_string = md5("$tmp1;$tmp2");
			$sql = "INSERT INTO session (session_string, user_id) VALUES ('$session_string', $user_id)";
			if (! $sth = $dbhandle->query($sql)) {
				die($db_error_xml);
			}
			echo("<ldbn type=\"session\"> 
					<session id=\"$session_string\" /> 
					<user id=\"$user_id\" is_admin=\"$is_admin\" is_su=\"$is_su\"/> 
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