<html>
<body>

<input type="button" onclick="sendjson()"/>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">
</script>

<script type="text/javascript">


function sendjson(){	
	
var json = JSON.stringify( {"name":"Tim Delesio","authenication":{"password":"test12","ticket":"123456","email":"tdelesio@gmail.com"});

$.ajax({
    type: "POST",
    contentType: "application/json; charset=utf-8",
    url: "/users/register"
//    url: "/dealfinder/rest/finder",
    data: json,
    dataType: "json"
});

}
</script>


</body>
</html>
