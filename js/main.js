function Analytics() {
    this.init = function(i,s,o,g,r,a,m){
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r]||function(){
            (i[r].q = i[r].q || []).push(arguments)
        },
        i[r].l = 1 * new Date();

        a = s.createElement(o),
        m = s.getElementsByTagName(o)[0];

        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a,m)
    }

    this.create = function(a, b, c) {
        ga(a, b, c);
    }

    this.send = function(a, b) {
        ga(a, b);
    }
}

function ButtonHandler(){
    var button, count, result;
    this.init = function(){
        button = document.getElementById("test_button");
        result = document.getElementById("result_element");
        count = 1;

        button.onclick = function(){
            if(count < 1){
                button.style.backgroundColor = "#FFFFFF";
                result.innerHTML = "";
                count = 1;
            } else {
                button.style.backgroundColor = "#7FFF00";
                result.innerHTML = "Bitbar Testing real device cloud, with real iOS and Android devices!";
                count = 0;
            }
        }

    }
}

var BH = new ButtonHandler();
var GA = new Analytics();


window.onload = function(){
    GA.init(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
    GA.create('create', 'UA-11260761-16', 'auto');
    GA.send('send', 'pageview');
    BH.init();
};


