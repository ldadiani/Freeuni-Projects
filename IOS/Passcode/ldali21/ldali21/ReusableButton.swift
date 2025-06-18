import UIKit

protocol ReusableButtonDelegate: AnyObject {
    func buttonTapped()
}

class ReusableButton: UIButton {
    
    weak var delegate: ReusableButtonDelegate?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        createButton()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        createButton()
    }
    
    private func createButton() {
        setTitleColor(.black, for: .normal)
        backgroundColor = UIColor(white: 0.9, alpha: 1)
        layer.cornerRadius = 20
        addTarget(self, action: #selector(buttonTapped), for: .touchUpInside)
    }
    
    @objc private func buttonTapped() {
        delegate?.buttonTapped()
    }
}
