//
//  ForecastViewController.swift
//  ldadi21_final_project App
//
//  Created by lizi dadiani on 26.01.24.
//

import UIKit

class HourlyWeatherViewController: UIViewController {
    
    
    private var data: [[List]] = []
    
    
    
    private let spinner: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView()
        view.style = .large
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()

    private let tableView: UITableView = {
        let tableView = UITableView()
        tableView.register(ThreeHoursTableViewCell.self, forCellReuseIdentifier: ThreeHoursTableViewCell.identifier)
        tableView.register(ThreeHoursTableViewHeader.self, forHeaderFooterViewReuseIdentifier: ThreeHoursTableViewHeader.identifier)
        tableView.backgroundColor = .clear
        return tableView
    }()
    
    private let errorView = ErrorView()
    
    private let constants = Constants.shared
    
    
    //-------------------------------------------------------------------------------------------
    override func viewDidLoad() {
        super.viewDidLoad()
        configureNavBar()
        tableView.delegate = self
        tableView.dataSource = self
        data.append([List]())
        addSubViewsAndTargets()
        activeConstraints()
        view.backgroundColor = .systemTeal.withAlphaComponent(1)
        
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        tableView.frame = view.bounds
        tableView.isHidden = true
        errorView.isHidden = true
        refresh()
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        tableView.backgroundView?.layer.sublayers?[0].frame = view.bounds
        tableView.frame = view.bounds
    }
    

    private func configureNavBar() {
        navigationItem.titleView = {
            let label = UILabel()
            label.text = "Forecast"
            label.textColor = .white
            let font = UIFont.systemFont(ofSize: constants.navBarFontSize, weight: .semibold)
            label.font = font
            return label
        }()
        
        let leftImige = UIImage(systemName: "arrow.clockwise")
        navigationItem.leftBarButtonItem = UIBarButtonItem(image: leftImige, style: .done, target: self, action: #selector(refresh))
        navigationItem.leftBarButtonItem?.tintColor = .yellow
    }
    
    
    private func addSubViewsAndTargets() {
        view.addSubview(tableView)
        view.addSubview(spinner)
        view.addSubview(errorView)
        errorView.reloadButton.addTarget(self, action: #selector(refresh), for: .touchUpInside)
        errorView.translatesAutoresizingMaskIntoConstraints = false
    }
    
    @objc private func refresh() {
        hideInformation()
        spinner.startAnimating()
        
        let city = UserDefaults.standard.object(forKey: "currentCity") ?? "Tbilisi"
        
        APICaller.shared.getFiveDayThreeHoursWeather(at: city as! String) { [weak self] results in
            guard let self = self else { return }
            DispatchQueue.main.async {
                switch results {
                case.success(let model):
                    self.divideData(fullData: model)
                    self.tableView.reloadData()
                    self.showInformation()
                case.failure(let error):
                    print(error)
                    self.showError()
                }
                self.spinner.stopAnimating()
            }
        }
    }
    
    private func hideInformation() {
        tableView.isHidden = true
        spinner.isHidden = false
    }
    
    private func showInformation() {
        tableView.isHidden = false
        spinner.isHidden = true
    }
    
    private func showError() {
        spinner.isHidden = true
        errorView.isHidden = false
    }
    
    private func divideData(fullData fiveDaysThreeHoursWeather: HourlyWeatherModel) {
        data = [[List]()] // refresh data
        var firstDateNumber = takeDateNumber(dateTxt: fiveDaysThreeHoursWeather.list[0].dt_txt!)
        for listModel in fiveDaysThreeHoursWeather.list {
            let currentDateNumber = takeDateNumber(dateTxt: listModel.dt_txt!)
            if currentDateNumber > firstDateNumber {
                let newListArray: [List] = []
                data.append(newListArray)
                data[data.count-1].append(listModel)
                firstDateNumber = currentDateNumber
            } else {
                data[data.count-1].append(listModel)
            }
        }
    }
    
    private func takeDateNumber(dateTxt: String) -> String {
        let dateArr = dateTxt.components(separatedBy: ["-"," "])
        return dateArr[2]
    }
    
    
    private func activeConstraints() {
        NSLayoutConstraint.activate([
            spinner.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            spinner.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            
            errorView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            errorView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            errorView.widthAnchor.constraint(equalToConstant: constants.errorViewWidth),
            errorView.heightAnchor.constraint(equalToConstant: constants.errorViewHeight)
        ])
    }
}

extension HourlyWeatherViewController: UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data[section].count
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return data.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if let cell = tableView.dequeueReusableCell(withIdentifier: ThreeHoursTableViewCell.identifier) as? ThreeHoursTableViewCell {
            cell.configure(with: data[indexPath.section][indexPath.row])
            cell.backgroundColor = .clear
            return cell
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return constants.tableViewHeightForRow
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        guard let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: ThreeHoursTableViewHeader.identifier) as? ThreeHoursTableViewHeader else {return UITableViewHeaderFooterView()}
        if data[section].isEmpty {return UITableViewHeaderFooterView()} // check if data is empty
        header.configure(with: data[section][0].dt_txt!)
        return header
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return constants.tableViewHeightForHeader
    }
}
