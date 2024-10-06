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

    const existingForm = container.querySelector('.course-form');
        if (existingForm) {
            existingForm.remove();
        }

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
    form.appendChild(submitButton);

    form.addEventListener('submit', (e) => {
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

    container.appendChild(form);
}