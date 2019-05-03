package com.mycompany.imagej;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BatchTest {

    private static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static PrintStream originalOut = System.out;
    private static PrintStream originalErr = System.err;

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }


    /*
    @Test
    public void testAbortWhenInsufficientArgumentsSupplied() {
        App.main();
        assertEquals(App.MSG_TOO_FEW_ARGUMENTS + EOL +
                App.USAGE + EOL, bytes.toString());
    }

    @Test
    public void testAbortWhenTooManyArgumentsSupplied() {
        App.main("a", "b", "c");
        assertEquals(App.MSG_TOO_MANY_ARGUMENTS + EOL +
                App.USAGE + EOL, bytes.toString());
    }
    */

    @Test
    public void out() {
        System.out.println("hello");
        assertEquals("hello\n", outContent.toString());
    }

    @Test
    public void err() {
        System.err.println("hello again");
        assertEquals("hello again\n", errContent.toString());
    }

    @Test
    void main() {
        // Batch.main("-d","1,2,3,4,");
        // assertEquals("B---", outContent.toString());
        // assertEquals("A---", errContent.toString());
        try {
            outContent.flush();
            outContent.reset();
        } catch(Exception e) {

        }
        System.out.println("B---");
        assertEquals("B---\n", outContent.toString());

        System.out.println("Printing bytes");
        // System.out.println(bytes.toString());
        System.out.println("--------------");
    }

    @Test
    void main2() {
        restoreStreams();
        Batch.main("-d","1,2,3,4,", "-i", "inputfile", "-n", "10");
        System.out.println(Batch.options.anInt);
        System.out.println(Batch.options.inputFile);
        System.out.println(Batch.options.aListStrings);
        System.out.format("Received %s %d...\n", Batch.options.inputFile, Batch.options.anInt);
        for (String el : Batch.options.aListStrings) {
            System.out.format("\\--> <%s>\n", el);
        }
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}
