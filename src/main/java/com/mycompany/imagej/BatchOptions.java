package com.mycompany.imagej;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

import java.util.List;

public class BatchOptions extends OptionsBase {

    @Option(
            name = "help",
            abbrev = 'h',
            help = "Prints usage info.",
            defaultValue = "true"
    )
    public boolean help;

    @Option(
            name = "input file",
            abbrev = 'i',
            help = "Input file",
            category = "startup",
            defaultValue = ""
    )
    public String inputFile;

    @Option(
            name = "an integer",
            abbrev = 'n',
            help = "An integer.",
            category = "startup",
            defaultValue = "42"
    )
    public int anInt;

    @Option(
            name = "aListStrings",
            abbrev = 'd',
            help = "A list of strings.",
            category = "startup",
            allowMultiple = true,
            defaultValue = ""
    )
    public List<String> aListStrings;

}
