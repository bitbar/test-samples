import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:integration_test/integration_test.dart';
import 'package:my_app/main.dart' as app;
import 'helper.dart';


void main() {
  final questionTextFinder = find.byKey(Key('question-text'));
  final wrongAnswerButton1Finder = find.byKey(Key('wrong-answer1'));
  final wrongAnswerButton2Finder = find.byKey(Key('wrong-answer2'));
  final correctAnswerButton1Finder = find.byKey(Key('correct-answer'));
  final textEditFinder = find.byKey(Key('textbox'));

  final answerTextFinder = find.byKey(Key('answer-text'));
  final noteTextFinder = find.byKey(Key('note-text'));
  final goBackButtonFinder = find.byKey(Key('back-button'));
  final tryAgainButtonTextFinder = find.byKey(Key('back-button-text'));

  String goBack = 'Go back';
  String enterName = 'Enter your name here';
  String questionText = 'What is the best way to test your applications against over one hundred devices?';
  String correctAnswerText = "You are right!!!";
  String correctAnswerText2 = "Congratulations";
  String wrongAnswerText = "Wrong answer!!!";
  String wrongAnswerText2 = "Haven't you heard about Bitbar Cloud? You should try it";
  String userName = "Anonym Tester";

  final binding = IntegrationTestWidgetsFlutterBinding.ensureInitialized()
  as IntegrationTestWidgetsFlutterBinding;

  group('Home Screen Test', () {
    testWidgets('verify the text on home screen', (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();
      expect(find.text(questionText), findsOneWidget);
      await takeScreenshot(tester, binding, 'app-open');
    });

    testWidgets('input text', (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();

      expect(find.text(enterName), findsOneWidget);

      await tester.tap(textEditFinder);
      await tester.enterText(textEditFinder, userName);

      expect(find.text(userName), findsOneWidget);
      await takeScreenshot(tester, binding, 'name-typed');
    });

    testWidgets('wrong answer', (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();

      await tester.tap(wrongAnswerButton1Finder);
      await tester.pumpAndSettle();

      expect(await answerTextFinder, findsOneWidget);
      await takeScreenshot(tester, binding, 'wrong-answer');
    });

    testWidgets('correct answer', (WidgetTester tester) async {
      app.main();
      await tester.pumpAndSettle();

      await tester.tap(correctAnswerButton1Finder);
      await tester.pumpAndSettle();

      expect(await answerTextFinder, findsOneWidget);
      await takeScreenshot(tester, binding, 'correct-answer');
    });
  });
}
