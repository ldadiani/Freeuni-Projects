//
//  ViewController.swift
//  Assignment_Contacts_Lizi_Dadiani
//
//  Created by lizi dadiani on 15.01.24.
//

import UIKit
import CoreData

class ViewController: UIViewController {
    
    // coredata
    var dbContext = DBManager.shared.persistentContainer.viewContext
    
    private var Sections: [Section] = []
        
    private var nameInBox = ""
    
    private var numberInBox = ""
    
    private var tableViewLook = true
    
    private let tableView: UITableView = {
        let table = UITableView()
        table.register(ContactsTableViewCell.self, forCellReuseIdentifier: ContactsTableViewCell.identifier)
        table.register(TableHeaderView.self, forHeaderFooterViewReuseIdentifier: TableHeaderView.identifier)
        return table
    }()
    
    private let collectioView: UICollectionView = {

        // create layout
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        let itemWidth = 100
        let itemHeight = 100
        layout.itemSize = CGSize(width: itemWidth, height: itemHeight)
        
        // create collectionView
        let collection = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collection.register(ContactsCollectionViewCell.self, forCellWithReuseIdentifier: ContactsCollectionViewCell.identifier)
        collection.register(GridHeader.self, forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: GridHeader.identifier)
        collection.isHidden = true
        collection.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        
        
        return collection
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .brown
        configureNavBar()
        view.addSubview(tableView)
        tableView.delegate = self
        tableView.dataSource = self
        collectioView.delegate = self
        collectioView.dataSource = self
        view.addSubview(collectioView)
        
    }
    
    override func viewDidAppear(_ animated: Bool) {
        fetchPeople()
    }
    
    func fetchPeople() {
        Sections = []
        for char in "abcdefghijklmnopqrstuvwxyz" {
            let request = Person.fetchRequest() as NSFetchRequest<Person>
            
            let preicate = NSPredicate(format: "name BEGINSWITH %@", "\(char)")
            let sort = NSSortDescriptor(key: "name", ascending: true)
            
            request.predicate = preicate
            request.sortDescriptors = [sort]
            do {
                let numberCells = try dbContext.fetch(request)
                if numberCells.count == 0 {continue}
                let newHeaderModel = HeaderModel(charachter: "\(char)")
                let newSectionModel = Section(headerModel: newHeaderModel, numberCells: numberCells)
                Sections.append(newSectionModel)
            } catch {
                fatalError()
            }
        }
        tableView.reloadData()
        collectioView.reloadData()
    }
    
    


    private func configureNavBar() {
        navigationItem.title = "Contacts"
        navigationController?.navigationBar.backgroundColor = .systemGray6
        
        var leftImige = UIImage(systemName: "plus")
        navigationItem.leftBarButtonItem = UIBarButtonItem(image: leftImige, style: .done, target: self, action: #selector(addContact))
        
        var rightImige = UIImage(systemName: "square.grid.3x3.fill")
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: rightImige, style: .done, target: self, action: #selector(changeLayout))
    }
    
    
    @objc func addContact() {
        // create alert
        let alert = UIAlertController(
            title: "Add Contact",
            message: "input the name and the number",
            preferredStyle: .alert
        )
        
        
        // add elements to alert
        alert.addTextField {[unowned self] textField in
            textField.placeholder = "Name"
            textField.keyboardType = .default
            textField.addTarget(self, action: #selector(self.nameInputed(textField:)), for: .editingChanged)
        }
        
        alert.addTextField {[unowned self] textField in
            textField.placeholder = "Number"
            textField.keyboardType = .numberPad
            textField.addTarget(self, action: #selector(self.numberInputed(textField:)), for: .editingChanged)
        }
        
        alert.addAction(
            UIAlertAction(
                title: "Cancel",
                style: .cancel,
                handler: { [unowned self] _ in
                    nameInBox = ""
                    numberInBox = ""
                }
            )
        )
        alert.addAction(UIAlertAction(
            title: "Add",
            style: .default,
            handler: {[unowned self] _ in
                let person = Person(context: dbContext)
                person.name = nameInBox.lowercased()
                person.number = numberInBox
                do {
                    try dbContext.save()
                    fetchPeople()
                } catch {}
            }
        )
        )
        present(alert, animated: true)
    }
    
    
    
    private func takeFirstCharacter(from word: String) -> String {
        let result = word.first!.uppercased()
        return "\(result)"
    }
    
    @objc func nameInputed(textField: UITextField) {
        nameInBox = textField.text ?? ""
    }
    
    @objc func numberInputed(textField: UITextField) {
        numberInBox = textField.text ?? ""
    }
    
    @objc func changeLayout() {
        if tableViewLook {
            tableViewLook.toggle()
            navigationItem.rightBarButtonItem?.image = UIImage(systemName: "line.3.horizontal")
            tableView.isHidden = true
            collectioView.isHidden = false
            fetchPeople()
        } else {
            tableViewLook.toggle()
            navigationItem.rightBarButtonItem?.image = UIImage(systemName: "square.grid.3x3.fill")
            collectioView.isHidden = true
            tableView.isHidden = false
            fetchPeople()
        }
    }
    
    
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        tableView.frame = view.bounds
        collectioView.frame = view.bounds
    }
}






extension ViewController: UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return Sections.count
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if !Sections[section].isApended {
            return 0
        }
        return Sections[section].numberCells.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        guard let cell = tableView.dequeueReusableCell(withIdentifier: ContactsTableViewCell.identifier) as? ContactsTableViewCell else {
            return UITableViewCell()
        }
        
        cell.namelabel.text = Sections[indexPath.section].numberCells[indexPath.row].name ?? "Error"
        cell.numberLabel.text = Sections[indexPath.section].numberCells[indexPath.row].number ?? "Error"
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        guard let header = tableView.dequeueReusableHeaderFooterView(withIdentifier: TableHeaderView.identifier) as? TableHeaderView else {
            return UITableViewHeaderFooterView()
        }
        
        var model = HeaderModel(charachter: Sections[section].headerModel.charachter)
        model.isAppended = Sections[section].headerModel.isAppended
        header.configure(with: model)
        header.delegate = self
        return header
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return 44
    }
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        let delete = UIContextualAction(style: .destructive, title: "Delete", handler: {[unowned self] _,_,_ in
            deleteCell(at: indexPath)
        })
        
        let configuration = UISwipeActionsConfiguration(actions: [delete])
        return configuration
    }
    
    private func deleteCell(at indexPath: IndexPath) {
        // delete from coreData
        let person = Sections[indexPath.section].numberCells[indexPath.row]
        dbContext.delete(person)

        do {
            try dbContext.save()
           fetchPeople()
        } catch {}
        
//        if data[indexPath.section].numberCells.count > 1 {
//            let index = indexPath.row
//            data[indexPath.section].numberCells.remove(at: index)
//            tableView.deleteRows(at: [indexPath], with: .automatic)
//            collectioView.deleteItems(at: [indexPath])
//        } else {
//            let index = indexPath.section
//            data.remove(at: index)
//            tableView.deleteSections(IndexSet(integer: index), with: .automatic)
//            collectioView.deleteSections(IndexSet(integer: index))
//        }
    }
    
    @objc func handleDelete(longpressGestureRecognizer: UILongPressGestureRecognizer) {
        if (longpressGestureRecognizer.state == .began) {
            let location = longpressGestureRecognizer.location(in: collectioView)
            if let indexPath = collectioView.indexPathForItem(at: location) {
                let alert = UIAlertController(
                           title: "Delete?",
                           message: "sure about delete? \(Sections[indexPath.section].numberCells[indexPath.row].name ?? "Error!") from your contacts?",
                           preferredStyle: .actionSheet
                       )
               
                       alert.addAction(UIAlertAction(
                           title: "Delete",
                           style: .destructive,
                           handler:  {[unowned self] _ in
               
                               
                               let person = Sections[indexPath.section].numberCells[indexPath.row]
                               dbContext.delete(person)

                               do {
                                   try dbContext.save()
                                  fetchPeople()
                               } catch {}
//                               if data[indexPath.section].numberCells.count > 1 {
//                                   let index = indexPath.row
//                                   data[indexPath.section].numberCells.remove(at: index)
//                                   tableView.deleteRows(at: [indexPath], with: .automatic)
//                                   collectioView.deleteItems(at: [indexPath])
//                               } else {
//                                   let index = indexPath.section
//                                   data.remove(at: index)
//                                   tableView.deleteSections(IndexSet(integer: index), with: .automatic)
//                                   collectioView.deleteSections(IndexSet(integer: index))
//                               }
                           }
                       )
                       )
               
                       alert.addAction(UIAlertAction(title: "Cancel", style: .default))
               
                       present(alert, animated: true)
            }
        }
    }
    
}

extension ViewController: TableHeaderViewDelegate {
    func didTapAppendButton(header: TableHeaderView, model: HeaderModel) {
        
        for sectionIndex in 0...Sections.count-1 {
            if Sections[sectionIndex].headerModel.charachter.uppercased() == model.charachter {
                Sections[sectionIndex].isApended.toggle()
                Sections[sectionIndex].headerModel.isAppended.toggle()
                header.configure(with: Sections[sectionIndex].headerModel)
                tableView.reloadSections(IndexSet(integer: sectionIndex), with: .automatic)
                collectioView.reloadSections(IndexSet(integer: sectionIndex))
                return
            }
        }
    }
}


extension ViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return Sections.count
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if !Sections[section].isApended {
            return 0
        }
        return Sections[section].numberCells.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ContactsCollectionViewCell.identifier, for: indexPath) as? ContactsCollectionViewCell else {
            return UICollectionViewCell()
            
        }
    
        cell.addGestureRecognizer(UILongPressGestureRecognizer(target: self, action: #selector(handleDelete)))
        
        
        let cellModel = Sections[indexPath.section].numberCells[indexPath.row]
        cell.namelabel.text = cellModel.name
        cell.numberLabel.text = cellModel.number
        return cell
    }
    
    
    
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        guard let header = collectionView.dequeueReusableSupplementaryView(ofKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: GridHeader.identifier, for: indexPath) as? GridHeader else {
            return UICollectionReusableView()
        }
        
        var model = HeaderModel(charachter: Sections[indexPath.section].headerModel.charachter)
        model.isAppended = Sections[indexPath.section].headerModel.isAppended
        header.configure(with: model)
        header.delegate = self
        return header
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        CGSize(width: collectionView.frame.width, height: 44)
    }
}



extension ViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        if Sections[section].isApended {
            return UIEdgeInsets(top: 20, left: 20, bottom: 20, right: 20)
        } else {
            return UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 100, height: 100)
    }
}



extension ViewController: GridHeaderDelegate {
    func didTapAppendButton(header: GridHeader, model: HeaderModel) {
        for sectionIndex in 0...Sections.count-1 {
            if Sections[sectionIndex].headerModel.charachter.uppercased() == model.charachter {
                Sections[sectionIndex].isApended.toggle()
                Sections[sectionIndex].headerModel.isAppended.toggle()
                header.configure(with: Sections[sectionIndex].headerModel)
                tableView.reloadSections(IndexSet(integer: sectionIndex), with: .automatic)
                collectioView.reloadSections(IndexSet(integer: sectionIndex))
                return
            }
        }
    }
}


