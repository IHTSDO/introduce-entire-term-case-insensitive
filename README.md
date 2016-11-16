# Introducing Entire Term Case Insensitive in SNOMED CT Descriptions

## Overview
At time of writing the International Edition of SNOMED CT has the following description case sensitivity options:
* Entire term case sensitive (core metadata concept)
* Only initial character case insensitive (core metadata concept)
* Entire term case insensitive (core metadata concept)

But only the first two are currently used.

This application can be used to introduce the use of 'Entire term case insensitive' within the descriptions of any SNOMED CT edition or extension.

## Algorithm
The algorithm is very simple. When the current case significance of a description is "Only initial character case insensitive", if there are no uppercase characters after the first character set case significance to "Entire term case insensitive".

## How to use
You will need Java 1.7 or later.

Download the latest application jar file from the [releases page](https://github.com/IHTSDO/introduce-entire-term-case-insensitive/releases) or clone this project and compile your own using maven.

Run the application giving your description RF2 Snapshot as input.
```
java -jar case-sensitivity-batch-script.jar /my-files/releases/sct2_Description_Snapshot-en_INT_20160731.txt
```

The application will produce the following: 
* **output/sct2_Description_Delta.txt** An RF2 delta of the descriptions which were found to require Entire term case insensitive
* **output/description-case-significance-change-report.tsv** A report containing all the changed descriptions in tab separated format.

The report has the following columns: ConceptID, TermID, Term, CaseSignificanceID, New_CaseSignificanceID
