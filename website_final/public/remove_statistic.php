<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php confirm_logged_in(); ?>

<?php
  $stat = find_stat_by_id($_GET["id"]);
  if (!$stat) {
    // admin ID was missing or invalid or 
    // admin couldn't be found in database
    redirect_to("statistics.php");
  }
  
  $id = $stat["id"];
  $query = "DELETE FROM statistics WHERE id = {$id} LIMIT 1";
  $result = mysqli_query($connection, $query);

  if ($result && mysqli_affected_rows($connection) == 1) {
    // Success
    $_SESSION["message"] = "Statistic removed.";
    redirect_to("statistics.php");
  } else {
    // Failure
    $_SESSION["message"] = "Statistic not removed.";
    redirect_to("statistics.php");
  }
  
?>
