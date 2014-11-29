package de.outstare.kinosim.movie;

/**
 * A Genre of a film.
 */
public enum Genre {
	Action(GroupAspect.Mood),
	Adventure(GroupAspect.Plot),
	Animation(GroupAspect.NarrativeForm),
	Biography(GroupAspect.NarrativeForm),
	Comedy(GroupAspect.NarrativeForm),
	Crime(GroupAspect.Plot),
	Documentary(GroupAspect.NarrativeForm),
	Drama(GroupAspect.NarrativeForm),
	Fantasy(GroupAspect.Plot),
	Horror(GroupAspect.Mood),
	Music(GroupAspect.Plot),
	Mystery(GroupAspect.Mood),
	Romance(GroupAspect.Mood),
	SciFi(GroupAspect.Plot),
	Sport(GroupAspect.Plot),
	Thriller(GroupAspect.Plot),
	War(GroupAspect.Plot),
	Western(GroupAspect.Plot);

	public enum GroupAspect {
		Plot,
		Mood,
		NarrativeForm
	}

	private final GroupAspect aspect;

	private Genre(final GroupAspect aspect) {
		this.aspect = aspect;
	}

	public GroupAspect getAspect() {
		return aspect;
	}
}
