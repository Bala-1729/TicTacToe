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
    if(doc.getAttribute("value")==='.'){
        doc.setAttribute("value","X");
        array[id-1]='X';
        doc.className = "player1";
        document.getElementById("cpu_first").style.display="none";
    }
    else if(id=="cpu_first"){
        document.getElementById(id).style.display="none";
    }
    else {return;}


    HTTPRequest().then((res) => {
        const response = JSON.parse(res);
        console.log(response);
        doc = document.getElementById("result");
        if(response["status"]==="checkmate") {

            if(response["player"]=="1")
                doc.innerHTML="Player 1 Wins!!!";
            else
            {
                doc.innerHTML="CPU Wins!!!";
                doc = document.getElementById(parseInt(response["cpu_position"])+1);
                doc.setAttribute("value","O");
                doc.className="player2";
                
            }
            document.getElementById("reset").style.display = "block";
            document.querySelectorAll('input').forEach(x => {if(x.id-1!=response["index1"] && x.id-1!=response["index2"] && x.id-1!=response["index3"]) x.setAttribute('disabled',true)});
        }
        else if(response["status"]==="tie"){
            if(response["cpu_position"]!==undefined)
            {
                doc1 = document.getElementById(parseInt(response["cpu_position"])+1)
                doc1.setAttribute("value","O");
                array[parseInt(response["cpu_position"])]='O';
                doc1.className = "player2"; 
            }
            doc.innerHTML="Match Tie!!!";
            document.getElementById("reset").style.display = "block";
        }
        else {
            doc = document.getElementById(parseInt(response["cpu_position"])+1)
            doc.setAttribute("value","O");
            array[parseInt(response["cpu_position"])]='O';
            doc.className = "player2";  
        }
    });      
}