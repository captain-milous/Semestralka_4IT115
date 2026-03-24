package cz.vse.semestralka_4it115.main;

/**
 * Represents one item row shown in inventory and room item lists.
 *
 * @param name displayed item name
 * @param weight item weight in kilograms
 * @param placeholder indicates non-interactive placeholder row
 * @author Miloš Tesař
 * @version 1.0.0
 * @since 2026-03-23
 */
public record ItemListEntry(String name, double weight, boolean placeholder) {
}
