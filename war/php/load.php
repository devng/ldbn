<?php
require_once("opendb.php");
require_once("common.php");

$assignment_id;
if (isset($_POST['assignment_id'])) {
	$assignment_id = $_POST['assignment_id'];
	checkID($assignment_id);
	$sql = "SELECT xml, name FROM assignment WHERE id=$assignment_id";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		echo str_replace("type=\"assignment\"", "type=\"assignment\" name=\"$row[1]\"", "$row[0]");
	} else {
		echo ('<ldbn type="msg"><msg type="warn">DB did not returned any assignments.</msg></ldbn>');
	}
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
?>