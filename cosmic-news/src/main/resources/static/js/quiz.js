document.addEventListener('DOMContentLoaded', function () {
    const options = document.querySelectorAll('.option input[type="radio"]');
    
    options.forEach(option => {
        option.addEventListener('change', function () {
            const questionId = this.name.split('-')[1];
            const selectedOption = document.querySelector(`#question-${questionId} .option.selected`);
            if (selectedOption) {
                selectedOption.classList.remove('selected');
            }
            this.parentNode.classList.add('selected');
        });
    });
});