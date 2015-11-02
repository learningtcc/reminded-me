<html>
<head>
        <script src="jquery-1.7.1.min.js"></script>
        <script>
        function senddata(){
                $.ajax({
                    type: "POST",
                    url: "/ws-core/rest/core/validate/payload/"+$("#url").val(),
                    contentType: "application/json",
                    dataType: "json",
                    data: $("#data").val(),
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
		<h1>404 Helper</h1>
		<h2>Instructions</h2>
		A lot of times UI developers will get a 404 back from the server and have not real way to verfiy where and what
		the actual error that occured.  When you get a 404, you can use this page to verify the json payloads that you
		are sending.  The below form will work to validate any objects, although I tried to make it easy and list out
		the objects in a dropdown.  If you do not see the object you are trying to validate in the list, let me know 
		and I can add it.  In order to validate a JSON, take what you think is the correct json (what you sent to the
		server and got a 404 on) and paste it into the text area.  This is the same parser that is used on the server
		so it will tell you want you need to change in order to fix your 404s.  
		<br/>
		<br/>
		Because I am so nice here is a link to the <a href="http://test.homefellas.com/static/api/index.html">API</a> to help you fix your problems
		<br/> 
		<h2>Validate JSON </h2>
		<input type="button"  onclick="senddata()" value="Send Data" />
        <table>
        	<tr>
        		<td>Model</td>
        		<td>
<!--         			<input style="width:800px" id="url" value="com.homefellas.rm.task.Task" /> -->
        			<select id="url" >
        				<option value="com.homefellas.rm.task.Task">Task</option>
        				<option value="com.homefellas.rm.task.Category">Category</option>
				 		<option value="com.homefellas.rm.task.Goal">Goal</option>
				 		<option value="com.homefellas.rm.task.TaskComment">TaskComment</option>
				 		<option value="com.homefellas.rm.task.TimelessTaskStat">TimelessTaskStat</option>
				 		<option value="com.homefellas.rm.task.Calendar">Calendar</option>
				 		<option value="com.homefellas.rm.task.RepeatSetup">RepeatSetup</option>
				 		<option value="com.homefellas.rm.reminder.ReminderComment">ReminderComment</option>
				 		<option value="com.homefellas.rm.reminder.Reminder">Reminder</option>
				 		<option value="com.homefellas.rm.reminder.Alarm">Alarm</option>
				 		<option value="com.homefellas.rm.note.Note">Note</option>
				 		<option value="com.homefellas.rm.share.Share">Share</option>
				 		<option value="com.homefellas.rm.share.ShareCalendar">ShareCalendar</option>
				 		<option value="com.homefellas.rm.share.Invite">Invite</option>				 		
				 		<option value="com.homefellas.rm.notification.ClientNotification">ClientNotification</option>
				 		<option value="com.homefellas.rm.user.PersonalPointScore">PersonalPointScore</option>
				 		<option value="com.homefellas.user.Profile">Profile</option>
				 		<option value="com.homefellas.user.ExtendedProfile">ExtendedProfile</option>
				 		<option value="com.homefellas.rm.task.AppleIOSCalEvent">AppleIOSCallEvent</option>
        			</select>
        		</td>
        	</tr>
        	<tr>
        		<td>json</td>
        		<td><textarea style="width:800px;height:400px" id="data">{"id":"8db041e0-41de-11e2-94d0-3fd23413f161","title":"time test","startTime":null,"endTime":"2012-12-19T03:57:00-05:00","startTimeZone":null,"endTimeZone":"EDT","progress":"2","priority":"0","taskLocation":null,"createdDate":"1355043625570","modifiedDate":"1355608377218","sortTimeStamp":"2012-12-19T13:57:00Z","sortOrder":"0","aParent":"false","publicTask":"true","show":"true","categories":[{"id":"cfc45300-dc2b-11e1-b6c9-4bec6eb18bef"}],"calendars":[{"id":"3b67f9d0-d552-11e1-b335-3d0b8700e7c1"}],"taskCreator":{"id":"2"},"modelName":"Task","lastModifiedDeviceId":"-8976730079442771761-1370800390510261698"}</textarea><br/></td>
        	</tr>
        
        
        
        </table>
        

        <div id="result" style="width:800px;height:auto;padding:10px;">
        </div>
        
</body>
</html>