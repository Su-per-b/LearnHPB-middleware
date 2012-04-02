


function findSocketServer3() {

	url = String (window.location);
	console.log('window.location: '+ url);
	
	hostname = url.split('/')[2];
	console.log('hostname: '+ hostname);
	
	SocketServer3Url = "ws://" + hostname + ":8081/";

}

function onLoad() {
	
	$('clearButton').onclick = function(event) {
		$('messageBox').innerHTML = '';
	};
	
	// "Join" button click opens Chat WebSockets
	$('submitButton').onclick = function(event) {
	
		//filename = $F('simulationSelect');
		
		channel.sendMessage("start");
		
		//$('commandInput').value = '';
		
		return false;
	};
	
	SocketServer3Url = 'none';
	findSocketServer3();


	
	
	if (!window.WebSocket && !window.MozWebSocket )
		alert("WebSocket not supported by this browser");
	
	
	channel.connect();
	
	
}



function $() {
	return document.getElementById(arguments[0]);
}
function $F() {
	return document.getElementById(arguments[0]).value;
}



function getKeyCode(ev) {
	if (window.event)
		return window.event.keyCode;
	return ev.keyCode;
}
	

var channel = {
		
	connect : function() {

		channel.output('establishing connection to: ' + SocketServer3Url);

		
		if (window.MozWebSocket) {
			this._ws = new MozWebSocket(SocketServer3Url);
		} else if (window.WebSocket) {
			this._ws = new WebSocket(SocketServer3Url);			
		}
			
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
	
	


	