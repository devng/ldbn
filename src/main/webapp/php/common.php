<?php
/**
 * Contains some common functions and variables which are used across the whole backend.
 */
require_once ("config.php");

// =============================================================================
// Common Messages
// =============================================================================

$msg_db_error = 'Request failed due to a database error.';
$msg_email_disabled = 'Email activation is disabled.';
$msg_missing_arguments = 'Some arguments are not set.';
$msg_wrong_credentials = 'Username or password is incorrect.';
$msg_insufficient_privileges = 'Insufficient privileges.';

// =============================================================================
// Common Functions
// =============================================================================

/**
 * Generates a random password/string.
 */
function generatePassword($length = 8) {
	// start with a blank password
	$password = "";
	// define possible characters
	$possible = "0123456789bcdfghjkmnpqrstvwxyz";
	// set up a counter
	$i = 0;
	// add random characters to $password until $length is reached
	while ( $i < $length ) {
		
		// pick a random character from the possible ones
		$char = substr ( $possible, mt_rand ( 0, strlen ( $possible ) - 1 ), 1 );
		
		// we don't want this character if it's already in the password
		if (! strstr ( $password, $char )) {
			$password .= $char;
			$i ++;
		}
	}
	// done!
	return $password;
}

/**
 * Generates a passowrd hash using Sha256.
 * Optionaly provide a salt value for the password.
 */
function generatePasswordHash($user_pass, $pass_salt = '') {
	$pass_hash = hash ( "sha256", $user_pass . $pass_salt );
	return $pass_hash;
}

/**
 * The PHP script dies with an LDBN error XML message and type.
 * The message is presented by the front end.
 */
function dieWithXMLMessage($msg, $type = "error") {
	die ( '<ldbn type="msg"><msg type="' . $type . '">' . $msg . '</msg></ldbn>' );
}

/**
 * Check if the provided string is a valid ID, i.e., it is an integer string.
 */
function checkID($id, $error_msg = "Invalid ID.") {
	if (! preg_match ( "/^[0-9]+$/D", $id )) {
		dieWithXMLMessage ( $error_msg );
	}
}

/**
 * Check if the provided string is a valid user name.
 */
function checkName($name, $error_msg = "Invalid name.") {
	if (! preg_match ( "/^([\w]|\-){1,20}$/D", $name )) {
		dieWithXMLMessage ( $error_msg );
	}
}

/**
 * Check if a string contains only hexadecimal chars, its length must be 64 chars.
 * See sha256 algorithm.
 */
function checkSha256($str, $error_msg = "Invalid string.") {
	if (! preg_match ( "/^([0-9a-fA-F]){64}$/D", $str )) {
		dieWithXMLMessage ( $error_msg );
	}
}

/**
 * Check if a string consists only of base64 characters.
 */
function checkBase64($str, $error_msg = "Invalid string.") {
	if (! preg_match ( "/[0-9a-zA-Z\+\/\=]+/", $str )) {
		dieWithXMLMessage ( $error_msg );
	}
}

/**
 * Check if the email maches a predefined email regex.
 */
function checkMail($mail, $error_msg = "Invalid email.") {
	if (! preg_match ( "/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,10}$/D", $mail )) {
		dieWithXMLMessage ( $error_msg );
	}
}

/**
 * Check the MIME file type for text based files.
 */
function checkFileType($type, $error_msg = "Invalid file type.") {
	if (! isset ( $type ) || ! preg_match ( "/text.*/", $type )) {
		dieWithXMLMessage ( $error_msg );
	}
}

// =============================================================================
// Common DataBase Functions
// =============================================================================

/**
 * Open and return a connection to the database.
 */
function openDB() {
	global $db_url;
	$dbhandle = new PDO ( $db_url ) or dieWithXMLMessage ( 'Cannot open the database file.' );
	$dbhandle->setAttribute ( PDO::ATTR_ERRMODE, PDO::ERRMODE_WARNING );
	return $dbhandle;
}

/**
 * Returns true if the database is a MySQL one, otherwise we assume SQLite3 and return false.
 */
function isMySqlDB() {
	global $db_url;
	if (strpos ( $db_url, 'mysql' )) {
		return true;
	} else {
		return false;
	}
}

/**
 * Changes the user's password hash and salt in the database.
 */
function changeUserPassword($dbhandle, $user_id, $user_pass, $pass_salt) {
	$pass_hash = generatePasswordHash ( $user_pass, $pass_salt );
	$sql = "UPDATE user SET pass_hash ='$pass_hash', pass_salt = '$pass_salt' WHERE user_id=$user_id;";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
}

/**
 * Checks if the provided user name already exists in the DB.
 */
function checkNameUnique($dbhandle, $user_name) {
	$sql = "SELECT user_id FROM user WHERE name='$user_name'";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		dieWithXMLMessage ( 'Username is already taken.', 'warn' );
	}
}

/**
 * Checks if the provided user name already exists in the DB.
 */
function checkMailUnique($dbhandle, $user_mail) {
	$sql = "SELECT user_id FROM user WHERE email='$user_mail'";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		dieWithXMLMessage ( 'Email address already exists.', 'warn' );
	}
}

/**
 * Check the user's session by verifying if the session is in the database, if not die with an error.
 * The request POST parameters id_user and id_session must be set.
 */
function checkSession($dbhandle) {
	$id_session;
	$user_id;
	$is_su = false; // does the user have super user (su) rights
	$is_admin = false; // does the user have administrator rights, is true if the user also has su rights
	
	if (isset ( $_POST ['id_user'] ) && isset ( $_POST ['id_session'] )) {
		$user_id = $_POST ['id_user'];
		checkID ( $user_id );
		$id_session = $_POST ['id_session'];
		checkSha256 ( $id_session );
		$sql = "SELECT user_id, added_on FROM session WHERE session_string='$id_session' AND user_id=$user_id";
		if (! $sth = $dbhandle->query ( $sql )) {
			dieWithXMLMessage ( $msg_db_error );
		}
		if ($row = $sth->fetch ()) {
			// checks if the user is admin and/or super user
			$sql = "SELECT user_id, is_admin, is_su FROM user WHERE user_id=$user_id";
			if (! $sth = $dbhandle->query ( $sql )) {
				dieWithXMLMessage ( $msg_db_error );
			}
			if ($row = $sth->fetch ()) {
				$is_admin = $row [1] == "1";
				$is_su = $row [2] == "1";
			}
		} else {
			dieWithXMLMessage ( 'Your session has expired.\n Try to login again.', 'warn' );
		}
	} else {
		dieWithXMLMessage ( 'Session arguments are missing.\n Try to login again.', 'warn' );
	}
	
	return array (
			$id_session,
			$user_id,
			$is_su,
			$is_admin 
	);
}

/**
 * Clears sessions older than one day from the DB.
 */
function deleteOldSessions($dbhandle) {
	$sql;
	if (isMySqlDB ()) {
		$sql = "DELETE FROM session WHERE DATEDIFF(CURRENT_TIMESTAMP, added_on) > 0";
	} else {
		$sql = "DELETE FROM session WHERE JULIANDAY('now') - JULIANDAY(added_on) > 0";
	}
	$dbhandle->query ( $sql );
}

/**
 * Sends an activation email to to user which contains a link with an activation randrom string.
 *
 * NB: The variable $activation_string in optional and if it is not provided a new one will be generated
 * and stored in the db.
 */
function sendEmailActivation($dbhandle, $user_id, $user_name, $user_mail, $activation_string = NULL) {
	global $ldbnhost, $ldbnemail, $use_account_email_activation;
	
	if (! $use_account_email_activation) {
		dieWithXMLMessage ( 'Registration successful. You can now login with your username and password.', 'ok' );
	}
	
	if (! is_null ( $activation_string )) {
		$tmp1 = time ();
		$tmp2 = rand ();
		$activation_string = generatePasswordHash ( "$tmp1", "$tmp2" );
		$sql = "INSERT INTO activation (activation_string, user_id) VALUES ('$activation_string', $user_id)";
		if (! $sth = $dbhandle->query ( $sql )) {
			dieWithXMLMessage ( $msg_db_error );
		}
	}
	
	$user_link = "http://$ldbnhost/php/email_activation_page.php?a=$activation_string";
	
	// send mail
	$subject = "{$ldbnhost} User account activation";
	$message = "Thank You for your registration.\n\n
			Below is your $ldbnhost account information:\n\n
			Username  : $user_name\n
			Email     : $user_mail\n
			Password  : <Cannot be shown>\n\n
			In order to verify your account, please  follow the link bellow. If the link does not work, try to copy it and then paste it in the browser.\n\n
			$user_link";
	$headers = "From: $ldbnemail\r\nReply-To: $ldbnemail";
	$mail_sent = mail ( $user_mail, $subject, $message, $headers );
	if ($mail_sent) {
		dieWithXMLMessage ( 'Activation mail has been sent.', 'ok' );
	} else {
		dieWithXMLMessage ( 'Could not send activation mail.' );
	}
}
?>
