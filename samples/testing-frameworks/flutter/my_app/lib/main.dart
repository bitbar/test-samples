import 'package:flutter/material.dart';
import 'dart:developer';
import 'package:flutter/foundation.dart';

String titleText = 'What is the best way to test your applications against over one hundred devices?';
String answerText1 = 'Buy 101 devices';
String answerText2 = 'Ask mom for help';
String answerText3 = 'Use Bitbar Cloud';

String subPageAnswerText = "Hi";
String subPageNameText = "Hi";
String userName = "Hi";

String correctAnswerText = "You are right!!!";
String correctAnswerText2 = "Congratulations";
String wrongAnswerText = "Wrong answer!!!";
String wrongAnswerText2 = "Haven't you heard about Bitbar Cloud? You should try it";

bool correctAnswer = false;
TextEditingController userNameController = new TextEditingController();

Color elementColor;

void main() {
  runApp(App());
}

class App extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Sample',
      home: MainPage(),
    );
  }
}

class MainPage extends StatefulWidget {

  @override
  _MainPageState createState() => _MainPageState();

 }

class _MainPageState extends State<MainPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Bitbar Flutter Demo'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Image.asset('assets/images/bitbar_new.png',fit: BoxFit.fitWidth),
            Expanded(
              flex: 1,
              child: Container(
                //color: Colors.white,
                padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                child: Text(
                  '$titleText',
                  textAlign: TextAlign.center,
                  key: Key('question-text'),
                  style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontStyle: FontStyle.italic,
                      fontSize: 18.0,
                      color: Colors.blueGrey),
                ),
              ),
            ),
            Expanded(
              child: Container(
                //color: Colors.white,
                //width: 100,
                padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                child: RaisedButton(child: Text(answerText1),
                  key: Key('wrong-answer1'),
                  onPressed: () {
                    correctAnswer = false;
                    changeText(correctAnswer);
                    navigateToSubPage(context);
                  },
                  color: Colors.white,
                  textColor: Colors.grey,
                  padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                  splashColor: Colors.grey,
                ),
              ),
            ),
            Expanded(
              child: Container(
                //color: Colors.white,
                //width: 100,
                padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                child: RaisedButton(child: Text(answerText2),
                  key: Key('wrong-answer2'),
                  onPressed: () {
                    correctAnswer = false;
                    changeText(correctAnswer);
                    navigateToSubPage(context);
                  },
                  color: Colors.white,
                  textColor: Colors.grey,
                  padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                  splashColor: Colors.grey,
                ),
              ),
            ),
            Expanded(
              child: Container(
                //color: Colors.white,
                //width: 100,
                padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                child:  RaisedButton(child: Text(answerText3),
                  key: Key('correct-answer'),
                  onPressed: () {
                    correctAnswer = true;
                    changeText(correctAnswer);
                    navigateToSubPage(context);
                  },
                  color: Colors.white,
                  textColor: Colors.grey,
                  padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                  splashColor: Colors.grey,
                ),
              ),
            ),
            TextField(
              key: Key('textbox'),
              //key: _textKey,
              textAlign: TextAlign.center,
              controller: userNameController,
              style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontSize: 18.0,
                  color: Colors.blueGrey),
              decoration: InputDecoration(
                  contentPadding: new EdgeInsets.all(12.0),
                  border: InputBorder.none,
                  hintText: 'Enter your name here'
              ),
            ),
            Expanded(
              flex: 1,
              child: Container(
                //color: Colors.white,
                width: 500,
                padding: EdgeInsets.fromLTRB(100, 50, 100, 50),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class SubPage extends StatelessWidget {

  int value = 1;
  void decrement() => value--;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Bitbar Flutter Demo'),
        backgroundColor: elementColor,
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.start,
          children: <Widget>[
            Image.asset('assets/images/logo_cloud_new.png',fit: BoxFit.fitWidth),
            Container(
              //color: Colors.white,
              width: 500,
              padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
            ),
            Text(
              '$subPageAnswerText',
              textAlign: TextAlign.center,
              key: Key('answer-text'),
              style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontStyle: FontStyle.italic,
                  fontSize: 16.0,
                  color: Colors.blueGrey),
            ),
            Text(
              '$subPageNameText',
              textAlign: TextAlign.center,
              key: Key('note-text'),
              style: TextStyle(
                  fontWeight: FontWeight.bold,
                  fontStyle: FontStyle.italic,
                  fontSize: 16.0,
                  color: Colors.blueGrey),
            ),
            Container(
              //color: Colors.white,
              width: 200,
              padding: EdgeInsets.fromLTRB(100, 10, 100, 10),
            ),
            Expanded(
              flex: 1,
              child: Container(
                //color: Colors.white,
                //width: 100,
                padding: EdgeInsets.fromLTRB(100, 10, 100, 10),
                child: RaisedButton(
                  key: Key('back-button'),
                  textColor: Colors.white,
                  color: elementColor,
                  padding: EdgeInsets.fromLTRB(10, 10, 10, 10),
                  child: Text(
                    'Go back',
                    key: Key('back-button-text'),
                  ),
                  onPressed: () {
                    Navigator.pop(context);
                  },
                )
              ),
            ),
            Expanded(
              flex: 3,
              child: Container(
                //color: Colors.white,
                width: 500,
                padding: EdgeInsets.fromLTRB(100, 50, 100, 50),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

void changeText(bool answer) {
  if (answer == true) {
    subPageAnswerText = correctAnswerText;
    subPageNameText = correctAnswerText2;
    elementColor = Colors.green;
  } else {
    subPageAnswerText = wrongAnswerText;
    subPageNameText = wrongAnswerText2;
    elementColor = Colors.red;
  }
  //debugPrint('answer correct: $answer');
}

Future navigateToSubPage(context) async {
  //debugPrint('answer correct: $correctAnswer');
  userName =  userNameController.text;
  subPageNameText =  subPageNameText + " " + userName;
  Navigator.push(context, MaterialPageRoute(builder: (context) => SubPage()));
}
