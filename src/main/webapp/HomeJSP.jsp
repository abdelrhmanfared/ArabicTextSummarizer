<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="Home.css">
<link href="https://fonts.googleapis.com/css2?family=Lato&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Alfa+Slab+One&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.0/jquery.min.js"></script>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Fredoka&display=swap" rel="stylesheet">

<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous"> 
<script src="https://kit.fontawesome.com/a121f2d42e.js" crossorigin="anonymous"></script>
<title>Home</title>
</head>
<body>
<%String s;
   if (request.getAttribute("result") == null)
    s = "";
     else
     s = request.getAttribute("result").toString();
              %>
<div class="page">
     <div class="container">
         <div class="leftcon">
             <form action="Working" method="post" class="form">
             <div class="topHeader">
                 <header class="Header">Original</header>
                 <select name="TechniquesBox" id="TechniquesBox">
                     <option value="Score Based">Score Based</option>
                     <option value="SVM">SVM</option>
                     <option value="Neural Network">Neural Network</option>
                 </select>
             </div>
             <div class="AreaField">
                <textarea name="originalText" id="textarea1" cols="30" rows="10">${original }</textarea>
             </div>
             <div class="bottomHeader">
                <a href="#" id="uploadbtn"><i class="fa-solid fa-upload"></i>&nbsp;&nbsp;<strong>Upload File</strong></a>
                <input type="file" id="upload">
                <input type="submit" value="Summarize" class="Summarize">
            </div>
            </form>
         </div>
         <div class="rightcon">
             <div class="topHeader">
                <header class="Header">Summarized</header>
             </div>
             <div class="AreaField">
                 <textarea readonly name="originalText" id="" cols="30" rows="10">${result }</textarea>
            </div>
             <div class="bottomHeader">
                <header class="Accuracy">Accuraccy:</header>
            </div>
         </div>
     </div>
</div>
<script>
    let upload = document.getElementById("upload");
    let uploadbtn = document.getElementById("uploadbtn");
    let textarea = document.getElementById("textarea1");

    uploadbtn.addEventListener('click',()=>{
        upload.click();
});
upload.addEventListener('change',()=>{
    let fr = new FileReader();
    fr.readAsText(upload.files[0]);
    fr.onload = function(){
        textarea.innerHTML = fr.result;
    }
});
</script>
</body>
</html>