<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php require_once("../includes/validation_functions.php"); ?>
<?php confirm_logged_in(); ?>

<?php
if (isset($_POST['submit'])) {
   
  // validations
  $required_fields = array("stat_name", "count" ); //don't include last_login here
  validate_presences($required_fields);
  
  
  if (empty($errors)) {
    // Perform Create

    $stat_name = mysql_prep($_POST["stat_name"]);	
	$count = mysql_prep($_POST["count"]);
	$date = new DateTime('now');
	$time = $date->format('Y-m-d H:i:s');
	    
    $query  = "INSERT INTO statistics (";
    $query .= "  stat_name, count, time";
    $query .= ") VALUES (";
    $query .= "  '{$stat_name}', '{$count}', '{$time}' ";
    $query .= ")";
    $result = mysqli_query($connection, $query);

    if ($result) {
      // Success
      $_SESSION["message"] = "Statistic added.";
      redirect_to("statistics.php");
    } else {
      // Failure
      $_SESSION["mismatch"] = "Could not add statistic.";
    }
  }
} else {
  // This is probably a GET request
  
} // end: if (isset($_POST['submit']))

?>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta http-equiv="x-ua-compatible" content="ie=edge">
  <link rel="stylesheet" href="css/bootstrap.min.css">
  <link rel="stylesheet" href="css/styles.css">
  <title>Schedule Builder</title>
</head>

<body>

<nav class="navbar navbar-inverse bg-inverse navbar-toggleable-sm" id="navigation">
  <div class="container">  
   <a class="navbar-brand" href="#">
   
   <img src="images/logo.png" style="width: 70px; height: 70px;" class="d-inline align-center" alt="">
   <h1 class="d-inline align-bottom">Schedule Builder</h1>
   
  </a>
  
    <div class="navbar-nav">
    </div><!-- navbar-nav -->
  </div><!-- container -->
</nav>


 
  <div class="container mt-5" id="main">
    <?php echo mismatch(); ?>
    <?php echo form_errors($errors); ?>
    
    <h2>Add Statistic</h2>
    <form action="add_statistic.php" method="post">
      <p>Name:
        <input type="text" name="stat_name" value="" />
      </p>
	  <p>Value:
        <input type="text" name="count" value="" />
      </p>
      <input type="submit" name="submit" value="Create Statistic" />
    </form>
    <br />
    <a href="statistics.php">Cancel</a>
</div><!-- content container -->
<script src="js/jquery.slim.min.js"></script>
<script src="js/tether.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/scripts.js"></script>

</body>
</html>



<?php
  // 5. Close database connection
	if (isset($connection)) {
	  mysqli_close($connection);
	}
?>
