package titanic.weka;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.EvaluationUtils;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.core.converters.Loader;

/**
 * The Verify class uses the trained model and the predicted data to verify the
 * classification actually worked.
 * 
 * @author jbirchfield
 * 
 */
public class Verify {

	public static void main(String[] args) throws Exception {

		/*
		 * First we load our preditons from the CSV formatted file.
		 */
		CSVLoader predictCsvLoader = new CSVLoader();
		predictCsvLoader.setSource(new File("predict.csv"));

		/*
		 * Since we are not using the ARFF format here, we have to give the
		 * loader a little bit of information about the data types. Columns
		 * 3,8,10 need to be of type string and columns 1,4,11 are nominal
		 * types.
		 */
		predictCsvLoader.setStringAttributes("3,8,10");
		predictCsvLoader.setNominalAttributes("1,4,11");
		Instances predictDataSet = predictCsvLoader.getDataSet();

		/*
		 * Here we set the attribute we want to test the predicitons with
		 */
		Attribute testAttribute = predictDataSet.attribute(0);
		predictDataSet.setClass(testAttribute);

		/*
		 * We still have to remove all string attributes before we can test
		 */
		predictDataSet.deleteStringAttributes();

		/*
		 * Now we read in the serialized model from disk
		 */
		RandomForest forest = (RandomForest) SerializationHelper
				.read("titanic.model");

		/*
		 * Next we will use an Evaluation class to evaluate the performance of
		 * our Classifier.
		 */
		Evaluation evaluation = new Evaluation(predictDataSet);
		evaluation.evaluateModel(forest, predictDataSet, new Object[] {});

		/*
		 * After we evaluate the Classifier, we write out the summary
		 * information to the screen.
		 */
		System.out.println(evaluation.toSummaryString());

	}
}