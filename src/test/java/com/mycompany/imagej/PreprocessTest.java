package com.mycompany.imagej;

import ij.IJ;
import ij.ImagePlus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;

import java.io.File;

class PreprocessTest {
    File file;
    ImagePlus nextImage;
    @BeforeEach
    void setUp() {
    }

    @Test
    void getNumbers() {
        file = new File("src/test/resources/Images/root_1_lg.png");
        nextImage = IJ.openImage(file.getAbsolutePath());

        Preprocess pp = new Preprocess(nextImage, true);
        nextImage.show();
        pp.im.show();
        pp.skel.show();

        Scanner scanner = new Scanner(System.in);
        String myString = scanner.next();
        int myInt = scanner.nextInt();
        scanner.close();
    }

    @AfterEach
    void tearDown() {
    }
}