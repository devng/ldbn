<?php
require_once("sessioncheck.php");

function deleteAssignment($id) {
	$sql = "DELETE FROM assignment WHERE id=".$id;
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	$sql = "DELETE FROM comment WHERE assignment_id=".$id;
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	die ('<ldbn type="msg"><msg type="ok">The assignment was successfully removed from the DB.</msg></ldbn>');
}

$assignment_id;
if (isset($_POST['assignment_id'])) {
	$assignment_id = $_POST['assignment_id'];
	checkID($assignment_id);
	//If the user is supper user then delete imedeatelly
	if ($is_su) {
		deleteAssignment($assignment_id);
	}
	//otherwise check if the user is permitted to delete the asssignment
	$sql = "SELECT user_id FROM assignment WHERE id=$assignment_id";
	if (! $sth = @mysql_query($sql)) {
		die(getDBErrorXML());
	}
	if ($row = mysql_fetch_row($sth)) {
		$submitted_by_id = $row[0];
		$sql = "SELECT user_id, is_admin, is_su FROM user WHERE user_id=$submitted_by_id";
		$is_admin2    = false;
		$is_su2       = false;
	 	if (! $sth = @mysql_query($sql)) {
			die(getDBErrorXML());
		}
		if ($row = mysql_fetch_row($sth)) {
			$is_admin2    = $row[1] == "1";
			$is_su2       = $row[2] == "1";
		}
		//the user who submitted the assignment and the current user are the same
		if(strcasecmp($user_id, $submitted_by_id) == 0) {
			deleteAssignment($assignment_id);
		}
		//the user who submitted the assignment is an admin => error
		if($is_admin2) {
			die ('<ldbn type="msg"><msg type="error">You cannot delete assignments submitted by other administrators.</msg></ldbn>');
		}
		//the user who submitted the assignment is not an admin and the current user is
		if($is_admin) {
			deleteAssignment($assignment_id);
		}
	} else {
		echo ('<ldbn type="msg"><msg type="warn">The requested assignment is not present in the DB.</msg></ldbn>');
	}
} else {
	die ('<ldbn type="msg"><msg type="error">Some arguments are not set.</msg></ldbn>');
}
?>
