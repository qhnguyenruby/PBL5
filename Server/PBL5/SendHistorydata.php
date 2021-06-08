
<?php

$servername = "localhost";
$dbname = "data_PBL5";
$username = "root";
$password = "";

     
        $luongnuoc = $_POST["luongnuoc"];
        $chedo = $_POST["chedo"];
        $loaidat = $_POST["loaidat"];
        

        
        $conn = new mysqli($servername, $username, $password, $dbname);

        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        } 
        
        $sql = "INSERT INTO lichsutuoi SET luongnuoc='".$luongnuoc."',idchedo = '".$chedo."',iddat = '".$loaidat."'";
        
        if ($conn->query($sql) === TRUE) {
            echo "New record created successfully";
        } 
        else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }
    
        $conn->close();
    

function test_input($data) {
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}
?>
