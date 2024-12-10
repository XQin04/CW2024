package com.example.demo.actors;

/**
 * Represents an entity that can take damage and be destroyed.
 *
 * <p>Classes implementing this interface must provide concrete implementations
 * for handling damage application and destruction behavior.</p>
 */
public interface Destructible {

	/**
	 * Applies damage to the entity.
	 *
	 */
	void takeDamage();

	/**
	 * Destroys the entity.
	 *
	 */
	void destroy();
}
