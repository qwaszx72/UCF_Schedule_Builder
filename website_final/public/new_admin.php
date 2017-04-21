<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php require_once("../includes/validation_functions.php"); ?>
<?php confirm_logged_in(); ?>

<?php
if (isset($_POST['submit'])) {
  // Process the form
  
   //check if username exists
   $username = mysql_prep($_POST["username"]);
   validate_username($username);
   
   $fields_with_min_lengths = array("username" => 3, "name" => 3, "password" => 7);
   validate_min_lengths($fields_with_min_lengths);
   
  // validations
  $required_fields = array("username", "name", "password", "email" ); //don't include last_login here
  validate_presences($required_fields);
  
  $fields_with_max_lengths = array("username" => 32, "name" => 64, "password" => 32, "email" => 256);
  validate_max_lengths($fields_with_max_lengths);
     
  // check if email is valid  
  $email = mysql_prep($_POST["email"]);
  validate_email($email);
   
   
  
  if (empty($errors)) {
    // Perform Create

    $username = mysql_prep($_POST["username"]);
	$name = mysql_prep($_POST["name"]);
    $password_hash = password_encrypt($_POST["password"]);
	
	$date = new DateTime('now');
	$last_login = $date->format('Y-m-d H:i:s');
	
	$email = mysql_prep($_POST["email"]);
    
    $query  = "INSERT INTO admin_users (";
    $query .= "  username, name, password_hash, last_login, email";
    $query .= ") VALUES (";
    $query .= "  '{$username}', '{$name}', '{$password_hash}', '{$last_login}', '{$email}' ";
    $query .= ")";
    $result = mysqli_query($connection, $query);

    if ($result) {
      // Success
      $_SESSION["message"] = "Admin created.";
      redirect_to("manage_admins.php");
    } else {
      // Failure
      $_SESSION["message"] = "Admin creation failed.";
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
    <?php echo message(); ?>
    <?php echo form_errors($errors); ?>
    
    <h2>Create Admin</h2>
	<br />
	 <form action="new_admin.php" method="post">
	 
	
		<!-- Form start -->
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label" for="name"><h5>Name</h5></label>
					<input id="name" name="name" type="text" value="" placeholder="name" class="form-control input-md">
				</div>
			</div>
			<!-- Text input-->
			<div class="col-md-6">
				<div class="form-group">
				
				<label class="control-label" for="username"><h5>Username</h5></label>
				<input id="username" type="text" name="username" placeholder="username" value="" class="form-control input-md">
				</div>
			</div>
			<!-- Text input-->
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label" for="email"><h5>Email</h5></label>
					<input id="email" name="email" value="" type="text" placeholder="email" class="form-control input-md">
				</div>
			</div>
			
			
			<!-- Text input-->
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label" for="date"><h5>Password</h5></label>
					<input type="password" name="password" value="" id="date" name="date" placeholder="password" class="form-control input-md">
				</div>
			</div>
			
			<!-- Button -->
			<div class="col-md-12">
				<div class="form-group">
					<button id="singlebutton" type="submit" name="submit" class="btn btn-primary">Create Admin</button>
				</div>
			</div>
		</div>
	</form>
	
    <a href="manage_admins.php">Cancel</a>
	
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
