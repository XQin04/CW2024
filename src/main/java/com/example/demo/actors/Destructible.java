package com.example.demo.actors;

/**
 * Represents an entity that can take damage and be destroyed.
 * Classes implementing this interface must define how the entity takes damage and gets destroyed.
 */
public interface Destructible {

	/**
	 * Applies damage to the entity. The specific behavior (e.g., reducing health)
	 * must be implemented by the class.
	 */
	void takeDamage();

	/**
	 * Destroys the entity. The specific behavior (e.g., removing it from the game)
	 * must be implemented by the class.
	 */
	void destroy();
}
