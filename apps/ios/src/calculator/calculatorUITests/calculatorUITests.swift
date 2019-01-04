//
//  calculatorUITests.swift
//  calculatorUITests
//
//  Created by Petri Wunsch on 08/11/2016.
//  petri.wunsch@bitbar.com
//  Copyright © 2016 Petri Wunsch. All rights reserved.
//

import XCTest
import UIKit


class calculatorUITests: XCTestCase {
    
    let app = XCUIApplication()
    
    
    override func setUp() {
        super.setUp()
        
        // Put setup code here. This method is called before the invocation of each test method in the class.
        
        // In UI tests it is usually best to stop immediately when a failure occurs.
        continueAfterFailure = false
        // UI tests must launch the application that they test. Doing this in setup will make sure it happens for each test method.
        XCUIApplication().launch()

        // In UI tests it’s important to set the initial state - such as interface orientation - required for your tests before they run. The setUp method is a good place to do this.
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    
    func testButtonCount(){
        XCTAssertEqual(app.buttons.count,14,"Should be 14")
    }
    func testInsertText() {
        app.buttons["9"].tap()
        app.buttons ["8"].tap()
        app.buttons ["7"].tap()
       XCTAssertEqual(app.staticTexts["numberField"].label, "987", "Should be 987")
    }
    func testClearText(){
       
        app.buttons ["9"].tap()
        app.buttons ["8"].tap()
        app.buttons ["7"].tap()
        app.buttons ["C"].tap()
        XCTAssertEqual(app.staticTexts["numberField"].label, "0", "Should be 0")
        
    }
    
    
    func testTapOnPoint(){
        let point = CGPoint( x:32,y:32)
        // tap inside ui element at point x:32:y32
        app.buttons["5"].tapAtPosition(position: point)
        XCTAssertEqual(app.staticTexts["numberField"].label, "5", "Should be 5")
    }
    
}

