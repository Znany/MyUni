
<?php

date_default_timezone_set("Europe/Warsaw");
$pass = $_GET["password"];
$option = $_GET["option"];
if (!isset($pass) || !($pass == $SET_VAR_PASS)) {
    header("HTTP/1.1 401 Unauthorized");
    exit("Unauthorized");
}

require 'options.php';

if ($mysqli->connect_errno) {
    echo "Failed to connect to MySQL: " . $mysqli->connect_error;
    exit();
}
if ($option == "tasks") {
    $sql =
        "SELECT tasks.id, tasks.groupid,subjects.name,subjects.teacher,tasks.header,tasks.description,tasks.due_date FROM tasks LEFT JOIN subjects on tasks.subject_id=subjects.subject_id ORDER BY tasks.due_date";

    if ($result = $mysqli->query($sql)) {
        while ($row = $result->fetch_assoc()) {
            $tablica[] = $row;
        }
        $podstawka = ["resultset" => $tablica];
        echo json_encode($podstawka);
        $result->free_result();
    }

    $mysqli->close();
} elseif ($option == "timetable") {
    $sql =
        "SELECT days.name AS days_id,days.id,timetable.time_start,timetable.time_end,timetable.room,subjects.name,lesson_types.name AS lesson_type_name from timetable LEFT JOIN subjects ON subjects.subject_id=timetable.subject_id LEFT JOIN days on timetable.day=days.id LEFT JOIN lesson_types ON subjects.lesson_type=lesson_types.type ORDER BY days.id,timetable.time_start";

    if ($result = $mysqli->query($sql)) {
        while ($row = $result->fetch_assoc()) {
            $day = $row["id"];

            $dni[$day][] = $row;
        }

        $result->free_result();
    }

    $mysqli->close();
    $podstawka = ["resultset" => $dni];
    echo json_encode($podstawka);
} elseif ($option == "add_task") {
    $s1 = $_GET["groupid"];
    $s2 = $_GET["subject_id"];
    $s3 = $_GET["header"];
    $s4 = $_GET["description"];
    $s5 = $_GET["due_date"];

    if (isset($s1) && isset($s2) && isset($s3) && isset($s4) && isset($s5)) {
        $stmt = $mysqli->prepare(
            "INSERT INTO tasks (groupid,subject_id,header,description,due_date) VALUES (?,?,?,?,?)"
        );
        $stmt->bind_param("sssss", $s1, $s2, $s3, $s4, $s5);
        if ($stmt->execute()) {
            header("HTTP/1.0 200 good query");
            echo "INSERT OK";
        } else {
            header("HTTP/1.0 500 query error");
            echo "INSERT ERROR";
        }

        $stmt->close();
        $mysqli->close();
    } else {
        header("HTTP/1.0 400 parameters error");
        echo "FILL ALL PARAMETERS";
    }
} elseif ($option == "subjects") {
    $sql =
        "SELECT subjects.subject_id,subjects.name,subjects.teacher,lesson_types.name AS typ from subjects LEFT JOIN lesson_types ON subjects.lesson_type=lesson_types.type";

    if ($result = $mysqli->query($sql)) {
        while ($row = $result->fetch_assoc()) {
            $tablica[] = $row;
        }
        $podstawka = ["resultset" => $tablica];
        echo json_encode($podstawka);
        $result->free_result();
    }

    $mysqli->close();
} elseif ($option == "groups") {
    $sql = "SELECT * from groups";

    if ($result = $mysqli->query($sql)) {
        while ($row = $result->fetch_assoc()) {
            $tablica[] = $row;
        }
        $podstawka = ["resultset" => $tablica];
        echo json_encode($podstawka);
        $result->free_result();
    }

    $mysqli->close();
} elseif ($option == "widget") {
    $phpday = date("w");
    switch ($phpday) {
        case 0:
            //niedziela
            $dzien = 1;
            break;
        case 1:
            $dzien = 1;
            break;
        case 2:
            $dzien = 2;
            break;
        case 3:
            $dzien = 3;
            break;

        case 4:
            $dzien = 4;
            break;

        case 5:
            $dzien = 5;
            break;

        case 6:
            //sobota
            $dzien = 1;
            break;
    }

    $sql = "SELECT days.name AS days_id,days.id,timetable.time_start,timetable.time_end,timetable.room,subjects.name,lesson_types.name AS lesson_type_name from timetable LEFT JOIN subjects ON subjects.subject_id=timetable.subject_id LEFT JOIN days on timetable.day=days.id LEFT JOIN lesson_types ON subjects.lesson_type=lesson_types.type WHERE days.id = '$dzien' ORDER BY days.id,timetable.time_start";

    if ($result = $mysqli->query($sql)) {
        while ($row = $result->fetch_assoc()) {
            $tablica[] = $row;
        }
        $podstawka = ["resultset" => $tablica, "dzien" => $dzien];
        echo json_encode($podstawka);
        $result->free_result();
    }

    $mysqli->close();
} elseif ($option == "day_select") {
    $dzien_select = $_GET["day"];

    $sql = "SELECT days.name AS days_id,days.id,timetable.time_start,timetable.time_end,timetable.room,subjects.name,lesson_types.name AS lesson_type_name from timetable LEFT JOIN subjects ON subjects.subject_id=timetable.subject_id LEFT JOIN days on timetable.day=days.id LEFT JOIN lesson_types ON subjects.lesson_type=lesson_types.type WHERE days.id = '$dzien_select' ORDER BY days.id,timetable.time_start";

    if ($result = $mysqli->query($sql)) {
        while ($row = $result->fetch_assoc()) {
            $tablica[] = $row;
        }
        $podstawka = ["resultset" => $tablica, "dzien" => $dzien];
        echo json_encode($podstawka);
        $result->free_result();
    }

    $mysqli->close();
} else {
    echo "bad option";
}


?>
