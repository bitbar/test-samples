import 'package:flutter_driver/flutter_driver.dart';
import 'package:test/test.dart';
import 'package:screenshots/screenshots.dart';

void main() {

final questionTextFinder = find.byValueKey('question-text');
final wrongAnswerButton1Finder = find.byValueKey('wrong-answer1');
final wrongAnswerButton2Finder = find.byValueKey('wrong-answer2');
final correctAnswerButton1Finder = find.byValueKey('correct-answer');
final textEditFinder = find.byValueKey('textbox');

final answerTextFinder = find.byValueKey('answer-text');
final noteTextFinder = find.byValueKey('note-text');
final goBackButtonFinder = find.byValueKey('back-button');
final tryAgainButtonTextFinder = find.byValueKey('back-button-text');

String questionText = 'What is the best way to test your applications against over one hundred devices?';
String correctAnswerText = "You are right!!!";
String correctAnswerText2 = "Congratulations";
String wrongAnswerText = "Wrong answer!!!";
String wrongAnswerText2 = "Haven't you heard about Bitbar Cloud? You should try it";
String userName = "Anonym Tester";

  group('Home Screen Test', () {

    final config = Config();
    FlutterDriver driver;
    setUpAll(() async {
      // Connects to the app
      driver = await FlutterDriver.connect();
    });
    tearDownAll(() async {
      if (driver != null) {
        // Closes the connection
        driver.close();
    }
    });

    test('verify the text on home screen', () async {

      Health health = await driver.checkHealth();
      //print(health.status);

      SerializableFinder message = find.text(questionText);
      await driver.waitFor(message);
      expect(await driver.getText(message), questionText);
      await screenshot(driver, config, 'app-open');
    });

    test('input text', () async {

      Health health = await driver.checkHealth();
      //print(health.status);

      SerializableFinder message = find.text("Enter your name here");
      await driver.waitFor(message);
      await driver.tap(textEditFinder);
      await driver.enterText(userName);

      await driver.waitFor(find.text(userName));
      await screenshot(driver, config, 'name typed');

      //await Future.delayed(const Duration(seconds: 5), (){});
    });

    test('wrong answer', () async {

      Health health = await driver.checkHealth();
      //print(health.status);

      await driver.tap(wrongAnswerButton1Finder);
      expect(await driver.getText(answerTextFinder), wrongAnswerText);
      await screenshot(driver, config, 'wrong-answer');

      //await Future.delayed(const Duration(seconds: 5), (){});
    });

    test('correct answer', () async {

      Health health = await driver.checkHealth();
      print(health.status);

      SerializableFinder goBackButton = find.text('Go back');
      try {
        await driver.waitFor(goBackButton);
        await driver.tap(goBackButtonFinder);
      } catch (e) {
        print('try again not visible');
      }

      await driver.tap(correctAnswerButton1Finder);
      expect(await driver.getText(answerTextFinder), correctAnswerText);
      await screenshot(driver, config, 'correct-answer');
    });
    });
}
