package org.ihtsdo.otf.casesensitivity;

import java.io.*;

public class Application {

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Please provide a single argument which is the absolute path to your latest snapshot description RF2 file.\nExiting.");
			System.exit(1);
		}
		final String descriptionSnapshotPath = args[0];

		final File descriptionSnapshot = new File(descriptionSnapshotPath);
		if (!descriptionSnapshot.isFile()) {
			System.err.println(descriptionSnapshot.getAbsoluteFile() + " is not a file.");
			System.exit(1);
		}

		new File("output").mkdirs();
		final String deltaPath = "output/sct2_Description_Delta.txt";
		final String reportPath = "output/description-case-significance-change-report.tsv";
		new CaseSensitivityProcessor().process(
				new FileInputStream(descriptionSnapshot),
				new FileOutputStream(deltaPath),
				new FileOutputStream(reportPath)
		);
		System.out.println();
		System.out.println("Created:");
		System.out.println("\t" + deltaPath);
		System.out.println("\t" + reportPath);
	}

}
