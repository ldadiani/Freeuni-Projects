//
//  GridHeader.swift
//  Assignment_Contacts_Lizi_Dadiani
//
//  Created by lizi dadiani on 15.01.24.
//

import UIKit

protocol GridHeaderDelegate: AnyObject {
    func didTapAppendButton(header: GridHeader, model: HeaderModel)
}

class GridHeader: UICollectionReusableView {
    
    static let identifier = "GridHeaderHeader"
    
    
    private let characterLabel: UILabel = {
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        label.font = .systemFont(ofSize: 18, weight: .regular)
        return label
    }()
    
    var appendButton: UIButton = {
        let button = UIButton()
        button.setTitleColor(.systemBlue, for: .normal)
        button.setTitle("Collapse", for: .normal)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    weak var delegate: GridHeaderDelegate?
    
    var isAppended = true
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupheader()
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    private func setupheader() {
        backgroundColor = .systemGray5
        addSubview(characterLabel)
        addSubview(appendButton)
        appendButton.addTarget(self, action: #selector(handleTapButton), for: .touchUpInside)
        applyConstraints()
    }
    
    @objc func handleTapButton(){
        let model = HeaderModel(charachter: characterLabel.text ?? "wrong!")
        self.delegate?.didTapAppendButton(header: self, model: model)
    }
    
    public func configure(with model: HeaderModel) {
        characterLabel.text = model.charachter.uppercased()
        
        if model.isAppended {
            appendButton.setTitle("Collapse", for: .normal)
        } else {
            appendButton.setTitle("Append", for: .normal)
        }
    }
    
    private func applyConstraints() {
        let characterLabelConstraints = [
            characterLabel.leadingAnchor.constraint(equalTo: leadingAnchor, constant: 25),
            characterLabel.centerYAnchor.constraint(equalTo: centerYAnchor)
        ]
        
        let appendButtonConstraints = [
            appendButton.trailingAnchor.constraint(equalTo: trailingAnchor, constant: -20),
            appendButton.centerYAnchor.constraint(equalTo: centerYAnchor)
        ]
        
        NSLayoutConstraint.activate(appendButtonConstraints)
        NSLayoutConstraint.activate(characterLabelConstraints)
    }
}
