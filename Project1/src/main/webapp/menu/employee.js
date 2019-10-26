const reimburseTypes = [
    'Lodging',
    'Travel',
    'Food',
    'Other'
];

const statusColors = [
    ['#292929', 'Pending'],
    ['#00a000', 'Approved'],
    ['#a00000', 'Denied']
]

let descCount = document.getElementById("reimb-char-count");
let btn = document.getElementById("reimb-allow");
let disableButton = false;
//let tbl = document.getElementById('reimb-dyn-table');
//Set the default emptyTable message.
let smartT = $('#reimb-main-table').DataTable( {
    "language": {
        "emptyTable": "Loading..."
    },
    
    //Remove the search bar
    dom: 'lrtip'
} );
//Custom search event
$('#reimb-status-selector').on('change', function() {
    smartT.search(this.value).draw();
})

let localUsername = localStorage.getItem('username');
let localUserId = null;

function getUserInfo() {
    fetch("/Project1/info/user/" + localUsername)
        .then((response) => { return response.json() })
        .then(json => setUserValues(json));
}

function getUserReimbursements() {
    fetch("/Project1/info/reimbursement/" + localUserId)
        .then((response) => { return response.json() })
        .then(json => populateTable(json));
}

function setUserValues(j) {
    if (j != null) {
        localUserId = j.user_id;
        document.getElementById("info-username").innerHTML = j.username;
        document.getElementById("info-email").innerHTML = j.email;
        document.getElementById("info-fullname").innerHTML = j.firstname + ' ' + j.lastname;
    }

    getUserReimbursements();
}

function formatDateTime(i) {
    if (i != '-') {
        let j = new Date(i).toString().split(' ');
        let time = j[4].split(':');
        let hour = parseInt(time[0]);
        let amPm = (hour >= 12 ? 'PM' : 'AM');
        let trueTime = (hour > 12 ? hour % 12 : hour) + ':' + time[1] + ':' + time[2];
        return j[1] + ' ' + j[2] + ' ' + j[3] + ' ' + trueTime + amPm;
    } else {
        return '-';
    }
}

let count = 0;
function insertToTable(j) {
    let t = $('#reimb-main-table').DataTable();
    if (j.resolved == null) {
        j.resolved = '-';
        j.resolver = '-';
    }

    let n = t.row.add([
        j.id,
        '$' + j.amount.toFixed(2),
        formatDateTime(j.submitted),
        formatDateTime(j.resolved),
        j.resolver,
        reimburseTypes[j.type_id],
        j.description,
        statusColors[j.status_id][1]
    ]).draw().node();

    $(n).find('td:eq(6)').css('width', 800);
    $(n).find('td:eq(7)').css('color', statusColors[j.status_id][0]);
    count++;
}
/*
function insertToTable(j) {
    //Create a new table row
    let t = document.createElement("tr");
    t.innerHTML = `<th scope="row">${j.id}</th>
                   <td>$${parseFloat(j.amount).toFixed(2)}</td>
                   <td>${reimburseTypes[j.type_id]}</td>
                   <td style="width: 800px;">${j.description}</td>
                   <td style="color: ${statusColors[j.status_id][0]};">${statusColors[j.status_id][1]}</td>`;
    tbl.appendChild(t);
    count++;
}
*/

function populateTable(j) {
    for(let i = 0; i < j.length; i++) {
        insertToTable(j[i]);
    }
    document.getElementById("info-reimb-count").innerHTML = count;
}

let l = 0;
function charCount(str) {
    l = str.length;
    descCount.innerHTML = 'Description (' + l + '/250)';
    confirmRequirements();
}

let cash = 0;
function validAmt(str) {
    if(!isNaN(str)) {
        cash = parseFloat(str).toFixed(2);
    } else {
        cash = 0;
    }
    confirmRequirements();
}

function confirmRequirements() {
    if(!disableButton && l > 0 && (cash > .99 && cash < 5000.01)) {
        btn.disabled = false;
    } else {
        btn.disabled = true;
    }
}

let rAmt = document.getElementById("reimb-amt");

//Prevent the use of the enter key
rAmt.onkeypress = function(e) {
    let key = e.charCode || e.keyCode || 0;
    if (key == 13) {
        e.preventDefault();
    }
}

let rType = document.getElementById("reimb-type");
let rDesc = document.getElementById("reimb-desc");
let rStatus = document.getElementById('reimb-submit-status');

function stringJSON() {
    return JSON.stringify({'id': -1, 'amount': `${rAmt.value}`, 'submitted': null, 'resolved': null,
                           'description':`${rDesc.value}`, 'author': localUserId, 'status_id': 0,
                           'type_id': parseInt(rType.value)});
}

function submitData() {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/Project1/submit/reimbursement', true);
    xhr.setRequestHeader("Content-Type", "application/json");
    disableButton = true;
    confirmRequirements();
    let data = stringJSON();

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('Sent successfully');
            //Clear remaining info
            rAmt.value = null;
            rDesc.value = '';

            //Reset the form
            l = 0;
            disableButton = false;
            confirmRequirements();

            /*
            console.log(data);
            //Update the local HTML
            insertToTable(JSON.parse(data));
            document.getElementById("info-reimb-count").innerHTML = count;
            */

            //Show the status message upon success.
            rStatus.style.display = 'block';
            setTimeout(function() {
                console.log('5 seconds should have gone by.');
                rStatus.style.display = 'none';
            }, 5000);

            //Clear out the table for refreshing of data.
            let t = $('#reimb-main-table').DataTable();
            t.clear().draw();
            count = 0;
            getUserReimbursements();
        }
    }

    xhr.send(data);
}

document.getElementById('reimb-allow').addEventListener('click', submitData);