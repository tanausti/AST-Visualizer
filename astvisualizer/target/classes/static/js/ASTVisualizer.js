document.getElementById('clang-button').click();
document.getElementById('graphic-ast-button').click();


function getCode() {
    const code = document.getElementById("code").value;
	
	if (code === "") {
	        document.getElementById("errorText").textContent = "Please enter some code!";
	        document.getElementById("errorText").style.display = "block";			document.getElementById("json").value = "";

							const svg = d3.select("#tree-svg");
							svg.selectAll("*").remove();
	        return;
	}
	else{
    	sendCode(code);
	}
}


function sendCode(code) {
    let endpoint = "/parse/" + mode;

    fetch(endpoint, {
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
            document.getElementById("errorText").textContent = "Failed to parse code!";
            document.getElementById("errorText").style.display = "block";
            document.getElementById("json").value = "";

            const svg = d3.select("#tree-svg");
            svg.selectAll("*").remove();
        } else {
            document.getElementById("errorText").style.display = "none";

            const tree = JSON.parse(result);

            visualizeTree(tree);
            visualizeJson(result);
        }
    });
}

function setModeClang() {
    mode = "clang";
}

function setModeKiln() {
    mode = "kiln";
}
function visualizeJson(result){
	document.getElementById("json").value = result;
}

function clearText() {
    document.getElementById("code").value = "";
}

function setButtonPressedColors(id){
	document.getElementById(id).style.backgroundColor = "lightgray";
	document.getElementById(id).style.color = "black";
}

function setButtonUnpressedColors(id){
	document.getElementById(id).style.backgroundColor = "";
	document.getElementById(id).style.color = "white";
}

function hide(id){
	document.getElementById(id).style.display = "none";
}

function show(id){
	document.getElementById(id).style.display = "block";
}

