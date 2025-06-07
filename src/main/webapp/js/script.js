var ws;

function connect(){
    var senderName = document.getElementById("username").value;
    if(senderName.trim() === ""){
        displayErrorMessage("Please Provide a username");
        return;
    }
    // ws://localhost:8080/ChatApplication/chat/username
    
    if (ws && ws.readyState === WebSocket.OPEN){
        displayErrorMessage("You are already connected to the server");
        return;
    }
    
    var host = document.location.host; // Getting the IP Address and Port Number
    var pathname = document.location.pathname; // Getting the project name
    
    ws = new WebSocket("ws://" + host + pathname + "chat/" + senderName);
    
    ws.onmessage = function(event) {
        var data = JSON.parse(event.data);
        
        // Checking if the data received is an array or not
        if (Array.isArray(data)){
            updateUserList(data);
        }else{
            displayMessage(data.senderName, data.messageContent);
        }
    }; 
}

function send(){
    if(!ws || ws.readyState !== WebSocket.OPEN){
        displayErrorMessage("You are not connected to the chat. Please Provide your Username");
        return;
    }
    var content = document.getElementById("message").value;
    
    if(content.trim() ===""){
        displayErrorMessage("Please Enter a message");
        return;
    }
    var json = JSON.stringify({
        messageContent: content
    });
    
    ws.send(json);
    
    document.getElementById("message").value = "";
}

function disconnect(){
    if(ws){
        ws.close();
        var userListElement = document.getElementById("userList");
        userListElement.innerHTML = "";
        displayErrorMessage("You are disconnected from the server");
    }
}

function displayErrorMessage(message){
    var errorMessageElement = document.getElementById("error-message");
    errorMessageElement.textContent = message;
    errorMessageElement.classList.remove("hidden");
    
    setTimeout(() =>{
        errorMessageElement.classList.add("hidden");
    }, 7000);
}

function updateUserList(users){
    var userListElement = document.getElementById("userList");
    userListElement.innerHTML = "";
    
    users.forEach((user) => {
        var listItem = document.createElement("li");
        listItem.textContent = user;
        userListElement.appendChild(listItem); 
    });
}

function displayMessage(sender, messageContent){
    var chatLog = document.getElementById("chatLog");
    chatLog.innerHTML += `${sender}: ${messageContent} \n`;
    chatLog.scrollTop = chatLog.scrollHeight;
}
