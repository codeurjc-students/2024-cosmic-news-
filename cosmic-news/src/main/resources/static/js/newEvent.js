document.addEventListener('DOMContentLoaded', () => {
    const iconSelect = document.getElementById('icon');
    const iconPreview = document.getElementById('iconPreview');

    iconSelect.addEventListener('change', (event) => {
        const selectedIcon = event.target.value;
        iconPreview.innerHTML = `<i class="${selectedIcon}"></i>`;
    });
});