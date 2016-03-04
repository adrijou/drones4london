package com.propero.drones.utils;

import com.propero.drones.exceptions.NonCSVFileFoundException;
import com.propero.drones.exceptions.UnsupportedCSVFileExceptionImpl;
import com.propero.drones.pojo.TubeStation;

import au.com.bytecode.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: adrian.salas<br/>
 * Date: 03/03/16<br/>
 */
public final class CsvTubeStations {

    private static final Logger LOG =
            LoggerFactory.getLogger(CsvTubeStations.class);

    private CsvTubeStations() {
    }


    public static CsvTubeStations newInstance() {
        return new CsvTubeStations();
    }

    public List<TubeStation> getTubeStationFromCSV(final String nameFile)
            throws NonCSVFileFoundException, UnsupportedCSVFileExceptionImpl {
        CSVReader reader = null;
        List<TubeStation> tubeStationList = new ArrayList<TubeStation>();
        TubeStation tubeStation;

        try {

            //Get the CSVReader instance with specifying the delimiter to be use
            InputStream is = CsvTubeStations.class.getClassLoader()
                    .getResourceAsStream(nameFile);
            if (is == null) {
                throw new NonCSVFileFoundException(nameFile);
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, "UTF-8"));
            reader = new CSVReader(br);
            String[] nextLine;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null) {
                tubeStation = new TubeStation();
                tubeStation.setTubeName(nextLine[0]);
                tubeStation.setLongitude(Double.parseDouble(nextLine[1]));
                tubeStation.setLatitude(Double.parseDouble(nextLine[2]));

                tubeStationList.add(tubeStation);
            }
        } catch (IOException e) {
            LOG.info("Error reading file" + e.getMessage());
        } catch (NonCSVFileFoundException ex) {
            throw new NonCSVFileFoundException(nameFile);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new UnsupportedCSVFileExceptionImpl(nameFile);
        } catch (NumberFormatException nfe) {
            throw new UnsupportedCSVFileExceptionImpl(nameFile, nfe.toString());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tubeStationList;
    }
}
