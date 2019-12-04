import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:my_app/main.dart';

void main() {

    testWidgets('wrong answer test', (WidgetTester tester) async {
      final app = App();
      await tester.pumpWidget(app);
      expect(find.text("What is the best way to test your applications against over one hundred devices?"), findsOneWidget);
      await tester.tap(find.byKey(Key('wrong-answer2')));
      await tester.pumpAndSettle();
      expect(find.text("Wrong answer!!!"), findsOneWidget);
      });

    testWidgets('correct answer test', (WidgetTester tester) async {
      final app = App();
      await tester.pumpWidget(app);
      expect(find.text("What is the best way to test your applications against over one hundred devices?"), findsOneWidget);
      await tester.tap(find.byKey(Key('correct-answer')));
      await tester.pumpAndSettle();
      expect(find.text("You are right!!!"), findsOneWidget);
      });

    testWidgets('type name test', (WidgetTester tester) async {
      final app = App();
      await tester.pumpWidget(app);
      expect(find.text("What is the best way to test your applications against over one hundred devices?"), findsOneWidget);
      await tester.enterText(find.byKey(Key('textbox')),"Anonym Tester");
      await tester.pumpAndSettle();
      expect(find.text("Anonym Tester"), findsOneWidget);
      });

}
