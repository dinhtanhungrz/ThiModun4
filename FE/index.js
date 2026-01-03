const API_TOPIC = "http://localhost:8080/topics";
const API_TAG = "http://localhost:8080/tags";
const API_LOGIN = "http://localhost:8080/login";

let accessToken = undefined;

/* ================= LOGIN ================= */

function showFormLogin() {
    document.getElementById("root").innerHTML = `
        <center>
            <input type="text" id="username" placeholder="Username"><br><br>
            <input type="password" id="password" placeholder="Password"><br><br>
            <button onclick="login()">Login</button>
        </center>
    `;
}

function login() {
    let dataLogin = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    axios.post(API_LOGIN, dataLogin)
        .then(res => {
            localStorage.setItem("user", JSON.stringify(res.data));
            accessToken = res.data.accessToken;
            showHome();
        })
        .catch((error) => {
        console.log(error);
        let mess = error.response.data;
        alert(mess); 
    });
}

function logout() {
    localStorage.clear();
    accessToken = undefined;
    showFormLogin();
}

function checkLogin() {
    let user = JSON.parse(localStorage.getItem("user"));
    if (user && user.accessToken) {
        accessToken = user.accessToken;
        showHome();
    } else {
        showFormLogin();
    }
}

/* ================= HOME ================= */

function showHome() {
    document.getElementById("root").innerHTML = `
        <div>
            <button onclick="main()">Home</button>
            <button onclick="showFormCreate()">Create Topic</button>
            <button onclick="logout()">Logout</button>
        </div>
        <br>
        <div id="main"></div>
    `;
    main();
}

/* ================= LIST TOPIC ================= */

function main() {
    document.getElementById("main").innerHTML = `
        <table border="1">
            <tr>
                <td>ID</td>
                <td>Title</td>
                <td>Description</td>
                <td>Tags</td>
                <td>Delete</td>
                <td>Edit</td>
            </tr>
            <tbody id="list"></tbody>
        </table>
    `;

    axios.get(API_TOPIC, {
        headers: { Authorization: `Bearer ${accessToken}` }
    }).then(res => {
        let list = res.data;
        let html = "";

        for (let i = 0; i < list.length; i++) {
            html += `
                <tr>
                    <td>${list[i].id}</td>
                    <td>${list[i].title}</td>
                    <td>${list[i].description}</td>
                    <td>
            `;

            let tags = list[i].tags;
            if (tags) {
                for (let j = 0; j < tags.length; j++) {
                    html += tags[j].name + ", ";
                }
            }

            html += `
                    </td>
                    <td><button onclick="removeTopic(${list[i].id})">Delete</button></td>
                    <td><button onclick="showFormEdit(${list[i].id})">Edit</button></td>
                </tr>
            `;
        }

        document.getElementById("list").innerHTML = html;
    });
}

/* ================= CREATE ================= */

function showFormCreate() {
    axios.get(API_TAG, {
        headers: { Authorization: `Bearer ${accessToken}` }
    }).then(res => {
        let tags = res.data;
        let html = `
            <input id="title" placeholder="Title"><br><br>
            <input id="description" placeholder="Description"><br><br>
            <b>Tags</b><br>
        `;

        for (let i = 0; i < tags.length; i++) {
            html += `
                <input type="checkbox" name="tags" value="${tags[i].id}">
                ${tags[i].name}<br>
            `;
        }

        html += `<br><button onclick="createTopic()">Submit</button>`;
        document.getElementById("main").innerHTML = html;
    });
}

function createTopic() {
    let checkboxes = document.getElementsByName("tags");
    let listTags = [];

    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            listTags.push({ id: checkboxes[i].value });
        }
    }

    let topic = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value,
        tags: listTags
    };

    axios.post(API_TOPIC, topic, {
        headers: { Authorization: `Bearer ${accessToken}` }
    }).then(() => main());
}

/* ================= UPDATE ================= */

function showFormEdit(id) {
    axios.get(`${API_TOPIC}/${id}`, {
        headers: { Authorization: `Bearer ${accessToken}` }
    }).then(res => {
        let topic = res.data;
        document.getElementById("main").innerHTML = `
            <input value="${topic.title}" id="title"><br><br>
            <input value="${topic.description}" id="description"><br><br>
            <button onclick="editTopic(${id})">Update</button>
        `;
    });
}

function editTopic(id) {
    let topic = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value
    };

    axios.put(`${API_TOPIC}/${id}`, topic, {
        headers: { Authorization: `Bearer ${accessToken}` }
    }).then(() => main());
}

/* ================= DELETE ================= */

function removeTopic(id) {
    if (confirm("Are you sure?")) {
        axios.delete(`${API_TOPIC}/${id}`, {
            headers: { Authorization: `Bearer ${accessToken}` }
        }).then(() => main());
    }
}

/* ================= START ================= */

checkLogin();
