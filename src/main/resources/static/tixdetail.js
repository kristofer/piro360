const API_URL = `http://localhost:8080`;

function fetchTicket(ticketid) {
  fetch(`${API_URL}/api/tickets/${ticketid}`)
    .then(res => {
      //console.log("res is ", Object.prototype.toString.call(res));
      return res.json();
    })
    .then(data => {
      showTicketDetail(data);
    })
    .catch(error => {
      console.log(`Error Fetching data : ${error}`);
      document.getElementById('posts').innerHTML = 'Error Loading Single Ticket Data';
    });
}

function parseTicketId() {
  try {
    var url_string = window.location.href.toLowerCase();
    var url = new URL(url_string);
    var ticketid = url.searchParams.get('ticketid');
    return ticketid;
  } catch (err) {
    console.log("Issues with Parsing URL Parameter's - " + err);
    return '0';
  }
}
// takes a UNIX integer date, and produces a prettier human string
function dateOf(date) {
  const milliseconds = date * 1000; // 1575909015000
  const dateObject = new Date(milliseconds);
  const humanDateFormat = dateObject.toLocaleString(); //2019-12-9 10:30:15
  return humanDateFormat;
}

function showTicketDetail(post) {
  // the data parameter will be a JS array of JS objects
  // this uses a combination of "HTML building" DOM methods (the document createElements) and
  // simple string interpolation (see the 'a' tag on title)
  // both are valid ways of building the html.
  const ul = document.getElementById('post');
  const detail = document.createDocumentFragment();

  console.log('Ticket:', post);
  let li = document.createElement('div');
  let title = document.createElement('h2');
  let body = document.createElement('p');
  let by = document.createElement('p');
  title.innerHTML = `${post.title}`;
  body.innerHTML = `${post.description}`;
  //let postedTime = dateOf(post.time)
  by.innerHTML = `${post.date} - ${post.reportedBy}`;

  li.appendChild(title);
  li.appendChild(body);
  li.appendChild(by);
  detail.appendChild(li);

  ul.appendChild(detail);
}

function handlePage() {
  let ticketid = parseTicketId();
  console.log('ticketId: ', ticketid);

  if (ticketid != null) {
    console.log('found a ticketId');
    fetchTicket(ticketid);
  }
}

handlePage();
