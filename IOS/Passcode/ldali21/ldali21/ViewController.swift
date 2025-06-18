import UIKit

class ViewController: UIViewController, ReusableButtonDelegate {
    func buttonTapped() {
        let seconVC = FaceIDClass()
        navigationController?.pushViewController(seconVC, animated: true)
    }

    var screenStack = UIStackView()
    var bogImage = UIImageView()
    var smallStack = UIStackView()
    var bigStack = UIStackView()
    var passcodeButton = ReusableButton()
    var passcodeLabel = UILabel()
    var dotStack = UIStackView()
    var numberStack = UIStackView()
    var firstDot = UIView()
    var secondDot = UIView()
    var thirdDot = UIView()
    var fourthDot = UIView()
    var smallImage = UIImageView()
    var labelStack = UIStackView()
    var smallButton = UIButton()
    var userLabel = UILabel()
    var usernameLabel = UILabel()
    var numberArray: [ReusableButton] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        createView()
    }
    
    func createView() {
        view.backgroundColor = .lightGray
        let leftImage = UIImage(named: "Lang")
        let rightImage = UIImage(named: "Chat")
        let leftButton = UIButton()
        leftButton.setImage(leftImage, for: .normal)
        leftButton.setTitle("English", for: .normal)
        let rightButton = UIButton()
        rightButton.setImage(rightImage, for: .normal)
        navigationItem.leftBarButtonItem = UIBarButtonItem(customView: leftButton)
        navigationItem.rightBarButtonItem = UIBarButtonItem(customView: rightButton)
        viewConst()
        createScreenStack()
    }
    
    func viewConst() {
        view.addSubview(screenStack)
        screenStack.translatesAutoresizingMaskIntoConstraints = false
        screenStack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor).isActive = true
        screenStack.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 15).isActive = true
        screenStack.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -15).isActive = true
        screenStack.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor).isActive = true
    }
    
    func createScreenStack() {
        screenStack.axis = .vertical
        screenStack.distribution = .equalSpacing
        bogImage.image = UIImage(named: "BOG")
        bogImage.contentMode = .scaleAspectFit
        bogImage.transform = CGAffineTransform(scaleX: 0.5, y: 0.5)
        createSmallStack()
        createBigStack()
        passcodeButton.setTitle("დაგავიწყდა პასკოდი?", for: .normal)
        passcodeButton.setTitleColor(.orange, for: .normal)
        passcodeButton.titleLabel?.font = UIFont.systemFont(ofSize: UIFont.labelFontSize, weight: .bold)
        passcodeButton.backgroundColor = .clear
        passcodeButton.delegate = self
        screenStackConst()
    }
    
    func screenStackConst() {
        screenStack.addArrangedSubview(bogImage)
        screenStack.addArrangedSubview(smallStack)
        screenStack.addArrangedSubview(bigStack)
        screenStack.addArrangedSubview(passcodeButton)
        bogImage.heightAnchor.constraint(equalTo: screenStack.heightAnchor, multiplier: 0.2).isActive = true
        smallStack.heightAnchor.constraint(equalTo: screenStack.heightAnchor, multiplier: 0.1).isActive = true
        bigStack.heightAnchor.constraint(equalTo: screenStack.heightAnchor, multiplier: 0.6).isActive = true
        passcodeButton.heightAnchor.constraint(equalTo: screenStack.heightAnchor, multiplier: 0.05).isActive = true
    }
    
    func createSmallStack() {
        smallStack.backgroundColor = .white
        smallStack.axis = .horizontal
        smallStack.distribution = .equalSpacing
        smallStack.layer.cornerRadius = 15
        smallImage.image = UIImage(named: "User")
        smallImage.contentMode = .scaleAspectFit
        smallImage.image = smallImage.image?.withRenderingMode(.alwaysTemplate)
        smallImage.tintColor = UIColor.orange
        userLabel.text = "მომხმარებელი"
        usernameLabel.text = "Liza Dadiani"
        labelStack.axis = .vertical
        labelStack.distribution = .fillEqually
        smallButton.setTitle("შეცვლა", for: .normal)
        smallButton.setTitleColor(.black, for: .normal)
        smallButton.titleLabel?.font = UIFont.boldSystemFont(ofSize: 15)
        smallButton.backgroundColor = UIColor(white: 0.9, alpha: 1)
        smallButton.layer.cornerRadius = 20
        labelStack.addArrangedSubview(userLabel)
        labelStack.addArrangedSubview(usernameLabel)
        smallStackConst()
    }
        
    func smallStackConst() {
        smallStack.addArrangedSubview(smallImage)
        smallStack.addArrangedSubview(labelStack)
        smallStack.addArrangedSubview(smallButton)
        smallImage.widthAnchor.constraint(equalTo: smallStack.widthAnchor, multiplier: 0.2).isActive = true
        labelStack.widthAnchor.constraint(equalTo: smallStack.widthAnchor, multiplier: 0.5).isActive = true
        smallButton.widthAnchor.constraint(equalTo: smallStack.widthAnchor, multiplier: 0.2).isActive = true
        smallButton.topAnchor.constraint(equalTo: smallStack.topAnchor, constant: 15).isActive = true
        smallButton.bottomAnchor.constraint(equalTo: smallStack.bottomAnchor, constant: -15).isActive = true
        labelStack.topAnchor.constraint(equalTo: smallStack.topAnchor).isActive = true
    }
    
    func createBigStack() {
        bigStack.axis = .vertical
        bigStack.distribution = .equalSpacing
        bigStack.backgroundColor = .white
        bigStack.layer.cornerRadius = 20
        passcodeLabel.text = "ჩაწერეთ მობილბანკის პასკოდი"
        passcodeLabel.textAlignment = .center
        createDotStack()
        createNumberStack()
        bigStackConst()
    }
    
    
    func bigStackConst() {
        bigStack.addArrangedSubview(passcodeLabel)
        bigStack.addArrangedSubview(dotStack)
        bigStack.addArrangedSubview(numberStack)
        passcodeLabel.heightAnchor.constraint(equalTo: bigStack.heightAnchor, multiplier: 0.1).isActive = true
        dotStack.heightAnchor.constraint(equalTo: bigStack.heightAnchor, multiplier: 0.05).isActive = true
        numberStack.heightAnchor.constraint(equalTo: bigStack.heightAnchor, multiplier: 0.8).isActive = true
        dotStack.leadingAnchor.constraint(equalTo: bigStack.leadingAnchor, constant: 100).isActive = true
        dotStack.trailingAnchor.constraint(equalTo: bigStack.trailingAnchor, constant: -100).isActive = true
        passcodeLabel.leadingAnchor.constraint(equalTo: bigStack.leadingAnchor).isActive = true
        passcodeLabel.trailingAnchor.constraint(equalTo: bigStack.trailingAnchor).isActive = true
    }
    
    func createDotStack() {
        dotStackConst()
        dotStack.axis = .horizontal
        dotStack.distribution = .equalSpacing
        firstDot.backgroundColor = .lightGray
        secondDot.backgroundColor = .lightGray
        thirdDot.backgroundColor = .lightGray
        fourthDot.backgroundColor = .lightGray
        firstDot.layer.cornerRadius = 5
        secondDot.layer.cornerRadius = 5
        thirdDot.layer.cornerRadius = 5
        fourthDot.layer.cornerRadius = 5
    }
    
    func dotStackConst() {
        dotStack.addArrangedSubview(firstDot)
        dotStack.addArrangedSubview(secondDot)
        dotStack.addArrangedSubview(thirdDot)
        dotStack.addArrangedSubview(fourthDot)
        firstDot.widthAnchor.constraint(equalTo: dotStack.heightAnchor).isActive = true
        secondDot.widthAnchor.constraint(equalTo: dotStack.heightAnchor).isActive = true
        thirdDot.widthAnchor.constraint(equalTo: dotStack.heightAnchor).isActive = true
        fourthDot.widthAnchor.constraint(equalTo: dotStack.heightAnchor).isActive = true
    }
    
    func createNumberStack() {
        for i in 0...11 {
            let currButton = ReusableButton()
            if((i>=0 && i<=8) || i==10) {
                currButton.setTitle("\(i+1)", for: .normal)
            } else if (i==9) {
                let faceID = UIImage(named: "FaceID")
                currButton.setImage(faceID, for: .normal)
            } else if (i==11) {
                let delete = UIImage(named: "Delete")
                currButton.setImage(delete, for: .normal)
            }
            
            numberArray.append(currButton)
        }
        numberStack.axis = .vertical
        numberStack.distribution = .fillEqually
        numberStack.spacing = 20
        for i in 0...3 {
            let horizontalStack = UIStackView()
            horizontalStack.axis = .horizontal
            horizontalStack.distribution = .fillEqually
            horizontalStack.spacing = 20
            for j in 0...2 {
                let currButton = numberArray[i * 3 + j]
                currButton.delegate = self
                horizontalStack.addArrangedSubview(currButton)
            }
            numberStack.addArrangedSubview(horizontalStack)
        
        }
        
    }

}

