package de.joblift.service.gitlabpanorama.core.models;

/**
 * Available states in Gitlab
 */
public enum Status {

	// active
	running,
	pending,

    // unclear what this state means
	manual,

	// sleeping
	success,
	failed,
	canceled,
	skipped;

}
