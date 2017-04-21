<?php require_once("../includes/session.php"); ?>
<?php require_once("../includes/db_connection.php"); ?>
<?php require_once("../includes/functions.php"); ?>
<?php confirm_logged_in(); ?>

<?php
  $admin_set = find_all_admins();
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
      <a class="nav-item nav-link" href="statistics.php">Statistics</a>
      <a class="nav-item nav-link active" href="manage_admins.php">Manage Admins</a>
      <a class="nav-item nav-link " href="documentation.php">Documentation</a>
      <a class="nav-item nav-link" href="logout.php">Logout</a>
    </div><!-- navbar-nav -->
  </div><!-- container -->
</nav>


 
  <div class="container mt-5" id="main">
    <?php echo message(); ?>
    <h2>Manage Admins</h2> <br />
    <table class="table table-hover">
      <tr>
        <th> <h5>Username </h5></th>
		<th> <h5>Last Login</h5></th>
        <th colspan="2"></th>
		
		
      </tr>
    <?php while($admin = mysqli_fetch_assoc($admin_set)) { ?>
      <tr>
        <td><?php echo htmlentities($admin["username"]); ?></td> 
		<td><?php echo htmlentities($admin["last_login"]); ?></td>
        <td><a href="edit_admin.php?id=<?php echo urlencode($admin["id"]); ?>">Edit</a></td>
        <td><a href="delete_admin.php?id=<?php echo urlencode($admin["id"]); ?>" onclick="return confirm('Are you sure?');">Delete</a></td>
		
      </tr>
    <?php } ?>
    </table>
    <br />
    <a href="new_admin.php">Add new admin</a>
  </div>

<?php include("../includes/layouts/footer.php"); ?>
