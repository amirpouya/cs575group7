package dragonbids.structures.listings;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import dragonbids.api.ListingSkeleton;
import java.util.*;
import java.time.*;
import java.io.Serializable;

/**
 * @author Lew Cannalongo
 * @version 11-21-16
 */
public abstract class Listing extends Observable implements Serializable {
	String title;
	String description;
	int listingID;
	long currentPrice;
	LocalDateTime createDate;
	LocalDateTime expirationDate;
	String creatorID;
	Object photo;
	private static final long serialVersionUID = 856;

	// using a variety of setProperty() methods allows a proxy to easily
	// manipulate a Listing

	public Listing(int listingUID, String creatorID, String title, String description, Object photo, int startingPrice,
			int duration) {
		this.createDate = new Timestamp(new java.util.Date().getTime()).toLocalDateTime();
		this.listingID = listingUID;
		this.creatorID = creatorID;

		setExpiration(createDate.plusMinutes(duration));

		setStartingPrice(startingPrice);
		setTitle(title);
		setDesc(description);
		setPhoto(photo);

	}
	
	// setter methods. consider making PROTETECTED rather than PUBLIC and moving Handler classes into Listing package


	public void setExpiration(LocalDateTime endingTime) {
		expirationDate = endingTime;
		setChanged();
		notifyObservers(endingTime);
	}

	public void setStartingPrice(long price) {
		currentPrice = price;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDesc(String desc) {
		this.description = desc;
	}

	public void setPhoto(Object photo) {
		this.photo = photo;
	}
	
	@Override
	public String toString() {
		return this.getClass() + " // Listing ID: " + listingID + " // " + this.title;
	}

	// getter methods
		
	public final String getDesc() {
		return description;
	}

	public final String getTitle() {
		return title;
	}
	
	public final long getCurrentPrice()
	{
		return currentPrice;
	}
	
	public final long timeLeft() {
		return Duration.between(LocalDateTime.now(), expirationDate).toMinutes();
	}

	public final LocalDateTime getExpiration() {
		return expirationDate;
	}

	public final ListingSkeleton extractSkeleton() throws RemoteException {
		ListingSkeleton skele = new ListingSkeleton();
		skele.auctionDescription = this.description;
		skele.auctionTile = this.title;
		skele.sellerUsername = this.creatorID;
		skele.listingId = this.listingID;
		skele.currentPrice = this.currentPrice;
		skele.auctionCompletionDateTime = this.expirationDate;

		return skele;
	}

}