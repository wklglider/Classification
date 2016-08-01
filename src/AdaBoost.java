import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdaBoost {

	static class Utils {
		public static ArrayList<ArrayList<Double>> loadDataSet(String filename)
				throws IOException {
			ArrayList<ArrayList<Double>> dataSet = new ArrayList<ArrayList<Double>>();
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = "";

			while ((line = br.readLine()) != null) {
				ArrayList<Double> data = new ArrayList<Double>();
				String[] s = line.split(" ");

				for (int i = 1; i < s.length; i++) {
					data.add(Double.parseDouble(s[i].substring(
							s[i].indexOf(":") + 1, s[i].length())));
				}
				dataSet.add(data);
			}
			br.close();
			return dataSet;
		}

		public static ArrayList<Integer> loadLabelSet(String filename)
				throws NumberFormatException, IOException {
			ArrayList<Integer> labelSet = new ArrayList<Integer>();

			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = "";

			while ((line = br.readLine()) != null) {
				String[] s = line.split(" ");
				labelSet.add(Integer.parseInt(s[0]));
			}
			br.close();
			return labelSet;
		}

		public static void showDataSet(ArrayList<ArrayList<Double>> dataSet) {
			for (ArrayList<Double> data : dataSet) {
				System.out.println(data);
			}
		}

		public static double getMax(ArrayList<ArrayList<Double>> dataSet,
				int index) {
			double max = -9999.0;
			for (ArrayList<Double> data : dataSet) {
				if (data.get(index) > max) {
					max = data.get(index);
				}
			}
			return max;
		}

		public static double getMin(ArrayList<ArrayList<Double>> dataSet,
				int index) {
			double min = 9999.0;
			for (ArrayList<Double> data : dataSet) {
				if (data.get(index) < min) {
					min = data.get(index);
				}
			}
			return min;
		}

		public static ArrayList<Integer> getClassify(
				ArrayList<ArrayList<Double>> dataSet, int feature,
				double thresh, String condition) {
			ArrayList<Integer> labelList = new ArrayList<Integer>();
			if (condition.compareTo("lt") == 0) {
				for (ArrayList<Double> data : dataSet) {
					if (data.get(feature) <= thresh) {
						labelList.add(1);
					} else {
						labelList.add(-1);
					}
				}
			} else {
				for (ArrayList<Double> data : dataSet) {
					if (data.get(feature) >= thresh) {
						labelList.add(1);
					} else {
						labelList.add(-1);
					}
				}
			}
			return labelList;
		}

		public static double getError(ArrayList<Integer> fake,
				ArrayList<Integer> real, ArrayList<Double> weights) {
			double error = 0;
			for (int i = 0; i < fake.size(); i++) {
				if (fake.get(i) != real.get(i)) {
					error += weights.get(i);

				}
			}

			return error;
		}

		public static Stump buildStump(ArrayList<ArrayList<Double>> dataSet,
				ArrayList<Integer> labelSet, ArrayList<Double> weights, int n) {
			int featureNum = dataSet.get(0).size();

			int rowNum = dataSet.size();
			Stump stump = new AdaBoost().new Stump();
			double minError = 999.0;
			System.out.println("Iteration: " + n);
			for (int i = 0; i < featureNum; i++) {
				double min = getMin(dataSet, i);
				double max = getMax(dataSet, i);
				double step = (max - min) / (rowNum);
				for (double j = min - step; j <= max + step; j = j + step) {
					String[] conditions = { "lt", "gt" };
					for (String condition : conditions) {
						ArrayList<Integer> labelList = getClassify(dataSet, i,
								j, condition);

						double error = Utils.getError(labelList, labelSet,
								weights);
						if (error < minError) {
							minError = error;
							stump.dim = i;
							stump.thresh = j;
							stump.condition = condition;
							stump.error = minError;
							stump.labelList = labelList;
							stump.factor = 0.5 * (Math.log((1 - error) / error));
						}

					}
				}

			}

			return stump;
		}

		public static ArrayList<Double> getInitWeights(int n) {
			double weight = 1.0 / n;
			ArrayList<Double> weights = new ArrayList<Double>();
			for (int i = 0; i < n; i++) {
				weights.add(weight);
			}
			return weights;
		}

		public static ArrayList<Double> updateWeights(Stump stump,
				ArrayList<Integer> labelList, ArrayList<Double> weights) {
			double Z = 0;
			ArrayList<Double> newWeights = new ArrayList<Double>();
			int row = labelList.size();
			double e = Math.E;
			double factor = stump.factor;
			for (int i = 0; i < row; i++) {
				Z += weights.get(i)
						* Math.pow(e, -factor * labelList.get(i)
								* stump.labelList.get(i));
			}

			for (int i = 0; i < row; i++) {
				double weight = weights.get(i)
						* Math.pow(e, -factor * labelList.get(i)
								* stump.labelList.get(i)) / Z;
				newWeights.add(weight);
			}
			return newWeights;
		}

		public static ArrayList<Double> InitAccWeightError(int n) {
			ArrayList<Double> accError = new ArrayList<Double>();
			for (int i = 0; i < n; i++) {
				accError.add(0.0);
			}
			return accError;
		}

		public static ArrayList<Double> accWeightError(
				ArrayList<Double> accerror, Stump stump) {
			ArrayList<Integer> t = stump.labelList;
			double factor = stump.factor;
			ArrayList<Double> newAccError = new ArrayList<Double>();
			for (int i = 0; i < t.size(); i++) {
				double a = accerror.get(i) + factor * t.get(i);
				newAccError.add(a);
			}
			return newAccError;
		}

		public static double calErrorRate(ArrayList<Double> accError,
				ArrayList<Integer> labelList) {
			int wrong = 0;
			for (int i = 0; i < accError.size(); i++) {
				if (accError.get(i) > 0) {
					if (labelList.get(i) == -1) {
						wrong++;
					}
				} else if (labelList.get(i) == 1) {
					wrong++;
				}
			}
			double error = wrong * 1.0 / accError.size();
			return error;
		}

		public static void showStumpList(ArrayList<Stump> G) {
			for (Stump s : G) {
				System.out.println(s);
				System.out.println(" ");
			}
		}
	}

	class Stump {
		public int dim;
		public double thresh;
		public String condition;
		public double error;
		public ArrayList<Integer> labelList;
		double factor;

		public String toString() {
			return "dim is " + dim + "\nthresh is " + thresh
					+ "\ncondition is " + condition + "\nerror is " + error
					+ "\nfactor is " + factor + "\nlabel is " + labelList;
		}
	}

	public static ArrayList<Stump> AdaBoostTrain(
			ArrayList<ArrayList<Double>> dataSet, ArrayList<Integer> labelList) {
		int row = labelList.size();
		ArrayList<Double> weights = Utils.getInitWeights(row);
		ArrayList<Stump> G = new ArrayList<Stump>();
		ArrayList<Double> accError = Utils.InitAccWeightError(row);
		int n = 1;
		while (true) {
			Stump stump = Utils.buildStump(dataSet, labelList, weights, n);
			G.add(stump);
			weights = Utils.updateWeights(stump, labelList, weights);
			accError = Utils.accWeightError(accError, stump);
			double error = Utils.calErrorRate(accError, labelList);
			if (error < 0.32) {
				break;
			}
			n++;
		}
		return G;
	}

	public void exicute() throws IOException {
		String file = "./Datasets/poker, test.txt";
		ArrayList<ArrayList<Double>> dataSet = Utils.loadDataSet(file);
		ArrayList<Integer> labelSet = Utils.loadLabelSet(file);
		ArrayList<Stump> G = AdaBoostTrain(dataSet, labelSet);
		Utils.showStumpList(G);
		System.out.println("finished");
	}

}
