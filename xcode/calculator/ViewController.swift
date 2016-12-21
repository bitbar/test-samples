    //
//  ViewController.swift
//  calculator
//
//  Created by Petri Wunsch on 08/11/2016.
//  Petri.Wunsch@bitbar.com
//  Copyright © 2016 Petri Wunsch . All rights reserved.
//

import UIKit

enum modes {
    case NOT_SET
    case ADDITION
    case SUBTRACTION
}

class ViewController: UIViewController {
    var currentMode:modes = modes.NOT_SET
    var labelString:String = "0"
    var SavedNum:Int = 0
    var result:Int = 0
    var lastButtonMode:Bool = false
    
    @IBOutlet weak var label: UILabel!
    
    
   
   
    func tappedNumber(num:Int){
        if lastButtonMode {
            lastButtonMode = false
            labelString = "0"
        }
        labelString = labelString.stringByAppendingString("\(num)")
        updateText()
    }

    
    
    @IBAction func tappedEquals(sender: AnyObject) {
    
        guard let num:Int = Int(labelString) else{
            return
        }
        if currentMode == modes.NOT_SET || lastButtonMode{
            return
        }
        if currentMode == modes.ADDITION {
           let op = operations()
            result = op.Addition(SavedNum, number2: num)
     
        }
        else if currentMode == modes.SUBTRACTION {
                let op = operations()
                result = op.Subtraction(SavedNum, number2: num)
        }
        
        currentMode = modes.NOT_SET
        labelString = "\(result)"
        updateText()
        lastButtonMode = true
    
    }
    
    
    @IBAction func TappedMinus(sender: AnyObject) {
        setMode(modes.SUBTRACTION)
    
    }
    
    @IBAction func tappedPlus(sender: AnyObject) {
            setMode(modes.ADDITION)
    }
    
    
    @IBAction func tapped0(sender: AnyObject) {tappedNumber(0)}
    @IBAction func tapped1(sender: AnyObject) {tappedNumber(1)}
    @IBAction func tapped2(sender: AnyObject) {tappedNumber(2)}
    @IBAction func tapped3(sender: AnyObject) {tappedNumber(3)}
    @IBAction func tapped4(sender: AnyObject) {tappedNumber(4)}
    @IBAction func tapped5(sender: AnyObject) {tappedNumber(5)}
    @IBAction func tapped6(sender: AnyObject) {tappedNumber(6)}
    @IBAction func tapped7(sender: AnyObject) {tappedNumber(7)}
    @IBAction func tapped8(sender: AnyObject) {tappedNumber(8)}
    @IBAction func tapped9(sender: AnyObject) {tappedNumber(9)}
    
    @IBAction func tappedC(sender: AnyObject) {
        SavedNum = 0
        labelString = "0"
        currentMode = modes.NOT_SET
        lastButtonMode = false
        label.text = "0"
    }
    func updateText(){
        guard let labelInt:Int = Int(labelString) else{
            label.text = "Number too big"
            return
        }
        if currentMode == modes.NOT_SET {
            SavedNum = labelInt
        }
        label.text = "\(labelInt)"
      
        
    }
    
    func setMode(newMode:modes){
        if SavedNum == 0 {
            return
        }
        currentMode = newMode
        lastButtonMode = true
    }
   
        
        override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

