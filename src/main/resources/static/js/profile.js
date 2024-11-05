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
                renderAnswers(questionId, answers);
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

            // Если этот ответ подтверждён, добавляем метку "Верный ответ"
            if (answer.confirmed) {
                const confirmedLabel = document.createElement('span');
                confirmedLabel.className = 'confirmed-label';
                confirmedLabel.textContent = 'Подтвержденный ответ';
                answerDiv.appendChild(confirmedLabel);
            }

            // Если нет подтверждённого ответа, добавляем кнопку подтверждения
            if (!hasConfirmed) {
                const confirmButton = document.createElement('button');
                confirmButton.className = 'button confirm-answer-button';
                confirmButton.textContent = 'Этот ответ помог';
                confirmButton.onclick = () => confirmAnswer(questionId, userId, answer.lawyer_id, answer.id);
                answerDiv.appendChild(confirmButton);
            }

            answersSection.appendChild(answerDiv);
        });
    } else {
        answersSection.innerHTML = '<p class="empty-message">Нет ответов на этот вопрос.</p>';
    }
}


// Обновлённая функция для подтверждения ответа
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

async function submitRating(rating) {
    const selectedLawyerId = document.getElementById("lawyerSelect").value;
    const requestBody = {user_id: userId, lawyer_id: selectedLawyerId, question_id: currentQuestionId, rating};

    try {
        const response = await fetch(`/api/ratings/submit`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(requestBody)
        });

        if (response.ok) {
            alert("Оценка успешно отправлена!");
            closeRatingModal();
        } else {
            alert("Ошибка при отправке оценки.");
        }
    } catch (error) {
        console.error('Ошибка при отправке оценки:', error);
    }
}

function openRatingModal(questionId) {
    currentQuestionId = questionId;
    fetchLawyersAndAnswers(questionId);
    document.getElementById("ratingModal").style.display = "flex";
}

function closeRatingModal() {
    document.getElementById("ratingModal").style.display = "none";
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

// Функция для отправки ответа
async function submitAnswer() {
    const answerText = document.getElementById("answerText").value.trim();
    if (!answerText) {
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
            alert("Ответ успешно отправлен!");
            closeAnswerModal();
            // Обновляем ответы для данного вопроса
            await loadAnswersForQuestion(currentQuestionId);
        } else {
            const errorData = await response.json();
            alert("Ошибка при отправке ответа: " + (errorData.message || "Неизвестная ошибка."));
        }
    } catch (error) {
        console.error('Ошибка при отправке ответа:', error);
        alert("Ошибка при отправке ответа.");
    }
}

// Функция для загрузки ответов для конкретного вопроса (для юристов)
async function loadAnswersForQuestion(questionId) {
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

// Функция для отображения/скрытия ответов (для обычных пользователей)
async function toggleAnswers(questionId) {
    const answersSection = document.getElementById(`answers-${questionId}`);
    if (!answersSection) return;

    if (answersSection.style.display === "none" || answersSection.style.display === "") {
        // Загружаем ответы, если они еще не загружены
        if (!answersSection.dataset.loaded) {
            try {
                const response = await fetch(`/api/answers/get-all/${questionId}`);
                if (response.ok) {
                    const answers = await response.json();
                    renderAnswers(questionId, answers);
                    answersSection.dataset.loaded = "true";
                } else {
                    console.error(`Ошибка при загрузке ответов для вопроса ${questionId}:`, response.status);
                    answersSection.innerHTML = '<p class="empty-message">Не удалось загрузить ответы.</p>';
                    answersSection.style.display = "block";
                }
            } catch (error) {
                console.error(`Ошибка при загрузке ответов для вопроса ${questionId}:`, error);
                answersSection.innerHTML = '<p class="empty-message">Произошла ошибка при загрузке ответов.</p>';
                answersSection.style.display = "block";
            }
        } else {
            answersSection.style.display = "block";
        }
    } else {
        answersSection.style.display = "none";
    }
}


// Функция для экранирования HTML, чтобы предотвратить XSS
function sanitizeHTML(str) {
    const temp = document.createElement('div');
    temp.textContent = str;
    return temp.innerHTML;
}

document.addEventListener('DOMContentLoaded', loadUserInfo);
