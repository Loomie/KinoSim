package de.outstare.kinosim.movie.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.lang3.RandomStringUtils;

import de.outstare.kinosim.guituil.WindowUtil;
import de.outstare.kinosim.movie.Movie;
import de.outstare.kinosim.util.Randomness;

/**
 * A MovieIcon generates a random icon for a given {@link Movie}.
 */
public class MovieIcon {
	private static final int	WIDTH	= 64;
	private static final int	HEIGHT	= 64;

	private int					seed;
	private Random				random;

	private final ImageIcon		icon;

	public MovieIcon(final Movie movie) {
		this(movie.hashCode());
	}

	private MovieIcon(final int hashCode) {
		// fields for convenient access (TODO extract builder class)
		seed = hashCode;
		random = new Random(seed);

		icon = new ImageIcon(createImage());

		// fields no longer necessary
		seed = 0;
		random = null;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	private Image createImage() {
		final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		final Graphics2D g2 = image.createGraphics();
		g2.setColor(getBG());
		g2.fillRect(0, 0, WIDTH, HEIGHT);

		final int shapes = getShapeCount();
		for (int i = 0; i < shapes; i++) {
			g2.setColor(getFG(i + 1));
			final Shape shape = getShape();
			g2.fill(shape);
		}

		return image;
	}

	private Shape getShape() {
		final int points = 3 + random.nextInt(4);
		final Path2D path = new Path2D.Float();
		path.moveTo(nextX(), nextY());
		for (int i = 0; i < points; i++) {
			switch (random.nextInt(3)) {
			case 0:
				path.lineTo(nextX(), nextY());
				break;
			case 1:
				path.curveTo(nextX(), nextY(), nextX(), nextY(), nextX(), nextY());
				break;
			case 2:
				path.quadTo(nextX(), nextY(), nextX(), nextY());
				break;
			default:
				throw new IllegalArgumentException("type not supported!");
			}
		}
		path.closePath();
		return path;
	}

	private float nextX() {
		return random.nextFloat() * WIDTH;
	}

	private float nextY() {
		return random.nextFloat() * HEIGHT;
	}

	private Color getBG() {
		// a light color, therefore at least 128 each
		final int rgb = seed | 0xB0B0B0;
		return new Color(rgb);
	}

	private Color getFG(final int number) {
		final float hue = random.nextFloat();
		final float saturation = random.nextFloat();
		final float brightness = 0.5f + random.nextFloat() / 2f;
		return Color.getHSBColor(hue, saturation, brightness);
	}

	private int getShapeCount() {
		return 2 + (seed % 2);
	}

	public static void main(final String[] args) {
		final JPanel panel = new JPanel(new GridLayout(5, 5));
		for (int i = 0; i < 5; i++) {
			final String title = "THE Movie " + i;
			final MovieIcon icon = new MovieIcon(title.hashCode());
			panel.add(new JLabel(title, icon.getIcon(), SwingConstants.CENTER));
		}
		for (int i = 0; i < 4 * 5; i++) {
			final String title = randomTitle();
			final MovieIcon icon = new MovieIcon(title.hashCode());
			panel.add(new JLabel(title, icon.getIcon(), SwingConstants.CENTER));
		}
		WindowUtil.showAndClose(panel, "MovieIconDemo", new Dimension(1000, 700));
	}

	private static String randomTitle() {
		final int length = (int) (4 + Randomness.nextDouble() * 8);
		return RandomStringUtils.randomAlphabetic(length);
	}
}
