package ru.frigesty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ru.frigesty.modal.Phone;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTests {

    private ClassLoader cl = JsonTests.class.getClassLoader();
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Test
    void jsonTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("Phone.json");
            InputStreamReader isr = new InputStreamReader(is)) {
            Phone phone = new ObjectMapper().readValue(isr, Phone.class);
            Phone.Camera cameraS = new Phone.Camera();
            assertEquals("Samsung", phone.brand);
            assertEquals("Galaxy S23 Ultra", phone.model);
            List<String> expectedNavigationSystem = Arrays.asList("GPS", "GLONASS", "Beidou", "Galileo", "QZSS");
            assertEquals(expectedNavigationSystem, phone.navigationSystem);
            assertEquals("Android", phone.operatingSystem);
            assertEquals(256, phone.memoryGB);
            assertEquals(3.36, phone.cpu.cPUFrequencyGHz);
            assertEquals("8 core", phone.cpu.cPUType);
            assertEquals(200, phone.camera.resolutionMP);
            assertEquals(true, phone.camera.autofocus);
            assertEquals("100x", phone.camera.zoom);
            }
    }
}

