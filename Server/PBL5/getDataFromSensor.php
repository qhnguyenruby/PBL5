<?php
$servername = "localhost";
$dbname = "data_PBL5";
$username = "root";
$password = "";

  $conn = new mysqli($servername, $username, $password, $dbname);

  if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
  } 
  $sql = "SELECT * FROM sensordata ORDER BY id DESC LIMIT 1";
  $result = mysqli_query($conn,$sql) or die("Failed");
  $row = mysqli_fetch_array($result);
  $data[] = $row;
  print(json_encode($data));
        $conn->close();
 ?>