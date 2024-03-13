//if code = 401
function refreshToken() {
    try {
        fetch("/api/v1/auth/refresh-token", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
            if (!response.ok) {
                localStorage.clear();
                sessionStorage.clear();
                window.location.href = "/api/v1/main/authentication";
            }
            else {
                return response.json();
            }
        }).then(json => {
            localStorage.setItem("accessToken", json.accessToken);
            window.location.reload();
        });
    }
    catch (error) {
        console.log("Error:" + error);
    }
}
