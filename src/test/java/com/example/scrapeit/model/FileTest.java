package com.example.scrapeit.model;

import com.example.scrapeit.exception.LicenseDuplicateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    private File file;

    @BeforeEach
    void setUp() {
        file = new File("Test.txt");
    }

    @Test
    void addsLicenseIfDistinct() {
        //given
        License license = new License("11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO");
        License license1 = new License("22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO");
        file.addLicense(license);
        file.addLicense(license1);

        //then
        assertEquals(2, file.getLicenses().size());
    }

    @Test
    void throwsLicenseDuplicateExceptionIfNotDistinct() {
        //given
        License license = new License("11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO");
        file.addLicense(license);

        //then
        LicenseDuplicateException licenseDuplicateException = assertThrows(LicenseDuplicateException.class, () -> file.addLicense(license));
        assertEquals("License duplication", licenseDuplicateException.getMessage());
        assertEquals(1, file.getLicenses().size());
    }
}