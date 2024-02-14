function validateEmail(email) {
    const regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return regex.test(String(email).toLowerCase());
}

async function authenticate() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const message = document.getElementById("message");

    if (!validateEmail(email)) {
        message.innerText = "Email is not valid";
        return;
    }

    await fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    }).then(response => {
        if (!response.ok) {
            message.innerText = "Error";
        }
        return response.json();
    }).then(data => {
        localStorage.setItem("accessToken", data.accessToken);
        document.cookie = data.cookie;
        return window.location.href = "";
    })

}

//password button
document.addEventListener("DOMContentLoaded", function () {
    const passwordInput = document.getElementById("password");
    const toggleButton = document.getElementById("togglePassword");

    toggleButton.addEventListener("click", function () {
        passwordInput.type = (passwordInput.type === "password") ? "text" : "password";
        toggleButton.textContent = (passwordInput.type === "password") ? "Show Password" : "Hide Password";
    });
});

























