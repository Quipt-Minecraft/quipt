package com.quiptmc.core.entity;

import java.util.UUID;

/**
 * Represents a player in the Quipt system.
 *
 * @param name the name of the player
 * @param uuid the unique identifier of the player
 */
public record QuiptPlayer(String name, UUID uuid) {
}
