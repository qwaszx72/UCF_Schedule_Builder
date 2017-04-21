<?php

	session_start();
	
	function message() {
		if (isset($_SESSION["message"])) {
			$output = "<div class=\"message\">";
			$output .= htmlentities($_SESSION["message"]);
			$output .= "</div>";
			
			// clear message after use
			$_SESSION["message"] = null;
			
			return $output;
		}
	}
	
	// hack 
	function mismatch() {
		if (isset($_SESSION["mismatch"])) {
			$output = "<div class=\"error\">";
			$output .= htmlentities($_SESSION["mismatch"]);
			$output .= "</div>";
			
			// clear message after use
			$_SESSION["mismatch"] = null;
			
			return $output;
		}
	}

	function errors() {
		if (isset($_SESSION["errors"])) {
			$errors = $_SESSION["errors"];
			
			// clear message after use
			$_SESSION["errors"] = null;
			
			return $errors;
		}
	}
	
?>