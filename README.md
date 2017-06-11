Audit Reporter for Csv files.

Changes can be found under commits section.

Didn't use 3rd party library. However Spring Batch and Spring CLI can be used.
I want to Sipmle Stupid with no dependency for 3rd party library.
There is no need Spring Batch because there isn't any csv to DB operation. Or bulk operations.

Usage;

file1.csv file2.csv -c => Gives csv output.
file1.csv file2.csv --top n => Gives n number of record with default print style.
file1.csv file2.csv -c --top n => Gives n number of record with csv format.
