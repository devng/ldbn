<?php
//require_once("opendb.php");
//require_once("checkinput.php");
require_once("sessioncheck.php");
$xml;
$name;
$id;
$sql;

if (isset($_POST['name'])) {
	$name = $_POST['name'];
}

if (isset($_POST['xml'])) {
	$xml = $_POST['xml'];
}

if (isset($_POST['id']) && $_POST['id'] != "") {
	$id = $_POST['id'];
}

if(isset($id) && $id != "") {
	if(isset($xml) && isset($name)) {
		checkID($id);
		checkName($name);
		$sql = 'UPDATE assignment SET name=\''.$name.'\', xml=\''.$xml.'\' WHERE id='.$id.';';
	} else {
		die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
	}
} else {
	if(isset($xml) && isset($name)) {
		checkName($name);
		$sql = "INSERT INTO assignment(user_id, name, xml) VALUES ('$user_id', '$name', '$xml');";
	} else {
		die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
	};
}
@mysql_query($sql) or die (getDBErrorXML());
echo ('<ldbn type="msg">' .
		'<msg type="ok">Assignment is stored in the DB.</msg>' .
	  '</ldbn>');
?>