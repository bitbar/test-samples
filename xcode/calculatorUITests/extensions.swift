//
//  extensions.swift
//  calculator
//
//  Created by Petri Wunsch  on 17/01/2017.
//  Copyright © 2017 Petri W. All rights reserved.
//
import XCTest

extension XCUIElement{
    func tapAtPosition(position: CGPoint) {
        let cooridnate = self.coordinate(withNormalizedOffset: CGVector(dx: 0, dy: 0)).withOffset(CGVector(dx: position.x, dy: position.y))
        cooridnate.tap()
    }
}
