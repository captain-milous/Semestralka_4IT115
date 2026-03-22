package cz.vse.semestralka_4it115.main;

/**
 * Data holder for one row in inventory/room item list.
 *
 * @param name item display name
 * @param weight item weight in kilograms
 * @param placeholder true for non-selectable placeholder rows
 */
public record ItemListEntry(String name, double weight, boolean placeholder) {
}
