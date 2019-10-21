/*
* Made by Will Bresnahan 2017
* 
* */

(function() {
    
    var images;
    var quizCorrectIndex = -1;
    var answerTimesClicked = 0;
    
    var quiz = $("#multiplechoice");
    var image = $("#mcqimage");
    var choices = $("#choices");
    var correct = "";
    
    $(document).ready(function() {
    
        /*$.getJSON('../db/database.json', function(data) {
            images = data;
            showMainMenu();
        });*/
        
        images = JSON.parse(dbjson);
        showMainMenu();
        
    });
    
    function getRandomInt(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min)) + min;
    }
    
    
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
    
    function mcqAnswer(entered) {
        console.log(entered);
        if (quizCorrectIndex === entered) {
            $("#infocontainer").html("Correct. It is " + images[quizCorrectIndex]["name"]);
            answerTimesClicked++;
            if (answerTimesClicked >= 2) {
                newMcqProblem();
            }
        }else {
            $("#infocontainer").html("Incorrect");
        }
    }
    
    function newMcqProblem() {
    
        answerTimesClicked = 0;
        
        // get 5 random numbers
        var randomNums = [];
        while (randomNums.length < 5) {
            var possibleRandom = getRandomInt(0, images.length);
            if (!randomNums.includes(possibleRandom)) {
                randomNums.push(possibleRandom);
            }
        }
        
        // get the correct answer index
        quizCorrectIndex = randomNums[getRandomInt(0, randomNums.length)];
        
        // the correct answer
        correct = images[quizCorrectIndex]["name"];
        console.log("Correct name is " + correct);
        console.log("Correct index is " + quizCorrectIndex);
        var possibleImages = images[quizCorrectIndex]["links"];
        var imageSrc = possibleImages[getRandomInt(0, possibleImages.length)];
        image.attr("src", imageSrc);
        
        // all possible answers
        choices.html("");
        for (var i = 0; i < randomNums.length; i++) {
            choices.append('<a href="javascript:mcqAnswer(' + randomNums[i] + ');" id="choice_' + i + '">' + images[randomNums[i]]["name"] + '</a><br>');
        }
        
    }
    
    function mcq() {
        
        // mutliple choice quiz
        quiz = $("#multiplechoice");
    
        // set up
        quiz.append('<img id="mcqimage" class="bigImage" src="#" />');
        quiz.append('<div id="choices"></div>');
        
        image = $("#mcqimage");
        choices = $("#choices");
        correct = "";
        
        $("#nextbuttondiv").append('<a href="javascript:newMcqProblem();">Next</a>')
        
        newMcqProblem();
        
    }
    
    var choice = function(option) {
        
        console.log("Picked " + option);
        
        clear();
        returnMenu();
        if (option === "list") {
            list();
        }else if (option === "mc") {
            mcq();
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
    window.newMcqProblem = newMcqProblem;
    window.mcqAnswer = mcqAnswer;
    
})();
