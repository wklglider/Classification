import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSet {

	private boolean[] isCategory;
	private double[][] features;
	private double[] labels;
	private int numAttributes;
	private int numInstnaces;

	public DataSet(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			for (int i = 0; i < 17; i++) {
				String[] attInfo = reader.readLine().split(" ");
				if (numAttributes < (attInfo.length - 1))
					numAttributes = attInfo.length - 1;
			}
			isCategory = new boolean[numAttributes + 1];
			for (int i = 0; i < isCategory.length; i++) {
				isCategory[i] = i == numAttributes ? true : false;
			}

			numInstnaces = 17;
			while (reader.readLine() != null) {
				numInstnaces++;
			}

			features = new double[numInstnaces][numAttributes];
			labels = new double[numInstnaces];

			reader.close();
			reader = new BufferedReader(new FileReader(path));
			String line;
			int ind = 0;
			for (int i = 0; i < numInstnaces; i++) {
				for (int j = 0; j < numAttributes; j++) {
					features[i][j] = 0;
				}
			}
			while ((line = reader.readLine()) != null) {
				String[] atts = line.split(" ");
				for (int i = 0; i < atts.length - 1; i++) {
					features[ind][(int) Double.parseDouble(atts[i + 1]
							.substring(0, atts[i + 1].indexOf(":"))) - 1] = Double
							.parseDouble(atts[i + 1].substring(
									atts[i + 1].indexOf(":") + 1,
									atts[i + 1].length()));
				}
				labels[ind] = Double.parseDouble(atts[0]);
				ind++;
			}
			reader.close();
		} catch (FileNotFoundException ex) {
			Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IOException ex) {
			Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public boolean[] getIsCategory() {
		return isCategory;
	}

	public double[][] getFeatures() {
		return features;
	}

	public double[] getLabels() {
		return labels;
	}

	public int getNumAttributes() {
		return numAttributes;
	}

	public int getNumInstnaces() {
		return numInstnaces;
	}
}
