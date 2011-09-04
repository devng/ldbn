<?php
require_once("common.php");
$fileType = $_FILES['clientfile']['type']; 
checkFileType($fileType);
$fileSize = $_FILES['clientfile']['size']; 
if($fileSize > 512000) { //512 KB
	die ('<ldbn type="msg">' .
		'<msg type="error">File is too big. Max 512KB.</msg>'.
		'</ldbn>');
}

if ($_FILES['clientfile']['error'] > 0) {
    die ('<ldbn type="msg">' .
		'<msg type="error">File error: '.$_FILES['clientfile']['error'].'</msg>'.
		'</ldbn>');
}

if (is_uploaded_file($_FILES['clientfile']['tmp_name'])) {
	$fileData = file_get_contents($_FILES['clientfile']['tmp_name']);
	//header('Content-type: text/plain');
	echo "@".$fileData; //otherwise not the whole responce is beening shown
} else {
	die('<ldbn type="msg">' .
		'<msg type="error">File cannot be readed.</msg>'.
		'</ldbn>');
}

?>