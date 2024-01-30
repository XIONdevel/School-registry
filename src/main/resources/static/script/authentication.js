//TODO: add error handling
//TODO: add response handling
function logout() {
    localStorage.removeItem("accessToken");
    sessionStorage.removeItem("accessToken");
    fetch("/api/v1/auth/logout",
        {
            method: "POST",
            credentials: "include"
        }
    );
    window.location.href = "/";
}

function refresh() {
    localStorage.removeItem("accessToken");
    sessionStorage.removeItem("accessToken");
    fetch("/api/v1/auth/refresh-token",
        {
            method: "POST",
            credentials: "include"
        }
    );
}

function register(email, password, role) {
    fetch("/api/v1/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password,
                role: role
            })
    });
}

function authenticate(email, password) {
    fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    });
}