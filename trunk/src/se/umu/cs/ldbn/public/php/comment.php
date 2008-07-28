<?php
require_once("opendb.php");
require_once("common.php");

$assignment_id;
if (isset($_POST['aid']) && $_POST['aid'] != "") {
	$assignment_id = $_POST['aid'];
	checkID($assignment_id);
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
 //create new comment return only a list with the new comment
if (isset($_POST['comment']) && $_POST['comment'] != "") {
	require_once("sessioncheck.php");
	$comment = $_POST['comment'];
	checkBase64($comment);
	$sql = "INSERT INTO comment(assignment_id, user_id, comment_val) VALUES ('$assignment_id', '$user_id', '$comment');";
	@mysql_query($sql) or die (getDBErrorXML());
	$sql = "SELECT c.id, c.user_id, u.name, c.modified_on, c.comment_val
        FROM comment AS c, user AS u
        WHERE c.user_id=u.user_id AND assignment_id=$assignment_id AND c.user_id=$user_id
        ORDER BY c.id DESC
        LIMIT 1";
} else { //return all comments
	$sql = "SELECT c.id, c.user_id, u.name, c.modified_on, c.comment_val
        FROM comment AS c, user AS u
        WHERE c.user_id=u.user_id AND assignment_id=$assignment_id
        ORDER BY c.id";
}

if (! $sth = @mysql_query($sql)) {
	die(getDBErrorXML());
}

echo '<ldbn type="comments_list" assignment_id="'.$assignment_id.'">';
//NB this could be empty !!!
while ($row = mysql_fetch_row($sth)) {
	echo ("<comment id=\"$row[0]\" author_id=\"$row[1]\" author=\"$row[2]\" last_modified=\"$row[3]\">$row[4]</comment>");
}

echo '</ldbn>';


?>