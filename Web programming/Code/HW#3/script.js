
$(document).ready( function(){
  $("#B").click(function(){
  
    $("#color_B").prop("disabled", false);
	$("#list_B").prop("disabled", false);
  
  });
  
  
  $("#B_Less").click( function(){
  
    $("#color_B").prop("disabled", true);
	$("#list_B").prop("disabled", true);
	
  });
});

function getTheOutput(){
	
 var i=document.getElementById("input"); //input
 var o=document.getElementById("output"); //output
 var tc=document.getElementById("T_color");
 var bgc=document.getElementById("Bg_color");
 var bc=document.getElementById("color_B"); 
 var B_on = document.getElementById("B"); 
 var B_off = document.getElementById("B_Less"); 
 
 
if(i.value != '') {
 o.innerHTML = i.value;
 o.style.color = tc.value;
 o.style.background = bgc.value;
 
 if(B_on.checked){
 var e = document.getElementById("list_B");
 o.style.border = "5px "+e.options[e.selectedIndex].text+" "+bc.value;
 }
 else if(B_off.checked){
 o.style.border = "";
 }
                  }
else { 
 o.innerHTML = "The input is Empty"; 
 o.style.color = "red";
 o.style.background = null;
 o.style.border = null;
 
     }				  
}

function getTheLocation(){
	
 var i = document.getElementById("input"); //input
 var o = document.getElementById("output"); //output
 var iw = document.getElementById("inputWord");
 var wo= document.getElementById("outo");
 
 var word=iw.value.toLowerCase(); //the word to highlight.
 word=word.trim();
 var para=i.value.toLowerCase(); // the text area to find the word in it.
 
 
 
 var loc=0;
 var over=true;
 var noOfWords=0;
 
 o.innerHTML ="";
 
 if( para.includes(word) && !(word === "") )
 {
	 wo.innerHTML="none";
	 wo.style.color = null;
 while(loc <= para.length && over){
  loc = para.indexOf(word,loc);
  noOfWords++;
  //o.innerHTML +=" " + loc; //+(loc+word.length-1);
 if( (para.indexOf(word,loc+1)) == -1){over=false;}
 else{ loc++;  }
 }

 var reWord = new RegExp(word,"g");
 var res = para.replace(reWord, "<mark>"+word+"</mark>");
 
 o.innerHTML=res;
 wo.innerHTML=noOfWords + " mathces has found";
 }
 else if( word === "" ){wo.innerHTML="EMPTY INPUT"; wo.style.color = "red";}
 else{wo.innerHTML="THE WORD DOES NOT EXIST IN THE PARAGRAPH"; wo.style.color = "red";}
				  
}

function clearTheHightlight(){
	$("mark").replaceWith(function() { return $(this).contents(); });	
}
