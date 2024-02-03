window.onload = function() {
    const newElement = document.createElement("div");
    document.body.appendChild(newElement);

    fetch("http://localhost:8080/api/members/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        username: "user1",
        password: "1234",
      }),
    })
    .then((response) => response.json())
    .then((json) => localStorage.setItem("accessToken", json.data.accessToken))
    .catch((error) => console.log(error))


    alert(localStorage.getItem("accessToken"));
}