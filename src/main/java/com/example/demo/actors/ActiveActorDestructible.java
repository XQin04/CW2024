package com.example.demo.actors;

/**
 * Represents an active actor in the game that can be destroyed.
 *
 * <p>This abstract class extends {@link ActiveActor} and implements the {@link Destructible}
 * interface, combining movement and destruction capabilities for game entities.</p>
 */
public abstract class ActiveActorDestructible extends ActiveActor implements Destructible {

	private boolean isDestroyed; // Tracks if the actor has been destroyed

	/**
	 * Constructs an ActiveActorDestructible with the specified image, size, and initial position.
	 *
	 * @param imageName   Name of the image file for the actor (e.g., "enemy.png").
	 * @param imageHeight Height of the image to be displayed, in pixels.
	 * @param initialXPos Initial X-coordinate position of the actor on the screen.
	 * @param initialYPos Initial Y-coordinate position of the actor on the screen.
	 */
	public ActiveActorDestructible(String imageName, int imageHeight, double initialXPos, double initialYPos) {
		super(imageName, imageHeight, initialXPos, initialYPos);
		this.isDestroyed = false; // Actor starts in an un-destroyed state
	}

	/**
	 * Updates the position of the actor.
	 */
	@Override
	public abstract void updatePosition();

	/**
	 * Updates the actor's state, including position, interactions, or effects.
	 */
	public abstract void updateActor();

	/**
	 * Handles damage logic for the actor.
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
