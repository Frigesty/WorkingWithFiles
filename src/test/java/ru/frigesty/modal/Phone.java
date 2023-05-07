package ru.frigesty.modal;

import java.util.List;

public class Phone {
    public String brand;
    public String model;
    public List<String> navigationSystem;
    public String operatingSystem;
    public Integer memoryGB;
    public CPU cpu;
    public Camera camera;

    public static class CPU {
        public Double cPUFrequencyGHz;
        public String cPUType;
    }
    public static class Camera {
        public Integer resolutionMP;
        public Boolean autofocus;
        public String zoom;
    }
}
