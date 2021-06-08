<?php
$servername = "localhost";
$dbname = "data";
$username = "root";
$password = "";

 $conn = new mysqli($servername, $username, $password, $dbname);

 if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $date= strtotime("today"); 

   $sql = $sql = "SELECT * from lichsutuoi where thoigian between '".date("y/m/d" ,$date)." 00:00:00' and '".date("y/m/d" ,$date)." 23:59:59' order by thoigian desc";
        
         $result = mysqli_query($conn,$sql) or die("Failed");

         $row = mysqli_fetch_array($result);

         if(!empty($row))
         {
            echo "1";
         }
         else
         {
            echo "0";
         }
    
        $conn->close();
 ?>