import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Logger;

public class MainTest {

    private static final Logger LOGGER = Logger.getLogger(MainTest.class.getName());

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    private ByteArrayOutputStream outContent;
    private Main mainInstance;

    @BeforeEach
    public void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void runTestWithInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Scanner scanner = new Scanner(System.in);
        mainInstance = new Main(scanner);
        mainInstance.run();
    }

    @Test
    public void testValidCity() {
        runTestWithInput("paris\n");
        String response = mainInstance.responseString;
        LOGGER.info("testValidCity - Actual response: " + response);
        assertTrue(response.contains("Température"), "Expected response to contain 'Température', but got: " + response);
    }

    @Test
    public void testInvalidCity() {
        runTestWithInput("InvalidCity\n");
        String response = mainInstance.responseString;
        LOGGER.info("testInvalidCity - Actual response: " + response);
        assertEquals("Nom de ville invalide ou connexion internet indisponible.", response, "Expected 'Erreur de lecture de la ville.' but got: " + response);
    }

    @Test
    public void testExit() {
        runTestWithInput("exit\n");
        String response = mainInstance.responseString;
        LOGGER.info("testExit - Actual response: " + response);
        assertEquals("Au revoir !", response, "Expected 'Au revoir !' but got: " + response);
    }

    @Test
    public void testEmptyCity() {
        runTestWithInput("\n");
        String response = mainInstance.responseString;
        LOGGER.info("testEmptyCity - Actual response: " + response);
        assertEquals("Veuillez entrer le nom d'une ville.", response, "Expected 'Veuillez entrer le nom d'une ville.' but got: " + response);
    }


    @Test
    public void testValeurTemperature() {
        runTestWithInput("paris\n");
        String response = mainInstance.responseString;
        LOGGER.info("testValeurTemperature - Actual response: " + response);
        assertTrue((-10.0f < mainInstance.response) || (mainInstance.response < 40.0f), "Expected response to be between -10.0 and 40.0°C, but got: " + response);
    }
}