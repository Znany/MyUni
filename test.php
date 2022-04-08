<?php

$datetime = new DateTime($date);
echo $datetime->format('w');
echo $phpday . '<br>';
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
echo $dzien;

?>