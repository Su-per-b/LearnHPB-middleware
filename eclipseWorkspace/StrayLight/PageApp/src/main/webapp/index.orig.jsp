
<html>
<head>
<title>WebSocket Test Time: <%= new java.util.Date() %></title>
<script type="text/javascript">



	SocketServer3Url = 'none';
	findSocketServer3();
	
	function findSocketServer3() {

		url = String (window.location)
		console.log('window.location: '+ url)
		
		hostname = url.split('/')[2];
		console.log('hostname: '+ hostname)
		
		SocketServer3Url = "ws://" + hostname + ":8080/"

	}



	var remote
	if (!window.WebSocket)
		alert("WebSocket not supported by this browser");

	function $() {
		return document.getElementById(arguments[0]);
	}
	function $F() {
		return document.getElementById(arguments[0]).value;
	}
	function connect() {
		channel.connect();
	}


	function getKeyCode(ev) {
		if (window.event)
			return window.event.keyCode;
		return ev.keyCode;
	}
	

	var channel = {
			
		connect : function() {

			channel.output('establishing connection to: ' + SocketServer3Url);


			
			
			this._ws = new WebSocket(SocketServer3Url);
			this._ws.onopen = this._onopen;
			this._ws.onmessage = this._onmessage;
			this._ws.onclose = this._onclose;
			this._ws.onerror = this._onerror;
		},

		sendMessage : function(text) {
			
			gallons = text;
			
			if (text != null && text.length > 0)
				channel._send(text);
				
			channel.output('request sent');
		},

		_onopen : function() {			
			$('submitButton').disabled = false;
			//$('submitButton').disabled = false;
			
			channel.output('connected');
		},
		
		output : function(text) {
				var messageBox = $('messageBox');
				var spanFrom = document.createElement('span');
				var spanText = document.createElement('span');
				spanText.className = 'text';
				spanText.innerHTML = text;
				var lineBreak = document.createElement('br');
				messageBox.appendChild(spanFrom);
				messageBox.appendChild(spanText);
				messageBox.appendChild(lineBreak);
				messageBox.scrollTop = messageBox.scrollHeight - messageBox.clientHeight;
		},
		
		_onmessage : function(m) {
			if (m.data) {

				var text = m.data;
				channel.output(text);
			}
		},

		_onclose : function(m) {
			this._ws = null;

			$('messageBox').innerHTML = '';
		},

		_onerror : function(e) {
			alert(e);
		},
		
		_send : function( message) {

			if (this._ws)
				this._ws.send(message);
		}
	};
</script>
<style type='text/css'>

body {
	font-family:sans-serif;
}
div {
	border: 0px solid black;
}

div#messageBox {
	clear: both;
	width: 90%;
	height: 80ex;
	overflow: auto;
	background-color: #f0f0f0;
	padding: 4px;
	border: 1px solid black;
}

div#input {
	clear: both;
	width: 40em;
	padding: 4px;
	background-color: #e0e0e0;
	border: 1px solid black;
	border-top: 0px
}

input#phrase {
	width: 30em;
	background-color: #e0f0f0;
}

input#username {
	width: 14em;
	background-color: #e0f0f0;
}

div.hidden {
	display: none;
}

span.from {
	font-weight: bold;
}
</style>
</head>
<body onload="connect()" >
	<h3>Simulation Test</h3>
	
	<div>

		<div>
		
			simulation:&nbsp;
			<select id="simulationSelect">
			  <option selected="selected" value="testFMI.fmu">testFMI.fmu</option>
			  <option value="bouncingBall.fmu">bouncingBall.fmu</option>
			  <option value="Furuta.fmu">Furuta.fmu</option>
			  <option value="SecondOrder.fmu">SecondOrder.fmu</option>
			</select>
		
			<input id="submitButton" disabled="disabled"
				class="button" type="submit" name="submitButton" value="Submit" />
				
			<input id="clearButton" 
				class="button" type="button" name="submitButton" value="Clear" />		
		</div>
		
	</div>
	
		<br />
	<div id="messageBox"></div>
	

	
	<script type="text/javascript">	

	$('clearButton').onclick = function(event) {
		
		$('messageBox').innerHTML = '';
	}
	
	// "Join" button click opens Chat WebSockets
	$('submitButton').onclick = function(event) {
	
		filename = $F('simulationSelect');
		
		channel.sendMessage(filename);
		
		//$('commandInput').value = '';
		
		return false;
	};
	
		

	</script>
		
</body>
</html>