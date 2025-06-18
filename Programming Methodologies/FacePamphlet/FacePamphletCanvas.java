/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;


import java.awt.Image;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {

	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	GLabel message;

	public void showMessage(String msg) {
		if (message != null) {
			remove(message);
		}
		message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		add(message, (getWidth() - message.getWidth()) / 2, getHeight() - message.getHeight() - BOTTOM_MESSAGE_MARGIN);

	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {

		addName(profile.getName());
		addImage(profile.getImage());
		addStatus(profile.getName(), profile.getStatus());
		addFriendsLabel(profile.getFriendsList());
	}

	private ArrayList<GLabel> friendList = new ArrayList<GLabel>();
	private GLabel friendLabel;

	/* This method adds the friendlist to the canvas */
	private void addFriendsLabel(ArrayList<String> arrayList) {
		friendLabel = new GLabel("Friends:");
		friendLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
		add(friendLabel, getWidth() / 2, imagerect.getY());

		if (friendList != null) {
			Iterator<GLabel> delete = friendList.iterator();
			while (delete.hasNext()) {
				remove(delete.next());
			}
		}

		int PROFILE_FRIEND_LIST_SPACING = 20;
		Iterator<String> iterator = arrayList.iterator();
			if (iterator.hasNext()) {
				for (int i = 0; i < arrayList.size(); i++) {
					String nextFriend = iterator.next();
					GLabel FriendLabel = new GLabel(nextFriend);
					FriendLabel.setFont(PROFILE_FRIEND_LABEL_FONT);
					add(FriendLabel, getWidth() / 2,
							imagerect.getY() + friendLabel.getHeight() + i * PROFILE_FRIEND_LIST_SPACING);
					friendList.add(FriendLabel);
				}
				
		}
	}

	private GLabel status;

	/* This method adds the status on the canvas */
	private void addStatus(String profName, String profStatus) {
		if (status != null) {
			remove(status);
		}
		if (profStatus.trim().equals("")) {
			status = new GLabel("No current status");
			status.setFont(PROFILE_STATUS_FONT);
			add(status, LEFT_MARGIN, TOP_MARGIN + name.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + 2 * STATUS_MARGIN);
		} else {
			status = new GLabel(profName + " is " + profStatus);
			status.setFont(PROFILE_STATUS_FONT);
			add(status, LEFT_MARGIN, TOP_MARGIN + name.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + 2 * STATUS_MARGIN);
		}
	}

	private GImage gimage;

	private GRect imagerect;

	/*
	 * This method adds the image space with the message "no image" and adds the
	 * image if the profile has already added it
	 */
	private void addImage(GImage profImage) {
		if (gimage != null)
			remove(gimage);

		imagerect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
		add(imagerect, LEFT_MARGIN, TOP_MARGIN + name.getHeight() + IMAGE_MARGIN);

		GLabel imageLabel = new GLabel("NO IMAGE");
		imageLabel.setFont(PROFILE_IMAGE_FONT);
		add(imageLabel, imagerect.getX() + imagerect.getWidth() / 2 - imageLabel.getWidth() / 2,
				imagerect.getY() + imagerect.getHeight() / 2);

		if (profImage != null) {
			gimage = profImage;
			gimage.setBounds(imagerect.getX(), imagerect.getY(), IMAGE_WIDTH, IMAGE_HEIGHT);
			add(gimage);
		}

	}

	private GLabel name;

	/* This method adds the name label to the canvas */
	private void addName(String profName) {
		if (name != null)
			remove(name);
		name = new GLabel(profName);
		name.setFont(PROFILE_NAME_FONT);
		add(name, LEFT_MARGIN, 3 * TOP_MARGIN);

	}

}
