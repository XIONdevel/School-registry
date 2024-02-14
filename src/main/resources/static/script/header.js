document.addEventListener("DOMContentLoaded", function () {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/v1/main/header', true);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            document.getElementById('header').innerHTML = this.responseText;
            const headerLoadedEvent = new Event('headerLoaded');
            document.dispatchEvent(headerLoadedEvent);
        }
    };
    xhr.send();
});

document.addEventListener("headerLoaded", function () {
    function logCheck() {
        const accessToken = localStorage.getItem("accessToken");
        if (accessToken.length < 10) {
            return false;
        } else {
            fetch("/api/v1/auth/loginCheck", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer " + accessToken
                }
            }).then(response => {
                return response.ok;
            });
        }
    }

    function createButton(text, link) {
        const button = document.createElement("button");
        button.innerText = text;
        button.addEventListener("click", function () {
            window.location.href = link;
        });
        document.getElementById("buttonContainer").appendChild(button);
    }

    async function fillButtons() {
        const ul = document.getElementById("links");
        if (await logCheck()) {
            createButton("logout", "/api/v1/auth/logout");
            createButton("menu", "http://localhost:8080/menu"); //TODO: add menu implementation
            createButton("profile", "http://localhost:8080/profile");//TODO: add profile implementation
        } else {
            createButton("Sign in", "/api/v1/main/authentication");
            createButton("Sign up", "/api/v1/main/registration"); //TODO: change for user registration
        }
    }

    fillButtons();
})


