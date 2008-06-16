<?php
	// Use this script to close the connection to the DB. 
	// Note that You have to include opendb.php in the same script,
	// where you include this one.
	
	@mysql_close($conn) or die (getDBErrorXML());
?>