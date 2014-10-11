<?php
$request_type = $_REQUEST['request_type'];

$con=mysqli_connect("mysql4.000webhost.com","a9581814_tigran","hakobyan1991mysql","a9581814_data4");
// Check connection
if (mysqli_connect_errno()) {
	echo "Failed to connect to MySQL: " . mysqli_connect_errno();
}



switch($request_type) {

	case "create_new_user": {
		$username = $_REQUEST['username'];

		mysqli_query($con,"INSERT INTO Users (username) VALUES ('$username')");

		$result = mysqli_query($con,"SELECT * FROM Users ORDER BY ID DESC LIMIT 1");

		$row = mysqli_fetch_array($result);
		 
		if($row== FALSE){
		 
		die(mysqli_error($row));
		}
		 
		printf("%d:%s:%d,", $row['ID'],$row['username'],$row['score']);
		break;
	}
	case "start_game_screen": {
		$user_id = $_REQUEST['user_id'];
		
		mysqli_query($con,"UPDATE Users SET isOnline=1 WHERE ID=$user_id");

		$result = mysqli_query($con,"SELECT * FROM Users WHERE isOnline=1");
		
		while($row = mysqli_fetch_array($result)) {
                        if($row['ID'] != $user_id) {

	        		printf("%d:%s:%d;", $row['ID'],$row['username'],$row['score']);
                        }
		}
		
		$result = mysqli_query($con,"SELECT * FROM message WHERE message.second_user_id = $user_id AND message.message = 'Game'");
		
		
		if(mysqli_num_rows($result) != 0) {
			$row =  mysqli_fetch_array($result);
			$first_user_id = $row['first_user_id'];

			if($anotherResult = mysqli_query($con,"SELECT * FROM Users WHERE Users.ID = $first_user_id")) {
			
				mysqli_query($con,"DELETE FROM message WHERE first_user_id='$first_user_id' AND second_user_id='$user_id' AND message='Game'");
				
				mysqli_query($con, "UPDATE Users SET isOnline=0 WHERE ID=$first_user_id");
				mysqli_query($con, "UPDATE Users SET isOnline=0 WHERE ID=$user_id");

				$anotherRow = mysqli_fetch_array($anotherResult);
			
				printf("%d:%s:%d,", $anotherRow['ID'],$anotherRow['username'],$anotherRow['score']);
			}
		}
		else {
			printf("-1::-1,");
		}
		break;
	}
	case "update": {
		$user_id = $_REQUEST['user_id'];
		$new_name = $_REQUEST['new_name'];
		$new_score = $_REQUEST['new_score'];
		
		mysqli_query($con,"UPDATE Users SET username = '$new_name', score = '$new_score' WHERE ID = '$user_id'");
		break;
	}
}

mysqli_close($con);

printf(",");

?>