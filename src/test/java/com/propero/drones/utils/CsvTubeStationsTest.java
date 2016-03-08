package com.propero.drones.utils;

import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileException;
import com.propero.drones.pojo.TubeStation;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public class CsvTubeStationsTest {

    private static final double DELTA = 1e-15;

    @Test
    public void tubeListFromCSVFile()
            throws NonCSVFileFoundException, UnsupportedCSVFileException {

        String tubeNameFile = "tubeStationsExample.csv";

        List<TubeStation> tubeStationList
                = CsvTubeStations.newInstance()
                    .getTubeStationFromCSV(tubeNameFile);

        assertEquals(2, tubeStationList.size());
        assertEquals("Vauxhall", tubeStationList.get(0).getTubeName());
        assertThat(51.484833, closeTo(tubeStationList.get(0)
                                .getLatitude(), DELTA));
        assertThat(-0.126416, closeTo(tubeStationList.get(0)
                .getLongitude(), DELTA));
    }

    @Test(expected = UnsupportedCSVFileException.class)
    public void tubeListFromCSVSetStringInsteadOfDouble()
            throws FileNotFoundException, NonCSVFileFoundException,
            UnsupportedCSVFileException {

        String tubeStation = "mockFile.csv";
        updateFile(tubeStation, "\"Vauxhall\",\"51.484833\"\n");

        List<TubeStation> stationList
                = CsvTubeStations.newInstance()
                .getTubeStationFromCSV(tubeStation);
    }

    @Test(expected = NonCSVFileFoundException.class)
    public void CsvTubeStationsFileDoesNotExists()
            throws UnsupportedCSVFileException, NonCSVFileFoundException {

        String noCSVwithName = "doesNotExists.csv";

        List<TubeStation> stationList
                = CsvTubeStations.newInstance()
                .getTubeStationFromCSV(noCSVwithName);
    }


    @Test(expected = UnsupportedCSVFileException.class)
    public void tubeStationsFileListContainWrongParameters()
            throws UnsupportedCSVFileException, NonCSVFileFoundException {

        String tubeStation = "tubeStationsError.csv";

        List<TubeStation> stationList
                = CsvTubeStations.newInstance()
                .getTubeStationFromCSV(tubeStation);

    }

    @Test(expected = UnsupportedCSVFileException.class)
    public void tubeStationsFileListContainWrongFormatFile()
            throws UnsupportedCSVFileException, NonCSVFileFoundException,
                   FileNotFoundException, URISyntaxException {

        String tubeStation = "mockFile.csv";
        updateFile(tubeStation, "\"Vauxhall\",,,,\"51.484833\"\n");

        List<TubeStation> stationList
                = CsvTubeStations.newInstance()
                .getTubeStationFromCSV(tubeStation);
    }

    @Test(expected = UnsupportedCSVFileException.class)
    public void tubeStationsFileListContainWrongFormatFileSeparator()
            throws UnsupportedCSVFileException, NonCSVFileFoundException,
            FileNotFoundException, URISyntaxException {

        String tubeStation = "mockFile.csv";
        updateFile(tubeStation, "\"Vauxhall\":\"51.484833\"\n");

        List<TubeStation> stationList
                = CsvTubeStations.newInstance()
                .getTubeStationFromCSV(tubeStation);
    }


    private void updateFile(String fileName, String content)
            throws FileNotFoundException {
        try {
            //open file in override mode
            FileOutputStream fop =
                    new FileOutputStream(new File(
                            getClass().getClassLoader()
                                    .getResource(fileName).toURI()));

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



}
