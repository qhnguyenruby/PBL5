<?php
	include_once("Connect.php");
  	Connect("data_PBL5");

  	$enddate= strtotime("today");
  	$startdate=strtotime("-30 days", $enddate);

  	$sql = "SELECT * from luongmua where thoigian between '".date("y/m/d" ,$startdate)." 00:00:00' and '".date("y/m/d" ,$enddate)." 23:59:59'";

  	$result = ExecuteQuery($sql);
  	$data = array();
  	$data['data'] = array();

  	while($row = mysqli_fetch_array($result))
	{
		array_push($data['data'], $row);
	}
	print(json_encode($data));
	mysqli_close($link);
	?>