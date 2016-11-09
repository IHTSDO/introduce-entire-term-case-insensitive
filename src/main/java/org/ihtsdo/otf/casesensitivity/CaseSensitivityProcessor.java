package org.ihtsdo.otf.casesensitivity;

import java.io.*;

public class CaseSensitivityProcessor {

	public static final String EXPECTED_HEADER = "id\teffectiveTime\tactive\tmoduleId\tconceptId\tlanguageCode\ttypeId\tterm\tcaseSignificanceId";
	private static final String REPORT_HEADER = "ConceptID\tTermID\tTerm\tCaseSignificanceID\tNew_CaseSignificanceID";
	private static final String ONLY_INITIAL_CHARACTER_CASE_INSENSITIVE = "900000000000020002";
	private static final String ENTIRE_TERM_CASE_INSENSITIVE = "900000000000448009";
	private static final String TAB = "\t";

	public void process(InputStream descriptionSnapshotIn, OutputStream descriptionDeltaOut, OutputStream changeReportOut) throws IOException {
		long descriptionsChecked = 0;
		long descriptionsChanged = 0;
		try (final BufferedReader snapshotReader = new BufferedReader(new InputStreamReader(descriptionSnapshotIn));
			 final BufferedWriter deltaWriter = new BufferedWriter(new OutputStreamWriter(descriptionDeltaOut));
			 final BufferedWriter reportWriter = new BufferedWriter(new OutputStreamWriter(changeReportOut))) {

			System.out.println("Processing description file...");
			System.out.println();

			// Check header of input file
			checkHeader(snapshotReader.readLine());

			// Write header of output file
			deltaWriter.write(EXPECTED_HEADER);
			deltaWriter.newLine();

			// Write header of report
			reportWriter.write(REPORT_HEADER);
			reportWriter.newLine();

			// Read all remaining lines of input file
			String line, active, term, caseSignificanceId;
			while ((line = snapshotReader.readLine()) != null) {

				// Read column values of line
				final String[] values = line.split(TAB);
				active = values[2];
				term = values[7];
				caseSignificanceId = values[8];

				// If description row is active and initial character case insensitive
				if (active.equals("1") && caseSignificanceId.equals(ONLY_INITIAL_CHARACTER_CASE_INSENSITIVE)) {
					// If there are no uppercase characters in the term after the first character
					if (!term.substring(1).matches(".*\\p{Lu}.*")) {
						// Change the case sensitivity to entire term case insensitive and write to output file
						values[8] = ENTIRE_TERM_CASE_INSENSITIVE;
						// Clear date for delta
						values[1] = "";
						deltaWriter.write(String.join(TAB, values));
						deltaWriter.newLine();

						reportWriter.write(String.join(TAB, values[4], values[0], term, ONLY_INITIAL_CHARACTER_CASE_INSENSITIVE, ENTIRE_TERM_CASE_INSENSITIVE));
						reportWriter.newLine();

						descriptionsChanged++;
					}
				}
				descriptionsChecked++;
			}
		}
		System.out.println("Process complete:");
		System.out.println("\t" + descriptionsChecked + " descriptions checked");
		System.out.println("\t" + descriptionsChanged + " descriptions updated");
	}

	private void checkHeader(String actualHeader) {
		if (actualHeader == null) {
			throw new IllegalArgumentException("The specified file is empty.");
		} else if (!actualHeader.equals(EXPECTED_HEADER)) {
			throw new IllegalArgumentException("The specified file has an unexpected header.");
		}
	}

}
