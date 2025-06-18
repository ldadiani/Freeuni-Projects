//
//  TodayViewController.swift
//  ldadi21_final_project App
//
//  Created by lizi dadiani on 26.01.24.
//

import UIKit
import CoreLocation

class CurrentWeatherViewController: UIViewController {
    
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    
    private var locationManager: CLLocationManager?
    
    
    private let cloudDetail = DetailView()
    private let pressureDetail = DetailView()
    private let windSpeedDetail = DetailView()
    private let windDirectionDetail = DetailView()
    private let humidityDetail = DetailView()
    
    
    
    private let weatherIconImigeView: UIImageView = {
        let view = UIImageView()
        view.contentMode = .scaleAspectFit
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let locationLabel: UILabel = {
        let label = UILabel()
        label.font = label.font.withSize(Constants.shared.cellLocationLabelFontSize)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.textColor = .label
        return label
    }()
    
    private let temperatureLabel: UILabel = {
        let label = UILabel()
        label.font = label.font.withSize(Constants.shared.cellTemperatureLabelFontSize)
        label.textColor = .systemBlue
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    
    private let topStack = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.alignment = .center
        stack.axis = .vertical
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    
    private let midStack = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.alignment = .center
        stack.axis = .horizontal
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    private let bottomStack = {
        let stack = UIStackView()
        stack.distribution = .fillEqually
        stack.alignment = .center
        stack.axis = .horizontal
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    
    
    private let spinner: UIActivityIndicatorView = {
        let view = UIActivityIndicatorView()
        view.style = .large
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private let errorView = ErrorView()
    
    private let constants = Constants.shared
    
    //_______________________________________________________________________________________________________
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .systemTeal.withAlphaComponent(1)
        configureNavBar()
        addViews()
        setConstraintsForCurrentOrientation()
        addTargets()
        findUserLocation()
        configureDetailViews()
        fillStacks()
        
        refresh()
    }
    
    
    
    private func addViews() {
        // add
        view.addSubview(errorView)
        view.addSubview(spinner)
        view.addSubview(topStack)
        view.addSubview(midStack)
        view.addSubview(bottomStack)
        view.addSubview(weatherIconImigeView)
        view.addSubview(locationLabel)
        view.addSubview(temperatureLabel)
        // hide
        errorView.isHidden = true
    }
    
    private func configureNavBar() {
        navigationItem.titleView = {
            let label = UILabel()
            label.text = "Today"
            label.textColor = .white
            let font = UIFont.systemFont(ofSize: constants.navBarFontSize, weight: .semibold)
            label.font = font
            return label
        }()
        let leftImige = UIImage(systemName: "arrow.clockwise")
        navigationItem.leftBarButtonItem = UIBarButtonItem(image: leftImige, style: .done, target: self, action: #selector(refresh))
        
        let rightImage = UIImage(systemName: "square.and.arrow.up")
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: rightImage, style: .done, target: self, action: #selector(refresh))
        
        
    }
    
    
    private func configureDetailViews() {
        cloudDetail.configure(imageName: "cloud.fog", text: "0.0")
        humidityDetail.configure(imageName: "humidity", text: "0.0")
        windSpeedDetail.configure(imageName: "wind", text: "0.0")
        windDirectionDetail.configure(imageName: "arrow.left.arrow.right", text: "N")
        pressureDetail.configure(imageName: "drop", text: "0.0")
        
    }
    
    private func fillStacks() {
        topStack.addArrangedSubview(locationLabel)
        topStack.addArrangedSubview(temperatureLabel)
        
        midStack.addArrangedSubview(cloudDetail)
        midStack.addArrangedSubview(humidityDetail)
        midStack.addArrangedSubview(pressureDetail)
        
        bottomStack.addArrangedSubview(windSpeedDetail)
        bottomStack.addArrangedSubview(windDirectionDetail)
    }
    
    private func addTargets() {
        errorView.reloadButton.addTarget(self, action: #selector(refresh), for: .touchUpInside)
    }
    
    private func findUserLocation() {
        locationManager = CLLocationManager()
        locationManager?.delegate = self
        locationManager?.requestAlwaysAuthorization()
        locationManager?.startUpdatingLocation()
    }
    
    
    @objc private func refresh() {
        hideWeather()
        
        
        APICaller.shared.getCurrentWeatherWithCoordinates(lon: longitude, lat: latitude) { [weak self] result in
            guard let self = self else { return }
            
            DispatchQueue.main.async {
                switch result {
                case.success(let model):
                    self.updateFirstModel(model: model)
                case.failure(let error):
                    print(error)
                    self.showError()
                }
            }
        }
        
    }
    
    
    private func updateFirstModel(model: CurrentWeatherModel) {
        if model.coord.lon == nil || model.coord.lat == nil {
            showError()
        } else {
            showWeather(model: model)
            UserDefaults.standard.set(model.name, forKey: "currentCity")
        }
    }
    
    
    private func showWeather(model: CurrentWeatherModel) {
        if errorView.isHidden == false {return}
        spinner.isHidden = true
        topStack.isHidden = false
        midStack.isHidden = false
        bottomStack.isHidden = false
        weatherIconImigeView.isHidden = false
        cloudDetail.configure(withText: "\(model.clouds.all ?? 0) %")
        pressureDetail.configure(withText: "\(model.main.pressure ?? 0) hPa")
        humidityDetail.configure(withText: "\(model.main.humidity ?? 0) mm")
        windSpeedDetail.configure(withText: "\(model.wind.speed ?? 0) km/h")
        windDirectionDetail.configure(withText: calculateWindDirection(deg: model.wind.deg ?? 400))
        
        locationLabel.text = (model.name ?? "error") + ", " + (model.sys.country ?? "")
        temperatureLabel.text = "\(Int(model.main.temp ?? 0))°C | \(model.weather[0].main ?? "error")"
        
        guard let url = URL(string: "http://openweathermap.org/img/wn/\(model.weather[0].icon ?? "01d").png") else { return }
        weatherIconImigeView.sd_setImage(with: url, completed: nil)
    }
    
    private func hideWeather() {
        spinner.isHidden = false
        errorView.isHidden = true
        spinner.startAnimating()
    }
    
    
    private func showError() {
        errorView.isHidden = false
        errorView.reloadButton.addTarget(self, action: #selector(refresh), for: .touchUpInside)
        spinner.isHidden = true
        topStack.isHidden = true
        midStack.isHidden = true
        bottomStack.isHidden = true
        weatherIconImigeView.isHidden = true
    }
    
    
    private func calculateWindDirection(deg: Int) -> String {
        switch deg {
        case 11...34: return "NNE"
        case 34...56: return "NE"
        case 56...78: return "ENE"
        case 78...101: return "E"
        case 101...124: return "ESE"
        case 124...146: return "SE"
        case 146...169: return "SSE"
        case 169...191: return "S"
        case 191...214: return "SSW"
        case 214...236: return "SW"
        case 236...259: return "WSW"
        case 259...281: return "W"
        case 282...304: return "WNW"
        case 304...326: return "NW"
        case 326...349: return "NNW"
        case 0...11, 349...360: return "N"
        default:
            return "error"
        }
    }
    
    
    private func removeConstraints() {
        
        view.removeConstraints(view.constraints)
    }
    
    private func setConstraintsForLandscape() {
        errorView.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            // ...
            // '''
            // ...
        ])
        
        additionalConstraints()
    }
    
    
    private func setConstraints() {
        errorView.translatesAutoresizingMaskIntoConstraints = false
        
        NSLayoutConstraint.activate([
            cloudDetail.heightAnchor.constraint(equalToConstant: 100),
            cloudDetail.widthAnchor.constraint(equalToConstant: 100),
            
            pressureDetail.heightAnchor.constraint(equalToConstant: 100),
            pressureDetail.widthAnchor.constraint(equalToConstant: 100),
            
            humidityDetail.heightAnchor.constraint(equalToConstant: 100),
            humidityDetail.widthAnchor.constraint(equalToConstant: 100),
            
            windSpeedDetail.heightAnchor.constraint(equalToConstant: 100),
            windSpeedDetail.widthAnchor.constraint(equalToConstant: 100),
            
            windDirectionDetail.heightAnchor.constraint(equalToConstant: 100),
            windDirectionDetail.widthAnchor.constraint(equalToConstant: 100),
            
            
            bottomStack.bottomAnchor.constraint(equalTo: view.bottomAnchor, constant: -120),
            bottomStack.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            bottomStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 40),
            bottomStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -40),
            
            midStack.bottomAnchor.constraint(equalTo: bottomStack.topAnchor, constant: -50),
            midStack.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            midStack.centerYAnchor.constraint(equalTo: view.centerYAnchor, constant: 120),
            midStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 40),
            midStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -40),
            
            topStack.bottomAnchor.constraint(equalTo: midStack.topAnchor, constant: -50),
            topStack.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 25),
            topStack.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -25),
            
            weatherIconImigeView.topAnchor.constraint(equalTo: view.topAnchor, constant: 80),
            weatherIconImigeView.widthAnchor.constraint(equalToConstant: 200),
            weatherIconImigeView.heightAnchor.constraint(equalToConstant: 200),
            weatherIconImigeView.bottomAnchor.constraint(equalTo: topStack.topAnchor, constant: -25),
            weatherIconImigeView.centerXAnchor.constraint(equalTo: view.centerXAnchor)
        ])
        
        additionalConstraints()
    }
    
    
    private func additionalConstraints() {
        NSLayoutConstraint.activate([
            spinner.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            spinner.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            
            errorView.widthAnchor.constraint(equalToConstant: constants.errorViewWidth),
            errorView.heightAnchor.constraint(equalToConstant: constants.errorViewHeight),
            errorView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            errorView.centerYAnchor.constraint(equalTo: view.centerYAnchor),
            
        ])
    }
    
    
    
    private func setConstraintsForCurrentOrientation() {
        
        setConstraints()
    }
    
}




extension CurrentWeatherViewController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let coordinate = manager.location?.coordinate
        self.latitude = coordinate?.latitude ?? 0
        self.longitude = coordinate?.longitude ?? 0
    }
    
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        let status = manager.authorizationStatus
        
        // Handle each case of location permissions
        switch status {
        case .authorizedAlways, .authorizedWhenInUse, .notDetermined:
            break
        case .denied, .restricted:
            showError()
        @unknown default:
            showError()
        }
    }
}
