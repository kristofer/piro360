const API_URL = `http://localhost:8080`;

function doPostOfForm(event) {
  //event.preventDefault();
  var floozy = new FormData(document.getElementById('addpiroform'));

  var object = {};
  for (var p of floozy) {
    let name = p[0];
    let value = p[1];
    object[name] = value;
  }
  var json = JSON.stringify(object);
  // only need to stringify once
  postJSON(json);
}

async function postJSON(data) {
  try {
    const response = await fetch(`${API_URL}/api/piros`, {
      method: 'POST', // or 'PUT'
      headers: {
        accept: '*/*',
        'Content-Type': 'application/json',
      },
      body: data, // This was ALSO Stringifying the data
      // body: JSON.stringify(data)
      // but this is already a string because I did the stringify above
    });

    const result = await response.json();
    console.log('Success:', result);
  } catch (error) {
    console.error('Error:', error);
  }
}

window.addEventListener(
  'DOMContentLoaded',
  function () {
    const form = document.getElementById('addpiroform');
    const button1 = document.getElementById('button1');
    console.log('form is ', form, 'button1 is ', button1, 'doPostOfForm is ', doPostOfForm);

    form.addEventListener(button1, doPostOfForm);
  },
  false,
);

// document.addEventListener('DOMContentLoaded', function() {
//     const API_URL = `http://localhost:8080`;
//     const form = document.querySelector('addpiroform');
//     form.addEventListener('submit', function(event) {
//         event.preventDefault();

//         const title = document.getElementById('title').value;
//         const description = document.getElementById('description').value;
//         const s3urltovideo = document.getElementById('s3urltovideo').value;
//         const created = document.getElementById('created').value;

//         const data = {
//             title: title,
//             description: description,
//             s3urltovideo: s3urltovideo,
//             created: created
//         };
//         console.log('Data to be sent:', data);

//         fetch('${API_URL}/api/piros', {
//             method: 'POST',
//             headers: {
//                 'accept': '*/*',
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(data)
//         })
//         .then(response => response.json())
//         .then(result => {
//             console.log('Data added successfully:', result);
//             // Add any additional logic or UI updates here
//         })
//         .catch(error => {
//             console.error('Error adding data:', error);
//             // Handle any error scenarios here
//         });
//     });
// });
