//
//  APICaller.swift
//  ldadi21_final_project
//
//  Created by lizi dadiani on 01.02.24.
//

import Foundation

class APICaller {
    
    static let shared = APICaller()
    private let constant = Constants.shared
    
    func getCurrentWeatherWithCityName(at city: String, completion: @escaping (Result<CurrentWeatherModel, Error>) -> Void) {
        guard let url = URL(string:"https://api.openweathermap.org/data/2.5/weather?q=\(city)&appid=\(constant.API_KEY)&units=metric") else { return }
        let request = URLRequest(url: url)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else { return }
            do {
                let result = try JSONDecoder().decode(CurrentWeatherModel.self, from: data)
                completion(.success(result))
            } catch {
                print("API Call Error")
                completion(.failure(error))
            }
        }
        task.resume()
    }
    
    
    func getCurrentWeatherWithCoordinates(lon: Double, lat: Double, completion: @escaping (Result<CurrentWeatherModel, Error>) -> Void) {
        guard let url = URL(string: "https://api.openweathermap.org/data/2.5/weather?lat=\(lat)&lon=\(lon)&appid=\(constant.API_KEY)&units=metric") else { return }
        let request = URLRequest(url: url)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else { return }
            do {
                let result = try JSONDecoder().decode(CurrentWeatherModel.self, from: data)
                completion(.success(result))
            } catch {
                completion(.failure(error))
            }
        }
        task.resume()
    }
    
    
    
    func getFiveDayThreeHoursWeather(at city: String, completion: @escaping (Result<HourlyWeatherModel, Error>) -> Void) {
        guard let url = URL(string:"https://api.openweathermap.org/data/2.5/forecast?q=\(city)&appid=\(constant.API_KEY)&units=metric") else { return }
        let request = URLRequest(url: url)
        let task = URLSession.shared.dataTask(with: request) { data, response, error in
            guard let data = data, error == nil else { return }
            do {
                let result = try JSONDecoder().decode(HourlyWeatherModel.self, from: data)
                completion(.success(result))
            } catch {
                print("API Call Error")
                completion(.failure(error))
            }
        }
        task.resume()
    }
}
