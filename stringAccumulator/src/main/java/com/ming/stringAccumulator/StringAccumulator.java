package com.ming.stringAccumulator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class StringAccumulator {

    private static final String PREDEFINED_PATTERN_START_CONST = "//";
    private static final String PREDEFINED_DELIMITER_REGEX = ",|\n";
    private static final String CUSTOM_DELIMITER_REGEX = "^//(.*)\n(.*)$";

    private static final String COMMA_DELIMITER_CONST = ",";
    private static final String PIPE_DELIMITER_CONST = "|";


    /***
     * Split and tokenize the input string, perform validations and sum valid integers up.
     * Empty input string will return 0.
     * Invalid Integers are
     *  1) Negative Integer : Exception Thrown
     *  2) Integer larger than 1000 : Ignored
     * @param inputStr Input String
     * @return The summed integer value
     *
     */
    public int add(String inputStr) {
        if (StringUtils.isBlank(inputStr)) {
            return 0;
        }

        String[] inputArray = splitString(inputStr);
        List<Integer> integerInputList = convertToIntList(inputArray);

        //Validate - Throw Exception if negative number is detected
        validateNegative(integerInputList);

        //Validate - Exclude any values > 1000
        return integerInputList.stream()
                .filter(p -> p <= 1000)
                .reduce(0, Integer::sum);
    }


    /***
     * Helper function to split or tokenize a string.
     * Split by , or \n. Or split by user defined delimiters.
     * @param inputStr Input String
     * @return The split String Array. Or an Empty Array if nothing matched.
     */
    private String[] splitString(String inputStr) {

        if (inputStr.startsWith(PREDEFINED_PATTERN_START_CONST)) {
            Pattern pattern = Pattern.compile(CUSTOM_DELIMITER_REGEX);
            Matcher matcher = pattern.matcher(inputStr);

            if (matcher.find()) {
                String delimiters = matcher.group(1);
                String value = matcher.group(2);

                if (StringUtils.isNotBlank(delimiters) && StringUtils.isNotBlank(value)) {
                    //Since the input string allows using */? etc. as delimiters, need some more polish to avoid dangling meta characters
                    value = reformatStringWithDelimiter(value, delimiters, COMMA_DELIMITER_CONST);
                    return value.split(COMMA_DELIMITER_CONST);
                }
            }

            return new String[0];
        }

        return inputStr.split(PREDEFINED_DELIMITER_REGEX);
    }


    /***
     * Clean up the string array, remove empty entries and extract integer value
     * from the array and convert into a List of Integers.
     * @param inputArray String Array
     * @return List of Integers
     */
    private List<Integer> convertToIntList(String[] inputArray) {
        List<Integer> integerInputList = Arrays.stream(inputArray)
                .map(String::trim)
                .filter(s -> !StringUtils.isBlank(s))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());
        return integerInputList;
    }


    /***
     * To replace every old delimiters with the specified new delimiter
     * @param value Input String
     * @param oldDelimiter String with pipe-separated delimiters to be replaced
     * @param newDelimiter New Delimiter
     * @return Pretty string using the new delimiter
     */
    private String reformatStringWithDelimiter(String value, String oldDelimiter, String newDelimiter) {

        List<String> delimiterList = oldDelimiter.contains(PIPE_DELIMITER_CONST) ?
            Arrays.asList(oldDelimiter.split(Pattern.quote(PIPE_DELIMITER_CONST))) :
            Arrays.asList(oldDelimiter);

        for (String s : delimiterList){
            value = value.replaceAll(Pattern.quote(s), newDelimiter);
        }
        return value;
    }


    /***
     * Validate any negative Integers present in a List, throw RuntimeException if any.
     * @param integerInputList List of Integers to be validated
     * @throws RuntimeException
     */
    private void validateNegative(List<Integer> integerInputList) throws RuntimeException {

        List<Integer> negativeIntegerList = integerInputList.stream()
                .filter(n -> n < 0)
                .collect(Collectors.toList());

        if (negativeIntegerList.size() > 0) {
            String negativeValues = negativeIntegerList.stream()
                    .map(n -> String.valueOf(n))
                    .collect(Collectors.joining(COMMA_DELIMITER_CONST));

            throw new RuntimeException("Negatives not allowed : " + negativeValues);
        }
    }
}
