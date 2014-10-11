<?php
$first_user_id = $_REQUEST['first_user_id'];
$second_user_id = $_REQUEST['second_user_id'];
$message = $_REQUEST['message'];


$con=mysqli_connect("mysql4.000webhost.com","a9581814_tigran","hakobyan1991mysql","a9581814_data4");
// Check connection
if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_errno();
}

if($message == "Game") {
	$result = mysqli_query($con,"SELECT * FROM message WHERE second_user_id='$second_user_id' AND message='Game'");

	if(mysqli_num_rows($result) == 0) {
		mysqli_query($con,"INSERT INTO message (first_user_id, second_user_id, message) VALUES ($first_user_id, $second_user_id, '$message')");
	}
	else {
		printf("Unsuccessful");
	}
}
else {
	mysqli_query($con,"INSERT INTO message (first_user_id, second_user_id, message) VALUES ($first_user_id, $second_user_id, '$message')");
}
mysqli_close($con);

printf(",");

?>