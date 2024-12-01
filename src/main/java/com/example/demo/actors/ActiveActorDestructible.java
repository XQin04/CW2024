package com.example.demo.actors;

/**
 * Represents an active actor in the game that can be destroyed.
 * This abstract class extends ActiveActor and implements the Destructible interface.
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed; // Tracks if the actor has been destroyed

	/**
	 * Constructs an ActiveActorDestructible with the specified image, size, and initial position.
	 *
	 * @param imageName   Name of the image file for the actor.
	 * @param imageHeight Height of the image to be displayed.
	 * @param initialXPos Initial X position of the actor.
	 * @param initialYPos Initial Y position of the actor.
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.isDestroyed = false; // Actor starts in an un-destroyed state
	}

	/**
	 * Updates the position of the actor. Subclasses must define specific behavior.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the actor's state, including position, interactions, or effects.
	 * Subclasses must implement this to define behavior.
	 */
	public abstract void updateActor();

	/**
	 * Handles damage logic for the actor. Subclasses must implement specific damage behavior.
	 */
	@Override
	public abstract void takeDamage();

	/**
	 * Destroys the actor by marking it as destroyed.
	 */
	@Override
	public void destroy() {
		setDestroyed(true);
	}

	/**
	 * Marks the actor as destroyed or un-destroyed.
	 *
	 * @param isDestroyed True if the actor is destroyed, false otherwise.
	 */
	protected void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}

	/**
	 * Checks if the actor is destroyed.
	 *
	 * @return True if the actor is destroyed, false otherwise.
	 */
	public boolean isDestroyed() {
		return isDestroyed;
	}
}
