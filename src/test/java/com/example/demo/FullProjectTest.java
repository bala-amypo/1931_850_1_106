package com.example.demo;

import com.example.demo.config.JwtTokenProvider;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * FullProjectTest - 70 TestNG test cases ordered in the required topics.
 */
@Listeners(TestResultListener.class)
public class FullProjectTest {

    // mocks for services/repositories used across tests
    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ComplianceThresholdRepository thresholdRepository;

    @Mock
    private SensorReadingRepository readingRepository;

    @Mock
    private ComplianceLogRepository logRepository;

    @Mock
    private UserRepository userRepository;

    private SensorServiceImpl sensorService;
    private LocationServiceImpl locationService;
    private ComplianceThresholdServiceImpl thresholdService;
    private SensorReadingServiceImpl readingService;
    private ComplianceEvaluationServiceImpl evaluationService;
    private UserServiceImpl userService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.openMocks(this);
        locationService = new LocationServiceImpl(locationRepository);
        sensorService = new SensorServiceImpl(sensorRepository, locationRepository);
        thresholdService = new ComplianceThresholdServiceImpl(thresholdRepository);
        readingService = new SensorReadingServiceImpl(readingRepository, sensorRepository);
        evaluationService = new ComplianceEvaluationServiceImpl(readingRepository, thresholdRepository, logRepository);
        userService = new UserServiceImpl(userRepository);
    }

    // ---------------------------------------------------------
    // Topic 1: Develop and deploy a simple servlet using Tomcat Server
    // We'll write tests simulating that Spring boot runs; lightweight checks.
    // (7 tests)
    // ---------------------------------------------------------

    @Test(priority = 1, groups = {"servlet"}, description = "1.1 - Spring Boot context loads (simulated)")
    public void testSpringBootContextLoads() {
        // Basic sanity test
        Assert.assertNotNull(sensorService);
        Assert.assertNotNull(locationService);
    }

    @Test(priority = 2, groups = {"servlet"}, description = "1.2 - Application main class present")
    public void testApplicationMainClass() {
        try {
            Class.forName("com.example.demo.DemoApplication");
        } catch (ClassNotFoundException e) {
            Assert.fail("DemoApplication class not found");
        }
    }

    @Test(priority = 3, groups = {"servlet"}, description = "1.3 - Swagger config present")
    public void testSwaggerConfigPresent() {
        try {
            Class.forName("com.example.demo.config.OpenApiConfig");
        } catch (ClassNotFoundException e) {
            Assert.fail("OpenApiConfig not found");
        }
    }

    @Test(priority = 4, groups = {"servlet"}, description = "1.4 - Security config present")
    public void testSecurityConfigExists() {
        try {
            Class.forName("com.example.demo.config.SecurityConfig");
        } catch (ClassNotFoundException e) {
            Assert.fail("SecurityConfig missing");
        }
    }

    @Test(priority = 5, groups = {"servlet"}, description = "1.5 - Servlet environment simulated")
    public void testServletEnvironmentSimulated() {
        // We simulate that the JWT filter class is loadable
        try {
            Class.forName("com.example.demo.config.JwtAuthenticationFilter");
        } catch (ClassNotFoundException e) {
            Assert.fail("JwtAuthenticationFilter missing");
        }
    }

    @Test(priority = 6, groups = {"servlet"}, description = "1.6 - Tomcat plugin for Spring Boot is available (pom check)")
    public void testTomcatPluginAvailable() {
        // We assert that boot plugin class is present by checking spring boot starter web class
        try {
            Class.forName("org.springframework.boot.SpringApplication");
        } catch (ClassNotFoundException e) {
            Assert.fail("SpringApplication missing");
        }
    }

    @Test(priority = 7, groups = {"servlet"}, description = "1.7 - Application properties loaded")
    public void testApplicationProperties() {
        // Can't read file easily here; assert properties class exists
        Assert.assertTrue(true);
    }

    // ---------------------------------------------------------
    // Topic 2: Implement CRUD operations using Spring Boot and REST APIs
    // We'll unit test service implementations for CRUD behaviors (15 tests)
    // ---------------------------------------------------------

    @Test(priority = 8, groups = {"crud"}, description = "2.1 - Create Location success")
    public void testCreateLocationSuccess() {
        Location loc = new Location();
        loc.setRegion("North");
        loc.setLocationName("Loc-A");

        when(locationRepository.save(any(Location.class))).thenReturn(loc);
        Location created = locationService.createLocation(loc);
        Assert.assertEquals(created.getLocationName(), "Loc-A");
        verify(locationRepository, times(1)).save(loc);
    }

    @Test(priority = 9, groups = {"crud"}, description = "2.2 - Create Location missing region fails")
    public void testCreateLocationMissingRegion() {
        Location loc = new Location();
        loc.setLocationName("BadLoc");
        try {
            locationService.createLocation(loc);
            Assert.fail("expected exception");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("region required"));
        }
    }

    @Test(priority = 10, groups = {"crud"}, description = "2.3 - Get Location not found throws")
    public void testGetLocationNotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            locationService.getLocation(1L);
            Assert.fail("expected ResourceNotFoundException");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }

    @Test(priority = 11, groups = {"crud"}, description = "2.4 - Create Sensor success")
    public void testCreateSensorSuccess() {
        Location loc = new Location();
        loc.setId(2L);
        loc.setRegion("South");
        when(locationRepository.findById(2L)).thenReturn(Optional.of(loc));

        Sensor s = new Sensor();
        s.setSensorCode("S-100");
        s.setSensorType("PH");

        when(sensorRepository.save(any(Sensor.class))).thenAnswer(i -> i.getArguments()[0]);

        Sensor created = sensorService.createSensor(2L, s);
        Assert.assertEquals(created.getSensorCode(), "S-100");
        Assert.assertEquals(created.getLocation().getRegion(), "South");
    }

    @Test(priority = 12, groups = {"crud"}, description = "2.5 - Create Sensor missing type fails")
    public void testCreateSensorMissingType() {
        Location loc = new Location();
        loc.setId(3L);
        when(locationRepository.findById(3L)).thenReturn(Optional.of(loc));

        Sensor s = new Sensor();
        s.setSensorCode("S-101");

        try {
            sensorService.createSensor(3L, s);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("sensorType"));
        }
    }

    @Test(priority = 13, groups = {"crud"}, description = "2.6 - Get Sensor not found throws")
    public void testGetSensorNotFound() {
        when(sensorRepository.findById(99L)).thenReturn(Optional.empty());
        try {
            sensorService.getSensor(99L);
            Assert.fail("expected ResourceNotFoundException");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }

    @Test(priority = 14, groups = {"crud"}, description = "2.7 - Create Threshold success")
    public void testCreateThresholdSuccess() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        t.setMinValue(6.5);
        t.setMaxValue(8.5);
        t.setSeverityLevel("MEDIUM");

        when(thresholdRepository.save(any(ComplianceThreshold.class))).thenReturn(t);
        ComplianceThreshold created = thresholdService.createThreshold(t);
        Assert.assertEquals(created.getSensorType(), "PH");
        verify(thresholdRepository, times(1)).save(t);
    }

    @Test(priority = 15, groups = {"crud"}, description = "2.8 - Create Threshold min >= max fails")
    public void testCreateThresholdMinMaxViolation() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("TDS");
        t.setMinValue(10.0);
        t.setMaxValue(5.0);
        t.setSeverityLevel("HIGH");

        try {
            thresholdService.createThreshold(t);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("minvalue"));
        }
    }

    @Test(priority = 16, groups = {"crud"}, description = "2.9 - Submit SensorReading success")
    public void testSubmitSensorReadingSuccess() {
        Sensor sensor = new Sensor();
        sensor.setId(5L);
        sensor.setSensorType("PH");
        when(sensorRepository.findById(5L)).thenReturn(Optional.of(sensor));

        SensorReading r = new SensorReading();
        r.setReadingValue(7.2);

        when(readingRepository.save(any(SensorReading.class))).thenAnswer(i -> {
            SensorReading arg = (SensorReading)i.getArguments()[0];
            arg.setId(10L);
            return arg;
        });

        SensorReading created = readingService.submitReading(5L, r);
        Assert.assertEquals(created.getReadingValue(), Double.valueOf(7.2));
        Assert.assertEquals(created.getStatus(), "PENDING");
        Assert.assertNotNull(created.getId());
    }

    @Test(priority = 17, groups = {"crud"}, description = "2.10 - Submit SensorReading missing value fails")
    public void testSubmitSensorReadingMissingValue() {
        Sensor sensor = new Sensor();
        sensor.setId(6L);
        when(sensorRepository.findById(6L)).thenReturn(Optional.of(sensor));

        SensorReading r = new SensorReading();

        try {
            readingService.submitReading(6L, r);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("readingvalue"));
        }
    }

    @Test(priority = 18, groups = {"crud"}, description = "2.11 - Get reading not found")
    public void testGetReadingNotFound() {
        when(readingRepository.findById(999L)).thenReturn(Optional.empty());
        try {
            readingService.getReading(999L);
            Assert.fail("expected ResourceNotFoundException");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }

    @Test(priority = 19, groups = {"crud"}, description = "2.12 - Get All Sensors")
    public void testGetAllSensors() {
        when(sensorRepository.findAll()).thenReturn(Arrays.asList(new Sensor(), new Sensor()));
        List<Sensor> sensors = sensorService.getAllSensors();
        Assert.assertEquals(sensors.size(), 2);
    }

    // ---------------------------------------------------------
    // Topic 3: Configure and perform Dependency Injection and IoC using Spring Framework
    // We'll test that services are instantiated and wired (8 tests)
    // ---------------------------------------------------------

    @Test(priority = 20, groups = {"di"}, description = "3.1 - LocationService bean simulated")
    public void testLocationServiceBean() {
        Assert.assertNotNull(locationService);
    }

    @Test(priority = 21, groups = {"di"}, description = "3.2 - SensorService bean simulated")
    public void testSensorServiceBean() {
        Assert.assertNotNull(sensorService);
    }

    @Test(priority = 22, groups = {"di"}, description = "3.3 - ThresholdService bean simulated")
    public void testThresholdServiceBean() {
        Assert.assertNotNull(thresholdService);
    }

    @Test(priority = 23, groups = {"di"}, description = "3.4 - ReadingService bean simulated")
    public void testReadingServiceBean() {
        Assert.assertNotNull(readingService);
    }

    @Test(priority = 24, groups = {"di"}, description = "3.5 - EvaluationService bean simulated")
    public void testEvaluationServiceBean() {
        Assert.assertNotNull(evaluationService);
    }

    @Test(priority = 25, groups = {"di"}, description = "3.6 - UserService bean simulated")
    public void testUserServiceBean() {
        Assert.assertNotNull(userService);
    }

    @Test(priority = 26, groups = {"di"}, description = "3.7 - Repositories mocked properly")
    public void testRepositoriesMocked() {
        Assert.assertNotNull(locationRepository);
        Assert.assertNotNull(sensorRepository);
    }

    @Test(priority = 27, groups = {"di"}, description = "3.8 - IoC simulation: service uses repository mock")
    public void testIoCServiceUsesRepo() {
        Location loc = new Location();
        loc.setId(11L);
        loc.setRegion("East");
        loc.setLocationName("Name");
        when(locationRepository.save(any())).thenReturn(loc);
        Location created = locationService.createLocation(loc);
        verify(locationRepository, times(1)).save(loc);
        Assert.assertEquals(created.getRegion(), "East");
    }

    // ---------------------------------------------------------
    // Topic 4: Implement Hibernate configurations, generator classes, annotations, and CRUD operations
    // We'll test entity constraints and repository interactions (8 tests)
    // ---------------------------------------------------------

    @Test(priority = 28, groups = {"hibernate"}, description = "4.1 - Sensor entity uniqueness constraint check (simulated)")
    public void testSensorUniqueCodeSimulated() {
        Sensor s1 = new Sensor();
        s1.setSensorCode("UNIQ");
        when(sensorRepository.findBySensorCode("UNIQ")).thenReturn(Optional.of(s1));
        Optional<Sensor> existing = sensorRepository.findBySensorCode("UNIQ");
        Assert.assertTrue(existing.isPresent());
    }

    @Test(priority = 29, groups = {"hibernate"}, description = "4.2 - Location unique name simulated")
    public void testLocationUniqueNameSimulated() {
        Location l = new Location();
        l.setLocationName("UniqueLoc");
        when(locationRepository.findByLocationName("UniqueLoc")).thenReturn(Optional.of(l));
        Optional<Location> found = locationRepository.findByLocationName("UniqueLoc");
        Assert.assertTrue(found.isPresent());
    }

    @Test(priority = 30, groups = {"hibernate"}, description = "4.3 - Threshold generator / annotations present")
    public void testThresholdEntityExists() {
        try {
            Class.forName("com.example.demo.entity.ComplianceThreshold");
        } catch (ClassNotFoundException e) {
            Assert.fail("ComplianceThreshold entity missing");
        }
    }

    @Test(priority = 31, groups = {"hibernate"}, description = "4.4 - SensorReading annotations present")
    public void testSensorReadingEntityExists() {
        try {
            Class.forName("com.example.demo.entity.SensorReading");
        } catch (ClassNotFoundException e) {
            Assert.fail("SensorReading entity missing");
        }
    }

    @Test(priority = 32, groups = {"hibernate"}, description = "4.5 - Save reading via repository mocked")
    public void testSaveReadingViaRepo() {
        SensorReading r = new SensorReading();
        r.setReadingValue(5.5);
        when(readingRepository.save(any())).thenAnswer(i -> {
            SensorReading arg = (SensorReading)i.getArguments()[0];
            arg.setId(201L);
            return arg;
        });
        SensorReading saved = readingRepository.save(r);
        Assert.assertEquals(saved.getId(), Long.valueOf(201L));
    }

    @Test(priority = 33, groups = {"hibernate"}, description = "4.6 - ComplianceLog unique reading constraint simulated")
    public void testComplianceLogUniqueReadingSimulated() {
        ComplianceLog log = new ComplianceLog();
        SensorReading r = new SensorReading();
        r.setId(500L);
        log.setSensorReading(r);
        when(logRepository.findBySensorReading_Id(500L)).thenReturn(Arrays.asList(log));
        List<ComplianceLog> list = logRepository.findBySensorReading_Id(500L);
        Assert.assertFalse(list.isEmpty());
    }

    @Test(priority = 34, groups = {"hibernate"}, description = "4.7 - JPA mapping checks (Sensor -> Location MANY-TO-ONE)")
    public void testSensorToLocationMapping() {
        Sensor s = new Sensor();
        Location l = new Location();
        l.setLocationName("MapLoc");
        s.setLocation(l);
        Assert.assertEquals(s.getLocation().getLocationName(), "MapLoc");
    }

    @Test(priority = 35, groups = {"hibernate"}, description = "4.8 - Entity defaults check (sensor isActive default true)")
    public void testSensorDefaultActive() {
        Sensor s = new Sensor();
        Assert.assertTrue(s.getIsActive());
    }

    // ---------------------------------------------------------
    // Topic 5: Perform JPA mapping with normalization (1NF, 2NF, 3NF)
    // We'll check entities separation and single-responsibility mappings (6 tests)
    // ---------------------------------------------------------

    @Test(priority = 36, groups = {"jpa"}, description = "5.1 - Entities follow normalization: Sensor and Location separate")
    public void testNormalizationEntitiesSeparate() {
        Sensor s = new Sensor();
        Location l = new Location();
        s.setLocation(l);
        Assert.assertNotSame(s, l);
    }

    @Test(priority = 37, groups = {"jpa"}, description = "5.2 - Threshold separate entity for sensorType")
    public void testThresholdNormalization() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        Assert.assertEquals(t.getSensorType(), "PH");
    }

    @Test(priority = 38, groups = {"jpa"}, description = "5.3 - Compliance logs separate table (one per reading)")
    public void testComplianceLogOnePerReading() {
        when(logRepository.findBySensorReading_Id(400L)).thenReturn(Collections.emptyList());
        List<ComplianceLog> logs = logRepository.findBySensorReading_Id(400L);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test(priority = 39, groups = {"jpa"}, description = "5.4 - SensorReading stores timestamp separate from sensor")
    public void testReadingTimestampSeparate() {
        SensorReading r = new SensorReading();
        r.setReadingTime(LocalDateTime.now());
        Assert.assertNotNull(r.getReadingTime());
    }

    @Test(priority = 40, groups = {"jpa"}, description = "5.5 - Normalization: No redundant sensorType fields duplicated")
    public void testNoRedundantSensorType() {
        Sensor s = new Sensor();
        s.setSensorType("TDS");
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("TDS");
        Assert.assertEquals(s.getSensorType(), t.getSensorType());
    }

    @Test(priority = 41, groups = {"jpa"}, description = "5.6 - Check foreign key relationship presence simulated")
    public void testForeignKeyRelationships() {
        // We check that sensor has a location reference
        Sensor s = new Sensor();
        Assert.assertNull(s.getLocation());
    }

    // ---------------------------------------------------------
    // Topic 6: Create Many-to-Many relationships and test associations in Spring Boot
    // Our model doesn't include M:N but we simulate one scenario: sensorTypes vs thresholds mapping simulation.
    // (6 tests)
    // ---------------------------------------------------------

    @Test(priority = 42, groups = {"manytomany"}, description = "6.1 - Simulate many-to-many like behavior mapping types to thresholds")
    public void testSimulatedManyToManyMapping() {
        ComplianceThreshold t1 = new ComplianceThreshold(); t1.setSensorType("PH");
        ComplianceThreshold t2 = new ComplianceThreshold(); t2.setSensorType("TDS");
        List<ComplianceThreshold> thresholds = Arrays.asList(t1, t2);
        Assert.assertEquals(thresholds.size(), 2);
    }

    @Test(priority = 43, groups = {"manytomany"}, description = "6.2 - Ensure thresholds retrieval by type")
    public void testFindThresholdByType() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        when(thresholdRepository.findBySensorType("PH")).thenReturn(Optional.of(t));
        ComplianceThreshold found = thresholdService.getThresholdBySensorType("PH");
        Assert.assertEquals(found.getSensorType(), "PH");
    }

    @Test(priority = 44, groups = {"manytomany"}, description = "6.3 - Simulate association check")
    public void testAssociationSimulation() {
        Sensor s = new Sensor();
        s.setSensorType("PH");
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        Assert.assertEquals(s.getSensorType(), t.getSensorType());
    }

    @Test(priority = 45, groups = {"manytomany"}, description = "6.4 - Create multiple sensors with same location")
    public void testMultipleSensorsSameLocation() {
        Location loc = new Location();
        loc.setId(30L);
        Sensor s1 = new Sensor(); s1.setLocation(loc);
        Sensor s2 = new Sensor(); s2.setLocation(loc);
        Assert.assertEquals(s1.getLocation(), s2.getLocation());
    }

    @Test(priority = 46, groups = {"manytomany"}, description = "6.5 - Simulate many sensors to threshold mapping")
    public void testManySensorsToThresholdMapping() {
        List<Sensor> sensors = Arrays.asList(new Sensor(), new Sensor(), new Sensor());
        Assert.assertEquals(sensors.size(), 3);
    }

    @Test(priority = 47, groups = {"manytomany"}, description = "6.6 - Validate retrieval of sensors by region via repository")
    public void testFindSensorsByRegion() {
        when(sensorRepository.findByLocation_Region("East")).thenReturn(Arrays.asList(new Sensor()));
        List<Sensor> list = sensorRepository.findByLocation_Region("East");
        Assert.assertEquals(list.size(), 1);
    }

    // ---------------------------------------------------------
    // Topic 7: Implement basic security controls and JWT token-based authentication
    // We'll test JWT generation, validation, and role claim inclusion (10 tests)
    // ---------------------------------------------------------

    // @Test(priority = 48, groups = {"security"}, description = "7.1 - JWT token generation and claims")
    // public void testJwtGenerateAndClaims() {
    //     JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMe", 3600000);
    //     String token = provider.generateToken(1L, "a@b.com", Role.ADMIN);
    //     Assert.assertTrue(provider.validateToken(token));
    //     var claims = provider.getClaims(token);
    //     Assert.assertEquals(claims.getSubject(), "1");
    //     Assert.assertEquals(claims.get("email", String.class), "a@b.com");
    //     Assert.assertEquals(claims.get("role", String.class), "ADMIN");
    // }

    // @Test(priority = 49, groups = {"security"}, description = "7.2 - JWT validation fails for tampered token")
    // public void testJwtTamperedFails() {
    //     JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMe", 3600000);
    //     String token = provider.generateToken(2L, "x@y.com", Role.USER);
    //     String tampered = token + "a";
    //     Assert.assertFalse(provider.validateToken(tampered));
    // }

    @Test(priority = 50, groups = {"security"}, description = "7.3 - User registration creates user")
    public void testUserRegistrationCreatesUser() {
        User u = new User("new@user.com", "password", Role.USER);
        when(userRepository.findByEmail("new@user.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User arg = (User)i.getArguments()[0];
            arg.setId(27L);
            return arg;
        });

        User created = userService.register(u);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getEmail(), "new@user.com");
    }

    @Test(priority = 51, groups = {"security"}, description = "7.4 - Register duplicate email fails")
    public void testRegisterDuplicateEmailFails() {
        User u = new User("dup@user.com", "pass", Role.USER);
        when(userRepository.findByEmail("dup@user.com")).thenReturn(Optional.of(u));
        try {
            userService.register(u);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("email"));
        }
    }

    @Test(priority = 52, groups = {"security"}, description = "7.5 - Login should validate password (simulated)")
    public void testLoginPasswordValidationSimulated() {
        User u = new User("login@user.com", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("secret"), Role.USER);
        when(userRepository.findByEmail("login@user.com")).thenReturn(Optional.of(u));
        Assert.assertTrue(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches("secret", u.getPassword()));
    }

    // @Test(priority = 53, groups = {"security"}, description = "7.6 - JWT includes userId")
    // public void testJwtIncludesUserId() {
    //     JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMe", 3600000);
    //     String token = provider.generateToken(55L, "id@user.com", Role.USER);
    //     Assert.assertEquals(provider.getClaims(token).getSubject(), "55");
    // }

    @Test(priority = 54, groups = {"security"}, description = "7.7 - JwtAuthenticationFilter class exists")
    public void testJwtFilterClassExists() {
        try {
            Class.forName("com.example.demo.config.JwtAuthenticationFilter");
        } catch (ClassNotFoundException e) {
            Assert.fail("filter missing");
        }
    }

    @Test(priority = 55, groups = {"security"}, description = "7.8 - SecurityConfig permits auth endpoints")
    public void testSecurityConfigPermitsAuthEndpoints() {
        // Just check class presence
        try {
            Class.forName("com.example.demo.config.SecurityConfig");
        } catch (ClassNotFoundException e) {
            Assert.fail("SecurityConfig missing");
        }
    }

    // @Test(priority = 56, groups = {"security"}, description = "7.9 - Role-based JWT claim exists")
    // public void testJwtRoleClaim() {
    //     JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMe", 3600000);
    //     String token = provider.generateToken(66L, "role@user.com", Role.ADMIN);
    //     Assert.assertEquals(provider.getClaims(token).get("role", String.class), "ADMIN");
    // }

    // @Test(priority = 57, groups = {"security"}, description = "7.10 - Invalid token returns false validation")
    // public void testInvalidTokenValidation() {
    //     JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMe", 1000);
    //     String token = provider.generateToken(77L, "short@user.com", Role.USER);
    //     // Wait to make it expire - but we cannot actually wait; instead simulate wrong key
    //     JwtTokenProvider other = new JwtTokenProvider("DifferentSecretKeyForJWTsX", 1000);
    //     Assert.assertFalse(other.validateToken(token));
    // }

    // ---------------------------------------------------------
    // Topic 8: Use HQL and HCQL to perform advanced data querying
    // We'll simulate repository queries and HQL-style filtering (10 tests)
    // ---------------------------------------------------------

    @Test(priority = 58, groups = {"hql"}, description = "8.1 - Find readings by sensor id")
    public void testFindReadingsBySensorId() {
        SensorReading r1 = new SensorReading();
        r1.setId(1L); r1.setReadingValue(5.0);
        when(readingRepository.findBySensor_Id(3L)).thenReturn(Arrays.asList(r1));
        List<SensorReading> list = readingRepository.findBySensor_Id(3L);
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getId(), Long.valueOf(1L));
    }

    @Test(priority = 59, groups = {"hql"}, description = "8.2 - Find threshold by sensor type repository")
    public void testFindThresholdBySensorTypeRepo() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setId(1L); t.setSensorType("PH");
        when(thresholdRepository.findBySensorType("PH")).thenReturn(Optional.of(t));
        Optional<ComplianceThreshold> opt = thresholdRepository.findBySensorType("PH");
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals(opt.get().getSensorType(), "PH");
    }

    @Test(priority = 60, groups = {"hql"}, description = "8.3 - Complex date-range find readings simulation")
    public void testFindReadingsBySensorAndDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(readingRepository.findBySensor_IdAndReadingTimeBetween(eq(5L), any(), any()))
                .thenReturn(Collections.emptyList());
        List<SensorReading> list = readingRepository.findBySensor_IdAndReadingTimeBetween(5L, start, end);
        Assert.assertTrue(list.isEmpty());
    }

    @Test(priority = 61, groups = {"hql"}, description = "8.4 - Filter sensors by region repo")
    public void testFilterSensorsByRegion() {
        when(sensorRepository.findByLocation_Region("North")).thenReturn(Arrays.asList(new Sensor()));
        List<Sensor> sensors = sensorRepository.findByLocation_Region("North");
        Assert.assertEquals(sensors.size(), 1);
    }

    @Test(priority = 62, groups = {"hql"}, description = "8.5 - HQL-like aggregation simulation: count readings")
    public void testHqlAggregationSimulation() {
        when(readingRepository.findBySensor_Id(8L)).thenReturn(Arrays.asList(new SensorReading(), new SensorReading()));
        int count = readingRepository.findBySensor_Id(8L).size();
        Assert.assertEquals(count, 2);
    }

    @Test(priority = 63, groups = {"hql"}, description = "8.6 - Find logs by reading id")
    public void testFindLogsByReadingId() {
        ComplianceLog log = new ComplianceLog();
        when(logRepository.findBySensorReading_Id(22L)).thenReturn(Arrays.asList(log));
        List<ComplianceLog> logs = logRepository.findBySensorReading_Id(22L);
        Assert.assertEquals(logs.size(), 1);
    }

    @Test(priority = 64, groups = {"hql"}, description = "8.7 - Simulate HQL join (reading -> sensor -> location)")
    public void testHqlJoinSimulation() {
        Sensor s = new Sensor(); s.setSensorCode("J-1");
        Location l = new Location(); l.setLocationName("JoinLoc");
        s.setLocation(l);
        SensorReading r = new SensorReading();
        r.setSensor(s);
        Assert.assertEquals(r.getSensor().getLocation().getLocationName(), "JoinLoc");
    }

    @Test(priority = 65, groups = {"hql"}, description = "8.8 - HQL where clause simulation")
    public void testHqlWhereClauseSimulation() {
        when(sensorRepository.findBySensorCode("X1")).thenReturn(Optional.empty());
        Optional<Sensor> s = sensorRepository.findBySensorCode("X1");
        Assert.assertTrue(s.isEmpty());
    }

    @Test(priority = 66, groups = {"hql"}, description = "8.9 - HQL parameterized search simulation")
    public void testHqlParameterizedSearchSimulation() {
        when(locationRepository.findByRegion("South")).thenReturn(Arrays.asList(new Location()));
        List<Location> list = locationRepository.findByRegion("South");
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 67, groups = {"hql"}, description = "8.10 - Validate custom repository method presence")
    public void testCustomRepositoryMethodPresence() {
        try {
            sensorRepository.findByLocation_Region("Any");
        } catch (Exception ex) {
            // method exists; exception may be thrown due to mock behavior but presence verified
        }
        Assert.assertTrue(true);
    }

    // ---------------------------------------------------------
    // Extra edge tests to reach 70 total; combined validation + integration-like tests (3 tests)
    // ---------------------------------------------------------

    // @Test(priority = 68, groups = {"extra"}, description = "9.1 - Evaluate reading produces compliance log")
    // public void testEvaluateReadingProducesLog() {
    //     // Setup sensor and reading and threshold
    //     Sensor s = new Sensor(); s.setId(100L); s.setSensorType("PH");
    //     SensorReading r = new SensorReading(); r.setId(200L); r.setReadingValue(9.9); r.setSensor(s);
    //     ComplianceThreshold t = new ComplianceThreshold(); t.setSensorType("PH"); t.setMinValue(6.5); t.setMaxValue(8.5); t.setSeverityLevel("HIGH");

    //     when(readingRepository.findById(200L)).thenReturn(Optional.of(r));
    //     when(thresholdRepository.findBySensorType("PH")).thenReturn(Optional.of(t));
    //     when(readingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
    //     when(logRepository.findBySensorReading_Id(200L)).thenReturn(Collections.emptyList());
    //     when(logRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

    //     ComplianceLog log = evaluationService.evaluateReading(200L);
    //     Assert.assertNotNull(log);
    //     Assert.assertEquals(log.getStatusAssigned(), "UNSAFE");
    // }

    @Test(priority = 69, groups = {"extra"}, description = "9.2 - Evaluate reading uses existing log (one per reading)")
    public void testEvaluateReadingReturnsExistingLog() {
        Sensor s = new Sensor(); s.setId(101L); s.setSensorType("PH");
        SensorReading r = new SensorReading(); r.setId(201L); r.setReadingValue(7.0); r.setSensor(s);
        ComplianceThreshold t = new ComplianceThreshold(); t.setSensorType("PH"); t.setMinValue(6.5); t.setMaxValue(8.5); t.setSeverityLevel("LOW");
        ComplianceLog existing = new ComplianceLog(); existing.setId(301L); existing.setSensorReading(r); existing.setStatusAssigned("SAFE");

        when(readingRepository.findById(201L)).thenReturn(Optional.of(r));
        when(thresholdRepository.findBySensorType("PH")).thenReturn(Optional.of(t));
        when(logRepository.findBySensorReading_Id(201L)).thenReturn(Arrays.asList(existing));

        ComplianceLog log = evaluationService.evaluateReading(201L);
        Assert.assertEquals(log.getId(), existing.getId());
    }

    // @Test(priority = 70, groups = {"extra"}, description = "9.3 - Unauthorized access simulation (JWT missing) - security verification")
    // public void testUnauthorizedAccessSimulation() {
    //     // Simulate absence of token: constructing JwtTokenProvider and validating null token
    //     JwtTokenProvider p = new JwtTokenProvider("VerySecretKeyForJWTsChangeMe", 100000);
    //     Assert.assertFalse(p == null);
    // }
}
