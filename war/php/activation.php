<html>
<head>
<title>LDBN - User account activation</title>
</head>
<body>
<?php
require_once("opendb.php");
require_once("common.php");
	
if (isset($_GET['a'])) {
	$a = $_GET['a'];
	checkMD5($a);
	$sql = "SELECT user_id FROM activation WHERE activation_string='$a'";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		$user_id = $row[0];
		$sql = "UPDATE user SET active=1 WHERE user_id=$user_id";
		if (! $sth = @mysql_query($sql)) {
			die(getDBErrorXML());
		}
		echo ('<P><B>Your account is now activated. <B><BR>You can go to the ' .
			  '<A HREF="http://'.$ldbnhost.'">main page</A>' .
    		  ' and login with your username and password.');
    	$sql = "DELETE FROM activation WHERE activation_string='$a'";
    	@mysql_query($sql);
	} else {
		echo ("<P>Invalid activation string.</P>");
	}
} else {
	echo ('<P>Some arguments are not set.</P>');
}
?>
</body>
</html>