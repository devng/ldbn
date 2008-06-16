<?php
	include("opendb.php");
	$sql = "SELECT id, name FROM assignment ORDER BY id DESC";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	
	echo '<ldbn type="assignment_list">';
	
	while ($row = mysql_fetch_row($sth)) {
		echo '<entry id="'.$row[0].'" name="'.$row[1].'" />';
	}
	
	echo '</ldbn>';
	
	include("closedb.php");
?>