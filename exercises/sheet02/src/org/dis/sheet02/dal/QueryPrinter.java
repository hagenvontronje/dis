package org.dis.sheet02.dal;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Utility class to print out the contents of a query to a text output.
 * 
 * @author Burkhart, Julian
 * @author Elshinawi, Ahmed
 *
 */
public class QueryPrinter {
	/** The number of spaces to add as padding between columns */
	private static final int COLUMN_PADDING = 1;
	private static final String PADDING_STRING = new String(new char[COLUMN_PADDING]).replace("\0", " ");

	/**
	 * Prints out a result set on a print stream.
	 * 
	 * @param result
	 *            The result set to print out.
	 * @param stream
	 *            The stream to use as output target.
	 * @throws SQLException
	 *             In case of an error during the operation.
	 */
	public static void printResult(ResultSet result, PrintStream stream)
			throws SQLException {
		if (result == null)
			return;
		int[] columnLenths = getColumnLengths(result.getMetaData());
		int lineLength = getLineLength(columnLenths);
		printHeader(stream, result.getMetaData(), columnLenths, lineLength);
		printSeparatorLine(stream, lineLength);
		while (result.next())
			printRow(stream, result, columnLenths, lineLength);
	}

	private static void printSeparatorLine(PrintStream stream, int length) {
		stream.println(new String(new char[length]).replace("\0", "-"));
	}

	private static int getLineLength(int[] columnLenths) {
		int lineLength = 0;
		for (int l : columnLenths)
			lineLength += l;
		lineLength += (columnLenths.length - 1) * COLUMN_PADDING;
		return lineLength;
	}

	private static void printHeader(PrintStream stream,
			ResultSetMetaData metaData, int[] columnLenths, int lineLength)
			throws SQLException {
		StringBuilder sb = new StringBuilder(lineLength);
		for (int i = 0; i < columnLenths.length; i++) {
			String format = String.format("%%-%ds", columnLenths[i]);
			sb.append(String.format(format, metaData.getColumnLabel(i + 1)));
			if (i < columnLenths.length - 1)
				sb.append(PADDING_STRING);
		}
		stream.println(sb);
	}

	private static void printRow(PrintStream stream, ResultSet row,
			int[] columnLenths, int lineLength) throws SQLException {
		ResultSetMetaData metaData = row.getMetaData();
		StringBuilder sb = new StringBuilder(lineLength);
		for (int i = 0; i < columnLenths.length; i++) {
			sb.append(formatValue(row, 
								  i + 1, 
								  metaData.getColumnType(i + 1),
								  columnLenths[i]));
			if (i < columnLenths.length - 1)
				sb.append(PADDING_STRING);
		}
		stream.println(sb);
	}

	private static String formatValue(ResultSet row, int columnIndex,
			int columnType, int columnLength) throws SQLException {
		String format;
		switch (columnType) {
    		case Types.INTEGER:
    		case Types.TINYINT:
    		case Types.BIGINT:
    		case Types.SMALLINT:
    			format = String.format("%%%dd", columnLength);
    			break;
    		case Types.DECIMAL:
    		case Types.FLOAT:
    		case Types.DOUBLE:
    		case Types.NUMERIC:
    		case Types.REAL:
    			int precision = row.getMetaData().getPrecision(columnIndex);
    			format = String.format("%%%d.%df", columnLength, precision);
    			break;
    		default:
    			format = String.format("%%-%ds", columnLength);
    			break;
		}
		Object value = row.getObject(columnIndex);
		String formattedValue = String.format(format, value);
		return formattedValue;
	}

	/**
	 * Prints out a result set on standard out.
	 * 
	 * @param result
	 *            The result set to print out.
	 * @throws SQLException
	 *             In case of an error during the operation.
	 */
	public static void printResult(ResultSet result) throws SQLException {
		printResult(result, System.out);
	}

	private static int[] getColumnLengths(ResultSetMetaData metaData)
			throws SQLException {
		int columnCount = metaData.getColumnCount();
		int[] lengths = new int[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			int displaySize = metaData.getColumnDisplaySize(i);
			int nameSize = metaData.getColumnLabel(i).length();
			lengths[i - 1] = Math.max(displaySize, nameSize);
		}
		return lengths;
	}
}
