package com.example.scrapeit.CSV;

import com.example.scrapeit.model.License;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVHelperTest {

    @Mock
    private CSVHelper csvHelper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    CSVHelperTest() throws IOException {
    }

    @Test
    void helperConvertsListOfLicensesToCSV() {
        //given
        License license = new License("11111|Henderson|Aron|Von|Miami|FL|Active|11/12/2012|11/12/2002|NO");
        License license1 = new License("22222|White|Dwayne||Miami|FL|Active|11/12/2012|11/12/2002|NO");
        List<License> licenses = new ArrayList<>();
        licenses.add(license);
        licenses.add(license1);

        //when
        ByteArrayInputStream in = CSVHelper.licencesToCSV(licenses);
        int n = in.available();
        byte[] bytes = new byte[n];
        in.read(bytes, 0, n);
        String csv = new String(bytes, StandardCharsets.UTF_8);
        //then
        assertEquals("licenseNumber,lastName,firstName,middleName,city,state,status,issueDate,expirationDate,boardAction\r\n11111,Henderson,Aron,Von,Miami,FL,Active,11/12/2012,11/12/2002,NO\r\n22222,White,Dwayne,,Miami,FL,Active,11/12/2012,11/12/2002,NO\r\n", csv);
    }
}