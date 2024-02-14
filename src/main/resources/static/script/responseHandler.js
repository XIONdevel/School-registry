//if code = 401
function refreshToken() {
    try {
        fetch("/api/v1/auth/refresh-token", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(function (response) {
            if (!response.ok) {
                localStorage.clear();
                sessionStorage.clear();
                window.location.href = "/api/v1/main/authentication";
            }
            else {
                return response.json();
            }
        }).then(function (json) {
            localStorage.setItem("accessToken", json.accessToken);
        });
    }
    catch (error) {
        console.log("Error:" + error);
    }
}
