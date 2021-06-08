<?php 

	$link = mysqli_connect("localhost","root","") or die ("Khong the ket noi den CSDL MySQL");
	function Connect($database){
		global $link;
		mysqli_select_db($link,$database);
	}
	function ExecuteQuery($query){
		global $link;
		$result=mysqli_query($link,$query);
		return $result;
}
?>