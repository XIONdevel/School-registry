document.addEventListener("DOMContentLoaded", function (): void {

    interface DataFormat {
        firstname: string;
        lastname: string;
        phone: string;
        dob: string;
        position: string;
        group: string;
    }

    function createProfileCard(data: DataFormat): HTMLElement {
        const profileCard = document.createElement('div');
        profileCard.classList.add('profile-card');
        console.log("Data F: " + data);

        if (data.firstname !== undefined && data.lastname !== undefined) {
            const nameElement = document.createElement('h2');
            nameElement.textContent = `${data.firstname} ${data.lastname}`;
            profileCard.appendChild(nameElement);
        }

        if (data.phone !== undefined) {
            const phoneElement = document.createElement('p');
            phoneElement.textContent = `Phone: ${data.phone}`;
            profileCard.appendChild(phoneElement);
        }

        if (data.dob !== undefined) {
            const dobElement = document.createElement('p');
            dobElement.textContent = `Date of Birth: ${data.dob}`;
            profileCard.appendChild(dobElement);
        }

        if (data.position !== undefined) {
            const positionElement = document.createElement('p');
            positionElement.textContent = `Position: ${data.position}`;
            profileCard.appendChild(positionElement);
        }

        if (data.group !== undefined) {
            const groupElement = document.createElement('p');
            groupElement.textContent = `Group: ${data.group}`;
            profileCard.appendChild(groupElement);
        }

        return profileCard;
    }

    function fetchProfileData() {
        fetch("/api/v1/profile/get", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("accessToken")
            }
        }).then(async response => {
            const status: number = response.status;
            console.log(status);
            console.log(response);
            if (status == 200) {
                displayProfileData(await response.json());
            } else if (status == 401) {
                refreshToken();
            } else if (status == 403) {
                window.history.back();
            } else if (status == 404) {
                const textDiv = document.createElement("div");
                textDiv.innerText = "Your data not found, ask administrator to fill it";
                document.getElementById("profile-container").appendChild(textDiv);
            } else {
                window.history.back();
            }
        });
    }

    function displayProfileData(data: object): void {
        console.log("Data: " + data);
        const userInfo: DataFormat = data as DataFormat;
        document.getElementById("profile-container").appendChild(createProfileCard(userInfo));
    }

    fetchProfileData();
});


