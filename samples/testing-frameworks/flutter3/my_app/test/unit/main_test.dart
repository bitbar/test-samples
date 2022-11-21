import 'package:test/test.dart';
import 'package:my_app/main.dart';

void main() {

  test('correct answer', () {
    final mainPage = MainPage();

    changeText(true);
    expect(subPageAnswerText, correctAnswerText);
  });

  test('wrong answer', () {
    final mainPage = MainPage();

    changeText(false);
    expect(subPageAnswerText, wrongAnswerText);
  });

  test('SubPage decrement value', () {
    final subPage = SubPage();

    subPage.decrement();
    //print(subPage.value);
    expect(subPage.value, 0);
  });

}
