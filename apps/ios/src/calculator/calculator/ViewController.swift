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
    case not_SET
    case addition
    case subtraction
}

class ViewController: UIViewController {
    var currentMode:modes = modes.not_SET
    var labelString:String = "0"
    var SavedNum:Int = 0
    var result:Int = 0
    var lastButtonMode:Bool = false
    
    @IBOutlet weak var label: UILabel!
    
    
   
   
    func tappedNumber(_ num:Int){
        if lastButtonMode {
            lastButtonMode = false
            labelString = "0"
        }
        labelString = labelString + "\(num)"
        updateText()
    }

    
    
    @IBAction func tappedEquals(_ sender: AnyObject) {
    
        guard let num:Int = Int(labelString) else{
            return
        }
        if currentMode == modes.not_SET || lastButtonMode{
            return
        }
        if currentMode == modes.addition {
           let op = operations()
            result = op.Addition(SavedNum, number2: num)
     
        }
        else if currentMode == modes.subtraction {
                let op = operations()
                result = op.Subtraction(SavedNum, number2: num)
        }
        
        currentMode = modes.not_SET
        labelString = "\(result)"
        updateText()
        lastButtonMode = true
    
    }
    
    
    @IBAction func TappedMinus(_ sender: AnyObject) {
        setMode(modes.subtraction)
    
    }
    
    @IBAction func tappedPlus(_ sender: AnyObject) {
            setMode(modes.addition)
    }
    
    
    @IBAction func tapped0(_ sender: AnyObject) {tappedNumber(0)}
    @IBAction func tapped1(_ sender: AnyObject) {tappedNumber(1)}
    @IBAction func tapped2(_ sender: AnyObject) {tappedNumber(2)}
    @IBAction func tapped3(_ sender: AnyObject) {tappedNumber(3)}
    @IBAction func tapped4(_ sender: AnyObject) {tappedNumber(4)}
    @IBAction func tapped5(_ sender: AnyObject) {tappedNumber(5)}
    @IBAction func tapped6(_ sender: AnyObject) {tappedNumber(6)}
    @IBAction func tapped7(_ sender: AnyObject) {tappedNumber(7)}
    @IBAction func tapped8(_ sender: AnyObject) {tappedNumber(8)}
    @IBAction func tapped9(_ sender: AnyObject) {tappedNumber(9)}
    
    @IBAction func tappedC(_ sender: AnyObject) {
        SavedNum = 0
        labelString = "0"
        currentMode = modes.not_SET
        lastButtonMode = false
        label.text = "0"
    }
    func updateText(){
        guard let labelInt:Int = Int(labelString) else{
            label.text = "Number too big"
            return
        }
        if currentMode == modes.not_SET {
            SavedNum = labelInt
        }
        label.text = "\(labelInt)"
      
        
    }
    
    func setMode(_ newMode:modes){
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

