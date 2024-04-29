// Funzione per il caricamento del file CSV
function uploadCSV() {
    const input = document.getElementById('fileInput');
    const data = new FormData();
    data.append('file', input.files[0]);

    fetch('/upload_csv', {
        method: 'POST',
        body: data
    })
    .then(response => response.text())
    .then(text => document.getElementById('csvUploadResult').innerText = text)
    .catch(err => document.getElementById('csvUploadResult').innerText = 'Error uploading file: ' + err.message);
}

function validateAndSavePhoneNumber() {
    const phoneNumber = document.getElementById('phoneNumberInput').value;
    fetch('/controllAndSave/' + encodeURIComponent(phoneNumber), {
        method: 'GET'
    })
    .then(response => {
        console.log('Response received:', response);
        if (response.ok) {
            return response.json();
        } else {
            response.text().then(text => {
                console.error('Failed to fetch data:', text);
                throw new Error('Failed to validate: ' + text);
            });
        }
    })
    .then(data => {
        console.log('Data:', data);
        if (data && data.length > 0) {
                const firstResult = data[0];
                const phoneNumber = firstResult.phoneNumber || 'N/A'; // Usa 'N/A' se il phoneNumber è vuoto
                const status = firstResult.status || 'N/A'; // Usa 'N/A' se lo status è vuoto
            const table = `<table>
                             <tr>
                               <th>Phone Number</th>
                               <th>Status</th>
                             </tr>
                             <tr>
                               <td>${phoneNumber}</td>
                               <td>${status}</td>
                             </tr>
                           </table>`;
            document.getElementById('phoneNumberResult').innerHTML = table;
        } else {
            document.getElementById('phoneNumberResult').innerText = 'No valid data returned';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('phoneNumberResult').innerText = 'Error: ' + error.message;
    });
}

