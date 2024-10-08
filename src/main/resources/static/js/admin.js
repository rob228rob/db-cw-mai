function init() {
    const navLinks = document.querySelectorAll('nav a.menu');
    const contentSections = document.querySelectorAll('section');

    navLinks.forEach((link) => {  
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const targetId = link.getAttribute('data-target');
        const targetSection = document.querySelector(targetId);
        contentSections.forEach((section) => {
        section.classList.add('hidden');
        });
        targetSection.classList.remove('hidden');
        navLinks.forEach((link) => {  
            link.classList.remove('active');
        });
        link.classList.add('active');
        switch(targetId) {
            case "#courses":
                fetchCourses();
                break;
            case "#users":
                fetch('/api/v1/user/get-all').then(Response => Response.json()).then(data => createUserTable(document.querySelector('.users_list'), data));
                break;
        }
    });
});
}
setTimeout(init, 0);

function createUserTable(container, data) {
    container.innerHTML = '';
    const table = document.createElement('table');
    table.style = 'width: 100%';
    table.cellPadding = table.cellSpacing = 0;

    let tr = document.createElement('tr');
    tr.innerHTML = '<th>Id</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Role</th>';
    table.append(tr);

    for (const user of data) {
        tr = document.createElement('tr');
        tr.innerHTML = `<td>${user.id}</td><td>${user.firstName}</td><td>${user.lastName}</td><td>${user.email}</td><td>${user.roleName}</td>`;
        table.append(tr);
    }

    container.append(table);
}
function createCoursesTable(container, data) {
    container.innerHTML = '';
    const table = document.createElement('table');
    table.style = 'width: 100%';
    table.cellPadding = table.cellSpacing = 0;

    let tr = document.createElement('tr');
    tr.innerHTML = '<th>Name</th><th>Description</th><th>Details</th>';
    table.append(tr);

    for (const course of data) {
        tr = document.createElement('tr');
        tr.innerHTML = `<td>${course.name}</td><td>${course.description}</td><td><a href="#" data-id="${course.id}" class="button details">Details</a></td>`;
        table.append(tr);
    }

    const courseLinks = table.querySelectorAll('a.details');
    courseLinks.forEach((link) => {  
        link.addEventListener('click', (e) => {
        
            e.preventDefault();
            fetch(`api/v1/courses/get/${link.getAttribute('data-id')}`).then(Response => Response.json()).then(data => createForm(container, { ...data, id: link.getAttribute('data-id')}));
            
        });
    });

    const button = document.createElement('button');
    button.textContent = 'Add New Course';
    button.className = 'button';
    
    button.addEventListener('click', () => {
        createForm(container);
    });

    container.append(table);
    container.appendChild(button);
}


function fetchCourses() {
    fetch('/api/v1/courses/get-all-id').then(Response => Response.json()).then(data => createCoursesTable(document.querySelector('.courses_list'), data));
}

function createForm(container, course = { name: '', description: '', id: undefined}) {

    container.querySelector('.course-form')?.remove();
    container.querySelector('.center')?.remove();
    container.querySelector('.article-container')?.remove();

    const form = document.createElement('form');
    form.className = 'course-form';

    const courseNameInput = document.createElement('input');
    courseNameInput.type = 'text';
    courseNameInput.placeholder = 'Course Name';
    courseNameInput.name = 'courseName';
    courseNameInput.value = course.name;

    const courseDescriptionInput = document.createElement('textarea');
    courseDescriptionInput.placeholder = 'Course Description';
    courseDescriptionInput.name = 'courseDescription';
    courseDescriptionInput.value = course.description;

    const submitButton = document.createElement('button');
    submitButton.type = 'submit';
    submitButton.textContent = course.id ? 'Update Course' :'Create Course';

    form.appendChild(courseNameInput);
    form.appendChild(courseDescriptionInput);
    const articleContainer = document.createElement('div');
    articleContainer.className = 'article-container'
    container.appendChild(form);
    container.appendChild(articleContainer);
    
    if (course.id) {
        createArticlesTable(articleContainer, course.id);
    }
    const center = document.createElement('div');
    center.className = 'center';
    center.appendChild(submitButton);   
    container.appendChild(center);

    submitButton.addEventListener('click', (e) => {
        e.preventDefault();
        const courseName = courseNameInput.value;
        const courseDescription = courseDescriptionInput.value;

        if (courseName === '' || courseDescription === '') {
            alert('Please fill in all fields');
            return;
        }

        const newCourse = {
            name: courseName,
            description: courseDescription
        };

        fetch(`/api/v1/courses/${course.id ? 'update/' + course.id : 'create'}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newCourse)
        })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            form.remove();
            fetchCourses();
        })
        .catch(error => console.error('Error:', error));

    });
}


function createArticlesTable(container, courseId) {

    container.querySelector('.article-form')?.remove();
    container.innerHTML = "";
    
    fetch(`/api/v1/articles/get-all/${courseId}`)
        .then(response => response.json())
        .then(data => {
            const articlesTable = document.createElement('table');
            articlesTable.style = 'width: 100%';
            articlesTable.cellPadding = articlesTable.cellSpacing = 0;

            let tr = document.createElement('tr');
            tr.innerHTML = '<th>Author</th><th>Title</th><th>Content</th><th>Delete</th>';
            articlesTable.append(tr);

            data.forEach(article => {
                tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${article.author}</td>
                    <td>${article.title}</td>
                    <td>${article.content}</td>
                    <td><button class="delete-article" data-id="${article.id}">Delete</button></td>
                `;
                articlesTable.append(tr);
            });

            const deleteButtons = articlesTable.querySelectorAll('.delete-article');
            deleteButtons.forEach(button => {
                button.addEventListener('click', e => {
                    e.preventDefault();
                    const articleId = button.getAttribute('data-id');
                    fetch(`/api/v1/articles/delete/${articleId}`, { method: 'DELETE' })
                        .then(() => {
                            container.querySelector('.article-form')?.remove();
                            createArticlesTable(container, courseId);
                        })
                        .catch(error => console.error('Error:', error));
                });
            });

            const addArticleButton = document.createElement('button');
            addArticleButton.textContent = 'Add Article';
            addArticleButton.className = 'button';

            addArticleButton.addEventListener('click', () => {
                const existingForm = container.querySelector('.article-form');
                if (existingForm) {
                    existingForm.remove();
                }

                const form = document.createElement('form');
                form.className = 'article-form';

                const authorInput = document.createElement('input');
                authorInput.type = 'text';
                authorInput.placeholder = 'Author';
                authorInput.name = 'author';

                const titleInput = document.createElement('input');
                titleInput.type = 'text';
                titleInput.placeholder = 'Title';
                titleInput.name = 'title';

                const contentInput = document.createElement('textarea');
                contentInput.placeholder = 'Content';
                contentInput.name = 'content';

                const submitButton = document.createElement('button');
                submitButton.type = 'submit';
                submitButton.textContent = 'Submit';

                form.appendChild(authorInput);
                form.appendChild(titleInput);
                form.appendChild(contentInput);
                form.appendChild(submitButton);

                form.addEventListener('submit', e => {
                    e.preventDefault();
                    const author = authorInput.value;
                    const title = titleInput.value;
                    const content = contentInput.value;
                    const course_id = courseId;

                    if (author === '' || title === '' || content === '') {
                        alert('Please fill in all fields');
                        return;
                    }

                    const newArticle = {
                        author,
                        title,
                        content,
                        course_id
                    };

                    fetch('/api/v1/articles/create', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(newArticle)
                    })
                    .then(response => response.json())
                    .then(data => {
                        console.log(data);
                        form.remove();
                        createArticlesTable(container, courseId);
                    })
                    .catch(error => console.error('Error:', error));
                });

                container.appendChild(form);
            });

            if (articlesTable) {container.appendChild(articlesTable);}
            container.appendChild(addArticleButton);
        })
        .catch(error => console.error('Error:', error));
}