package de.outstare.kinosim.movie.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

import de.outstare.kinosim.guituil.PercentageBar;
import de.outstare.kinosim.guituil.WindowUtil;
import de.outstare.kinosim.movie.Rating;
import de.outstare.kinosim.movie.RatingCategory;

/**
 * A RatingGui displays for each {@link RatingCategory} a bar how people rate it.
 */
public class RatingGui {
	private final Rating rating;

	public RatingGui(final Rating rating) {
		this.rating = rating;
	}

	public JComponent createUi() {
		final JPanel panel = new JPanel(new GridBagLayout());

		final GridBagConstraints layout = new GridBagConstraints();
		layout.anchor = GridBagConstraints.LINE_START;
		layout.fill = GridBagConstraints.BOTH;
		layout.insets = new Insets(2, 2, 2, 2);
		layout.gridy = 0;
		for (final RatingCategory category : RatingCategory.values()) {
			layout.weightx = 0.1;
			panel.add(getMinLabel(category), layout);
			layout.weightx = 0.8;
			panel.add(createCategoryBar(category), layout);
			layout.weightx = 0.1;
			panel.add(getMaxLabel(category), layout);
			layout.gridy++;
		}

		return panel;
	}

	private Component getMaxLabel(final RatingCategory category) {
		String text;
		switch (category) {
		case DURATION:
			text = "long";
			break;
		case EMOTION:
			text = "hot";
			break;
		case PROFESSIONALITY:
			text = "million budget";
			break;
		case REALITY:
			text = "live event";
			break;
		case SERIOUSITY:
			text = "documentary";
			break;
		default:
			throw new IllegalArgumentException("unknown category " + category);
		}
		return new JLabel(text);
	}

	private Component getMinLabel(final RatingCategory category) {
		String text;
		switch (category) {
		case DURATION:
			text = "short";
			break;
		case EMOTION:
			text = "cold";
			break;
		case PROFESSIONALITY:
			text = "amateur";
			break;
		case REALITY:
			text = "fantasy";
			break;
		case SERIOUSITY:
			text = "slapstick";
			break;
		default:
			throw new IllegalArgumentException("unknown category " + category);
		}
		return new JLabel(text);
	}

	private JComponent createCategoryBar(final RatingCategory category) {
		final double percent = rating.getValue(category) / (double) Rating.MAX_VALUE;
		final PercentageBar bar = new PercentageBar(percent, Color.BLUE);
		final String name = StringUtils.capitalize(category.name().toLowerCase());
		bar.setBorder(BorderFactory.createTitledBorder(name));
		return bar;
	}

	public static void main(final String[] args) {
		final Rating aRating = Rating.createRandom();
		final RatingGui ui = new RatingGui(aRating);
		WindowUtil.showAndClose(ui.createUi(), "RatingGuiDemo", null);
	}
}
