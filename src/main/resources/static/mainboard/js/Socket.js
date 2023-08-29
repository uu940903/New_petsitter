'user strict';

document.write("<script\n" +
    "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
    "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
    "  crossorigin=\"anonymous\"></script>")

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();

    isDuplicateName();

    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    var socket = new SockJS('/ws-stomp');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onerror)

    event.preventDefault();
}

function onConnected() {
    stompClient.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    stompClient.send("/pub/chat/enterUser", {}, JSON.stringify({"roomId": roomId, sender: username, type: 'ENTER'}))

    connectingElement.classList.add('hidden');
}

function isDuplicateName() {
    $.ajax({
        type: "GET",
        url: "/chat/duplicateName",
        data: {
            "username": username,
            "roomId": roomId
        },
        success: function (data) {
            console.log("함수 동작 확인: " + data);

            username = data;
        }
    })
}

function getUserList() {
    const $list = $("#list");

    $.ajax({
        type: "GET",
        url: "/chat/userlist",
        data: {
            "roomId": roomId
        },
        success: function (data) {
            var users = "";
            for (let i = 0; i < data.length; i++) {
                users += "<li class='dropdown-item'>" + data[i] + "</li>"
            }
            $list.html(users);
        }
    })
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again.';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            "roomId": roomId,
            sender: username,
            message: messageInput.value,
            type: 'TALK'
        };

        stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var chat = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (chat.type === 'ENTER') {
        messageElement.classList.add('event-message');
        chat.textContent = chat.sender + chat.message;
        getUserList();
    } else if (chat.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        getUserList();
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(chat.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(chat.message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);

    return colors[index];
}

usernameForm.addEventListener('submit', connect, true)
messageForm.addEventListener('submit', sendMessage, true)