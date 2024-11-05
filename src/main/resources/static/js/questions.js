let currentQuestionId = null;
let lawyerId = null;

// Функция для извлечения параметров из URL
function getQueryParams() {
    const params = {};
    window.location.search.substring(1).split("&").forEach(pair => {
        const [key, value] = pair.split("=");
        if (key) {
            params[decodeURIComponent(key)] = decodeURIComponent(value || '');
        }
    });
    return params;
}

async function loadQuestions() {
    const params = getQueryParams();
    lawyerId = params['lawyer_id'];

    let apiUrl = '/api/questions/get-all';
    if (lawyerId) {
        apiUrl = `/api/questions/get-all?lawyer_id=${lawyerId}`;
    }

    try {
        const response = await fetch(apiUrl);
        if (response.ok) {
            const questions = await response.json();
            renderQuestions(questions);
        } else {
            console.error('Ошибка при загрузке вопросов:', response.status);
            document.getElementById('question-container').innerHTML = '<p class="empty-message">Не удалось загрузить вопросы.</p>';
        }
    } catch (error) {
        console.error('Ошибка при загрузке вопросов:', error);
        document.getElementById('question-container').innerHTML = '<p class="empty-message">Произошла ошибка при загрузке вопросов.</p>';
    }
}

function renderQuestions(questions) {
    const questionContainer = document.getElementById('question-container');
    questionContainer.innerHTML = ''; // Очищаем контейнер перед рендерингом
    if (questions.length > 0) {
        questions.forEach(question => {
            const questionCard = document.createElement('div');
            questionCard.className = 'question-card';
            questionCard.innerHTML = `
                    <h3>${sanitizeHTML(question.title)}</h3>
                    <p>${sanitizeHTML(question.text)}</p>
                    <p class="date"><strong>Дата создания:</strong> ${sanitizeHTML(question.creation_date)}</p>
                `;

            // Кнопка "Ответить" появляется только для вопросов без ответа
            if (!question.answered) {
                const answerButton = document.createElement('button');
                answerButton.textContent = 'Ответить';
                answerButton.className = 'answer-button';
                answerButton.addEventListener('click', () => openAnswerModal(question.id));
                questionCard.appendChild(answerButton);
            }

            // Добавляем секцию для ответов
            const answersSection = document.createElement('div');
            answersSection.className = 'answers-section';
            answersSection.id = `answers-${question.id}`;
            questionCard.appendChild(answersSection);

            questionContainer.appendChild(questionCard);

            // Загружаем ответы для этого вопроса
            loadAnswers(question.id);
        });
    } else {
        questionContainer.innerHTML = '<p class="empty-message">Вопросы не найдены.</p>';
    }
}

async function loadAnswers(questionId) {
    try {
        const response = await fetch(`/api/answers/get-all/${questionId}`);
        if (response.ok) {
            const answers = await response.json();
            renderAnswers(questionId, answers);
        } else {
            console.error(`Ошибка при загрузке ответов для вопроса ${questionId}:`, response.status);
        }
    } catch (error) {
        console.error(`Ошибка при загрузке ответов для вопроса ${questionId}:`, error);
    }
}

function renderAnswers(questionId, answers) {
    const answersSection = document.getElementById(`answers-${questionId}`);
    answersSection.innerHTML = ''; // Очищаем секцию перед рендерингом

    if (answers.length > 0) {
        // Проверяем, есть ли хотя бы один подтверждённый ответ
        const hasConfirmed = answers.some(answer => answer.confirmed === true);

        answers.forEach(answer => {
            const answerDiv = document.createElement('div');
            answerDiv.className = 'answer';
            answerDiv.setAttribute('data-answer-id', answer.id); // Добавляем уникальный идентификатор

            // Если этот ответ подтверждён, добавляем класс для подсветки
            if (hasConfirmed && answer.confirmed) {
                answerDiv.classList.add('correct-answer');
            }

            // Формируем HTML для ответа
            answerDiv.innerHTML = `
                <div class="lawyer-info">Юрист: ${sanitizeHTML(answer.lawyer_data.first_name)} ${sanitizeHTML(answer.lawyer_data.last_name)}</div>
                <div class="answer-text">${sanitizeHTML(answer.answer)}</div>
                <div class="creation-date">Дата создания: ${sanitizeHTML(answer.creation_date)}</div>
            `;

            // Если этот ответ подтверждён, добавляем метку "Данный ответ помог"
            if (answer.confirmed) {
                const confirmedLabel = document.createElement('span');
                confirmedLabel.className = 'confirmed-label';
                confirmedLabel.textContent = 'Данный ответ помог';
                answerDiv.appendChild(confirmedLabel);
            }

            answersSection.appendChild(answerDiv);
        });
    } else {
        answersSection.innerHTML = '<p class="empty-message">Нет ответов на этот вопрос.</p>';
    }
}


function openAnswerModal(questionId) {
    currentQuestionId = questionId;
    document.getElementById("answerModal").style.display = "flex";
}

function closeModal() {
    document.getElementById("answerModal").style.display = "none";
    document.getElementById("answerText").value = ""; // Очистка поля ввода ответа
}

async function submitAnswer() {
    const answerText = document.getElementById("answerText").value;
    if (!answerText.trim()) {
        alert("Ответ не может быть пустым.");
        return;
    }

    if (!lawyerId) {
        alert("Отсутствует lawyer_id. Пожалуйста, убедитесь, что вы вошли как юрист.");
        return;
    }

    const requestBody = {
        lawyer_id: lawyerId,
        question_id: currentQuestionId,
        answer_text: answerText
    };

    try {
        const response = await fetch(`/api/answers/create`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            closeModal();
            await loadAnswers(currentQuestionId); // Обновить список ответов после отправки ответа
        } else {
            alert("Ошибка при отправке ответа.");
        }
    } catch (error) {
        console.error('Ошибка при отправке ответа:', error);
        alert("Ошибка при отправке ответа.");
    }
}

// Функция для экранирования HTML, чтобы предотвратить XSS
function sanitizeHTML(str) {
    const temp = document.createElement('div');
    temp.textContent = str;
    return temp.innerHTML;
}

document.addEventListener('DOMContentLoaded', () => {
    loadQuestions();
});