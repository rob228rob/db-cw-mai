// Глобальные переменные
let userId = null;
let isLawyer = false;
let lawyerId = null;
let currentQuestionId = null;

async function loadUserInfo() {
    try {
        const response = await fetch('/api/users/get/current');
        if (response.ok) {
            const user = await response.json();
            userId = user.id;  // Сохраняем userId в глобальной переменной
            const userInfoContainer = document.getElementById('user-info');
            const questionsContainer = document.getElementById('questions-container');
            const newQuestionButton = document.getElementById('new-question-button');
            const questionsLink = document.getElementById('questions-link');

            let userInfoHTML = `
                    <h2>${sanitizeHTML(user.first_name)} ${sanitizeHTML(user.last_name)}</h2>
                    <p>Email: ${sanitizeHTML(user.email)}</p>
                `;

            // Проверка, является ли пользователь юристом
            const isLawyerResponse = await fetch(`/api/users/is-lawyer`);
            if (isLawyerResponse.ok) {
                isLawyer = await isLawyerResponse.json();  // Сохраняем статус в глобальной переменной
                if (isLawyer) {
                    // Загружаем информацию о юристе и его ответы
                    const lawyerInfoResponse = await fetch(`/api/lawyers/get?user_id=${userId}`);
                    if (lawyerInfoResponse.ok) {
                        const lawyerInfo = await lawyerInfoResponse.json();
                        lawyerId = lawyerInfo.lawyer_id;
                        userInfoHTML += `
                                <h3>Профиль юриста</h3>
                                <p>Стаж: ${sanitizeHTML(lawyerInfo.years_experience)} лет</p>
                                <p>Специализация: ${sanitizeHTML(lawyerInfo.specialization_name)}</p>
                                <p>Номер лицензии: ${sanitizeHTML(lawyerInfo.licence_number)}</p>
                            `;
                        await loadAnswers(lawyerId);

                        // Обновляем ссылку на "Вопросы", добавляя lawyer_id в URL
                        questionsLink.href = `/questions?lawyer_id=${encodeURIComponent(lawyerId)}`;
                    }
                } else {
                    // Пользователь, не являющийся юристом: отображаем кнопку и его вопросы
                    newQuestionButton.style.display = 'block';
                    await loadQuestions(userId);
                }
            }
            userInfoContainer.innerHTML = userInfoHTML;
        } else {
            console.error('Ошибка при загрузке информации о пользователе:', response.status);
        }
    } catch (error) {
        console.error('Ошибка при загрузке информации о пользователе:', error);
    }
}

async function loadQuestions(userId) {
    try {
        const response = await fetch(`/api/questions/get-all-by?user_id=${userId}`);
        const questionsContainer = document.getElementById('questions-container');
        if (response.ok) {
            const questions = await response.json();
            questionsContainer.innerHTML = `
                <h3 class="table-title">Список ваших вопросов</h3>
                <table class="question-table">
                    <tr>
                        <th>Тема</th>
                        <th>Вопрос</th>
                        <th>Действия</th>
                    </tr>
                    ${questions.map(question => `
                        <tr id="question-${question.id}">
                            <td>${sanitizeHTML(question.title)}</td>
                            <td>${sanitizeHTML(question.text)}</td>
                            <td>
                                <button class="button view-answers-button" onclick="toggleAnswers('${question.id}')">Просмотреть ответы</button>
                                <button class="button delete-question-button" onclick="deleteQuestion('${question.id}')">Удалить</button>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <div id="answers-${question.id}" class="answers-section" style="display: none;"></div>
                            </td>
                        </tr>
                    `).join('')}
                </table>
            `;
        }
    } catch (error) {
        console.error('Ошибка при загрузке вопросов:', error);
    }
}


async function toggleAnswers(questionId) {
    const answersSection = document.getElementById(`answers-${questionId}`);
    if (!answersSection) return;

    if (answersSection.style.display === "none" || answersSection.style.display === "") {
        try {
            const response = await fetch(`/api/answers/get-all/${questionId}`);
            if (response.ok) {
                const answers = await response.json();
                await renderAnswers(questionId, answers);
                answersSection.style.display = "block";
            } else {
                answersSection.innerHTML = '<p class="empty-message">Не удалось загрузить ответы.</p>';
                answersSection.style.display = "block";
            }
        } catch (error) {
            answersSection.innerHTML = '<p class="empty-message">Произошла ошибка при загрузке ответов.</p>';
            answersSection.style.display = "block";
        }
    } else {
        answersSection.style.display = "none";
    }
}
async function deleteQuestion(questionId) {
    if (!confirm("Вы уверены, что хотите удалить этот вопрос вместе с его ответами?")) return;

    try {
        const deleteQuestionResponse = await fetch(`/api/questions/delete/${questionId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (deleteQuestionResponse.ok || deleteQuestionResponse.status === 204) {

            await loadQuestions(userId);
        } else {
            const errorData = await deleteQuestionResponse.json();
            alert("Ошибка при удалении вопроса: " + (errorData.message || "Неизвестная ошибка."));
        }
    } catch (error) {
        console.error('Ошибка при удалении вопроса или ответов:', error);
        alert("Ошибка при удалении вопроса или ответов.");
    }
}
// Обновленная функция renderAnswers
async function renderAnswers(questionId, answers) {
    const answersSection = document.getElementById(`answers-${questionId}`);
    answersSection.innerHTML = ''; // Очищаем секцию перед рендерингом

    if (answers.length > 0) {
        answers.forEach(answer => {
            const answerDiv = document.createElement('div');
            answerDiv.className = 'answer';
            answerDiv.setAttribute('data-answer-id', answer.id); // Добавляем уникальный идентификатор

            // Если этот ответ подтверждён, добавляем класс для подсветки
            if (answer.confirmed) {
                answerDiv.classList.add('correct-answer');
                const confirmedLabel = document.createElement('span');
                confirmedLabel.className = 'confirmed-label';
                confirmedLabel.textContent = 'Подтвержденный ответ';
                answerDiv.appendChild(confirmedLabel);

                if (!answer.rated) {
                    const rateButton = document.createElement('button');
                    rateButton.className = 'button rate-lawyer-button';
                    rateButton.textContent = 'Оценить юриста';

                    // Добавляем data-атрибуты для хранения необходимых ID
                    rateButton.dataset.lawyerId = answer.lawyer_id;
                    rateButton.dataset.questionId = questionId;
                    rateButton.dataset.answerId = answer.id;

                    answerDiv.appendChild(rateButton);
                } else {
                    // Создаём блок для отображения рейтинга
                    const ratingDiv = document.createElement('div');
                    ratingDiv.className = 'lawyer-rating'; // Добавьте соответствующие стили в CSS

                    // Проверяем наличие рейтинга и комментария
                    const ratingValue = answer.lawyer_rating ? sanitizeHTML(answer.lawyer_rating) : 'Не оценено';
                    const ratingComment = answer.rating_comment ? sanitizeHTML(answer.rating_comment) : ' ';

                    ratingDiv.innerHTML = `
        <p><strong>Рейтинг:</strong> ${ratingValue} / 5</p>
        <p><strong>Комментарий:</strong> ${ratingComment}</p>
    `;

                    answerDiv.appendChild(ratingDiv);
                }

            }

            // Формируем HTML для ответа
            answerDiv.innerHTML += `
                <div class="lawyer-info">Юрист: ${sanitizeHTML(answer.lawyer_data.first_name)} ${sanitizeHTML(answer.lawyer_data.last_name)}</div>
                <div class="answer-text">${sanitizeHTML(answer.answer)}</div>
                <div class="creation-date">Дата создания: ${sanitizeHTML(answer.creation_date)}</div>
            `;

            answersSection.appendChild(answerDiv);
        });
    } else {
        answersSection.innerHTML = '<p class="empty-message">Нет ответов на этот вопрос.</p>';
    }
}

// Обновляем функцию openRatingModal для приема answerId
function openRatingModal(lawyerId, questionId, answerId) {
    currentLawyerId = lawyerId;
    currentQuestionId = questionId;
    currentAnswerId = answerId; // Добавляем текущий ID ответа
    document.getElementById("ratingModal").style.display = 'flex';
}

// Функция для закрытия модального окна оценки
function closeRatingModal() {
    document.getElementById("ratingModal").style.display = 'none';
    document.getElementById("ratingValue").value = '';
    document.getElementById("ratingComment").value = '';
}

async function submitRating() {
    const ratingValue = document.getElementById("ratingValue").value;
    const ratingComment = document.getElementById("ratingComment").value.trim();

    if (!ratingValue) {
        alert("Пожалуйста, выберите оценку.");
        return;
    }

    if (ratingComment.length > 256) {
        alert("Комментарий не может превышать 256 символов.");
        return;
    }

    const requestBody = {
        answer_id: currentAnswerId,       // Используем currentAnswerId
        question_id: currentQuestionId,
        rating: parseInt(ratingValue, 10),
        comment: ratingComment
    };

    try {
        const response = await fetch(`/api/rating/add`, { // Изменено на правильный эндпоинт
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            alert("Рейтинг успешно отправлен!");
            closeRatingModal();

            // Обновляем список ответов после отправки рейтинга
            await loadAnswersForQuestion(currentQuestionId);
        } else {
            const errorData = await response.json();
            alert(`Ошибка при отправке оценки: ${errorData.message || 'Неизвестная ошибка.'}`);
        }
    } catch (error) {
        console.error('Ошибка при отправке оценки:', error);
        alert("Произошла ошибка при отправке оценки. Пожалуйста, попробуйте позже.");
    }
}


async function loadLawyerRating(lawyerId, answerDiv) {
    try {
        const response = await fetch(`/api/lawyer/${lawyerId}`);
        if (response.ok) {
            const ratingInfo = await response.json();
            const ratingDiv = document.createElement('div');
            ratingDiv.className = 'lawyer-rating';
            ratingDiv.innerHTML = `<p>Рейтинг: ${sanitizeHTML(ratingInfo.avg_rating)} / 5</p>`;
            answerDiv.appendChild(ratingDiv);
        }
    } catch (error) {
        console.error(`Ошибка при загрузке рейтинга юриста ${lawyerId}:`, error);
    }
}

async function confirmAnswer(questionId, userId, lawyerId, answerId) {
    const requestBody = {
        question_id: questionId,
        answer_id: answerId
    };

    try {
        const response = await fetch(`/api/confirmed-answers/confirm`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            alert("Ответ успешно подтвержден как полезный!");

            // Перезагружаем список ответов для данного вопроса
            await toggleAnswers(questionId); // Эта функция уже загружает и рендерит ответы
        } else {
            const errorData = await response.json();
            alert("Ошибка при подтверждении ответа: " + (errorData.message || "Неизвестная ошибка."));
        }
    } catch (error) {
        console.error('Ошибка при подтверждении ответа:', error);
        alert("Ошибка при подтверждении ответа.");
    }
}


async function fetchLawyersAndAnswers(questionId) {
    try {
        const response = await fetch(`/api/answers/get-lawyers/${questionId}`);
        if (response.ok) {
            const lawyers = await response.json();
            document.getElementById("lawyerSelect").innerHTML = lawyers.map(lawyer => `
                    <option value="${lawyer.lawyer_id}">${sanitizeHTML(lawyer.first_name)} ${sanitizeHTML(lawyer.last_name)}</option>
                `).join('');
        }
    } catch (error) {
        console.error('Ошибка при загрузке юристов:', error);
    }
}

async function loadAnswers(lawyerId) {
    try {
        const response = await fetch(`/api/answers/get-all-by?lawyer_id=${lawyerId}`);
        const questionsContainer = document.getElementById('questions-container');
        if (response.ok) {
            const answers = await response.json();
            if (answers.length > 0) {
                let answersHTML = `
                        <h3>Список ответов</h3>
                        <div id="answers-container">
                        ${answers.map(answer => `
                            <div class="answer">
                                <div class="lawyer-info"><strong>Юрист ID:</strong> ${sanitizeHTML(answer.lawyer_id)}</div>
                                <div class="answer-text"><strong>Ответ:</strong> ${sanitizeHTML(answer.answer)}</div>
                                <div class="creation-date"><strong>Дата:</strong> ${answer.creation_date ? sanitizeHTML(answer.creation_date) : 'Не указана'}</div>
                            </div>`).join('')}
                        </div>`;
                questionsContainer.innerHTML = answersHTML;
            } else {
                questionsContainer.innerHTML = `<div class="empty-message">Список ответов пуст</div>`;
            }
        } else {
            questionsContainer.innerHTML = `<div class="empty-message">Ошибка при загрузке ответов</div>`;
        }
    } catch (error) {
        console.error('Ошибка при загрузке ответов:', error);
        const questionsContainer = document.getElementById('questions-container');
        questionsContainer.innerHTML = `<div class="empty-message">Произошла ошибка при загрузке ответов</div>`;
    }
}

// Функция для открытия модального окна создания вопроса
function openModal() {
    document.getElementById("questionModal").style.display = "flex";
}

// Функция для закрытия модального окна создания вопроса
function closeModal() {
    document.getElementById("questionModal").style.display = "none";
    document.getElementById("questionTitle").value = "";
    document.getElementById("questionText").value = "";
}

// Функция для отправки нового вопроса
async function submitQuestion() {
    const title = document.getElementById("questionTitle").value.trim();
    const text = document.getElementById("questionText").value.trim();

    if (!title || !text) {
        alert("Пожалуйста, заполните все поля.");
        return;
    }

    const requestBody = {
        user_id: userId,  // Используем глобальный userId
        question_data: {
            title: title,
            text: text
        }
    };

    try {
        const response = await fetch('/api/questions/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            alert("Вопрос успешно создан!");
            closeModal();
            await loadQuestions(userId); // Обновить список вопросов
        } else {
            const errorData = await response.json();
            alert("Ошибка при создании вопроса: " + (errorData.message || "Неизвестная ошибка."));
        }
    } catch (error) {
        console.error('Ошибка при отправке вопроса:', error);
        alert("Ошибка при создании вопроса.");
    }
}

// Функция для открытия модального окна ответа
function openAnswerModal(questionId) {
    currentQuestionId = questionId;
    document.getElementById("answerModal").style.display = "flex";
}

// Функция для закрытия модального окна ответа
function closeAnswerModal() {
    document.getElementById("answerModal").style.display = "none";
    document.getElementById("answerText").value = ""; // Очистка поля ввода ответа
}

// Функция для загрузки ответов для конкретного вопроса (для юристов)
async function loadAnswersForQuestion(questionId) {
    const answersSection = document.getElementById(`answers-${questionId}`);
    if (!answersSection) return;

    try {
        const response = await fetch(`/api/answers/get-all/${questionId}`);
        if (response.ok) {
            const answers = await response.json();
            await renderAnswers(questionId, answers);
            answersSection.style.display = "block";
        } else {
            answersSection.innerHTML = '<p class="empty-message">Не удалось загрузить ответы.</p>';
            answersSection.style.display = "block";
        }
    } catch (error) {
        answersSection.innerHTML = '<p class="empty-message">Произошла ошибка при загрузке ответов.</p>';
        answersSection.style.display = "block";
        console.error(`Ошибка при загрузке ответов для вопроса ${questionId}:`, error);
    }
}




// Функция для экранирования HTML, чтобы предотвратить XSS
function sanitizeHTML(str) {
    const temp = document.createElement('div');
    temp.textContent = str;
    return temp.innerHTML;
}
document.addEventListener('DOMContentLoaded', loadUserInfo);
document.addEventListener('DOMContentLoaded', () => {

    const ratingModal = document.getElementById('ratingModal');
    const closeRatingModalBtn = document.getElementById('close-rating-modal');
    const submitRatingBtn = document.getElementById('submit-rating');

    let currentLawyerId = null;
    let currentQuestionId = null;
    let currentAnswerId = null;

    function openRatingModal(lawyerId, questionId, answerId) {
        currentLawyerId = lawyerId;
        currentQuestionId = questionId;
        currentAnswerId = answerId;
        ratingModal.style.display = 'flex';
    }

    // Функция для закрытия модального окна
    function closeRatingModal() {
        ratingModal.style.display = 'none';
        // Очистка полей ввода
        document.getElementById('ratingValue').value = '';
        document.getElementById('ratingComment').value = '';
        // Очистка текущих ID
        currentLawyerId = null;
        currentQuestionId = null;
        currentAnswerId = null;
    }

    // Добавляем обработчик кликов на кнопки "Оценить юриста" с помощью делегирования событий
    document.addEventListener('click', function(event) {
        if (event.target && event.target.classList.contains('rate-lawyer-button')) {
            const button = event.target;
            const lawyerId = button.dataset.lawyerId;
            const questionId = button.dataset.questionId;
            const answerId = button.dataset.answerId;
            openRatingModal(lawyerId, questionId, answerId);
        }
    });

    // Обработчик закрытия модального окна оценки юриста
    if (closeRatingModalBtn) {
        closeRatingModalBtn.addEventListener('click', closeRatingModal);
    }

    // Обработчик отправки рейтинга
    if (submitRatingBtn) {
        submitRatingBtn.addEventListener('click', async () => {
            const ratingValue = document.getElementById("ratingValue").value;
            const ratingComment = document.getElementById("ratingComment").value.trim();

            // Валидация данных
            if (!ratingValue) {
                alert("Пожалуйста, выберите оценку.");
                return;
            }

            if (ratingComment.length > 256) {
                alert("Комментарий не может превышать 256 символов.");
                return;
            }

            // Создание объекта запроса
            const ratingRequest = {
                lawyer_id: currentLawyerId,
                question_id: currentQuestionId,
                rating: parseInt(ratingValue, 10),
                comment: ratingComment
            };

            try {
                const response = await fetch('/api/rating/add', { // Изменено на правильный эндпоинт
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(ratingRequest)
                });

                if (response.ok) {
                    alert("Рейтинг успешно отправлен!");
                    closeRatingModal();

                    // Обновляем список ответов после отправки рейтинга
                    await loadAnswersForQuestion(currentQuestionId);
                } else {
                    const errorData = await response.json();
                    alert(`Ошибка при отправке оценки: ${errorData.message || 'Неизвестная ошибка.'}`);
                }
            } catch (error) {
                console.error('Ошибка при отправке оценки:', error);
                alert("Произошла ошибка при отправке оценки. Пожалуйста, попробуйте позже.");
            }
        });
    }

    // Закрытие модального окна при клике вне его содержимого
    window.addEventListener('click', (event) => {
        if (event.target === ratingModal) {
            closeRatingModal();
        }
    });

    // Закрытие модального окна по клавише Esc
    document.addEventListener('keydown', (event) => {
        if (event.key === 'Escape' && ratingModal.style.display === 'flex') {
            closeRatingModal();
        }
    });

    // ... остальной существующий код ...
});
