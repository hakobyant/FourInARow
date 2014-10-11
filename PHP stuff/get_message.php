<?php

$user_id = $_REQUEST['user_id'];

$con=mysqli_connect("mysql4.000webhost.com","a9581814_tigran","hakobyan1991mysql","a9581814_data4");
// Check connection
if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$result = mysqli_query($con,"SELECT * FROM message WHERE second_user_id = '$user_id' AND message!='Game' ORDER BY message_id DESC LIMIT 1");

$row = mysqli_fetch_array($result);
 
if($row == FALSE){
 
die(mysqli_error($row));
}
 
printf("%s:%s:%s,", $row['first_user_id'],$row['second_user_id'],$row['message']);

mysqli_close($con);

printf(",");

?>