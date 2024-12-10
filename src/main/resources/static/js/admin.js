document.addEventListener('DOMContentLoaded', init);

async function init() {
    const navLinks = document.querySelectorAll('nav a.menu');
    const contentSections = document.querySelectorAll('section');

    navLinks.forEach((link) => {
        link.addEventListener('click', async (e) => {
            e.preventDefault();

            const targetId = link.getAttribute('data-target');
            const targetSection = document.querySelector(targetId);

            contentSections.forEach((section) => {
                section.classList.add('hidden');
                section.classList.remove('visible');
            });

            targetSection.classList.remove('hidden');
            targetSection.classList.add('visible');

            navLinks.forEach((navLink) => {
                navLink.classList.remove('active');
            });

            link.classList.add('active');

            displayMessage('', '');

            // Выполнить соответствующие действия в зависимости от targetId
            try {
                switch(targetId) {
                    case "#dba":
                        await fetchDba();
                        break;
                    case "#users":
                        const usersData = await fetchUsers();
                        createUserTable(document.querySelector('.users_list'), usersData);
                        break;
                    default:
                        console.warn(`Нет обработчика для targetId: ${targetId}`);
                }
            } catch (error) {
                console.error(`Ошибка при обработке ${targetId}:`, error);
                displayMessage(`Ошибка при загрузке данных: ${error.message}`, 'error');
            }
        });
    });
}

/**
 * Асинхронная функция для получения данных DBA
 */
async function fetchDba() {
    try {
        displayMessage('Загрузка данных DBA...', 'info');
        const response = await fetch('/api/dba/get-data');
        if (!response.ok) {
            throw new Error(`Ошибка сети: ${response.status}`);
        }
        const data = await response.json();
        // Обработка данных DBA
        createDbaSection(document.querySelector('.dba_section'), data);
        displayMessage('Данные DBA загружены успешно', 'success');
    } catch (error) {
        console.error('Ошибка при получении данных DBA:', error);
        displayMessage(`Ошибка при получении данных DBA: ${error.message}`, 'error');
    }
}

/**
 * Асинхронная функция для получения данных пользователей
 * @returns {Promise<Array>} - Возвращает массив пользователей
 */
async function fetchUsers() {
    try {
        displayMessage('Загрузка данных пользователей...', 'info');
        const response = await fetch('/api/users/get-all');
        if (!response.ok) {
            throw new Error(`Ошибка сети: ${response.status}`);
        }
        const data = await response.json();
        displayMessage('Данные пользователей загружены успешно', 'success');
        return data;
    } catch (error) {
        console.error('Ошибка при получении данных пользователей:', error);
        displayMessage(`Ошибка при получении данных пользователей: ${error.message}`, 'error');
        throw error; // Перебросить ошибку для дальнейшей обработки
    }
}

/**
 * Функция для создания таблицы пользователей
 * @param {HTMLElement} container - Контейнер для таблицы
 * @param {Array} users - Массив пользователей
 */
function createUserTable(container, users) {
    if (!Array.isArray(users)) {
        console.error('Данные пользователей должны быть массивом');
        return;
    }

    // Очистить контейнер перед добавлением новой таблицы
    container.innerHTML = '';

    if (users.length === 0) {
        container.textContent = 'Пользователи не найдены.';
        return;
    }

    const table = document.createElement('table');
    table.border = '1';
    table.style.width = '100%';
    table.style.borderCollapse = 'collapse';

    // Создание заголовков таблицы
    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');

    Object.keys(users[0]).forEach((key) => {
        const th = document.createElement('th');
        th.textContent = capitalizeFirstLetter(key);
        th.style.padding = '8px';
        th.style.backgroundColor = '#f2f2f2';
        headerRow.appendChild(th);
    });

    thead.appendChild(headerRow);
    table.appendChild(thead);

    // Создание тела таблицы
    const tbody = document.createElement('tbody');

    users.forEach((user) => {
        const row = document.createElement('tr');
        Object.values(user).forEach((value) => {
            const td = document.createElement('td');
            td.textContent = value;
            td.style.padding = '8px';
            row.appendChild(td);
        });
        tbody.appendChild(row);
    });

    table.appendChild(tbody);
    container.appendChild(table);
}

/**
 * Функция для создания секции DBA
 * @param {HTMLElement} container - Контейнер для секции
 * @param {Object} data - Данные DBA
 */
function createDbaSection(container, data) {
    // Реализуйте логику отображения данных DBA
    // Например:
    container.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
}

/**
 * Функция для отображения сообщений пользователю
 * @param {string} message - Сообщение для отображения
 * @param {string} type - Тип сообщения ('success', 'error', 'info')
 */
function displayMessage(message, type) {
    const messageDiv = document.getElementById('message');
    messageDiv.textContent = message;
    switch(type) {
        case 'success':
            messageDiv.style.color = 'green';
            break;
        case 'error':
            messageDiv.style.color = 'red';
            break;
        case 'info':
            messageDiv.style.color = 'blue';
            break;
        default:
            messageDiv.style.color = 'black';
    }
}

/**
 * Функция для капитализации первой буквы строки
 * @param {string} str - Входная строка
 * @returns {string} - Строка с капитализированной первой буквой
 */
function capitalizeFirstLetter(str) {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1);
}
