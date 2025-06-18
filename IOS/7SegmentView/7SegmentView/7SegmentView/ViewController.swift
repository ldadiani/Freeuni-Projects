//
//  ViewController.swift
//  7SegmentView
//
//  Created by Liza Dadiani on 10/10/23.
//

import UIKit

class ViewController:
UIViewController {
    let x = 901
    @IBOutlet var firstNumView: [UIView]!
    
    @IBOutlet var secondNumView: [UIView]!
    
    @IBOutlet var thirdNumView: [UIView]!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        let firstNum = x / 100
        let secondNum = (x / 10) % 10
        let thirdNum = x%10
        colorView(num : firstNum, v: firstNumView)
        colorView(num : secondNum, v: secondNumView)
        colorView(num : thirdNum, v: thirdNumView)
        
        
    }
    
    
    func colorView(num : Int, v: [UIView]!){
        if(num == 0) {
            v[3].backgroundColor = .systemBackground
        } else if (num == 1) {
            v[0].backgroundColor = .systemBackground
            v[1].backgroundColor = .systemBackground
            v[3].backgroundColor = .systemBackground
            v[4].backgroundColor = .systemBackground
            v[6].backgroundColor = .systemBackground
        } else if (num == 2) {
            v[1].backgroundColor = .systemBackground
            v[5].backgroundColor = .systemBackground
        }  else if (num == 3) {
            v[1].backgroundColor = .systemBackground
            v[4].backgroundColor = .systemBackground
        } else if(num == 4){
            v[0].backgroundColor = .systemBackground
            v[4].backgroundColor = .systemBackground
            v[6].backgroundColor = .systemBackground
        }  else if (num == 5) {
            v[2].backgroundColor = .systemBackground
            v[4].backgroundColor = .systemBackground
        } else if (num == 6) {
            v[2].backgroundColor = .systemBackground
        } else if (num == 7) {
            v[1].backgroundColor = .systemBackground
            v[3].backgroundColor = .systemBackground
            v[4].backgroundColor = .systemBackground
            v[6].backgroundColor = .systemBackground
        } else if (num == 9) {
            v[4].backgroundColor = .systemBackground
       }
        
    }


}

