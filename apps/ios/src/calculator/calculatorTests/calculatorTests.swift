//
//  calculatorTests.swift
//  calculatorTests
//
//  Created by Petri Wunsch  on 12/12/2016.
//  petri.wunsch@bitbar.com
//  Copyright © 2016 Petri Wunsch . All rights reserved.
//

import XCTest
@testable import calculator
class calculatorTests: XCTestCase {
    
    override func setUp() {
        super.setUp()
        // Put setup code here. This method is called before the invocation of each test method in the class
    
    }
    
    override func tearDown() {
        // Put teardown code here. This method is called after the invocation of each test method in the class.
        super.tearDown()
    }
    //unit Xctest for addition function of the calculator
    func testAddition() {
        let op = operations()
        let result = op.Addition(5,  number2: 5)
        XCTAssertEqual(result, 10, "Should be 10")
        
}
    //unit Xctest for addition function of the calculator

    func testSubstraction() {
        let op = operations()
        let result = op.Subtraction(15,  number2: 5)
        XCTAssertEqual(result, 10, "Should be 5")
        
    }
    
}
