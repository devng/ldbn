<html>
<head>
<title>LDBN - User account activation</title>
</head>
<body>
<?php
/**
 * This page is used as a link for the email account activation.
 */
require_once ("common.php");

$dbhandle = openDB ();

if ($use_account_email_activation && isset ( $_GET ['a'] )) {
	$a = $_GET ['a'];
	checkSha256 ( $a );
	$sql = "SELECT user_id FROM activation WHERE activation_string='$a'";
	if (! $sth = $dbhandle->query ( $sql )) {
		dieWithXMLMessage ( $msg_db_error );
	}
	if ($row = $sth->fetch ()) {
		$user_id = $row [0];
		$sql = "UPDATE user SET is_active=1 WHERE user_id=$user_id";
		if (! $sth = $dbhandle->query ( $sql )) {
			dieWithXMLMessage ( $msg_db_error );
		}
		echo ('<P><B>Your account is now activated. <B><BR>You can go to the ' . '<A HREF="http://' . $ldbnhost . '">main page</A>' . ' and login with your username and password.');
		$sql = "DELETE FROM activation WHERE activation_string='$a'";
		$dbhandle->query ( $sql );
	} else {
		echo ("<P>Invalid activation string.</P>");
	}
} else {
	echo ("<P>$msg_missing_arguments</P>");
}
?>
</body>
</html>