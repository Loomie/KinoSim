package de.outstare.kinosim.movie.popularity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;
import de.outstare.kinosim.movie.popularity.Audience;
import de.outstare.kinosim.movie.popularity.MoviePopularity;

public class MoviePopularityTest {

    @Test
    public void testGetPopularityMin() {
	final MoviePopularity testObject = new MoviePopularity(createRating(0, 0, 0, 0, 0));
	double result;
	// for (final Audience a : Audience.values()) {
	// result = testObject.getPopularity(a);
	// System.out.println(a + " " + result);
	// }
	result = testObject.getPopularity(Audience.ADULTS);
	assertEquals(-0.390837117337661, result, 0);
	result = testObject.getPopularity(Audience.KIDS);
	assertEquals(0.5468341864356077, result, 0);
	result = testObject.getPopularity(Audience.SENIORS);
	assertEquals(-0.2572372751252366, result, 0);
	result = testObject.getPopularity(Audience.TEENS);
	assertEquals(0.39721359549995794, result, 0);
	result = testObject.getPopularity(Audience.TWENS);
	assertEquals(-0.3999423734377209, result, 0);
    }

    @Test
    public void testGetPopularityMax() {
	final int max = Rating.MAX_VALUE;
	final MoviePopularity testObject = new MoviePopularity(createRating(max, max, max, max, max));
	double result;
	result = testObject.getPopularity(Audience.ADULTS);
	assertEquals(0.390837117337661, result, 0);
	result = testObject.getPopularity(Audience.KIDS);
	assertEquals(0.15225424859373685, result, 0);
	result = testObject.getPopularity(Audience.SENIORS);
	assertEquals(0.34540506296910767, result, 0);
	result = testObject.getPopularity(Audience.TEENS);
	assertEquals(1.2246467991473533E-17, result, 0);
	result = testObject.getPopularity(Audience.TWENS);
	assertEquals(0.399942373437721, result, 0);
    }

    @Test
    public void testGetPopularityMedium() {
	final int medium = Rating.MAX_VALUE / 2;
	final MoviePopularity testObject = new MoviePopularity(createRating(medium, medium, medium, medium, medium));
	double result;
	result = testObject.getPopularity(Audience.ADULTS);
	assertEquals(0.9116876901488806, result, 0);
	result = testObject.getPopularity(Audience.KIDS);
	assertEquals(0.4429130110463495, result, 0);
	result = testObject.getPopularity(Audience.SENIORS);
	assertEquals(0.8441887218542157, result, 0);
	result = testObject.getPopularity(Audience.TEENS);
	assertEquals(0.8518638338353186, result, 0);
	result = testObject.getPopularity(Audience.TWENS);
	assertEquals(0.8504734520300096, result, 0);
    }

    @Test
    public void testGetPopularityPerfect() {
	MoviePopularity testObject;
	double result;
	testObject = new MoviePopularity(createRating(60, 67, 70, 60, 60));
	result = testObject.getPopularity(Audience.ADULTS);
	assertEquals(1.0, result, 0);

	testObject = new MoviePopularity(createRating(10, 60, 80, 10, 20));
	result = testObject.getPopularity(Audience.KIDS);
	assertEquals(1.0, result, 0);

	testObject = new MoviePopularity(createRating(70, 80, 50, 30, 70));
	result = testObject.getPopularity(Audience.SENIORS);
	assertEquals(1.0, result, 0);

	testObject = new MoviePopularity(createRating(20, 40, 40, 50, 40));
	result = testObject.getPopularity(Audience.TEENS);
	assertEquals(1.0, result, 0);

	testObject = new MoviePopularity(createRating(50, 50, 60, 80, 70));
	result = testObject.getPopularity(Audience.TWENS);
	assertEquals(1.0, result, 0);
    }

    private Rating createRating(final int serious, final int real, final int emotion, final int length, final int pro) {
	final Map<RatingCategory, Integer> ratingPerCategory = new HashMap<RatingCategory, Integer>();
	ratingPerCategory.put(RatingCategory.DURATION, length);
	ratingPerCategory.put(RatingCategory.EMOTION, emotion);
	ratingPerCategory.put(RatingCategory.PROFESSIONALITY, pro);
	ratingPerCategory.put(RatingCategory.REALITY, real);
	ratingPerCategory.put(RatingCategory.SERIOUSITY, serious);
	return Rating.create(ratingPerCategory);
    }
}
