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
        
       
        
        
        </script>
</head>
<body>
		<div id="result" style="width:800px;height:auto;padding:10px;">
        </div>
        
        <input style="width:800px" id="rurl" value="/ws/rest/" /><br />
        <textarea style="width:800px;height:100px" id="rdata"></textarea><br/>
        <input type="button"  onclick="register()" value="Register" />
		<br/>
		<br/>
		<br/>
		

        
        
</body>
</html>