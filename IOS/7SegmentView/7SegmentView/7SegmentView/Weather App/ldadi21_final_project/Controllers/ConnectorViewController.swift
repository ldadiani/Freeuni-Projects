//
//  ConnectorViewController.swift
//  ldadi21_final_project
//
//  Created by lizi dadiani on 26.01.24.
//

import UIKit

class ConnectorViewController: UITabBarController {
    
    private let constants = Constants.shared

    override func viewDidLoad() {
        super.viewDidLoad()
        let vc1 = UINavigationController(rootViewController: CurrentWeatherViewController())
        let vc2 = UINavigationController(rootViewController: HourlyWeatherViewController())
        
        vc1.tabBarItem.image = UIImage(systemName: "sun.max")
        vc2.tabBarItem.image = UIImage(systemName: "clock")
        
        
        UITabBar.appearance().unselectedItemTintColor = .white
        
        vc1.title = "Today"
        vc2.title = "Forecast"
        
        setViewControllers([vc1,vc2], animated: true)
    }
    

    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        view.layer.sublayers?[0].frame = view.bounds
    }

}

