'use strict';

var HermesRTC = function HermesRTC(appKey) {
    if (!(this instanceof HermesRTC)) {
        return new HermesRTC(appKey);
    }
    
    var config = {
			wsURL : 'wss://127.0.0.1:8443/signaling',
			mediaConfig : {
				video : true,
				audio : true,
			},
			peerConfig : {
				iceServers: [
			        {urls: "stun:23.21.150.121"},
			        {urls: "stun:stun.l.google.com:19302"},
			        {urls: "turn:numb.viagenie.ca", credential: "webrtcdemo", username: "louis@mozilla.com"}
			    ],
				iceTransportPolicy:'all',
				rtcpMuxPolicy:'negotiate'
			}
    }
    navigator.mediaDevices.enumerateDevices().then(
    		function (result) {
    			result.map(function (r) {
    				if(r.kind == "audioinput")
    					config.mediaConfig.audio = true;
    				else(r.kind == "videoinput")
    					config.mediaConfig.video = true;
    			})
    		}
    )
    
    var that = this;
    this.mediaConfig = config.mediaConfig !== undefined ? config.mediaConfig : null;
    this.type = config.type;
    this.signaling = new WebSocket(config.wsURL);
    this.peerConnections = {};
    this.localStream = null;
    this.signals = {};
    this.channelReady = false;
    this.waiting = [];
    this.appKey = appKey;
    this.customSignals = {};
    
    this.call = function(event, data) {
        for ( var signal in this.signals) {
            if (event === signal) {
                return this.signals[event](this, data);
            }
        }
        console.log('Event ' + event + ' do not have defined function');
    };

    this.request = function(signal, to, convId, custom) {
        var req = JSON.stringify({
            signal: signal,
            to: to,
            content: convId,
            appKey: this.appKey,
            custom: custom });
        if(!this.channelReady){
            this.waiting.push(req);
        } else {
            console.log("req: " + req);
            this.signaling.send(req);
        }
    };

    this.signaling.onmessage = function(event) {
        console.log("res: " + event.data);
        var signal = JSON.parse(event.data);
        that.call(signal.signal, signal);
    };

    this.signaling.onopen = function() {
        console.log("channel ready");
        that.setChannelReady();
    };

    this.signaling.onclose = function(event) {
        that.call('close', event);
    };

    this.signaling.onerror = function(event) {
        that.call('error', event);
    };

    this.setChannelReady = function(){
        for(var w in that.waiting){
            console.log("req: " + w);
            that.signaling.send(w);
        }
        that.channelReady = true;
    }

    this.preparePeerConnection = function(hermesRTC, member) {
        if (hermesRTC.peerConnections[member] == undefined) {
            var pc = new RTCPeerConnection(config.peerConfig);
            pc.onaddstream = function(evt) {
                hermesRTC.call('remoteStream', {
                    member : member,
                    stream : evt.stream
                });
            };
            pc.onicecandidate = function(evt) {
                handle(pc, evt);

                function handle(pc, evt){
                    if((pc.signalingState || pc.readyState) == 'stable'
                        && hermesRTC.peerConnections[member]['rem'] == true){
                        hermesRTC.onIceCandidate(hermesRTC, member, evt);
                        return;
                    }
                    setTimeout(function(){ handle(pc, evt); }, 2000);
                }
            };
            hermesRTC.peerConnections[member] = {}
            hermesRTC.peerConnections[member]['pc'] = pc;
            hermesRTC.peerConnections[member]['rem'] = false;
        }
        return hermesRTC.peerConnections[member];
    };

    this.offerRequest = function(hermesRTC, from) {
        hermesRTC.offerResponse(hermesRTC, from);
    };

    this.offerResponse = function(hermesRTC, signal) {
        var pc = hermesRTC.preparePeerConnection(hermesRTC, signal.from);
        pc['pc'].addStream(hermesRTC.localStream);
        pc['pc'].createOffer({offerToReceiveAudio: 1, offerToReceiveVideo: 1})
            .then(function(desc) {
                pc['pc'].setLocalDescription(desc)
                    .then(function() {
                        hermesRTC.request('offerResponse', signal.from, desc.sdp);
                    }, that.error);
            });
    };

    this.answerRequest = function(hermesRTC, signal) {
        hermesRTC.answerResponse(hermesRTC, signal);
    };

    this.answerResponse = function(hermesRTC, signal) {
        var pc = hermesRTC.preparePeerConnection(hermesRTC, signal.from);
        pc['pc'].addStream(hermesRTC.localStream);
        pc['pc'].setRemoteDescription(new RTCSessionDescription({
            type : 'offer',
            sdp : signal.content
        })).then(function() {
            pc['rem'] = true;
            pc['pc'].createAnswer().then(function(desc) {
                pc['pc'].setLocalDescription(desc).then(function() {
                    hermesRTC.request('answerResponse', signal.from, desc.sdp);
                });
              });
          });
    };

    this.finalize = function(hermesRTC, signal) {
        var pc = hermesRTC.preparePeerConnection(hermesRTC, signal.from);
        pc['pc'].setRemoteDescription(new RTCSessionDescription({
            type : 'answer',
            sdp : signal.content
        })).then(function(){
            pc['rem'] = true;
        });
    };

    this.close = function(hermesRTC, event) {
        hermesRTC.signaling.close();
        if(hermesRTC.localStream != null){
            hermesRTC.localStream.stop();
        }
    };

    this.candidate = function(hermesRTC, signal) {
        var pc = hermesRTC.preparePeerConnection(hermesRTC, signal.from);
        pc['pc'].addIceCandidate(new RTCIceCandidate(JSON.parse(signal.content.replace(new RegExp('\'', 'g'), '"'))), that.success, that.error);
    }

    this.init = function() {
        this.on('offerRequest', this.offerRequest);
        this.on('answerRequest', this.answerRequest);
        this.on('finalize', this.finalize);
        this.on('candidate', this.candidate);
        this.on('close', this.close);
        this.on('error', this.error);
        this.on('ping', function(){});
    };

    this.onIceCandidate = function(hermesRTC, member, event) {
        if (event.candidate) {
            hermesRTC.request('candidate', member, JSON.stringify(event.candidate));
        }
    }

    this.init();

    this.error = function(arg){
        console.log('error: ' + arg);
    }

    this.success = function(arg){
        console.log('success: ' + arg);
    }

    that.onReady = function() {
        console.log('It is highly recommended to override method HermesRTC.onReady');
    };


    if (document.addEventListener) {
        document.addEventListener('DOMContentLoaded', function() {
            that.onReady();
        });
    }
};

// responses and requests constants
HermesRTC.LOCAL_STREAM = "localStream";
HermesRTC.REMOTE_STREAM = "remoteStream";
HermesRTC.CREATE = "create";
HermesRTC.REMOVE = "remove";
HermesRTC.ERROR = "error";
HermesRTC.JOIN = "join";
HermesRTC.LEFT = "left";
HermesRTC.NEW_JOIN = "newJoined";
HermesRTC.CUSTOM_SIGNAL = "customSignal";

// error codes
HermesRTC.MEMBER_NOT_FOUND ="0001";//
HermesRTC.INVALID_RECIPIENT="0002"; //
HermesRTC.APPKEY_NOT_FOUND="0003";//
HermesRTC.SIGNAL_NOT_FOUND="0004" //

HermesRTC.prototype.on = function on(signal, operation) {
    this.signals[signal] = operation;
};

HermesRTC.prototype.create = function create(convId, custom) {
    var hermesRTC = this;
    this.getCustomSignals(this.appKey)
    navigator.mediaDevices.getUserMedia(hermesRTC.mediaConfig).then(function(stream) {
    hermesRTC.localStream = stream;
    hermesRTC.call(HermesRTC.LOCAL_STREAM, {
        stream : stream
    });
    hermesRTC.request(HermesRTC.CREATE, null, convId, custom);
    }, this.error);
};

HermesRTC.prototype.delegate = function delegate(convId, member ,custom) {
    hermesRTC.request('delegate', member, convId, custom);
};

HermesRTC.prototype.envideo = function envideo(convId, member ,custom) {
    hermesRTC.request('envideo', member, convId, custom);
};

HermesRTC.prototype.devideo = function devideo(convId, member ,custom) {
    hermesRTC.request('devideo', member, convId, custom);
};

HermesRTC.prototype.enmodify = function enmodify(convId, member) {
    hermesRTC.request('enmodify', member, convId, null);
};

HermesRTC.prototype.demodify = function demodify(convId, member ,custom) {
    hermesRTC.request('demodify', member, convId, custom);
};

HermesRTC.prototype.text = function text(convId, custom) {
    var hermesRTC = this;
    hermesRTC.request('text', null, convId, custom);
};

HermesRTC.prototype.getCustomSignals = function(appKey) {
	var hermesRTC = this;
	$.ajax({
		type: 'POST',
		url: '/signal/read/' + hermesRTC.appKey,
		contentType: 'application/json',
		dataType: 'json',
		data: {},
		success: function(data){
			hermesRTC.customSignals = data
			console.log(data)
		},
		error: function(error) {
			
		}
	})
};
HermesRTC.prototype.join = function join(convId, custom) {
    var hermesRTC = this;
    this.getCustomSignals(this.appKey)
    navigator.mediaDevices.getUserMedia(hermesRTC.mediaConfig).then(function(stream) {
        hermesRTC.localStream = stream;
        hermesRTC.call(HermesRTC.LOCAL_STREAM, {
            stream : stream
        });
        hermesRTC.request('join', null, convId, custom);
    }, this.error);
};

HermesRTC.prototype.leave = function leave() {
    var hermesRTC = this;
    hermesRTC.request('left');
    //hermesRTC.signaling.close();
    if(hermesRTC.localStream != null){
    	hermesRTC.localStream.stop();
    }
};

HermesRTC.prototype.remove = function remove(roomKey) {
	hermesRTC.request(HermesRTC.REMOVE, null, roomKey, null);
};

//module.exports.HermesRTC = HermesRTC;
