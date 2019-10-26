function reset() {
    localStorage.setItem('username', null);
}

function storeName() {
    localStorage.setItem('username', document.getElementById('u').value);
}