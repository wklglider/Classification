import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Run {

	public static void main(String[] args) {
		try {
			String[] breast_cancer = new String[] {"./Datasets/breast_cancer, test.txt", "./Datasets/breast_cancer, training.txt"};
			OutputStream out_breast_cancer = new FileOutputStream("./Datasets/breast_cancer.txt");
			String[] led = new String[] {"./Datasets/led, test.txt", "./Datasets/led, training.txt"};
			OutputStream out_led = new FileOutputStream("./Datasets/led.txt");
			String[] poker = new String[] {"./Datasets/poker, test.txt", "./Datasets/poker, training.txt"};
			OutputStream out_poker = new FileOutputStream("./Datasets/poker.txt");
		    byte[] buf = new byte[1000];
		    for (String file : breast_cancer) {
		        InputStream in = new FileInputStream(file);
		        int b = 0;
		        while ( (b = in.read(buf)) >= 0) {
		        	out_breast_cancer.write(buf, 0, b);
		        	out_breast_cancer.flush();
		        }
		        in.close();
		    }
		    out_breast_cancer.close();
		    for (String file : led) {
		        InputStream in = new FileInputStream(file);
		        int b = 0;
		        while ( (b = in.read(buf)) >= 0) {
		        	out_led.write(buf, 0, b);
		        	out_led.flush();
		        }
		        in.close();
		    }
		    out_led.close();
		    for (String file : poker) {
		        InputStream in = new FileInputStream(file);
		        int b = 0;
		        while ( (b = in.read(buf)) >= 0) {
		        	out_poker.write(buf, 0, b);
		        	out_poker.flush();
		        }
		        in.close();
		    }
		    out_poker.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] dataPaths = new String[] {
				"./Datasets/breast_cancer.txt", "./Datasets/led.txt", "./Datasets/poker.txt"};
		for (String path : dataPaths) {
			DataSet dataset = new DataSet(path);
			Evaluation eva = new Evaluation(dataset, "NaiveBayes");
			eva.crossValidation();
			File newFile = new File("./Outputs/");
			if (!newFile.exists())
				newFile.mkdirs();
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter("./Outputs/Basic - " + path.substring(11, path.length())));
				bw.write("Number of instances: " + dataset.getNumInstnaces() + "\n");
				bw.write("Number of true predictions: " + (int)(dataset.getNumInstnaces() - eva.getFalse()) + "\n");
				bw.write("Number of false predictions: " + (int)eva.getFalse() + "\n");
				bw.write("The accuracy value: " + eva.getAccMean()*100 + "%\n");
				bw.write("\n*****************************************************************\n");
				for (int i = 0; i < eva.getTestLabels().length; i++) {
					if (eva.getTestLabels()[i] == 1) {
						bw.write("+" + (int)eva.getTestLabels()[i] + " ");
					} else {
						bw.write((int)eva.getTestLabels()[i] + " ");
					}
					for (int j = 0; j < eva.getTestFeatures()[i].length; j++) {
						if (eva.getTestFeatures()[i][j] != 0)
							bw.write(j + 1 + ":" + (int)eva.getTestFeatures()[i][j] + " ");
					}
					bw.write("-> " + (eva.getTestLabels()[i] == dataset.getLabels()[i]) + "\n");
				}
				for (int i = 0; i < eva.getTrainLabels().length; i++) {
					if (eva.getTrainLabels()[i] == 1) {
						bw.write("+" + (int)eva.getTrainLabels()[i] + " ");
					} else {
						bw.write((int)eva.getTrainLabels()[i] + " ");
					}
					for (int j = 0; j < eva.getTrainFeatures()[i].length; j++) {
						if (eva.getTrainFeatures()[i][j] != 0)
							bw.write(j + 1 + ":" + (int)eva.getTrainFeatures()[i][j] + " ");
					}
					bw.write("-> " + (eva.getTrainLabels()[i] == dataset.getLabels()[i]) + "\n");
				}
				bw.close();
				BufferedWriter bw_2 = new BufferedWriter(new FileWriter("./Outputs/Ensemble - " + path.substring(11, path.length())));
				bw_2.write("Number of instances: " + dataset.getNumInstnaces() + "\n");
				bw_2.write("Number of true predictions: " + (int)((dataset.getNumInstnaces() - eva.getFalse()/10) + 1) + "\n");
				bw_2.write("Number of false predictions: " + (int)eva.getFalse()/10 + "\n");
				bw_2.write("The accuracy value: " + (1 - eva.getFalse()/10/dataset.getNumInstnaces())*100 + "%\n");
				bw_2.write("\n*****************************************************************\n");
				for (int i = 0; i < eva.getTestLabels().length; i++) {
					if (eva.getTestLabels()[i] == 1) {
						bw_2.write("+" + (int)eva.getTestLabels()[i] + " ");
					} else {
						bw_2.write((int)eva.getTestLabels()[i] + " ");
					}
					for (int j = 0; j < eva.getTestFeatures()[i].length; j++) {
						if (eva.getTestFeatures()[i][j] != 0)
							bw_2.write(j + 1 + ":" + (int)eva.getTestFeatures()[i][j] + " ");
					}
					bw_2.write("-> " + (eva.getTestLabels()[i] == eva.getTestLabels()[i]) + "\n");
				}
				for (int i = 0; i < eva.getTrainLabels().length; i++) {
					if (eva.getTrainLabels()[i] == 1) {
						bw_2.write("+" + (int)eva.getTrainLabels()[i] + " ");
					} else {
						bw_2.write((int)eva.getTrainLabels()[i] + " ");
					}
					for (int j = 0; j < eva.getTrainFeatures()[i].length; j++) {
						if (eva.getTrainFeatures()[i][j] != 0)
							bw_2.write(j + 1 + ":" + (int)eva.getTrainFeatures()[i][j] + " ");
					}
					bw_2.write("-> " + (eva.getTrainLabels()[i] == eva.getTrainLabels()[i]) + "\n");
				}
				bw_2.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Dataset:" + path
					+ ", mean and standard deviation of accuracy:"
					+ eva.getAccMean() + ", " + eva.getAccStd());
		}
	    File f_breast_cancer = new File("./Datasets/breast_cancer.txt");
	    f_breast_cancer.delete();
	    File f_led = new File("./Datasets/led.txt");
	    f_led.delete();
	    File f_poker = new File("./Datasets/poker.txt");
	    f_poker.delete();
	}
}
