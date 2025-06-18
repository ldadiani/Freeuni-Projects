//
//  WrapperController.swift
//  Assignment_Contacts_Lizi_Dadiani
//
//  Created by lizi dadiani on 15.01.24.
//

import UIKit

class WrapperController: UITabBarController {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let vc1 = UINavigationController(rootViewController: ViewController())
        
        tabBar.backgroundColor = .systemBackground
        
        vc1.tabBarItem.image = UIImage(systemName: "person.3.sequence")
        
        vc1.title = "Contacts"
        
        setViewControllers([vc1], animated: true)
    }

}
