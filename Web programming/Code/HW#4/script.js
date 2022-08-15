$(document).ready(function(){
  
  $("#F1").click(function(){
    $("#P1").toggle("slow");
	document.getElementById("img1").style.transform += "rotate(180deg)";	
  });
  
  $("#F2").click(function(){
    $("#P2").toggle("slow");
	document.getElementById("img2").style.transform += "rotate(180deg)";
  });
  
  $("#F3").click(function(){
    $("#P3").toggle("slow");
	document.getElementById("img3").style.transform += "rotate(180deg)";
  });
  
  $("#F4").click(function(){
    $("#P4").toggle("slow");
	document.getElementById("img4").style.transform += "rotate(180deg)";
  });
  
  $("#darkmode").click(function(){
    
	if( $(this).is(':checked') ){
        $("body").css("color", "white");
		$("body").css("background-color","#212121");
		$("#geninfo").css("background-color","#424242");
		
		$("#F1").css("background-color","#424242");
		$("#P1").css("background-color","#424242");
		
		$("#F2").css("background-color","#424242");
		$("#P2").css("background-color","#424242");
		
		$("#F3").css("background-color","#424242");
		$("#P3").css("background-color","#424242");
		
		$("#F4").css("background-color","#424242");
		$("#P4").css("background-color","#424242");
	
    } else {
        $("body").css("color", "black");
		$("body").css("background-color","#E0E0E0");
		$("#geninfo").css("background-color","lightgray");
		
		$("#F1").css("background-color","white");
		$("#P1").css("background-color","white");
		
		$("#F2").css("background-color","white");
		$("#P2").css("background-color","white");
		
		$("#F3").css("background-color","white");
		$("#P3").css("background-color","white");
		
		$("#F4").css("background-color","white");
		$("#P4").css("background-color","white");

    }
	
  });
  
  });
  
 