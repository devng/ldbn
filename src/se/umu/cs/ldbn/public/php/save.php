<?php
include("opendb.php");
	$xml;
	$name;
	if (isset($_POST['name'])) {
		$name = $_POST['name'];
	}
	
	if (isset($_POST['xml'])) {
		$xml = $_POST['xml'];
	}
	
	if(isset($xml) & isset($name)) {
		$sql = 'INSERT INTO assignment(name, xml) VALUES (\''.$name.'\', \''.$xml.'\');';
		@mysql_query($sql) or die (getDBErrorXML());
		echo ('<ldbn type="msg">' .
				'<msg type="ok">Assignment is stored in the DB.</msg>' .
			  '</ldbn>');
	} else {
		echo ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
	};
	
include("closedb.php");
?>