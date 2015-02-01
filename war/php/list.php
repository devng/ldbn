<?php
/**
 * This php-script returns a list of all assignements 
 */
require_once("opendb.php");

$sql = "SELECT a.id, a.name, a.user_id, u.name, u.is_admin, a.modified_on
        FROM assignment AS a, user AS u
        WHERE a.user_id=u.user_id
        ORDER BY a.id DESC";

if (! $sth = @mysql_query($sql)) {
	die(getDBErrorXML());
}

echo '<ldbn type="assignment_list">';

while ($row = mysql_fetch_row($sth)) {
	echo ("<entry id=\"$row[0]\" name=\"$row[1]\" author_id=\"$row[2]\" author=\"$row[3]\" is_admin=\"$row[4]\" last_modified=\"$row[5]\" />");
}

echo '</ldbn>';
?>