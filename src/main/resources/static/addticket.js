const API_URL = `http://localhost:8080`;

function doPostOfForm(event) {
  //event.preventDefault();
  const formData = new FormData(event);
  var object = {};
  formData.forEach(function (value, key) {
    object[key] = value;
  });
  var json = JSON.stringify(object);

  postJSON(json);
}

async function postJSON(data) {
  try {
    const response = await fetch(`${API_URL}/api/tickets/`, {
      method: 'POST', // or 'PUT'
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    const result = await response.json();
    console.log('Success:', result);
  } catch (error) {
    console.error('Error:', error);
  }
}

const form = document.getElementById('add-tix-form');
form.addEventListener('button', doPostOfForm);
