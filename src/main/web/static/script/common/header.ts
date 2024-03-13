document.addEventListener("DOMContentLoaded", function (): void {
    let xhr = new XMLHttpRequest();
    xhr.open('GET', '/api/v1/main/header', true);
    xhr.onreadystatechange = function (): void {
        if (this.readyState === 4 && this.status === 200) {
            document.getElementById('header').innerHTML = this.responseText;
            const headerLoadedEvent = new Event('headerLoaded');
            document.dispatchEvent(headerLoadedEvent);
        }
    };
    xhr.send();
});

document.addEventListener("headerLoaded", function (): void {
    const container = document.getElementById("button-container");

    function generateBasicButtons(): void {
        container.innerHTML = '';
        const loginButton = document.createElement("button");
        const signupButton = document.createElement("button");

        loginButton.innerText = "Log in";
        loginButton.addEventListener("click", function (): void {
            window.location.href = "/api/v1/main/authentication";
        });
        container.appendChild(loginButton);

        signupButton.innerText = "Sign up";
        signupButton.addEventListener("click", function (): void {
            window.location.href = "/api/v1/main/registration";
        });
        container.appendChild(signupButton);
    }

    function generateLoggedButtons(): void {
        container.innerHTML = '';
        createMenu();
        createProfileButton();
        createMenuButton();
    }

    function createProfileButton(): void {
        const profileButton = document.createElement("button");
        const profImg = document.createElement("img");
        profImg.src = "/images/profile.png";
        profImg.alt = "profile";
        profImg.id = "profImg";
        profileButton.appendChild(profImg);
        profileButton.addEventListener("click", function (): void {
            window.location.href = "/api/v1/main/profile";
        });
        profileButton.id = "profileButton";
        container.appendChild(profileButton);
    }

    function createMenuButton(): void {
        const menuButton = document.createElement("button");
        const menuImg = document.createElement("img");
        menuImg.src = "/images/menu.jpg";
        menuImg.alt = "menu";
        menuImg.id = "menuImg";
        menuButton.appendChild(menuImg);
        menuButton.addEventListener("click", function (): void {
            const menu = document.getElementById("menu");
            if (menu.style.display === "block") {
                menu.style.display = "none";
            } else {
                menu.style.display = "block";
            }
        });
        menuButton.id = "menuButton";
        container.appendChild(menuButton);
    }

    function setLogo(): void {
        const logo = document.getElementById("logo");
        const logoImg = document.createElement("img");
        logoImg.alt = "logo";
        logoImg.src = "/images/logo.png";
        logoImg.id = "logoImg";
        logo.appendChild(logoImg);
        logo.addEventListener("click", function (): void {
            window.location.href = "/api/v1/main/";
        });
    }

    function fillButtons(): void {
        fetch("/api/v1/auth/loginCheck", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("accessToken")
            }
        }).then(response => {
            const status: number = response.status;
            if (status == 200) {
                generateLoggedButtons();
            } else if (status == 401) {
                fetch("/api/v1/auth/refresh-token", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    }
                }).then(response => {
                    if (!response.ok) {
                        localStorage.clear();
                        sessionStorage.clear();
                        generateBasicButtons();
                    } else {
                        localStorage.setItem("accessToken", response.headers.get("Authorization"));
                        generateLoggedButtons();
                    }
                });
            } else {
                console.log("Unusual status: " + status);
                generateBasicButtons();
            }
        });
    }

    function createMenu(): void {
        const menu = document.createElement("div");
        menu.id = "menu";
        menu.style.display = "none";

        //TODO: add more buttons

        document.body.appendChild(menu);
    }

    setLogo();
    fillButtons();
});


