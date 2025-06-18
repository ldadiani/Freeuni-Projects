//
//  ContactsCollectionViewCell.swift
//  Assignment_Contacts_Lizi_Dadiani
//
//  Created by lizi dadiani on 15.01.24.
//

import UIKit

class ContactsCollectionViewCell: UICollectionViewCell {
    
    static let identifier = "ContactsCollectionViewCell"
        
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
        stack.alignment = .center
        return stack
    }()
    
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupCell()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        layer.borderColor = UIColor.label.cgColor
    }
    
    private func setupCell() {
        contentView.addSubview(stackView)
        stackView.addArrangedSubview(namelabel)
        stackView.addArrangedSubview(numberLabel)
        layer.borderWidth = 1
        layer.cornerRadius = 5
        applyConstraints()
    }
    
    public func configure(with model: Person) {
        namelabel.text = model.name
        numberLabel.text = model.number
    }
    
    
    private func applyConstraints() {
        let stackViewConstraints = [
            stackView.centerYAnchor.constraint(equalTo: contentView.centerYAnchor),
            stackView.centerXAnchor.constraint(equalTo: contentView.centerXAnchor)
        ]
        NSLayoutConstraint.activate(stackViewConstraints)
    }
}
