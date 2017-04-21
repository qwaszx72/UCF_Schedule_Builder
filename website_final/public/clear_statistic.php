<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php require_once("../includes/validation_functions.php"); ?>
<?php confirm_logged_in(); ?>

<?php
  $stat = find_stat_by_id($_GET["id"]);
  
  if (!$stat) {
    // Statistic ID was missing or invalid or 
    // Statistic couldn't be found in database
    redirect_to("statistics.php");
  }
  
  if (empty($errors)) {
    
    // Perform Update

    $id = $stat["id"];
	$date = new DateTime('now');
	$time = $date->format('Y-m-d H:i:s');
	
    $query  = "UPDATE statistics SET ";
    $query .= "count = 0, ";
	$query .= "time = '{$time}' ";
    $query .= "WHERE id = {$id} ";
    $query .= "LIMIT 1";
    $result = mysqli_query($connection, $query);

    if ($result && mysqli_affected_rows($connection) == 1) {
      // Success
      $_SESSION["message"] = "Value cleared.";
      redirect_to("statistics.php");
    } else {
      // Failure
      $_SESSION["message"] = "Clear Value failed.";
    }
  
  }
 else {
  // This is probably a GET request
  
} // end: if (isset($_POST['submit']))

?>


