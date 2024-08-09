document.addEventListener('DOMContentLoaded', (event) => {
    const isEdit = document.body.getAttribute('isEdit') === 'true';
    let numQuestions = 0;

    function createQuestionHTML(index) {
        return `
            <div class="question-container">
                <h3>Pregunta ${index +1}</h3>
                <div class="form-group">
                    <label for="question${index}">Pregunta: </label>
                    <input type="text" class="form-control" name="questions[${index}][question]" placeholder="Introduzca una pregunta">
                </div>
                <div class="row">
                    <div class="form-group col-md-6 col-sm-12">
                        <label for="option1_${index}">Opción 1: </label>
                        <input type="text" class="form-control" name="questions[${index}][option1]" id="option1_${index}">
                    </div>
                    <div class="form-group col-md-6 col-sm-12">
                        <label for="option2_${index}">Opción 2: </label>
                        <input type="text" class="form-control" name="questions[${index}][option2]" id="option2_${index}">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-6 col-sm-12">
                        <label for="option3_${index}">Opción 3: </label>
                        <input type="text" class="form-control" name="questions[${index}][option3]" id="option3_${index}">
                    </div>
                    <div class="form-group col-md-6 col-sm-12">
                        <label for="option4_${index}">Opción 4: </label>
                        <input type="text" class="form-control" name="questions[${index}][option4]" id="option4_${index}">
                    </div>
                </div>
                <div class="row">
                    <div class="form-group>
                        <label for="answer_${index}">Opción correcta: </label>
                        <select class="form-control" name="questions[${index}][answer]" id="answer_${index}">
                            <option value="option1">Opción 1</option>
                            <option value="option2">Opción 2</option>
                            <option value="option3">Opción 3</option>
                            <option value="option4">Opción 4</option>
                        </select>
                    </div>
                </div>
            </div>
        `;
    }

    function updateQuestionCounter() {
        const counterInput = document.getElementById('numQuestions');
        counterInput.value = numQuestions;
    }

    document.getElementById('add-question-btn').addEventListener('click', () => {
        const questionsContainer = document.getElementById('questions-container');
        questionsContainer.insertAdjacentHTML('beforeend', createQuestionHTML(numQuestions));
        numQuestions++;
        updateQuestionCounter();
    });

    updateQuestionCounter();

    if(!isEdit){
        document.getElementById('add-question-btn').click();
    }
});