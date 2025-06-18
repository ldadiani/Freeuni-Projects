import UIKit

class CustomConditionsStack: UIStackView {
    
    init(img: String, uptxt: String, downtxt: String, clr: UIColor) {
        super.init(frame: .zero)
        stackFunction(img: img, uptxt: uptxt, downtxt: downtxt, clr: clr)
    }
    
    required init(coder: NSCoder) {
        super.init(coder: coder)
        stackFunction(img: "", uptxt: "", downtxt: "", clr: .black)
    }
    
    func stackFunction(img: String, uptxt: String, downtxt: String, clr: UIColor) {
        
        let condImage = UIImageView(image: UIImage(named: img))
        condImage.image = condImage.image?.withRenderingMode(.alwaysTemplate)
        condImage.tintColor = clr
        
        let upText = UILabel()
        upText.text = uptxt
        upText.font = UIFont(name: "Helvetica", size: 13.0)
        upText.textColor = UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.3)
        
        let downText = UILabel()
        downText.text = downtxt
        downText.font = UIFont(name: "Helvetica", size: 15.0)
        downText.textColor = UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8)
        
        let littleStack = UIStackView()
        littleStack.axis = .vertical
        littleStack.distribution = .fillEqually
        littleStack.spacing = 0
        
        littleStack.addArrangedSubview(upText)
        littleStack.addArrangedSubview(downText)
                
        axis = .horizontal
        spacing = 5
        
        addArrangedSubview(condImage)
        addArrangedSubview(littleStack)
        
        condImage.widthAnchor.constraint(equalToConstant: 50).isActive = true
        condImage.heightAnchor.constraint(equalToConstant: 50).isActive = true
    }
    
    
}
