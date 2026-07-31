function getCode() {
    const code = document.getElementById("code").value;
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
    .then(response => response.text())
	.then(result => {
	        if (result === "ERROR") {
	            console.log("Parsing failed.");
				document.getElementById("errorText").style.display = "block";
				//add a clear tree func
				const svg = d3.select("#tree-svg");
				svg.selectAll("*").remove();
	        } else {
				document.getElementById("errorText").style.display = "none";
	            const tree = JSON.parse(result);
	            visualizeTree(tree);
	        }
	    });
}



function clearText() {
    document.getElementById("code").value = "";
}