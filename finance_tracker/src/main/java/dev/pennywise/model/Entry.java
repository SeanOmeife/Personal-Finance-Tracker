package main.java.dev.pennywise.model;


/*
 * Sealed interface to demonstrate sealed classes/interfaces.
 * It permits the abstract BaseTransaction which is non-sealed,
 * allowing further subclassing in this module.
 */
public sealed interface Entry permits BaseTransaction{ }