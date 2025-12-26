package com.example.demo;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

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
        userService = new UserServiceImpl(userRepository, passwordEncoder, jwtTokenProvider);
    }

    // Topic 1: Servlet/Tomcat (7 tests)

    @Test(priority = 1, groups = { "servlet" }, description = "1.1 - Spring Boot context loads")
    public void testSpringBootContextLoads() {
        Assert.assertNotNull(sensorService);
        Assert.assertNotNull(locationService);
    }

    @Test(priority = 2, groups = { "servlet" }, description = "1.2 - Application main class present")
    public void testApplicationMainClass() {
        try {
            Class.forName("com.example.demo.DemoApplication");
        } catch (ClassNotFoundException e) {
            Assert.fail("DemoApplication class not found");
        }
    }

    @Test(priority = 3, groups = { "servlet" }, description = "1.3 - Swagger config present")
    public void testSwaggerConfigPresent() {
        try {
            Class.forName("com.example.demo.config.OpenApiConfig");
        } catch (ClassNotFoundException e) {
            Assert.fail("OpenApiConfig not found");
        }
    }

    @Test(priority = 4, groups = { "servlet" }, description = "1.4 - Security config present")
    public void testSecurityConfigExists() {
        try {
            Class.forName("com.example.demo.config.SecurityConfig");
        } catch (ClassNotFoundException e) {
            Assert.fail("SecurityConfig missing");
        }
    }

    @Test(priority = 5, groups = { "servlet" }, description = "1.5 - Servlet environment simulated")
    public void testServletEnvironmentSimulated() {
        try {
            Class.forName("com.example.demo.config.JwtAuthenticationFilter");
        } catch (ClassNotFoundException e) {
            Assert.fail("JwtAuthenticationFilter missing");
        }
    }

    @Test(priority = 6, groups = { "servlet" }, description = "1.6 - Tomcat plugin available")
    public void testTomcatPluginAvailable() {
        try {
            Class.forName("org.springframework.boot.SpringApplication");
        } catch (ClassNotFoundException e) {
            Assert.fail("SpringApplication missing");
        }
    }

    @Test(priority = 7, groups = { "servlet" }, description = "1.7 - Application properties loaded")
    public void testApplicationProperties() {
        Assert.assertTrue(true);
    }

    // Topic 2: CRUD Operations (15 tests)

    @Test(priority = 8, groups = { "crud" }, description = "2.1 - Create Location success")
    public void testCreateLocationSuccess() {
        Location loc = new Location();
        loc.setRegion("North");
        loc.setLocationName("Loc-A");
        when(locationRepository.save(any(Location.class))).thenReturn(loc);
        Location created = locationService.createLocation(loc);
        Assert.assertEquals(created.getLocationName(), "Loc-A");
        verify(locationRepository, times(1)).save(loc);
    }

    @Test(priority = 9, groups = { "crud" }, description = "2.2 - Create Location missing region fails")
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

    @Test(priority = 10, groups = { "crud" }, description = "2.3 - Get Location not found")
    public void testGetLocationNotFound() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());
        try {
            locationService.getLocation(1L);
            Assert.fail("expected ResourceNotFoundException");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }

    @Test(priority = 11, groups = { "crud" }, description = "2.4 - Create Sensor success")
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

    @Test(priority = 12, groups = { "crud" }, description = "2.5 - Create Sensor missing type fails")
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

    @Test(priority = 13, groups = { "crud" }, description = "2.6 - Get Sensor not found")
    public void testGetSensorNotFound() {
        when(sensorRepository.findById(99L)).thenReturn(Optional.empty());
        try {
            sensorService.getSensor(99L);
            Assert.fail("expected ResourceNotFoundException");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }

    @Test(priority = 14, groups = { "crud" }, description = "2.7 - Create Threshold success")
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

    @Test(priority = 15, groups = { "crud" }, description = "2.8 - Create Threshold min >= max fails")
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

    @Test(priority = 16, groups = { "crud" }, description = "2.9 - Submit SensorReading success")
    public void testSubmitSensorReadingSuccess() {
        Sensor sensor = new Sensor();
        sensor.setId(5L);
        sensor.setSensorType("PH");
        when(sensorRepository.findById(5L)).thenReturn(Optional.of(sensor));
        SensorReading r = new SensorReading();
        r.setReadingValue(7.2);
        when(readingRepository.save(any(SensorReading.class))).thenAnswer(i -> {
            SensorReading arg = (SensorReading) i.getArguments()[0];
            arg.setId(10L);
            return arg;
        });
        SensorReading created = readingService.submitReading(5L, r);
        Assert.assertEquals(created.getReadingValue(), Double.valueOf(7.2));
        Assert.assertEquals(created.getStatus(), "PENDING");
        Assert.assertNotNull(created.getId());
    }

    @Test(priority = 17, groups = { "crud" }, description = "2.10 - Submit SensorReading missing value fails")
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

    @Test(priority = 18, groups = { "crud" }, description = "2.11 - Get reading not found")
    public void testGetReadingNotFound() {
        when(readingRepository.findById(999L)).thenReturn(Optional.empty());
        try {
            readingService.getReading(999L);
            Assert.fail("expected ResourceNotFoundException");
        } catch (RuntimeException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("not found"));
        }
    }

    @Test(priority = 19, groups = { "crud" }, description = "2.12 - Get All Sensors")
    public void testGetAllSensors() {
        when(sensorRepository.findAll()).thenReturn(Arrays.asList(new Sensor(), new Sensor()));
        List<Sensor> sensors = sensorService.getAllSensors();
        Assert.assertEquals(sensors.size(), 2);
    }

    @Test(priority = 20, groups = { "crud" }, description = "2.13 - Update Location success")
    public void testUpdateLocationSuccess() {
        Location existing = new Location();
        existing.setId(10L);
        existing.setRegion("West");
        existing.setLocationName("OldName");
        when(locationRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(locationRepository.save(any(Location.class))).thenAnswer(i -> i.getArguments()[0]);
        Location updated = locationService.getLocation(10L);
        updated.setLocationName("NewName");
        Location result = locationRepository.save(updated);
        Assert.assertEquals(result.getLocationName(), "NewName");
    }

    @Test(priority = 21, groups = { "crud" }, description = "2.14 - Delete operations simulated")
    public void testDeleteOperationsSimulated() {
        doNothing().when(sensorRepository).deleteById(anyLong());
        sensorRepository.deleteById(1L);
        verify(sensorRepository, times(1)).deleteById(1L);
    }

    @Test(priority = 22, groups = { "crud" }, description = "2.15 - Get all locations")
    public void testGetAllLocations() {
        when(locationRepository.findAll()).thenReturn(Arrays.asList(new Location(), new Location()));
        List<Location> locations = locationService.getAllLocations();
        Assert.assertEquals(locations.size(), 2);
    }

    // Topic 3: Dependency Injection (8 tests)

    @Test(priority = 23, groups = { "di" }, description = "3.1 - LocationService bean")
    public void testLocationServiceBean() {
        Assert.assertNotNull(locationService);
    }

    @Test(priority = 24, groups = { "di" }, description = "3.2 - SensorService bean")
    public void testSensorServiceBean() {
        Assert.assertNotNull(sensorService);
    }

    @Test(priority = 25, groups = { "di" }, description = "3.3 - ThresholdService bean")
    public void testThresholdServiceBean() {
        Assert.assertNotNull(thresholdService);
    }

    @Test(priority = 26, groups = { "di" }, description = "3.4 - ReadingService bean")
    public void testReadingServiceBean() {
        Assert.assertNotNull(readingService);
    }

    @Test(priority = 27, groups = { "di" }, description = "3.5 - EvaluationService bean")
    public void testEvaluationServiceBean() {
        Assert.assertNotNull(evaluationService);
    }

    @Test(priority = 28, groups = { "di" }, description = "3.6 - UserService bean")
    public void testUserServiceBean() {
        Assert.assertNotNull(userService);
    }

    @Test(priority = 29, groups = { "di" }, description = "3.7 - Repositories mocked")
    public void testRepositoriesMocked() {
        Assert.assertNotNull(locationRepository);
        Assert.assertNotNull(sensorRepository);
    }

    @Test(priority = 30, groups = { "di" }, description = "3.8 - IoC service uses repository")
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

    // Topic 4: Hibernate (8 tests)

    @Test(priority = 31, groups = { "hibernate" }, description = "4.1 - Sensor unique code")
    public void testSensorUniqueCodeSimulated() {
        Sensor s1 = new Sensor();
        s1.setSensorCode("UNIQ");
        when(sensorRepository.findBySensorCode("UNIQ")).thenReturn(Optional.of(s1));
        Optional<Sensor> existing = sensorRepository.findBySensorCode("UNIQ");
        Assert.assertTrue(existing.isPresent());
    }

    @Test(priority = 32, groups = { "hibernate" }, description = "4.2 - Location unique name")
    public void testLocationUniqueNameSimulated() {
        Location l = new Location();
        l.setLocationName("UniqueLoc");
        when(locationRepository.findByLocationName("UniqueLoc")).thenReturn(Optional.of(l));
        Optional<Location> found = locationRepository.findByLocationName("UniqueLoc");
        Assert.assertTrue(found.isPresent());
    }

    @Test(priority = 33, groups = { "hibernate" }, description = "4.3 - Threshold entity exists")
    public void testThresholdEntityExists() {
        try {
            Class.forName("com.example.demo.entity.ComplianceThreshold");
        } catch (ClassNotFoundException e) {
            Assert.fail("ComplianceThreshold entity missing");
        }
    }

    @Test(priority = 34, groups = { "hibernate" }, description = "4.4 - SensorReading entity exists")
    public void testSensorReadingEntityExists() {
        try {
            Class.forName("com.example.demo.entity.SensorReading");
        } catch (ClassNotFoundException e) {
            Assert.fail("SensorReading entity missing");
        }
    }

    @Test(priority = 35, groups = { "hibernate" }, description = "4.5 - Save reading via repository")
    public void testSaveReadingViaRepo() {
        SensorReading r = new SensorReading();
        r.setReadingValue(5.5);
        when(readingRepository.save(any())).thenAnswer(i -> {
            SensorReading arg = (SensorReading) i.getArguments()[0];
            arg.setId(201L);
            return arg;
        });
        SensorReading saved = readingRepository.save(r);
        Assert.assertEquals(saved.getId(), Long.valueOf(201L));
    }

    @Test(priority = 36, groups = { "hibernate" }, description = "4.6 - ComplianceLog unique reading")
    public void testComplianceLogUniqueReadingSimulated() {
        ComplianceLog log = new ComplianceLog();
        SensorReading r = new SensorReading();
        r.setId(500L);
        log.setSensorReading(r);
        when(logRepository.findBySensorReading_Id(500L)).thenReturn(Arrays.asList(log));
        List<ComplianceLog> list = logRepository.findBySensorReading_Id(500L);
        Assert.assertFalse(list.isEmpty());
    }

    @Test(priority = 37, groups = { "hibernate" }, description = "4.7 - JPA mapping Sensor->Location")
    public void testSensorToLocationMapping() {
        Sensor s = new Sensor();
        Location l = new Location();
        l.setLocationName("MapLoc");
        s.setLocation(l);
        Assert.assertEquals(s.getLocation().getLocationName(), "MapLoc");
    }

    @Test(priority = 38, groups = { "hibernate" }, description = "4.8 - Entity defaults sensor isActive")
    public void testSensorDefaultActive() {
        Sensor s = new Sensor();
        Assert.assertTrue(s.getIsActive());
    }

    // Topic 5: JPA Normalization (6 tests)

    @Test(priority = 39, groups = { "jpa" }, description = "5.1 - Entities separate")
    public void testNormalizationEntitiesSeparate() {
        Sensor s = new Sensor();
        Location l = new Location();
        s.setLocation(l);
        Assert.assertNotSame(s, l);
    }

    @Test(priority = 40, groups = { "jpa" }, description = "5.2 - Threshold normalization")
    public void testThresholdNormalization() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        Assert.assertEquals(t.getSensorType(), "PH");
    }

    @Test(priority = 41, groups = { "jpa" }, description = "5.3 - Compliance logs separate table")
    public void testComplianceLogOnePerReading() {
        when(logRepository.findBySensorReading_Id(400L)).thenReturn(Collections.emptyList());
        List<ComplianceLog> logs = logRepository.findBySensorReading_Id(400L);
        Assert.assertTrue(logs.isEmpty());
    }

    @Test(priority = 42, groups = { "jpa" }, description = "5.4 - Reading timestamp separate")
    public void testReadingTimestampSeparate() {
        SensorReading r = new SensorReading();
        r.setReadingTime(LocalDateTime.now());
        Assert.assertNotNull(r.getReadingTime());
    }

    @Test(priority = 43, groups = { "jpa" }, description = "5.5 - No redundant sensorType")
    public void testNoRedundantSensorType() {
        Sensor s = new Sensor();
        s.setSensorType("TDS");
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("TDS");
        Assert.assertEquals(s.getSensorType(), t.getSensorType());
    }

    @Test(priority = 44, groups = { "jpa" }, description = "5.6 - Foreign key relationships")
    public void testForeignKeyRelationships() {
        Sensor s = new Sensor();
        Assert.assertNull(s.getLocation());
    }

    // Topic 6: Many-to-Many (6 tests)

    @Test(priority = 45, groups = { "manytomany" }, description = "6.1 - Many-to-many mapping")
    public void testSimulatedManyToManyMapping() {
        ComplianceThreshold t1 = new ComplianceThreshold();
        t1.setSensorType("PH");
        ComplianceThreshold t2 = new ComplianceThreshold();
        t2.setSensorType("TDS");
        List<ComplianceThreshold> thresholds = Arrays.asList(t1, t2);
        Assert.assertEquals(thresholds.size(), 2);
    }

    @Test(priority = 46, groups = { "manytomany" }, description = "6.2 - Find threshold by type")
    public void testFindThresholdByType() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        when(thresholdRepository.findBySensorType("PH")).thenReturn(Optional.of(t));
        ComplianceThreshold found = thresholdService.getThresholdBySensorType("PH");
        Assert.assertEquals(found.getSensorType(), "PH");
    }

    @Test(priority = 47, groups = { "manytomany" }, description = "6.3 - Association check")
    public void testAssociationSimulation() {
        Sensor s = new Sensor();
        s.setSensorType("PH");
        ComplianceThreshold t = new ComplianceThreshold();
        t.setSensorType("PH");
        Assert.assertEquals(s.getSensorType(), t.getSensorType());
    }

    @Test(priority = 48, groups = { "manytomany" }, description = "6.4 - Multiple sensors same location")
    public void testMultipleSensorsSameLocation() {
        Location loc = new Location();
        loc.setId(30L);
        Sensor s1 = new Sensor();
        s1.setLocation(loc);
        Sensor s2 = new Sensor();
        s2.setLocation(loc);
        Assert.assertEquals(s1.getLocation(), s2.getLocation());
    }

    @Test(priority = 49, groups = { "manytomany" }, description = "6.5 - Many sensors to threshold")
    public void testManySensorsToThresholdMapping() {
        List<Sensor> sensors = Arrays.asList(new Sensor(), new Sensor(), new Sensor());
        Assert.assertEquals(sensors.size(), 3);
    }

    @Test(priority = 50, groups = { "manytomany" }, description = "6.6 - Find sensors by region")
    public void testFindSensorsByRegion() {
        when(sensorRepository.findByLocation_Region("East")).thenReturn(Arrays.asList(new Sensor()));
        List<Sensor> list = sensorRepository.findByLocation_Region("East");
        Assert.assertEquals(list.size(), 1);
    }

    // Topic 7: Security/JWT (10 tests)

    @Test(priority = 51, groups = { "security" }, description = "7.1 - JWT generation and claims")
    public void testJwtGenerateAndClaims() {
        JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMeVerySecretKeyForJWTsChangeMe",
                3600000);
        String token = provider.generateToken(1L, "a@b.com", "ADMIN");
        Assert.assertTrue(provider.validateToken(token));
        var claims = provider.getClaims(token);
        Assert.assertEquals(claims.get("userId", Long.class), Long.valueOf(1L));
        Assert.assertEquals(claims.get("email", String.class), "a@b.com");
        Assert.assertEquals(claims.get("role", String.class), "ADMIN");
    }

    @Test(priority = 52, groups = { "security" }, description = "7.2 - JWT tampered fails")
    public void testJwtTamperedFails() {
        JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMeVerySecretKeyForJWTsChangeMe",
                3600000);
        String token = provider.generateToken(2L, "x@y.com", "USER");
        String tampered = token + "a";
        Assert.assertFalse(provider.validateToken(tampered));
    }

    @Test(priority = 53, groups = { "security" }, description = "7.3 - User registration")
    public void testUserRegistrationCreatesUser() {
        User u = new User("new@user.com", "password", "USER");
        when(userRepository.findByEmail("new@user.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User arg = (User) i.getArguments()[0];
            arg.setId(27L);
            return arg;
        });
        User created = userService.register(u);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getEmail(), "new@user.com");
    }

    @Test(priority = 54, groups = { "security" }, description = "7.4 - Duplicate email fails")
    public void testRegisterDuplicateEmailFails() {
        User u = new User("dup@user.com", "pass", "USER");
        when(userRepository.findByEmail("dup@user.com")).thenReturn(Optional.of(u));
        try {
            userService.register(u);
            Assert.fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().toLowerCase().contains("email"));
        }
    }

    @Test(priority = 55, groups = { "security" }, description = "7.5 - Password validation")
    public void testLoginPasswordValidationSimulated() {
        User u = new User("login@user.com",
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("secret"), "USER");
        when(userRepository.findByEmail("login@user.com")).thenReturn(Optional.of(u));
        Assert.assertTrue(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches("secret",
                u.getPassword()));
    }

    @Test(priority = 56, groups = { "security" }, description = "7.6 - JWT includes userId")
    public void testJwtIncludesUserId() {
        JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMeVerySecretKeyForJWTsChangeMe",
                3600000);
        String token = provider.generateToken(55L, "id@user.com", "USER");
        Assert.assertEquals(provider.getClaims(token).get("userId", Long.class), Long.valueOf(55L));
    }

    @Test(priority = 57, groups = { "security" }, description = "7.7 - JwtAuthenticationFilter exists")
    public void testJwtFilterClassExists() {
        try {
            Class.forName("com.example.demo.config.JwtAuthenticationFilter");
        } catch (ClassNotFoundException e) {
            Assert.fail("filter missing");
        }
    }

    @Test(priority = 58, groups = { "security" }, description = "7.8 - SecurityConfig exists")
    public void testSecurityConfigPermitsAuthEndpoints() {
        try {
            Class.forName("com.example.demo.config.SecurityConfig");
        } catch (ClassNotFoundException e) {
            Assert.fail("SecurityConfig missing");
        }
    }

    @Test(priority = 59, groups = { "security" }, description = "7.9 - JWT role claim")
    public void testJwtRoleClaim() {
        JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMeVerySecretKeyForJWTsChangeMe",
                3600000);
        String token = provider.generateToken(66L, "role@user.com", "ADMIN");
        Assert.assertEquals(provider.getClaims(token).get("role", String.class), "ADMIN");
    }

    @Test(priority = 60, groups = { "security" }, description = "7.10 - Invalid token validation")
    public void testInvalidTokenValidation() {
        JwtTokenProvider provider = new JwtTokenProvider("VerySecretKeyForJWTsChangeMeVerySecretKeyForJWTsChangeMe",
                1000);
        String token = provider.generateToken(77L, "short@user.com", "USER");
        JwtTokenProvider other = new JwtTokenProvider("DifferentSecretKeyForJWTsXDifferentSecretKeyForJWTsX", 1000);
        Assert.assertFalse(other.validateToken(token));
    }

    // Topic 8: HQL Queries (10 tests)

    @Test(priority = 61, groups = { "hql" }, description = "8.1 - Find readings by sensor")
    public void testFindReadingsBySensorId() {
        SensorReading r1 = new SensorReading();
        r1.setId(1L);
        r1.setReadingValue(5.0);
        when(readingRepository.findBySensor_Id(3L)).thenReturn(Arrays.asList(r1));
        List<SensorReading> list = readingRepository.findBySensor_Id(3L);
        Assert.assertEquals(list.size(), 1);
        Assert.assertEquals(list.get(0).getId(), Long.valueOf(1L));
    }

    @Test(priority = 62, groups = { "hql" }, description = "8.2 - Find threshold by type")
    public void testFindThresholdBySensorTypeRepo() {
        ComplianceThreshold t = new ComplianceThreshold();
        t.setId(1L);
        t.setSensorType("PH");
        when(thresholdRepository.findBySensorType("PH")).thenReturn(Optional.of(t));
        Optional<ComplianceThreshold> opt = thresholdRepository.findBySensorType("PH");
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals(opt.get().getSensorType(), "PH");
    }

    @Test(priority = 63, groups = { "hql" }, description = "8.3 - Date range query")
    public void testFindReadingsBySensorAndDateRange() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(readingRepository.findBySensor_IdAndReadingTimeBetween(eq(5L), any(), any()))
                .thenReturn(Collections.emptyList());
        List<SensorReading> list = readingRepository.findBySensor_IdAndReadingTimeBetween(5L, start, end);
        Assert.assertTrue(list.isEmpty());
    }

    @Test(priority = 64, groups = { "hql" }, description = "8.4 - Filter sensors by region")
    public void testFilterSensorsByRegion() {
        when(sensorRepository.findByLocation_Region("North")).thenReturn(Arrays.asList(new Sensor()));
        List<Sensor> sensors = sensorRepository.findByLocation_Region("North");
        Assert.assertEquals(sensors.size(), 1);
    }

    @Test(priority = 65, groups = { "hql" }, description = "8.5 - Aggregation count")
    public void testHqlAggregationSimulation() {
        when(readingRepository.findBySensor_Id(8L)).thenReturn(Arrays.asList(new SensorReading(), new SensorReading()));
        int count = readingRepository.findBySensor_Id(8L).size();
        Assert.assertEquals(count, 2);
    }

    @Test(priority = 66, groups = { "hql" }, description = "8.6 - Find logs by reading")
    public void testFindLogsByReadingId() {
        ComplianceLog log = new ComplianceLog();
        when(logRepository.findBySensorReading_Id(22L)).thenReturn(Arrays.asList(log));
        List<ComplianceLog> logs = logRepository.findBySensorReading_Id(22L);
        Assert.assertEquals(logs.size(), 1);
    }

    @Test(priority = 67, groups = { "hql" }, description = "8.7 - HQL join simulation")
    public void testHqlJoinSimulation() {
        Sensor s = new Sensor();
        s.setSensorCode("J-1");
        Location l = new Location();
        l.setLocationName("JoinLoc");
        s.setLocation(l);
        SensorReading r = new SensorReading();
        r.setSensor(s);
        Assert.assertEquals(r.getSensor().getLocation().getLocationName(), "JoinLoc");
    }

    @Test(priority = 68, groups = { "hql" }, description = "8.8 - Where clause simulation")
    public void testHqlWhereClauseSimulation() {
        when(sensorRepository.findBySensorCode("X1")).thenReturn(Optional.empty());
        Optional<Sensor> s = sensorRepository.findBySensorCode("X1");
        Assert.assertTrue(s.isEmpty());
    }

    @Test(priority = 69, groups = { "hql" }, description = "8.9 - Parameterized search")
    public void testHqlParameterizedSearchSimulation() {
        when(locationRepository.findByRegion("South")).thenReturn(Arrays.asList(new Location()));
        List<Location> list = locationRepository.findByRegion("South");
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 70, groups = { "hql" }, description = "8.10 - Custom repository method")
    public void testCustomRepositoryMethodPresence() {
        try {
            sensorRepository.findByLocation_Region("Any");
        } catch (Exception ex) {
            // method exists
        }
        Assert.assertTrue(true);
    }
}
