<?php
/**
 * Download an assignment as an XML file. The XML is send by the client.
 */
if (isset ( $_POST ['xml'] )) {
	header ( 'Content-type: text/xml' );
	header ( 'Content-Disposition: attachment; filename="ldbn.xml"' );
	$xml = $_POST ['xml'];
	echo ($xml);
}
?>