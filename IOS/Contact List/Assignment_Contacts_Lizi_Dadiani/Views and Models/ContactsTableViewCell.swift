//
//  ContactsTableViewCell.swift
//  Assignment_Contacts_Lizi_Dadiani
//
//  Created by lizi dadiani on 15.01.24.
//

import UIKit

class ContactsTableViewCell: UITableViewCell {
    
    static let identifier = "ContactsTableViewCell"
    
    let namelabel: UILabel = {
        let label = UILabel()
        return label
    }()
    
    let numberLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 16, weight: .bold)
        return label
    }()

    private let stackView: UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.translatesAutoresizingMaskIntoConstraints = false
        return stack
    }()
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        setupCell()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupCell() {
        contentView.addSubview(stackView)
        stackView.addArrangedSubview(namelabel)
        stackView.addArrangedSubview(numberLabel)
        applyConstraints()
    }
    
    
    public func configure(with model: Person) {
        namelabel.text = model.name
        numberLabel.text = model.number
    }
    
    
    private func applyConstraints() {
        let stackViewConstraints = [
            stackView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            stackView.leadingAnchor.constraint(equalTo: contentView.leadingAnchor, constant: 20)
        ]
        NSLayoutConstraint.activate(stackViewConstraints)
    }
}
