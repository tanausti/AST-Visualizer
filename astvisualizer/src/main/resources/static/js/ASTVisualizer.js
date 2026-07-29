function getCode() {
    const code = document.getElementById("code").value;
    clearText();
    sendCode(code);
}

function sendCode(code) {
    fetch("/parse", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain"
        },
        body: code
    })
    .then(response => response.json())
    .then(tree => {
        console.log(tree);
        visualizeTree(tree);
    });
}

function clearText() {
    document.getElementById("code").value = "";
}