async function checkPassword(){
    let passInput = document.getElementById("pass");
    let pass = passInput.value;
    let message = "";
    let color = "";
    if (!pass){
        message = "Introduzca una contraseña"
        color = "red"
    }else if (pass.length < 8){
        message = "Contraseña débil"
        color = "orange"
    }else{
        message = "Contraseña segura"
        color = "green"
    }

    const messageDiv = document.getElementById("passContent");
    messageDiv.innerHTML = message;
    messageDiv.style.color = color;
}

async function checkMail(){
    let mailInput = document.getElementById("mail");
    let mail = mailInput.value;

    const response = await fetch(`/availableMail?mail=${mail}`);
    const responseObj = await response.json();
    
    let message = responseObj.available ? "Email disponible" : "Email no disponible";
    let color = responseObj.available ? "green" : "red";

    const messageDiv = document.getElementById("mailContent");
    messageDiv.innerHTML = message;
    messageDiv.style.color = color;
}

async function checkNick(){
    let nickInput = document.getElementById("nick");
    let nick = nickInput.value;

    const response = await fetch(`/availableNick?nick=${nick}`);
    const responseObj = await response.json();
    
    let message = responseObj.available ? "Nick disponible" : "Nick no disponible";
    let color = responseObj.available ? "green" : "red";

    const messageDiv = document.getElementById("nickContent");
    messageDiv.innerHTML = message;
    messageDiv.style.color = color;
}