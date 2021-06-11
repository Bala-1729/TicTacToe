var clickCount = 0;
var array = ['.','.','.','.','.','.','.','.','.'];
var level="novice";
var operation="cleanslate";
var clean="true"

function reload() {
    window.location.reload();
}

function cleanSlate(){
    clean="true";
    document.getElementById("redo").setAttribute('disabled',true);
    HTTPRequest().then(()=>{
        clean="false";
        operation="move"
    });
}

function setLevel(){
    level = event.target.getAttribute("id");
    novice = document.getElementById("novice")
    expert = document.getElementById("expert");
    if(level=="novice"){
        novice.style.backgroundColor="lawngreen";
        expert.style.backgroundColor="transparent";
    }
    else{
        expert.style.backgroundColor="crimson";
        novice.style.backgroundColor="transparent";
    }
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

        xhr.send(JSON.stringify({"array":array,"operation":operation,"level":level,"clean":clean}));
    });

    return response;
}

function undoRedo(){
    if(event.target.id=="undo")
        operation="undo";
    else
        operation="redo";

    HTTPRequest().then((res) =>{
        const response = JSON.parse(res);
        console.log(response);
        if(operation=="undo"){
            document.getElementById("redo").disabled = false;
            id=parseInt(response["x2"])*3+parseInt(response["y2"])
            array[id]=".";
            document.getElementById(id+1).setAttribute("value",".");
            document.getElementById(id+1).className="undo"

            if(response["x1"]!=undefined){
                id=parseInt(response["x1"])*3+parseInt(response["y1"])
                array[id]=".";
                document.getElementById(id+1).setAttribute("value",".");
                document.getElementById(id+1).className="undo"
            }

            if(response["top"]==-1){
                document.getElementById("undo").disabled=true;
            }
        }
        else {
            document.getElementById("undo").disabled=false;
            if(response["x2"]){
                id=parseInt(response["x2"])*3+parseInt(response["y2"])
                array[id]= response["player2"]=="2"?"O":"X";
                document.getElementById(id+1).setAttribute("value",response["player2"]=="2"?"O":"X");
                document.getElementById(id+1).className=response["player2"]=="2"?"player2":"player1";
            }

            if(response["x1"]!=undefined){
                id=parseInt(response["x1"])*3+parseInt(response["y1"])
                array[id]= response["player1"]=="1"?"X":"O";
                document.getElementById(id+1).setAttribute("value",response["player1"]=="1"?"X":"O");
                document.getElementById(id+1).className=response["player1"]=="1"?"player1":"player2";
            }
        }
    });
}

function changeValue() {
    
    id = event.target.id;
    doc = document.getElementById(id) 
    operation="move";
    document.getElementById("undo").disabled=false;
    if(doc.getAttribute("value")==='.'){
        doc.setAttribute("value","X");
        array[id-1]='X';
        doc.className = "player1";
        //document.getElementById("cpu_first").style.display="none";
        document.getElementById("redo").disabled=true;
    }
    else {return;}
    
    document.getElementById("undoRedo").style.display="block";
    document.getElementById("level").style.display="none";

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
            document.getElementById("undoRedo").style.display="none";
            document.getElementById("reset").style.display = "block";
            document.querySelectorAll('input').forEach(x => {if(x.id-1!=response["index1"] && x.id-1!=response["index2"] && x.id-1!=response["index3"]) x.setAttribute('disabled',true)});
        }
        else if(response["status"]==="tie"){
            document.getElementById("undoRedo").style.display="none";
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