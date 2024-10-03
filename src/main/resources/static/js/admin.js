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
                console.log(1);
                document.querySelector(`${targetId}_list`).setHTMLUnsafe('Courses list');
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
    tr.innerHTML = '<th>Id</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Enabled</th><th>Token Expired</th><th>Creation Time</th><th>Modified Time</th>';
    table.append(tr);

    for (const user of data) {
        tr = document.createElement('tr');
        tr.innerHTML = `<td>${user.id}</td><td>${user.firstName}</td><td>${user.lastName}</td><td>${user.email}</td><td>${user.enabled}</td><td>${user.tokenExpired}</td><td>${user.creationTime}</td><td>${user.modifiedTime}</td>`;
        table.append(tr);
    }

    container.append(table);
}
