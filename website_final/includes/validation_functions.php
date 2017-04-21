<?php

$errors = array();

function fieldname_as_text($fieldname) {
  $fieldname = str_replace("_", " ", $fieldname);
  $fieldname = ucfirst($fieldname);
  return $fieldname;
}

// * presence
// use trim() so empty spaces don't count
// use === to avoid false positives
// empty() would consider "0" to be empty
function has_presence($value) {
	return isset($value) && $value !== "";
}

function validate_presences($required_fields) {
  global $errors;
  foreach($required_fields as $field) {
    $value = trim($_POST[$field]);
  	if (!has_presence($value)) {
  		$errors[$field] = fieldname_as_text($field) . " can't be blank";
  	}
  }
}

// * string length
// max length
function has_max_length($value, $max) {
	return strlen($value) <= $max;
}

function validate_max_lengths($fields_with_max_lengths) {
	global $errors;
	// Expects an assoc. array
	foreach($fields_with_max_lengths as $field => $max) {
		$value = trim($_POST[$field]);
	  if (!has_max_length($value, $max)) {
	    $errors[$field] = fieldname_as_text($field) . " is too long";
	  }
	}
}

function validate_min_lengths($fields_with_min_lengths) {
	global $errors;
	// Expects an assoc. array
	foreach($fields_with_min_lengths as $field => $min) {
		$value = trim($_POST[$field]);
	  if (has_max_length($value, $min)) {
	    $errors[$field] = fieldname_as_text($field) . " is too short";
	  }
	}
}

function validate_email($email){
	global $errors;
		//$email = trim($_POST[$email]);
		if (!filter_var($email, FILTER_VALIDATE_EMAIL) && has_presence($email) == true){
			//$errors[$email] = fieldname_as_text($email) . " format is invalid";
			$errors[$email] = "Email address format is invalid";
		}
		
		if(find_admin_by_email($email) != NULL){
			//$errors[$email] = "Email address: " . fieldname_as_text($email) . " has already been used";
			$errors[$email] = "Email address has already been used";
		}
		
}


function validate_email_mod($email){
	global $errors;
		//$email = trim($_POST[$email]);
		if (!filter_var($email, FILTER_VALIDATE_EMAIL) && has_presence($email) == true){
			//$errors[$email] = fieldname_as_text($email) . " format is invalid";
			$errors[$email] = "Email address format is invalid";
		}
		
}

// * inclusion in a set
function has_inclusion_in($value, $set) {
	return in_array($value, $set);
}


function validate_username($username){
	global $errors;
	
	if(find_admin_by_username($username) != NULL){
		//$errors[$username] = "Username: " . fieldname_as_text($username) . " already exists";
		$errors[$username] = "Username already exists";
	}
	
}

		

?>