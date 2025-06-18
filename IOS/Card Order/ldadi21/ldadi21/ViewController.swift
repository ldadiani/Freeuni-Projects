import UIKit

class ViewController: UIViewController {
    
    let bigStack = UIStackView()
    let orderLabel = UILabel()
    let cardImage = UIImageView()
    let schoolCard = UILabel()
    let free = UILabel()
    let conditions = UILabel()
    let condStack = UIStackView()
    let buttonStack = UIStackView()
    let littleButtons = UIStackView()
    let digital = UIButton()
    let physical = UIButton()
    let continueButton = UIButton()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        bigStackCreate()
        constraints()
        orderFunction()
        conditonFunction()
        buttonFunction()
    }
    
    func bigStackCreate() {
        view.addSubview(bigStack)
        bigStack.axis = .vertical
        bigStack.distribution = .fillProportionally
        bigStack.spacing = 10
    }
    
    func orderFunction() {
        orderLabel.text = "ბარათის შეკვეთა"
        orderLabel.font = UIFont(name: "Helvetica", size: 22.0)
        orderLabel.textColor = UIColor.black
        
        cardImage.clipsToBounds = true
        cardImage.contentMode = .scaleAspectFit
        cardImage.image = UIImage(named: "scool", in: Bundle(for: type(of: self)), compatibleWith: nil)
        
        schoolCard.text = "sCool Card"
        schoolCard.font = UIFont(name: "Helvetica", size: 22.0)
        schoolCard.textColor = UIColor.black
        schoolCard.textAlignment = .center
        
        free.text = "ღირებულება: უფასო"
        free.font = UIFont(name: "Helvetica", size: 18.0)
        free.textColor = UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8)
        free.textAlignment = .center
        
        bigStack.addArrangedSubview(orderLabel)
        bigStack.addArrangedSubview(cardImage)
        bigStack.addArrangedSubview(schoolCard)
        bigStack.addArrangedSubview(free)
    }
    
    func conditonFunction() {
        conditions.text = "ბარათის პირობები"
        conditions.font = UIFont(name: "Helvetica", size: 22.0)
        conditions.textColor = UIColor.black
        
        let st1 = CustomConditionsStack(img: "info", uptxt: "მოქმედების ვადა", downtxt: "4 წელი",
                                        clr: UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8))
        let st2 = CustomConditionsStack(img: "cash", uptxt: "განაღდების დღიური ლიმიტი", downtxt: "500 ₾",
                                        clr: UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8))
        let st3 = CustomConditionsStack(img: "cash", uptxt: "განაღდების საკომისიო", downtxt: "სტანდარტულად 0.2%, მინ. 0.2 ლარი",
                                        clr: UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8))
        let st4 = CustomConditionsStack(img: "info", uptxt: "", downtxt: "სხვა მომსახურების პირობები",
                                        clr: UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.4))
        
        condStack.axis = .vertical
        condStack.distribution = .fillEqually
        condStack.spacing = 5
        
        condStack.addArrangedSubview(st1)
        condStack.addArrangedSubview(st2)
        condStack.addArrangedSubview(st3)
        condStack.addArrangedSubview(st4)
        bigStack.addArrangedSubview(conditions)
        bigStack.addArrangedSubview(condStack)
    }
    
    func buttonFunction() {
        buttonStack.axis = .vertical
        buttonStack.distribution = .fillEqually
        buttonStack.spacing = 15
        
        littleButtons.axis = .horizontal
        littleButtons.distribution = .fillEqually
        littleButtons.spacing = 10
        
        
        digital.setTitle("ციფრული", for: .normal)
        digital.layer.cornerRadius = 35
        digital.titleLabel?.font = UIFont(name: "Helvetica", size: 15.0)
        digital.setTitleColor(UIColor.white, for: .normal)
        digital.backgroundColor = UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8)
        
        physical.setTitle("ფიზიკური", for: .normal)
        physical.layer.cornerRadius = 35
        physical.titleLabel?.font = UIFont(name: "Helvetica", size: 15.0)
        physical.setTitleColor(UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8), for: .normal)
        physical.backgroundColor = UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.2)
        
        continueButton.setTitle("გაგრძელება", for: .normal)
        continueButton.layer.cornerRadius = 18.0
        continueButton.titleLabel?.font = UIFont(name: "Helvetica", size: 20.0)
        continueButton.setTitleColor(UIColor.white, for: .normal)
        continueButton.backgroundColor = UIColor(red: 0.0, green: 0.5, blue: 1.0, alpha: 0.8)
        
        
        littleButtons.addArrangedSubview(digital)
        littleButtons.addArrangedSubview(physical)
        buttonStack.addArrangedSubview(littleButtons)
        buttonStack.addArrangedSubview(continueButton)
        bigStack.addArrangedSubview(buttonStack)
    }
    
    func constraints() {
        bigStack.translatesAutoresizingMaskIntoConstraints = false
        bigStack.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true
        bigStack.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 10).isActive = true
        bigStack.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -10).isActive = true
        bigStack.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: 0).isActive = true
    }

}

