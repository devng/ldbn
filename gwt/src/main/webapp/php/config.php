<?php
/**
 * Contains the backend configuration.
 */

// configuration for the database PDO URL, in this case it is a local SQLite3
$db_url = 'sqlite:/var/ldbn/sql/ldbn.db';

// configuration for the ldbn domain
$ldbnhost  = 'ldbnonline.com'; //domain without http:// at the begining and / at the end
$ldbnemail = 'admin@ldbnonline.com';

// If true an email with an activation link is sent when an user registeres.
// If false no email is sent and the user can login without activation
$use_account_email_activation = false;
?>