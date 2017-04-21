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
      <a class="nav-item nav-link " href="statistics.php">Statistics</a>
      <a class="nav-item nav-link " href="manage_admins.php">Manage Admins</a>
      <a class="nav-item nav-link active " href="documentation.php">Documentation</a>
      <a class="nav-item nav-link " href="logout.php">Logout</a>
    </div><!-- navbar-nav -->
  </div><!-- container -->
</nav>


 
  <div class="container mt-5" id="main">
  
  <h1> Documentation </h2>
  <br />
  Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque sapien nunc, viverra consequat justo sed, feugiat venenatis lacus. Suspendisse potenti. Quisque dignissim tristique tortor, a dignissim urna pellentesque at. Duis venenatis nec neque fermentum dapibus. Nullam ornare tempor risus, et sodales leo. In quis arcu ac mi feugiat fermentum. Quisque nibh massa, luctus quis est nec, blandit sollicitudin orci. Vivamus at quam at nisi vehicula blandit. Suspendisse tempus ac lorem in dapibus. Praesent tempus eleifend lorem ut porttitor. Phasellus semper quam lectus, id lacinia ipsum consequat ut.

Vivamus dignissim felis nec elit tempus suscipit. Morbi condimentum hendrerit fermentum. Duis tincidunt mauris vel augue cursus cursus. Quisque eu urna metus. Nullam id iaculis magna. Etiam et fringilla lectus. Ut in ex ultricies, dictum tellus vitae, commodo erat. Aliquam consectetur, ante sit amet mollis lobortis, dolor leo aliquet elit, id porta felis ligula eget diam. Aenean eu varius purus, varius interdum odio. Suspendisse rhoncus id urna sit amet ullamcorper. Suspendisse facilisis euismod iaculis. Sed elit sem, varius ut nisl vel, vestibulum convallis nibh. In eleifend rhoncus leo, sit amet fermentum dui bibendum ut. Aenean luctus vulputate ante.

Nullam tempor dolor ut libero maximus bibendum. Nulla et libero id ex porttitor consectetur. Maecenas fringilla nisi nec enim suscipit tempus. Ut finibus massa vitae scelerisque ultricies. Vestibulum sapien eros, dapibus nec augue ullamcorper, interdum efficitur quam. Donec rhoncus elit elit, id pharetra elit aliquam at. Fusce nisi sem, condimentum ut rutrum ac, volutpat eget augue. Donec sit amet vestibulum urna. Fusce in nisi augue. Nullam quis arcu maximus, cursus lacus vitae, pulvinar eros. Ut accumsan volutpat pulvinar. Cras vestibulum sapien et diam imperdiet, sed efficitur massa viverra. Proin libero ante, consequat vitae sodales in, sodales eget urna. Duis ornare ultrices interdum.



  </div>

<?php include("../includes/layouts/footer.php"); ?>
