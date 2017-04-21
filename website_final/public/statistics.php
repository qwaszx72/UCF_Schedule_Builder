<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php require_once("../includes/class_update.php"); ?>
<?php confirm_logged_in(); ?>

<?php
  $statistic_set = find_all_stats();
?>



<?php

// This code handles the Class Data update functionality

if (isset($_POST['submit'])) {
  
  if (empty($errors)) {
	  
	  
	  // add code here for class update here
	  // modify this function in class_update.php
	  class_update();
	  
	  // tell the admins that the update process has begun
	  $_SESSION["message"] = "Class data is now being updated.";			
      redirect_to("statistics.php");
	  
    } else {
      // Failure
      $_SESSION["mismatch"] = "Update failed.";
    }
	
} else {
  // This is probably a GET request
  
} // end: if (isset($_POST['submit']))

?>

<?php $layout_context = "admin"; ?>

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
      <a class="nav-item nav-link active" href="statistics.php">Statistics</a>
      <a class="nav-item nav-link" href="manage_admins.php">Manage Admins</a>
      <a class="nav-item nav-link " href="documentation.php">Documentation</a>
      <a class="nav-item nav-link" href="logout.php">Logout</a>
    </div><!-- navbar-nav -->
  </div><!-- container -->
</nav>

 <div class="container">
  <div id="main">
  
  
	 <?php echo message(); ?>

	<article class=" container text-center mt-5">
	<h2>Class Data</h2>
	<div>
	<br />
	<form action="statistics.php" method="post">
	 <input type="submit" name="submit" value="Update"/>
	</form>
	<br />
	<p>Note: this may take several hours to complete.</p>
	
	</div>
	<br />
	</article>
    <h2>Statistics</h2> <br />
    <table class="table table-hover">
      <thead ><tr>
        <th><h5>Name</h5></th>
		<th><h5>Value</h5></th>
		<th ><h5>Last Updated</h5></th>
		 <th colspan="2"></th></thead>
		
	 <tbody>
      </tr>
	 
	  
    <?php while($statistic = mysqli_fetch_assoc($statistic_set)) { ?>
      <tr>
        <td><?php echo htmlentities($statistic["stat_name"]); ?></td> 
		<td><?php echo htmlentities($statistic["count"]); ?></td>
		<td><?php echo htmlentities($statistic["time"]); ?></td>
		<td><a href="clear_statistic.php?id=<?php echo urlencode($statistic["id"]); ?>" onclick="return confirm('Are you sure?');">Clear</a></th>
		<td><a href="remove_statistic.php?id=<?php echo urlencode($statistic["id"]); ?>" onclick="return confirm('Are you sure?');">Remove</a></th>
		
       
      </tr>
    <?php } ?>
	</tbody>
    </table>
    <br />
	<a href="add_statistic.php">Add new statistic</a>
  </div>
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
