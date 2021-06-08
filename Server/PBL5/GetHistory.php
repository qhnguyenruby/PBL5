<?php
  include_once("Connect.php");
  Connect("data_PBL5");
  $enddate= strtotime("today");
  $startdate=strtotime("-7 days", $enddate);

  $sql = "SELECT * from lichsutuoi where thoigian between '".date("y/m/d" ,$startdate)." 00:00:00' and '".date("y/m/d" ,$enddate)." 23:59:59' order by thoigian desc";
  
  $result = ExecuteQuery($sql);
  $data = array();
  $data['data'] = array();
  
  
  while($row = mysqli_fetch_array($result))
  {
    $rs['id'] = $row['id'];
    $rs['luongnuoc'] = $row['luongnuoc'];
    $rs['tendat'] = getLoaiDatById($row['iddat']);
    $rs['tenchedo'] = getLoaiCheDoById($row['idchedo']);
    $rs['thoigian'] = $row['thoigian'];
    array_push($data['data'], $rs);
  }

  print(json_encode($data));
  mysqli_close($link);

  function getLoaiDatById($iddat){
    $sqldat = "SELECT * from dat where id = '$iddat'";
    $resultdat = ExecuteQuery($sqldat);
    $row = mysqli_fetch_array($resultdat);
    $data = $row['loaidat']; 
    return $data;
  }

  function getLoaiCheDoById($idchedo){
    $sqlchedo = "SELECT * from chedo where id = '$idchedo'";
    $resultchedo = ExecuteQuery($sqlchedo);
    $row = mysqli_fetch_array($resultchedo);
    $data = $row['chedo']; 
    return $data;
  }
         
    
 ?>