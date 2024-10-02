function init() {
    const navLinks = document.querySelectorAll('nav a');
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
        }
    });
    });
}
setTimeout(init, 0);
