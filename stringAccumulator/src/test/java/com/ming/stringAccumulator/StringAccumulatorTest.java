package com.ming.stringAccumulator;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StringAccumulator.class)
public class StringAccumulatorTest {

    @Autowired
    public StringAccumulator stringAccumulator;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void given_EmptyString_when_Sum_then_ExpectZero(){
        //Arrange
        String input = "";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(0, result);
    }


    @Test
    public void given_SinglePositiveInteger_when_Sum_then_ExpectSelf(){
        //Arrange
        String input = "1";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(1, result);
    }

    @Test
    public void given_PositiveIntegers_when_CommaDelimitedAndSum_then_ExpectSum(){
        //Arrange
        String input = "1,2";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(3, result);
    }

    @Test
    public void given_ManyPositiveIntegers_when_CommaDelimitedAndSum_then_ExpectSum(){
        //Arrange
        String input = "1,2,3";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(6, result);
    }

    @Test
    public void given_ManyPositiveIntegers_when_NewLineDelimitedAndSum_then_ExpectSum(){
        //Arrange
        String input = "1\n2,3";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(6, result);
    }

    @Test
    public void given_ManyPositiveIntegers_when_SingleCustomDelimiterAndSum_then_ExpectSum(){
        //Arrange
        String input = "//;\n1;2";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(3, result);
    }

    @Test
    public void given_MultiplePositiveIntegers_when_ManyCustomDelimitersAndSum_then_ExpectSum(){
        //Arrange
        String input = "//*|%\n1*2%3";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(6, result);
    }

    @Test
    public void given_SingleNegativeInteger_when_CommaDelimitedAndSum_then_ExpectError(){
        //Arrange
        String input = "1,-1000,3";
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Negatives not allowed : -1000");

        //Act & Assert
        int result = stringAccumulator.add(input);
    }

    @Test
    public void given_ManyNegativeIntegers_when_CustomDelimitedAndSum_then_ExpectError(){
        //Arrange
        String input = "//;\n1;-1000;-2000;3";
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Negatives not allowed : -1000,-2000");

        //Act & Assert
        int result = stringAccumulator.add(input);
    }


    @Test
    public void given_IntegersMoreThan1k_when_CommaDelimitedAndSum_then_ExpectExcludeMoreThan1kAndSum(){
        //Arrange
        String input = "1,1000,1001,3";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(1004, result);
    }

    @Test
    public void given_ManyPositiveIntegers_when_ManyLongCustomDelimiters_then_ExpectSum(){
        //Arrange
        String input = "//***|####|????\n1***2####3????4";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(10, result);
    }


    @Test
    public void given_ManyPositiveIntegers_when_MetaCharAsDelimiter_then_ExpectSum(){
        //Arrange
        String input = "//*\n1*2*3*4";

        //Act
        int result = stringAccumulator.add(input);

        //Assert
        Assert.assertEquals(10, result);
    }
}
