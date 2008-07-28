<?php
if (isset($_POST['xml'])) {
	header('Content-type: text/xml');
	header('Content-Disposition: attachment; filename="ldbn.xml"');
	$xml = $_POST['xml'];
	echo($xml);
}
?>