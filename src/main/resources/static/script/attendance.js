

document.addEventListener("DOMContentLoaded", function () {
    async function fetchDataForSelector() {
        const token = localStorage.getItem("accessToken");
        const response = await fetch("/api/v1/attend/selectors",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "accessToken": token,
                })
            }
        )
        return response.json();
    }

    async function fetchDataForTable(groupId, date) {
        const token = localStorage.getItem("accessToken");
        const response = await fetch("/api/v1/attend/get",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": "Bearer ${token}"
                },
                body: JSON.stringify({
                    "accessToken": token,
                    "groupId": groupId,
                    "date": date
                })
            });
        return response.json();
    }

    async function fillSelector() {
        let data = fetchDataForSelector();
        const selector = document.getElementById("groupSelector");
        data.forEach(group => {
            let option = document.createElement('option');
            option.value = group.id;
            option.text = group.name;
            selector.appendChild(option);
        });
    }

    fillSelector();

    async function fillTable() {
        const date = document.getElementById('dateSelector').value;
        const groupId = document.getElementById("groupSelector").value;
        const jsonData = await fetchDataForTable(groupId, date);
        const table = document.createElement("table");

        //filling
        //first row
        const headRow = table.insertRow(0);
        const dateCell = headRow.insertCell(0);
        dateCell.innerHTML = date;

        //TODO: attend
                

    }

});


