<?php
/**
 * Return a list of all assignements in the database.
 */
require_once ("common.php");

$dbhandle = openDB ();

$sql = "SELECT a.id, a.name, a.user_id, u.name, u.is_admin, a.modified_on
        FROM assignment AS a, user AS u
        WHERE a.user_id=u.user_id
        ORDER BY a.id DESC";

if (! $sth = $dbhandle->query ( $sql )) {
	dieWithXMLMessage ( $msg_db_error );
}

echo '<ldbn type="assignment_list">';

foreach ( $sth as $row ) {
	echo ("<entry id=\"$row[0]\" name=\"$row[1]\" author_id=\"$row[2]\" author=\"$row[3]\" is_admin=\"$row[4]\" last_modified=\"$row[5]\" />");
}

echo '</ldbn>';
?>