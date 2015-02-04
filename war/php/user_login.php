<?php
/**
 * Logs in a user by checking his credentials and if successful creating an unique session for him.
 * The session is then used for authentication the user. Furthermore, all sessions older than one day 
 * are deleted to minimize the risk of session hijacking.
 * 
 * Note that the client always sends the password encoded in Base64. This is needed not for security purposes,
 * but rather to ensure the correct transportation of any special characters. 
 */
require_once ("common.php");

$dbhandle = openDB ();

$user_name;
$user_pass;
$user_id;
$is_admin = 0; // administrator
$is_su = 0; // super_user
            
// clear old sessions
deleteOldSessions($dbhandle);

if (isset ( $_POST ['user_name'] ) && isset ( $_POST ['user_pass'] )) {
	$user_name = $_POST ['user_name'];
	checkName ( $user_name );
	
	$user_pass = $_POST ['user_pass'];
	checkBase64 ( $user_pass );
	$user_pass = base64_decode ( $user_pass );
	
	$sql = "SELECT * FROM user WHERE name='$user_name'";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	
	if ($row = $sth->fetch ()) {
		$user_id = $row ['user_id'];
		$user_name = $row ['name'];
		$user_pass_hash = $row ['pass_hash'];
		$user_pass_salt = $row ['pass_salt'];
		$user_email = $row ['email'];
		$user_is_active = $row ['is_active'];
		$is_admin = $row ['is_admin'];
		$is_su = $row ['is_su'];
		
		if ($use_account_email_activation == false) {
			// if we do not use email activation mark all users as active
			$user_is_active = 1;
		}
		
		// verify user password
		$pass_hash_new = generatePasswordHash ( $user_pass, $user_pass_salt );
		if ($pass_hash_new != $user_pass_hash) {
			dieWithXMLMessage ( $msg_wrong_credentials, 'warn' );
		}
		
		if (isset ( $user_is_active ) && $user_is_active != 0) {
			$tmp1 = time ();
			$tmp2 = rand ();
			$session_string = generatePasswordHash ( "$tmp1", "$tmp2" );
			$sql = "INSERT INTO session (session_string, user_id) VALUES ('$session_string', $user_id)";
			if (! $sth = $dbhandle->query ( $sql )) {
				dieWithXMLMessage ( $msg_db_error );
			}
			echo ("<ldbn type=\"session\"> 
					<session id=\"$session_string\" /> 
					<user id=\"$user_id\" is_admin=\"$is_admin\" is_su=\"$is_su\"/> 
					<email val=\"$user_email\" />
				</ldbn>");
		} else {
			dieWithXMLMessage ( 'User account is not activated.', 'warn' );
		}
	} else {
		dieWithXMLMessage ( $msg_wrong_credentials, 'warn' );
	}
} else {
	dieWithXMLMessage ( $msg_missing_arguments );
}
?>