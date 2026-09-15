const API_BASE_URL = "http://localhost:8080";

const currentPage =
    window.location.pathname.split("/").pop() || "index.html";


// =========================================
// COMMON HELPERS
// =========================================

function getStudentId() {
    return Number(localStorage.getItem("studentId"));
}

function requireLogin() {
    const studentId = localStorage.getItem("studentId");

    if (!studentId) {
        alert("Please login first.");
        window.location.href = "index.html";
        return false;
    }

    return true;
}


// =========================================
// LOAD NAVBAR
// =========================================

async function loadNavbar() {
    const navbarContainer = document.getElementById("navbar");

    if (!navbarContainer) {
        return;
    }

    try {
        const response = await fetch("navbar.html");

        if (!response.ok) {
            throw new Error("Unable to load navbar");
        }

        navbarContainer.innerHTML = await response.text();

        setupLogoutButton();
        setupActiveNavigation();

    } catch (error) {
        console.error("Navbar loading error:", error);
    }
}

loadNavbar();


// =========================================
// LOGIN
// =========================================

const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document
            .getElementById("email")
            .value
            .trim();

        const password = document
            .getElementById("password")
            .value;

        const submitButton = loginForm.querySelector(
            'button[type="submit"]'
        );

        if (!email || !password) {
            alert("Please enter email and password.");
            return;
        }

        try {
            submitButton.disabled = true;
            submitButton.textContent = "Logging in...";

            const response = await fetch(
                `${API_BASE_URL}/login`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        email: email,
                        password: password
                    })
                }
            );

            const result = await response.json();

            if (!response.ok || !result.success) {
                throw new Error(
                    result.message || "Invalid email or password"
                );
            }

            localStorage.setItem(
                "studentId",
                result.studentId
            );

            localStorage.setItem(
                "studentName",
                result.studentName
            );

            localStorage.setItem(
                "studentEmail",
                result.email
            );

            alert(result.message || "Login successful!");

            window.location.href = "dashboard.html";

        } catch (error) {
            console.error("Login error:", error);

            alert(
                error.message ||
                "Cannot connect to backend. Please start Spring Boot."
            );

        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Login";
        }
    });
}


// =========================================
// REGISTER
// =========================================

const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const name = document
            .getElementById("name")
            .value
            .trim();

        const email = document
            .getElementById("email")
            .value
            .trim();

        const password = document
            .getElementById("password")
            .value;

        if (!name || !email || !password) {
            alert("Please fill in all fields.");
            return;
        }

        if (password.length < 6) {
            alert("Password must be at least 6 characters.");
            return;
        }

        const submitButton = registerForm.querySelector(
            'button[type="submit"]'
        );

        try {
            submitButton.disabled = true;
            submitButton.textContent = "Creating account...";

            const response = await fetch(
                `${API_BASE_URL}/students`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        name: name,
                        email: email,
                        password: password
                    })
                }
            );

            const result = await response.text();

            if (!response.ok) {
                throw new Error(result);
            }

            alert(result);

            window.location.href = "index.html";

        } catch (error) {
            console.error("Registration error:", error);

            alert(
                "Registration failed: " + error.message
            );

        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Create Account";
        }
    });
}


// =========================================
// ADD SKILL
// =========================================

const skillForm = document.getElementById("skillForm");

if (skillForm) {
    skillForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        if (!requireLogin()) {
            return;
        }

        const studentId = getStudentId();

        const skillName = document
            .getElementById("skillName")
            .value
            .trim();

        const skillLevel = document
            .getElementById("skillLevel")
            .value;

        if (!skillName || !skillLevel) {
            alert("Please enter a skill and select a level.");
            return;
        }

        const submitButton = skillForm.querySelector(
            'button[type="submit"]'
        );

        try {
            submitButton.disabled = true;
            submitButton.textContent = "Adding skill...";

            const response = await fetch(
                `${API_BASE_URL}/skills`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        studentId: studentId,
                        skillName: skillName,
                        skillLevel: skillLevel
                    })
                }
            );

            const result = await response.text();

            if (!response.ok) {
                throw new Error(result);
            }

            alert(result);

            skillForm.reset();

            loadSkills();

        } catch (error) {
            console.error("Skill error:", error);

            alert(
                "Unable to add skill: " + error.message
            );

        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Add Skill";
        }
    });
}


// =========================================
// APPLY FOR OPPORTUNITY
// =========================================

async function applyForOpportunity(
    opportunityId,
    companyName
) {
    if (!requireLogin()) {
        return;
    }

    const studentId = getStudentId();

    const confirmation = confirm(
        `Do you want to apply for ${companyName}?`
    );

    if (!confirmation) {
        return;
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/applications`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    studentId: studentId,
                    opportunityId: opportunityId
                })
            }
        );

        const result = await response.text();

        if (!response.ok) {
            throw new Error(result);
        }

        alert(result);

        if (currentPage === "applications.html") {
            loadApplications();
        }

    } catch (error) {
        console.error("Application error:", error);

        alert(
            "Application failed: " + error.message
        );
    }
}


// =========================================
// LOAD STUDENTS
// =========================================

async function loadStudents() {
    const studentList =
        document.getElementById("studentList");

    if (!studentList) {
        return;
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/students`
        );

        if (!response.ok) {
            throw new Error("Failed to load students");
        }

        const students = await response.json();

        studentList.innerHTML = "";

        if (
            !Array.isArray(students) ||
            students.length === 0
        ) {
            studentList.innerHTML = `
                <div class="empty-state">
                    <h3>No students found</h3>
                    <p>No registered students are available.</p>
                </div>
            `;

            return;
        }

        students.forEach(student => {
            const studentCard =
                document.createElement("div");

            studentCard.className = "student-card";

            const heading =
                document.createElement("h3");

            heading.textContent =
                student.name || "Unknown student";

            const email =
                document.createElement("p");

            email.textContent =
                `Email: ${student.email}`;

            studentCard.appendChild(heading);
            studentCard.appendChild(email);

            studentList.appendChild(studentCard);
        });

    } catch (error) {
        console.error(
            "Student loading error:",
            error
        );

        studentList.innerHTML = `
            <div class="error-state">
                Unable to load students.
            </div>
        `;
    }
}

loadStudents();


// =========================================
// LOAD STUDENT COUNT
// =========================================

async function loadStudentCount() {
    const studentCount =
        document.getElementById("studentCount");

    if (!studentCount) {
        return;
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/students/count`
        );

        if (!response.ok) {
            throw new Error(
                "Failed to load student count"
            );
        }

        const count = await response.json();

        studentCount.textContent = count;

    } catch (error) {
        console.error(
            "Student count error:",
            error
        );

        studentCount.textContent = "Error";
    }
}

loadStudentCount();


// =========================================
// LOAD SKILLS
// =========================================

async function loadSkills() {
    const skillsList =
        document.getElementById("skillsList");

    if (!skillsList) {
        return;
    }

    if (!requireLogin()) {
        return;
    }

    const studentId = getStudentId();

    try {
        const response = await fetch(
            `${API_BASE_URL}/skills`
        );

        if (!response.ok) {
            throw new Error("Failed to load skills");
        }

        const skills = await response.json();

        const studentSkills = skills.filter(skill =>
            Number(skill.student_id) === studentId
        );

        skillsList.innerHTML = "";

        if (studentSkills.length === 0) {
            skillsList.innerHTML = `
                <div class="empty-state">
                    <h3>No skills added yet</h3>
                    <p>Add your first skill above.</p>
                </div>
            `;

            return;
        }

        const list = document.createElement("ul");

        studentSkills.forEach(skill => {
            const listItem =
                document.createElement("li");

            listItem.textContent =
                `${skill.skill_name} - Level ${skill.skill_level}`;

            list.appendChild(listItem);
        });

        skillsList.appendChild(list);

    } catch (error) {
        console.error(
            "Skill loading error:",
            error
        );

        skillsList.innerHTML = `
            <div class="error-state">
                Unable to load skills.
            </div>
        `;
    }
}

loadSkills();


// =========================================
// LOAD OPPORTUNITIES
// =========================================

async function loadOpportunities() {
    const opportunitiesList =
        document.getElementById("opportunitiesList");

    if (!opportunitiesList) {
        return;
    }

    try {
        const response = await fetch(
            `${API_BASE_URL}/opportunities`
        );

        if (!response.ok) {
            throw new Error(
                "Failed to load opportunities"
            );
        }

        const opportunities = await response.json();

        opportunitiesList.innerHTML = "";

        if (
            !Array.isArray(opportunities) ||
            opportunities.length === 0
        ) {
            opportunitiesList.innerHTML = `
                <div class="empty-state">
                    <h3>No opportunities available</h3>
                    <p>Please check again later.</p>
                </div>
            `;

            return;
        }

        opportunities.forEach(opportunity => {
            const card =
                document.createElement("div");

            card.className = "opportunity-card";

            const heading =
                document.createElement("h3");

            heading.textContent =
                opportunity.company_name ||
                "Unknown company";

            const role =
                document.createElement("p");

            role.textContent =
                `Role: ${
                    opportunity.role || "Not specified"
                }`;

            const deadline =
                document.createElement("p");

            if (opportunity.deadline) {
                deadline.textContent =
                    `Deadline: ${
                        new Date(
                            opportunity.deadline
                        ).toLocaleDateString()
                    }`;
            } else {
                deadline.textContent =
                    "Deadline: Not specified";
            }

            const applyButton =
                document.createElement("button");

            applyButton.textContent = "Apply";
            applyButton.type = "button";

            applyButton.addEventListener(
                "click",
                function () {
                    applyForOpportunity(
                        opportunity.id,
                        opportunity.company_name
                    );
                }
            );

            card.appendChild(heading);
            card.appendChild(role);
            card.appendChild(deadline);
            card.appendChild(applyButton);

            opportunitiesList.appendChild(card);
        });

    } catch (error) {
        console.error(
            "Opportunity loading error:",
            error
        );

        opportunitiesList.innerHTML = `
            <div class="error-state">
                <h3>Unable to load opportunities</h3>
                <p>Please check that the backend is running.</p>
            </div>
        `;
    }
}

loadOpportunities();


// =========================================
// LOAD APPLICATIONS
// =========================================

async function loadApplications() {
    const applicationsList =
        document.getElementById("applicationsList");

    if (!applicationsList) {
        return;
    }

    if (!requireLogin()) {
        return;
    }

    const studentId = getStudentId();

    applicationsList.innerHTML = `
        <p class="loading-state">
            Loading applications...
        </p>
    `;

    try {
        const response = await fetch(
            `${API_BASE_URL}/applications/student/${studentId}`
        );

        if (!response.ok) {
            throw new Error(
                `Server error: ${response.status}`
            );
        }

        const applications = await response.json();

        applicationsList.innerHTML = "";

        if (
            !Array.isArray(applications) ||
            applications.length === 0
        ) {
            applicationsList.innerHTML = `
                <div class="empty-state">
                    <h3>No applications yet</h3>
                    <p>Apply for an opportunity to see it here.</p>
                </div>
            `;

            return;
        }

        applications.forEach(application => {
            const card =
                document.createElement("article");

            card.className = "application-card";

            const heading =
                document.createElement("h3");

            heading.textContent =
                application.company_name ||
                "Unknown company";

            const role =
                document.createElement("p");

            role.textContent =
                `Role: ${
                    application.role || "Not specified"
                }`;

            const status =
                document.createElement("p");

            status.textContent =
                `Status: ${
                    application.status || "Unknown"
                }`;

            const statusValue =
                String(
                    application.status || ""
                ).toLowerCase();

            if (statusValue === "pending") {
                status.classList.add(
                    "status-pending"
                );
            }

            if (statusValue === "accepted") {
                status.classList.add(
                    "status-accepted"
                );
            }

            if (statusValue === "rejected") {
                status.classList.add(
                    "status-rejected"
                );
            }

            const applicationId =
                document.createElement("p");

            applicationId.textContent =
                `Application ID: ${application.id}`;

            card.appendChild(heading);
            card.appendChild(role);
            card.appendChild(status);
            card.appendChild(applicationId);

            applicationsList.appendChild(card);
        });

    } catch (error) {
        console.error(
            "Application loading error:",
            error
        );

        applicationsList.innerHTML = `
            <div class="error-state">
                <h3>Unable to load applications</h3>
                <p>${error.message}</p>
            </div>
        `;
    }
}

loadApplications();


// =========================================
// LIGHT / DARK MODE
// =========================================

const themeToggle =
    document.getElementById("themeToggle");

const savedTheme =
    localStorage.getItem("theme");

if (savedTheme === "dark") {
    document.body.classList.add("dark-mode");
}

function updateThemeButton() {
    if (!themeToggle) {
        return;
    }

    const isDark =
        document.body.classList.contains("dark-mode");

    themeToggle.textContent =
        isDark
            ? "☀️ Light Mode"
            : "🌙 Dark Mode";
}

updateThemeButton();

if (themeToggle) {
    themeToggle.addEventListener(
        "click",
        function () {
            document.body.classList.toggle(
                "dark-mode"
            );

            const isDark =
                document.body.classList.contains(
                    "dark-mode"
                );

            localStorage.setItem(
                "theme",
                isDark ? "dark" : "light"
            );

            updateThemeButton();
        }
    );
}


// =========================================
// DASHBOARD STATISTICS
// =========================================

async function loadDashboardStats() {
    const studentCount =
        document.getElementById("studentCount");

    const opportunityCount =
        document.getElementById("opportunityCount");

    const applicationCount =
        document.getElementById("applicationCount");

    const acceptedCount =
        document.getElementById("acceptedCount");

    const skillCount =
        document.getElementById("skillCount");

    const dashboardExists =
        studentCount ||
        opportunityCount ||
        applicationCount ||
        acceptedCount ||
        skillCount;

    if (!dashboardExists) {
        return;
    }

    if (!requireLogin()) {
        return;
    }

    const studentId = getStudentId();

    try {
        const [
            studentsResponse,
            opportunitiesResponse,
            applicationsResponse,
            skillsResponse
        ] = await Promise.all([
            fetch(`${API_BASE_URL}/students/count`),
            fetch(`${API_BASE_URL}/opportunities`),
            fetch(
                `${API_BASE_URL}/applications/student/${studentId}`
            ),
            fetch(`${API_BASE_URL}/skills`)
        ]);

        if (
            !studentsResponse.ok ||
            !opportunitiesResponse.ok ||
            !applicationsResponse.ok ||
            !skillsResponse.ok
        ) {
            throw new Error(
                "Failed to load dashboard statistics"
            );
        }

        const students =
            await studentsResponse.json();

        const opportunities =
            await opportunitiesResponse.json();

        const applications =
            await applicationsResponse.json();

        const skills =
            await skillsResponse.json();

        const studentSkills = skills.filter(skill =>
            Number(skill.student_id) === studentId
        );

        if (studentCount) {
            studentCount.textContent = students;
        }

        if (opportunityCount) {
            opportunityCount.textContent =
                opportunities.length;
        }

        if (applicationCount) {
            applicationCount.textContent =
                applications.length;
        }

        if (acceptedCount) {
            acceptedCount.textContent =
                applications.filter(application =>
                    String(
                        application.status
                    ).toLowerCase() === "accepted"
                ).length;
        }

        if (skillCount) {
            skillCount.textContent =
                studentSkills.length;
        }

    } catch (error) {
        console.error(
            "Dashboard statistics error:",
            error
        );

        [
            studentCount,
            opportunityCount,
            applicationCount,
            acceptedCount,
            skillCount
        ].forEach(element => {
            if (element) {
                element.textContent = "Error";
            }
        });
    }
}

loadDashboardStats();


// =========================================
// LOGOUT
// =========================================

function setupLogoutButton() {
    const logoutBtn =
        document.getElementById("logoutBtn");

    if (!logoutBtn) {
        return;
    }

    logoutBtn.addEventListener(
        "click",
        function () {
            localStorage.removeItem("studentId");
            localStorage.removeItem("studentName");
            localStorage.removeItem("studentEmail");

            window.location.href = "index.html";
        }
    );
}


// =========================================
// ACTIVE NAVIGATION LINK
// =========================================

function setupActiveNavigation() {
    const navigationLinks =
        document.querySelectorAll("nav a");

    navigationLinks.forEach(link => {
        const linkPage =
            link.getAttribute("href");

        if (linkPage === currentPage) {
            link.classList.add("active");
        }
    });
}