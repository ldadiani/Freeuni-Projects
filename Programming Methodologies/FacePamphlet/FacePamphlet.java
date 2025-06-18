/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;

import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {
	private JButton chargeStatus;
	private JButton changePicture;
	private JButton addFriend;
	private JButton addProfile;
	private JButton deleteProfile;
	private JButton lookUp;
	private JTextField statusField;
	private JTextField pictureField;
	private JTextField addFriendField;
	private JTextField nameField;
	private JLabel name;
	private FacePamphletProfile current = null;
	private FacePamphletProfile profile = null;
	private FacePamphletCanvas canvas;
	FacePamphletDatabase base = new FacePamphletDatabase();

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvas();
		add(canvas);

		addStatusInteractors();
		addPictureInteractors();
		addFriendInteractors();
		addProfileInteractors();

		addActionListeners();
	}

	/*
	 * This void adds profile interactors, buttons and fields that we can use to run
	 * the program
	 */
	private void addProfileInteractors() {
		name = new JLabel("Name");
		add(name, NORTH);

		nameField = new JTextField(TEXT_FIELD_SIZE);
		add(nameField, NORTH);

		addProfile = new JButton("Add");
		add(addProfile, NORTH);

		deleteProfile = new JButton("Delete");
		add(deleteProfile, NORTH);

		lookUp = new JButton("LookUp");
		add(lookUp, NORTH);

	}

	/* This method adds interactors that lets us to add friends */
	private void addFriendInteractors() {
		addFriendField = new JTextField(TEXT_FIELD_SIZE);
		add(addFriendField, WEST);
		addFriendField.addActionListener(this);

		addFriend = new JButton("Add Friend");
		add(addFriend, WEST);

	}

	/* This method adds interactors that lets us to add pictures */
	private void addPictureInteractors() {

		pictureField = new JTextField(TEXT_FIELD_SIZE);
		add(pictureField, WEST);
		pictureField.addActionListener(this);

		changePicture = new JButton("Change Picture");
		add(changePicture, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

	}

	/* This method adds interactors that lets us to add status */
	private void addStatusInteractors() {
		statusField = new JTextField(TEXT_FIELD_SIZE);
		add(statusField, WEST);
		statusField.addActionListener(this);

		chargeStatus = new JButton("Chance Status");
		add(chargeStatus, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (!nameField.getText().trim().equals("")) {
			profile = new FacePamphletProfile(nameField.getText());
		}

		if (e.getSource() == chargeStatus || e.getSource() == statusField) {
			addStatus();
		} else if (e.getSource() == changePicture || e.getSource() == pictureField) {
			addImage();
		} else if (e.getSource() == addFriend || e.getSource() == addFriendField) {
			addFriends();
		} else if (e.getSource() == addProfile) {
			addProfile();
		} else if (e.getSource() == deleteProfile) {
			deleteProfile();
		} else if (e.getSource() == lookUp) {
			lookUpProfile();
		}
	}

	/*
	 * This void looks up the profile if it exists and shows the message if it does
	 * not
	 */
	private void lookUpProfile() {
		canvas.showMessage("");
		if (base.containsProfile(nameField.getText())) {
			current = base.getProfile(nameField.getText());
			canvas.displayProfile(current);
			canvas.showMessage("Displaying " + nameField.getText());
		} else {
			canvas.showMessage("This profile does not exist in a base");
		}

	}

	/*
	 * This method deletes the profile from the base if it exists and shows the
	 * message if it does not, also after deleting it clears the canvas.
	 */
	private void deleteProfile() {
		canvas.showMessage("");
		if (base.containsProfile(nameField.getText())) {
			base.deleteProfile(nameField.getText());
			canvas.showMessage(nameField.getText() + " is deleted.");
			current = null;
			canvas.removeAll();
		} else {
			canvas.showMessage("The profile for " + nameField.getText() + " does not exist.");
		}

	}

	/*
	 * With this method you can add the friend. friendship is two sided. if you are
	 * the persons friend, the person is your friend too which means that you both
	 * are in one anothers friendlists
	 */
	private void addFriends() {
		canvas.showMessage("");
		if (current != null && !addFriendField.getText().equals(current.getName())
				&& !addFriendField.getText().trim().equals("")
				&& base.containsProfile(addFriendField.getText().trim())) {
			profile = current;
			current.addFriend(addFriendField.getText());
			base.getProfile(addFriendField.getText()).addFriend(current.getName());
			canvas.displayProfile(profile);
		} else {
			if (addFriendField.getText().equals(current.getName())) {
				canvas.showMessage("you can not add  yourself as a friend");
			} else if (addFriendField.getText().trim().equals("")) {
				canvas.showMessage("you can not add empty string");
			} else if (!base.containsProfile(addFriendField.getText().trim())) {
				canvas.showMessage("You can not add " + addFriendField.getText() + " ,as profile does nor exist.");
			}
		}

	}

	/*
	 * this method adds the picture if a picture exists and shows the message if it
	 * does not
	 */
	private void addImage() {
		canvas.showMessage("");
		if (current != null) {
			GImage image = null;
			try {
				image = new GImage(pictureField.getText());
				current.setImage(image);
				canvas.displayProfile(current);
				canvas.showMessage("Image updated");
			} catch (ErrorException ex) {
				canvas.showMessage("Unable to open image file "+pictureField.getText());
			}
		} else {
			canvas.showMessage("you need profile to add the picture to it");
		}
	}

	/* This method adds the status to the curret profile */
	private void addStatus() {
		canvas.showMessage("");
		if (current != null) {
			current.setStatus(statusField.getText());
			canvas.displayProfile(current);
			canvas.showMessage("Status updated to " + current.getStatus());
		} else {
			canvas.showMessage("You need profile to set the status for it");
		}
	}

	/*
	 * This method adds the profile in the base and shows the canvas of the newly
	 * created profile
	 */
	private void addProfile() {
		canvas.showMessage("");
		if (base.containsProfile(nameField.getText())) {
			canvas.showMessage("This profile already exists");
			current = base.getProfile(nameField.getText());
			canvas.displayProfile(current);
		} else {
			canvas.displayProfile(profile);
			base.addProfile(profile);
			canvas.showMessage("New profile created.");
		}
		current = base.getProfile(nameField.getText());

	}

}
