<html>
<head>
        <script src="jquery-1.7.1.min.js"></script>
        <script>
        function register(){
                $.ajax({
                    type: "POST",
                    url: $("#rurl").val(),
                    contentType: "application/json",
                    dataType: "json",
                    data: $("#rdata").val(),
                        success: function(res) {
                                $("#result").val(res);
                        },
                        error: function(res) {
                                $("#result").val(res);
                        }
                });
        }
        
        function login(){
            $.ajax({
                type: "POST",
                url: $("#lurl").val(),
                contentType: "application/json",
                dataType: "json",
                data: $("#ldata").val(),
                    success: function(res) {
                            $("#result").val(res);
                    },
                    error: function(res) {
                            $("#result").val(res);
                    }
            });
    }
        
        function serviceticket(){
            $.ajax({
                type: "POST",
                url: $("#sturl").val(),
                contentType: "application/json",
                dataType: "json",
                data: $("#stdata").val(),
                    success: function(res) {
                            $("#result").val(res);
                    },
                    error: function(res) {
                            $("#result").val(res);
                    }
            });
    }
        
        
        </script>
</head>
<body>
		<div id="result" style="width:800px;height:auto;padding:10px;">
        </div>
        
        INSTRUCTIONS:<br/>
        1.  First you need to register.  The email address must be unique.<br/>
        2.  Once you are registered, you can then create a TGT (ticket granting ticker) by logging in under form 2.  The TGT should be written down as it will be used to grant ST (service tickets).  If you lose your TGT or it expires for whatever reason, you can re-login to generate a new one.<br/>
		3.  Once you have a TGT, you can then use that to get a ST (service ticket).  A ST  will only last about 30 min and is defined per ws resource url.  We can look in the future on expanding this and/or changing this.<br/>
		4.  Once you get a ST, you can then pass that in the url to the WS resouce you were trying to access via a url parm ticket=${ST}<br/>
		<br/>
		<br/>
        1.  Register:<br/>	        
        <input style="width:800px" id="rurl" value="/ws/rest/users/register" /><br />
        <textarea style="width:800px;height:100px" id="rdata">{"name":"Tim Delesio","member":{"password":"test12","ticket":"123456","email":"tdelesio@gmail.com"}}</textarea><br/>
        <input type="button"  onclick="register()" value="Register" />
		<br/>
		<br/>
		<br/>
		
        2.  Login:<br/>
        <input style="width:800px" id="lurl" value="/login/v1/tickets" /><br />
        <textarea style="width:800px;height:100px" id="ldata">username=tdelesio@gmail.com&password=test12</textarea><br/>
        <input type="button"  onclick="login()" value="Login" />
		<br/>
		<br/>
		<br/>
	        
		
        3.  ST (Service Ticket):<br/>	        
        <input style="width:800px" id="sturl" value="/login/v1/tickets/TGT-1-prEVEMWu3P35T71NHXuQqHo5KVlQg2P3e2WrbkAf7JvH9qxUU0-cas" /><br />
        <textarea style="width:800px;height:100px" id="stdata">service=https://localhost/ws-core/rest/core/jsonize/com.homefellas.rm.task.Task</textarea><br/>
        <input type="button"  onclick="serviceticket()" value="Generate Service Ticket" />
        
        
</body>
</html>