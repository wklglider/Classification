import java.io.Serializable;

public abstract class Classifier implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;

	public abstract void train(boolean[] isCategory, double[][] features,
			double[] labels);

	public abstract double predict(double[] features);
}
