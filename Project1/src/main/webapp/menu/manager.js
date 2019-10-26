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
];

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

let reimbToAllow = [];
function removeFromAllow(i) {
    for(let j = 0; j < reimbToAllow.length; j++) {
        if (reimbToAllow[j] === i) {
            reimbToAllow.splice(j, 1);
        }
    }
}

let reimbToDeny = [];
function removeFromDeny(i) {
    for(let j = 0; j < reimbToDeny.length; j++) {
        if (reimbToDeny[j] === i) {
            reimbToDeny.splice(j, 1);
        }
    }
}

let sButton = document.getElementById('reimb-submit-change');
function updateSubmitButton() {
    let c = reimbToAllow.length + reimbToDeny.length;
    if (c > 0) {
        sButton.disabled = false;
    } else {
        sButton.disabled = true;
    }
}

//Set up the DataTables once the document has finished loading.
$(document).ready(function () {
    let mainT = $('#reimb-main-table').DataTable();

    //When clicking a row, change the reimbursement's status.
    $('#reimb-main-table tbody').on('click', 'tr', function() {
        let data = mainT.row(this).data();
        switch(data[8]) {
            case 'Pending':
                data[8] = 'Pending [Approve]';
                reimbToAllow.push(data[0]);
                break;
            case 'Pending [Approve]':
                data[8] = 'Pending [Deny]';
                reimbToDeny.push(data[0]);
                removeFromAllow(data[0]);
                break;
            case 'Pending [Deny]':
                data[8] = 'Pending';
                removeFromDeny(data[0]);
                break;
        }
        //Update the row after change.
        mainT.row(this).data(data).draw(false);
        updateSubmitButton();
    })
});

let localUsername = localStorage.getItem('username');
let localUserId = -1;
let tbl = document.getElementById('reimb-dyn-table');

function getUserInfo() {
    fetch("/Project1/info/user/" + localUsername)
        .then((response) => { return response.json() })
        .then(json => setUserValues(json));
}

function getAllReimbursements() {
    if (localUsername != null && localUsername != "null") {
        fetch("/Project1/info/reimbursement/all")
            .then((response) => { return response.json() })
            .then(json => populateTable(json));
    }
}

function setUserValues(j) {
    if (j != null) {
        localUserId = j.user_id;
        document.getElementById("info-username").innerHTML = j.username + '(ID: ' + localUserId + ')';
        document.getElementById("info-email").innerHTML = j.email;
        document.getElementById("info-fullname").innerHTML = j.firstname + ' ' + j.lastname;
    }

    getAllReimbursements();
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
        j.author,
        formatDateTime(j.resolved),
        j.resolver,
        reimburseTypes[j.type_id],
        j.description,
        statusColors[j.status_id][1]
    ]).draw().node();

    $(n).find('td:eq(7)').css('width', 800);
    $(n).find('td:eq(8)').css('color', statusColors[j.status_id][0]);
}

function populateTable(j) {
    for(let i = 0; i < j.length; i++) {
        insertToTable(j[i]);
    }
}

function stringJSON() {
    let finalArray = [
        reimbToAllow,
        reimbToDeny,
        localUserId
    ];
    console.log(JSON.stringify(finalArray));
    return JSON.stringify(finalArray);
}

let rStatus = document.getElementById('reimb-submit-status');

function submitChanges() {
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/Project1/submit/changes', true);
    xhr.setRequestHeader("Content-Type", "application/json");
    let data = stringJSON();

    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('Sent successfully');

            //Show the status message upon success.
            rStatus.style.display = 'block';
            setTimeout(function() {
                console.log('5 seconds should have gone by.');
                rStatus.style.display = 'none';
            }, 5000);

            //Clear out the table for refreshing of data.
            let t = $('#reimb-main-table').DataTable();
            t.clear().draw();
            getAllReimbursements();
        }
    }

    xhr.send(data);
}

sButton.addEventListener('click', submitChanges);