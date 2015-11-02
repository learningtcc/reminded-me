<html>
    <head>
        <link rel="openid.server" href="https://localhost/hf-web-login/" />
        <link rel="openid.delegate" href="http://tdelesio.localhost.com/">
    </head>


<body>

<input type="button" onclick="registerUser()" value="Register User"/><br/>
<input type="button" onclick="createCategory()" value="Create Category"/>
<input type="button" onclick="createTask()" value="Create Task"/>
<input type="button" onclick="createCalendar()" value="Create Calendar"/>

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js">
</script>

<script type="text/javascript">


function createCalendar()
{
	
	var json = JSON.stringify( {"createdDate":1339992218216,"modifiedDate":1339992218216,"createdDateZone":1339992218216,"modifiedDateZone":1339992218216,"id":0,"calendarName":"sample","member":{"createdDate":1339992217933,"modifiedDate":1339992217933,"createdDateZone":1339992217933,"modifiedDateZone":1339992217933,"id":"6723664187279295460-9058658185027217134"},"generic":false,"modelName":"Calendar"});
	$.ajax({
	    type: "POST",
	    contentType: "application/json; charset=utf-8",
	    url: "http://test.homefellas.com/ws/rest/task/calendar/create",
	    data: json,
	    dataType: "json"
	});
}

function registerUser(){	
	
	var json = JSON.stringify( {"name":"Tim Delesio","member":{"password":"test12","ticket":"123456","email":"tdelesio2@gmail.com"}});
	$.ajax({
	    type: "POST",
	    contentType: "application/json; charset=utf-8",
	    url: "http://test.homefellas.com/ws/rest/users/register",
	    data: json,
	    dataType: "json"
	});

	}
	

function createCategory(){	
	
//var json = JSON.stringify( {"name":"Tim Delesio","authenication":{"password":"test12","ticket":"123456","email":"tdelesio@gmail.com"});
//var json = JSON.stringify( {"categoryName":"??????????","privateCategory":true,"sortOrder":3424655,"member":{"password":"??????????","ticket":"??????????","roles":[{"role":"ROLE_HF_USER","id":1,"createdDate":1331670651928}],"joined":true,"email":"user@homefellas.com","id":4704826009253217592,"createdDate":-2092936553217511684},"id":0,"createdDate":6698307851216200510});
//$.ajax({
  //  type: "POST",
 //   contentType: "application/json; charset=utf-8",
//	url: "/ws/rest/task/createCategory",
 //   data: json,
//    dataType: "json"
//});

}

function createTask(){	
	
	//var json = JSON.stringify( {"name":"Tim Delesio","authenication":{"password":"test12","ticket":"123456","email":"tdelesio@gmail.com"});
	//var json = JSON.stringify( {"title":"some title","taskCreator":{"id":"3"},"reminders":[{"notificationType":"1","pushType":"0","alertTime":"2012-04-18"}]});
	var json = JSON.stringify( {"id":"be44ad30-a564-11e1-8966-4dcdac83958y","title":"Create Task","progress":"1","priority":"2","show":true,"publicTask":true,"taskCreator":{"id":"6723664187279295460-9058658185027217134"},"modifiedDate":"1337838790530","taskLocation":"Begumpet, Andhra Pradesh, India","startTime":"2012-05-24T06:00:00.000Z","endTime":"2012-05-24T12:00:00.000Z","sortTimeStamp":"2012-05-24T05:53:10.530Z"}  );
	$.ajax({
	    type: "POST",
	    contentType: "application/json; charset=utf-8",
//	    url: "/users/register"
//	    url: "/dealfinder/rest/finder",
		url: "/ws/rest/task/create",
	    data: json,
	    dataType: "json"
	});

	}
</script>


</body>
</html>
