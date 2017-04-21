<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php require_once("../includes/validation_functions.php"); ?>

<?php
$username = "";

if (isset($_POST['submit'])) {
  // Process the form
  
  // validations
  $required_fields = array("username", "password");
  validate_presences($required_fields);
  
  if (empty($errors)) {
    // Attempt Login

		$username = $_POST["username"];
		$password = $_POST["password"];
		$found_admin = attempt_login($username, $password);

    if ($found_admin) {
      // Success
			// Mark user as logged in
			$_SESSION["admin_id"] = $found_admin["id"];
			$_SESSION["username"] = $found_admin["username"];
			$_SESSION["name"] = $found_admin["name"];
			
			// update last_login
			$id = $found_admin["id"];
			$name = $found_admin["name"];
			
			$date = new DateTime('now');
			$last_login = $date->format('Y-m-d H:i:s');
			
			$query  = "UPDATE admin_users SET ";
			$query .= "last_login = '{$last_login}' ";
			$query .= "WHERE id = {$id} ";
			$query .= "LIMIT 1";
			$result = mysqli_query($connection, $query);
			
	  $_SESSION["message"] = "Welcome back, {$name}, your last login was: {$last_login}. ";			
      redirect_to("statistics.php");
    } else {
      // Failure
      $_SESSION["mismatch"] = "The username or password is incorrect.";
    }
  }
} else {
  // This is probably a GET request
  
} // end: if (isset($_POST['submit']))

?>

<?php $layout_context = "admin"; ?>
<?php 
	if (!isset($layout_context)) {
		$layout_context = "public";
	}
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
  
  </div><!-- container -->
</nav>

<div class="container mt-5">


<div>
  
  
  <div id="main">
  

 <?php echo mismatch(); ?>

 <?php echo form_errors($errors); ?>
 
 <div id="loginbox" style="margin-top:50px;" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">                    
            <div class="panel panel-info" >
                    <div class="panel-heading">
                        <div class="panel-title"><h2>Login</h2></div>
                    </div>     

                    <div style="padding-top:30px" class="panel-body" >

                            
                        <form id="loginform" class="form-horizontal" action="login.php" method="post" role="form">
                                    
                            <div style="margin-bottom: 25px" class="input-group">
                                        <input id="login-username" type="text" class="form-control" name="username" value="" placeholder="username">                                        
                                    </div>
                                
                            <div style="margin-bottom: 25px" class="input-group">
                                        <input id="login-password" type="password" class="form-control" name="password" placeholder="password">
                                    </div>
                                    


                                <div style="margin-top:10px" class="form-group">
                                    <!-- Button -->

                                    <div class="col-sm-25 controls">
									  <input type="submit" class="btn btn-primary " role="button" name="submit" value="Submit" />

                                    </div>
                                </div>
                        
                            </form>     



                        </div>                     
                    </div>  
        </div>
		
  </div>
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
