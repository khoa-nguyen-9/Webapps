<?php
    echo "Welcome, I am connecting Android to PHP, MySQL";
    $link=pg_connect("host=db port=5432 user=lab password=lab dbname=films" );
    if( ! $link )
    {
      die("couldn't connect to films");
    }
?>
