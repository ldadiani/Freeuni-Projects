//
//  ThreeHoursTableViewHeader.swift
//  ldadi21_final_project
//
//  Created by lizi dadiani on 02.02.24.
//

import UIKit

class ThreeHoursTableViewHeader: UITableViewHeaderFooterView {
    
    static let identifier = "ThreeHoursTableViewHeader"
    
    private let weekDays = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SETURDAY"]
    
    private let headerLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: Constants.shared.tableViewHeaderHeaderLabelFontSize, weight: .medium)
        label.textColor = .yellow
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private let constants = Constants.shared
    
    //--------------------------------------------------------------

    override init(reuseIdentifier: String?) {
        super.init(reuseIdentifier: reuseIdentifier)
        contentView.addSubview(headerLabel)
        
        activeConstraints()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) not supported")
    }
    
    public func configure(with dt_txt: String) {
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_UK") // set locale to reliable US_POSIX
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        let currentDate = dateFormatter.date(from:dt_txt)!
        let calendar = Calendar.current
        let date = calendar.dateComponents(in: .current, from: currentDate)
        let weekDay = date.weekday
        
        headerLabel.text = weekDays[(weekDay ?? 1)-1]
    }
    
    private func activeConstraints() {
        NSLayoutConstraint.activate([
            headerLabel.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            headerLabel.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: constants.tableViewHeaderHeaderLabelLeadingPadding)
        ])
    }
    
}
