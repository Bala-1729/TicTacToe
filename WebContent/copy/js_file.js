var clickCount = 0;
var array = ['.','.','.','.','.','.','.','.','.'];

function reload() {
    window.location.reload();
}

async function HTTPRequest() {
    const response = await new Promise( (resolve, reject) => {
        xhr = new XMLHttpRequest();

        xhr.open("POST","http://"+location.hostname+":8080/TicTacToe/ResponseServlet",true);
    
        xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
        xhr.setRequestHeader('Content-Type','application/json');
        //xhr.setRequestHeader('Access-Control-Allow-Headers', 'Access-Control-Allow-Origin, Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method');

        //xhr.withCredentials = false;
        //xhr.crossDomain = true;
        xhr.onload = () =>{
            if (xhr.status >= 300) {
                reject("Error, status code = " + xhr.status)
              } else {
                resolve(xhr.responseText);
              }
        };

        xhr.onerror = () => {
            reject("Error, status code = " + xhr.status);
        };

        
        xhr.send(array);
    });

    return response;
}

function changeValue() {
    
    id = event.target.id;
    doc = document.getElementById(id)
    initialValue = doc.getAttribute("value");
    if(clickCount && initialValue==='.')
    {
        doc.setAttribute("value","O");
        array[id-1]='O';
        doc.className = "player1";
        clickCount=!clickCount;
    }
    else if(initialValue==='.')
    {
        doc.setAttribute("value","X");
        array[id-1]='X';
        doc.className = "player2";
        clickCount=!clickCount;
    }


    HTTPRequest().then((response) => {
        doc = document.getElementById("result");
        if(response.slice(0,9)==="checkmate") {
            if(clickCount)
                doc.innerHTML="Player 1 Wins!!!";
            else
                doc.innerHTML="Player 2 Wins!!!";
            document.getElementById("reset").style.display = "block";
            document.querySelectorAll('input').forEach((x,reponse) => { if(x.id-1!=response[9] && x.id-1!=response[10] && x.id-1!=response[11]) {x.setAttribute('disabled',true);x.style.backgroundColor="rgba(0, 0, 0, 0.1)";}});
        }
        else if(response==="Tie"){
            doc.innerHTML="Match Tie!!!";
            document.getElementById("reset").style.display = "block";
        }
    });      
}