/*
* Made by Will Bresnahan 2017
* 
* */

(function() {
    
    var images;
    
    $(document).ready(function() {
    
        $.getJSON('../db/database.json', function(data) {
            images = data;
            showMainMenu();
        });
        
    });
    
    function clear() {
        $(".clearable").html("");
    }
    
    function returnMenu() {
        $("#returnmenu").html('<a href="javascript:showMainMenu();"><h1>Go Back</h1></a>');
    }
    
    var showMainMenu = function() {
        clear();
        // language=HTML
        $("#menuselection").html('<h1>What would you like to do?</h1>\n\n<a href="javascript:choice(\'list\');" class="optionchoice">List of all images</a><br>\n<a href="javascript:choice(\'mc\');" class="optionchoice">Multiple choice quiz</a><br>\n<a href="javascript:choice(\'sahelp\');" class="optionchoice">Short answer quiz (with help)</a><br>\n<a href="javascript:choice(\'sa\');" class="optionchoice">Short answer quiz (no help)</a><br>\n<a href="javascript:choice(\'fc\');" class="optionchoice">Flash cards</a><br>\n<a href="javascript:choice(\'match\');" class="optionchoice">Matching game</a><br>')
    };
    
    function list() {
        console.log(images);
        var list = $("#imagelist");
        for (var i = 0; i < images.length; i++) {
            var name = images[i]["name"];
            list.append('<br><br><h1>' + name + '</h1>');
            var max = Math.min(images[i]["links"].length, 10); // the second number is the image limit
            for (var j = 0; j < max; j++) {
                console.log(images[i]["links"][j]);
                list.append('<img class="imagethumbnail" src="' + images[i]["links"][j] + '" />');
            }
        }
    }
    
    var choice = function(option) {
        
        console.log("Picked " + option);
        
        clear();
        returnMenu();
        if (option === "list") {
            list();
        }else if (option === "mc") {
            
        }else if (option === "sahelp") {
            
        }else if (option === "sa") {
            
        }else if (option === "fc") {
            alert("not done yet");
        }else if (option === "match") {
            alert("not done yet");
        }
        
    };
    
    window.choice = choice;
    window.showMainMenu = showMainMenu;
    
})();
