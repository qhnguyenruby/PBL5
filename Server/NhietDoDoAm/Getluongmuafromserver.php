<?php
$servername = "localhost";
$dbname = "data";
$username = "root";
$password = "";
$luongnuoc = $luongmuadudoan = $luongmuathucte = "";

$luongnuoc = $_GET['luongnuoc'];
$luongmuadudoan = $_GET['luongmuadudoan'];
$luongmuathucte = $_GET['luongmuathucte'];
$conn = new mysqli($servername, $username, $password, $dbname);

 if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

      $sql = "INSERT INTO luongmua SET luongmuathucte='".$luongmuathucte."',luongmuadudoan ='".$luongmuadudoan."',luongnuoc='".$luongnuoc."'";

       if ($conn->query($sql) === TRUE) {
            echo "New record created successfully";
        } 
        else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
?>