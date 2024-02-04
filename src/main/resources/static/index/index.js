window.onload = function() {
    const newElement = document.createElement("div");
    document.body.appendChild(newElement);

    fetch("/api/members/login", {
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
    .then((json) => localStorage.setItem("sse_access_token", json.data.accessToken))
    .catch((error) => console.log(error))

    const sse = new EventSourcePolyfill(
                "/api/notifications/connect", {
                    headers: {
                        "Authorization": "Bearer " + localStorage.getItem("sse_access_token"),
                        "Last-Event-Id": localStorage.getItem("sse_last_event_id")
                    }
                }
            );

    sse.addEventListener("connect", e => {
        console.log(e.data);
        localStorage.setItem("sse_last_event_id", e.lastEventId);
    })
}