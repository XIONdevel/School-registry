//TODO: add error handling
function validatePassword(password) { //TODO: improve password validation
    return !(password.length < 8 || password.length > 32);
}

function validateEmail(email) {
    const regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return regex.test(String(email).toLowerCase());
}

async function register() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    const role = document.getElementById("roleSelect").value;

    const exception = document.getElementById("exception");

    if (!validateEmail(email)) {
        exception.innerHTML = "Email is not valid";
        return;
        } else if (!validatePassword(password)) {
            exception.innerHTML = "Password is not valid";
            return;
    } else if (String(role).length === 0) {
        exception.innerHTML = "Select the role";
        return;
    } else {
        exception.innerHTML = "";
    }

    await fetch("/api/v1/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password,
            role: role
        })
    }).then(response => {
        if (response.ok) {
            return response.json().then(data => {
                localStorage.setItem("accessToken", data.accessToken);
                document.cookie = data.cookie;
                exception.innerHTML = "User successfully saved!"
            });
        } else if (response.status === 409) {
            return response.json().then(errorData => {
                exception.innerHTML = errorData.message;
            });
        } else {
            console.error('Unexpected response status:', response.status);
            return Promise.reject('Unexpected response status');
        }
    }).catch(error => {
        console.error('Fetch error:', error);
    });
}

document.addEventListener("DOMContentLoaded", function () {
    const dynamicSelector = document.getElementById("roleSelect");
    fetch("/api/v1/auth/getRoles")
        .then(response => response.json())
        .then(data => {
            populateSelector(dynamicSelector, data);
        })
        .catch(error => console.error('Error:', error));
});

function populateSelector(selector, data) {
    selector.innerHTML = "";

    const defaultOption = document.createElement("option");
    defaultOption.text = "Select a role";
    defaultOption.value = "";
    selector.appendChild(defaultOption);

    data.forEach(role => {
        const option = document.createElement("option");
        option.text = role;
        option.value = role;
        selector.appendChild(option);
    });
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














