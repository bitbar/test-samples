/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Image,
  TextInput,
  Dimensions,
  PixelRatio,
  TouchableOpacity
} from 'react-native';

type Props = {};

export default class sampleproject extends Component {
  constructor(props) {
    super(props);
    let {width, height} = Dimensions.get('window')
    
    this.state = {
      titleText: "What is the best way to test your applications against over one hundred devices?",
      wrongAnswerText1: 'Buy 101 devices',
      wrongAnswerText2: 'Ask mom for help',
      correctAnswerText: 'Use Testdroid Cloud',
      typeYourNameText: 'Type your name here',
      wrongAnswerInfoText: "Haven't you heard about Testdroid Cloud?",
      text: "Anonym Testdroider",
      response: undefined,
      answerIsTrue: false
    };
  }
  render() {
    const backgroundColor  = 'white';
    if (this.state.response) return this.renderAfterButton();
    return (
      <View testID='question_screen' style={styles.container}>
        <Image source={require('./images/bitbar.png')} />
        <Text testID='question_text' style={styles.title}>
          {this.state.titleText}{'\n'}{'\n'}
        </Text>
        <TouchableOpacity testID='wrong_answer_button1' onPress={this.onButtonPress.bind(this, 'Wrong Answer',false)}>
          <Text style={styles.question_text}>
          {this.state.wrongAnswerText1}{'\n'}{'\n'}</Text>
        </TouchableOpacity>
        <TouchableOpacity testID='correct_answer_button' onPress={this.onButtonPress.bind(this, 'You are right',true)}>
          <Text style={styles.question_text}>
          {this.state.correctAnswerText}{'\n'}{'\n'}</Text>
        </TouchableOpacity>
        <TouchableOpacity testID='wrong_answer_button2' onPress={this.onButtonPress.bind(this, 'Wrong Answer',false)}>
          <Text style={styles.question_text}>
          {this.state.wrongAnswerText2}{'\n'}{'\n'}</Text>
        </TouchableOpacity>
        <TextInput testID='text_input_field'
          style={styles.name_textfield}
          returnKeyType = {"done"}
          placeholder={this.state.typeYourNameText}
          onChangeText={(text) => this.setState({text})}
        />
      </View>
    );
  }
  renderAfterButton() {
    if (this.state.answerIsTrue === true)
    {
      return (
      <View style={styles.answer_screen_container}>
      <Image source={require('./images/logo_cloud.png')} />
        <Text style={styles.right_answer_screen_title}>
          {this.state.response}!!!
        </Text>
        <Text style={styles.right_answer_screen_text}>
          Congratulations {this.state.text}{'\n'}{'\n'}</Text>
      </View>
    );
    }
    else
    {
      return (
      <View style={styles.answer_screen_container}>
      <Image source={require('./images/logo_cloud.png')} />
        <Text style={styles.wrong_answer_screen_title}>
          {this.state.response}!!!
        </Text>
        <Text style={styles.wrong_answer_screen_text}>
          {this.state.wrongAnswerInfoText}{'\n'}{'\n'}</Text>
      </View>
    );
    }
  }
  onButtonPress(response,answerIsTrue) {
    this.setState({
      response: response,
      answerIsTrue: answerIsTrue
    });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1, 
    borderRadius: 4,
    borderWidth: 0.5,
    paddingTop: 10, 
    paddingBottom: 10, 
    paddingLeft: 10, 
    paddingRight: 10, 
    justifyContent: 'flex-start', 
    alignItems: 'center', 
    backgroundColor: 'white',
    borderColor: 'lightblue',
  },
  title: {
    fontSize: PixelRatio.getFontScale() * (22),
    flex: 1, 
    fontWeight: 'bold',
    color: 'lightblue',
    marginTop: 20, 
    marginHorizontal: 20,
  },
  question_text: {
    fontSize: PixelRatio.getFontScale() * (18),
    fontWeight: 'bold',
    color: 'darkgray',
    marginTop: 5, 
    marginBottom: 5,
  },
  name_textfield: {
    fontSize: PixelRatio.getFontScale() * (16),
    flex: 0.3, 
    fontWeight: 'normal',
    color: 'darkgray',
    marginTop: 10, 
    marginBottom: 20,
    marginHorizontal: 20,
    height: 50, 
    width: 180,
  },
  answer_screen_container: {
    flex: 1,
    justifyContent: 'center', 
    alignItems: 'center',
    backgroundColor: 'white',
    marginTop: 10, 
    marginBottom: 20,
  },
  wrong_answer_screen_title: {
    fontSize: PixelRatio.getFontScale() * (25),
    fontWeight: 'bold',
    color: 'red',
    marginTop: 10, 
    marginBottom: 20,
  },
  wrong_answer_screen_text: {
    fontSize: PixelRatio.getFontScale() * (18),
    fontWeight: 'normal',
    color: 'darkgray',
    marginTop: 10, 
    marginBottom: 20,
  },
  right_answer_screen_title: {
    fontSize: PixelRatio.getFontScale() * (25),
    fontWeight: 'bold',
    color: 'green',
    marginTop: 10, 
    marginBottom: 20,
  },
  right_answer_screen_text: {
    fontSize: PixelRatio.getFontScale() * (18),
    fontWeight: 'normal',
    color: 'darkgray',
    marginTop: 10, 
    marginBottom: 20,
  },
});

