package store.service;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.tools.javac.Main;
import org.junit.jupiter.api.Test;

class InitialDataServiceTest {

    @Test
    void test() {
        System.out.println(Main.class.getClassLoader().getResourceAsStream("promotions.md"));
    }

}