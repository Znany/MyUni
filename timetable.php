<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <script src="https://kit.fontawesome.com/ce120f2cd7.js" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="timetable.css">
</head>
<body>
<?php
	ini_set('display_errors', 1);
	ini_set('display_startup_errors', 1);
	error_reporting(E_ALL);			
	require 'options.php';
	$sql = "SELECT days.name AS days_id,days.id,timetable.time_start,timetable.time_end,timetable.room,subjects.name,lesson_types.name AS lesson_type_name from timetable LEFT JOIN subjects ON subjects.subject_id=timetable.subject_id LEFT JOIN days on timetable.day=days.id LEFT JOIN lesson_types ON subjects.lesson_type=lesson_types.type ORDER BY days.id,timetable.time_start";
	if ($result = $mysqli -> query($sql)) {
	  while ($row = $result -> fetch_assoc()) {
			$day = $row["days_id"];
			$dni[$day][]=$row;
	  }
	  $result -> free_result();
	}
	$mysqli -> close();
		$max=0;
		foreach($dni as $key => $val){
			if($max<count($val)){
				$max = count($val);
			}
		}
?>
    <?php include 'header.html'; ?>
    <main>
        <table>
		<?php
		echo'<tr>';
		foreach ($dni as $key => $item) {
                echo'<th>'.$key.'</th>';
}
echo'</tr>';
	for($i = 0;$i<$max;$i++){
	echo'<tr>';
	foreach ($dni as $key => $item) {
		echo '<td>';
			if(isset($item[$i])){
				$time_start = new DateTime($item[$i]["time_start"]);
				$time_start = $time_start->format("H:i");
				$time_end = new DateTime($item[$i]["time_end"]);
				$time_end = $time_end->format("H:i");
				$room = $room = $item[$i]["room"];
				if($room==0){ 
					$room="online";
				}
				 echo $item[$i]["name"].'<br>'.$item[$i]["lesson_type_name"].'<br>'.$time_start.' - '.$time_end.'<br> Room: '.$room;
			}
			echo '</td>';
}
	echo'</tr>';
}
		?>
        </table>
    </main>
</body>
</html>