document.addEventListener('DOMContentLoaded', function() {
    var notificationElement = document.getElementById('notification');
    if (notificationElement) {
        notificationElement.style.display = 'block';
        setTimeout(function() {
            notificationElement.style.display = 'none';
        }, 10000);
    }
});

function closeNotification() {
    var notificationElement = document.getElementById('notification');
    if (notificationElement) {
        notificationElement.style.display = 'none';
    }
}