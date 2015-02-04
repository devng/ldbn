<?php
/**
 * Upload a file to the server and echo it back to the client. 
 * The file is not persistet.
 */
require_once ("common.php");

$fileType = $_FILES ['clientfile'] ['type'];
checkFileType ( $fileType );
$fileSize = $_FILES ['clientfile'] ['size'];
if ($fileSize > 512000) { // 512 KB
	dieWithXMLMessage ( 'File is too big. Max 512KB.' );
}

if ($_FILES ['clientfile'] ['error'] > 0) {
	dieWithXMLMessage ( 'File error: ' . $_FILES ['clientfile'] ['error'] );
}

if (is_uploaded_file ( $_FILES ['clientfile'] ['tmp_name'] )) {
	$fileData = file_get_contents ( $_FILES ['clientfile'] ['tmp_name'] );
	// header('Content-type: text/plain');
	echo "" . $fileData; // otherwise not the whole responce is beening shown
} else {
	dieWithXMLMessage ( 'Cannot read file.' );
}

?>