package org.dis.sheet02.dal;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
		List<String[]> lines = new ArrayList<String[]>();
		lines.add(getColumnNames(result.getMetaData()));
		int[] types = getTypes(result);
		addRows(lines, result, types);
		int[] columnLenths = getColumnLengths(lines);
		int lineLength = getLineLength(columnLenths);
		padLines(lines, columnLenths, types);
		for(int i = 0; i < lines.size(); i++) {
			stream.println(String.join(PADDING_STRING, lines.get(i)));
			if (i == 0)
				printSeparatorLine(stream, lineLength);
		}
		printSeparatorLine(stream, lineLength);
	}

	private static void padLines(	List<String[]> lines, 
									int[] lengths, 
									int[] types) 
	{
		for (String[] line : lines)
    		for (int i = 0; i < lengths.length; i++) {
    			String format = "";
    			if (isLeftAligned(types[i]))
    				format = String.format("%%-%ds", lengths[i]);
    			else
    				format = String.format("%%%ds", lengths[i]);
    			line[i] = String.format(format, line[i]);
    		}
	}

	private static void addRows(List<String[]> lines, ResultSet result, int[] types) 
			throws SQLException {
		int columnCount = result.getMetaData().getColumnCount();
		while (result.next()) {
			String[] line = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				line[i] = formatValue(result, i+1, types[i]);
			}
			lines.add(line);
		}
	}

	private static int[] getTypes(ResultSet result)
			throws SQLException {
		int columnCount = result.getMetaData().getColumnCount();
		int[] types = new int[columnCount];
		for (int i = 0; i < columnCount; i++)
			types[i] = result.getMetaData().getColumnType(i + 1);
		return types;
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
	
	private static String[] getColumnNames(ResultSetMetaData metaData) 
			throws SQLException {
		String[] names = new String[metaData.getColumnCount()];
		for (int i = 0; i < names.length; i++)
			names[i] = metaData.getColumnLabel(i + 1);
		return names;
	}
	
	private static boolean isLeftAligned(int type) {
		switch (type) {
    		case Types.INTEGER:
    		case Types.TINYINT:
    		case Types.BIGINT:
    		case Types.SMALLINT:
    		case Types.DECIMAL:
    		case Types.FLOAT:
    		case Types.DOUBLE:
    		case Types.NUMERIC:
    		case Types.REAL:
    			return false;
    		default:
    			return true;
		}
	}

	private static String formatValue(ResultSet row, int columnIndex, int type) 
			throws SQLException {
		String format;
		switch (type) {
    		case Types.INTEGER:
    		case Types.TINYINT:
    		case Types.BIGINT:
    		case Types.SMALLINT:
    			format = "%d";
    			break;
    		case Types.DECIMAL:
    		case Types.FLOAT:
    		case Types.DOUBLE:
    		case Types.NUMERIC:
    		case Types.REAL:
    			int precision = row.getMetaData().getPrecision(columnIndex);
    			format = String.format("%%.%df", precision);
    			break;
    		default:
    			format = "%s";
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

	private static int[] getColumnLengths(List<String[]> lines)
			throws SQLException {
		int[] lengths = new int[lines.get(0).length];
		for (String[] line : lines)
    		for (int i = 0; i < line.length; i++) {
    			lengths[i] = Math.max(lengths[i], line[i].length());
    		}
		return lengths;
	}
}
