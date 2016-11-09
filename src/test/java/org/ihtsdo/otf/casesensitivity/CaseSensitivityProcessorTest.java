package org.ihtsdo.otf.casesensitivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Iterator;

public class CaseSensitivityProcessorTest {

	private CaseSensitivityProcessor processor;
	private File tempDeltaFile;
	private File tempReportFile;

	@Before
	public void setUp() throws Exception {
		processor = new CaseSensitivityProcessor();
		tempDeltaFile = Files.createTempFile("CaseSensitivityProcessorTest-delta", ".txt").toFile();
		tempReportFile = Files.createTempFile("CaseSensitivityProcessorTest-report", ".txt").toFile();
	}

	@Test
	public void testProcess() throws Exception {
		Assert.assertEquals(0, countLines(tempDeltaFile));

		processor.process(
				getClass().getResourceAsStream("/sct2_Description_Snapshot-en_INT_20160731.txt"),
				new FileOutputStream(tempDeltaFile),
				new FileOutputStream(tempReportFile));

		Assert.assertEquals(4, countLines(tempDeltaFile));

		final Iterator<String> linesIterator = Files.readAllLines(tempDeltaFile.toPath(), Charset.forName("UTF-8")).iterator();
		Assert.assertEquals(CaseSensitivityProcessor.EXPECTED_HEADER, linesIterator.next());
		Assert.assertEquals("2580526015\t\t1\t900000000000207008\t420031001\ten\t900000000000013009\tAbdominal aorta angiogram\t900000000000448009", linesIterator.next());
		Assert.assertEquals("2839477017\t\t1\t900000000000207008\t64936001\ten\t900000000000013009\tLöffler syndrome\t900000000000448009", linesIterator.next());
		Assert.assertEquals("146359013\t\t1\t900000000000207008\t88284004\ten\t900000000000013009\tDisability evaluation, disability 50%\t900000000000448009", linesIterator.next());
	}

	@After
	public void tearDown() throws Exception {
		tempDeltaFile.delete();
		tempReportFile.delete();
	}

	private int countLines(File file) throws IOException {
		final BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;
		while (reader.readLine() != null) {
			lines++;
		}
		return lines;
	}
}